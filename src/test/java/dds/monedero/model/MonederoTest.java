package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;
  private Movimiento movimiento;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    movimiento = new Movimiento(LocalDate.now(),500,true);
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
    assertEquals(cuenta.getMovimientos().size(),1);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }

  @Test
  void Sacar() {
    cuenta.poner(500);
    cuenta.sacar(500);
    assertEquals(cuenta.getSaldo(),0);
    assertEquals(cuenta.getMovimientos().size(),2);
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.poner(90);
      cuenta.sacar(100);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.poner(2000);
      cuenta.sacar(500);
      cuenta.sacar(501);
    });
  }

  @Test
  public void MontoExtraidoDelDia(){
   cuenta.poner(1000);
   cuenta.sacar(200);
   cuenta.sacar(250);
   assertEquals(450,cuenta.getMontoExtraidoA(LocalDate.now()));
  }

  @Test
  public void esDeLaFecha(){
    assertTrue(movimiento.esDeLaFecha(LocalDate.now()));
  }
}