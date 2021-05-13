package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import jdk.vm.ci.meta.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> depositos = new ArrayList<>();
  private List<Movimiento> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double cuanto) {
    if(this.esDepositoValido(cuanto))
      cargarDeposito(cuanto);
  }

  public void cargarDeposito(double cuanto){
    depositos.add(new Movimiento(LocalDate.now(), cuanto, TipoMovimiento.DEPOSITO));
    this.saldo += cuanto;
  }

  public boolean esDepositoValido(double cuanto){
    return !montoNegativo(cuanto) && !excedeDepositosDiarios();
  }

  public boolean montoNegativo(double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    return true;
  }

  public boolean excedeDepositosDiarios(){
    if (cantidadDepositosDelDia() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
    return true;
  }

  public long cantidadDepositosDelDia(){
    return depositos.stream()
                    .filter(movimiento -> movimiento.esDeLaFecha(LocalDate.now()))
                    .count();
  }

  public void sacar(double cuanto) {
    if(esExtraccionValida(cuanto))
      cargarExtraccion(cuanto);
  }

  public void cargarExtraccion(double cuanto){
    extracciones.add(new Movimiento(LocalDate.now(),cuanto,TipoMovimiento.EXTRACCION));
  }

  public boolean esExtraccionValida(double cuanto){
    return !montoNegativo(cuanto) && !saldoInsuficiente(cuanto) && !excedeLimiteExtraccionesDiarias(cuanto);
  }

  public boolean saldoInsuficiente(double cuanto){
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    return true;
  }

  public boolean excedeLimiteExtraccionesDiarias(double cuanto){
    if (cuanto > extraccionMaximaPosible()) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 + " diarios, límite: " + extraccionMaximaPosible());
    }
    return true;
  }

  public double extraccionMaximaPosible(){
    return 1000 - getMontoExtraidoA(LocalDate.now());
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return extracciones.stream()
        .filter(movimiento ->  movimiento.esDeLaFecha(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getDepositos() {
    return depositos;
  }

  public List<Movimiento> getExtracciones() {
    return extracciones;
  }

  public double getSaldo() {
    return saldo;
  }
}
