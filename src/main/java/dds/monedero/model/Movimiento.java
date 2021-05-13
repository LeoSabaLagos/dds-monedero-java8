package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  private double monto;
  private TipoMovimiento tipoMovimiento;

  public Movimiento(LocalDate fecha, double monto, TipoMovimiento tipoMovimiento) {
    this.fecha = fecha;
    this.monto = monto;
    this.tipoMovimiento = tipoMovimiento;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public TipoMovimiento getTipoMovimiento() {
    return tipoMovimiento;
  }
}
