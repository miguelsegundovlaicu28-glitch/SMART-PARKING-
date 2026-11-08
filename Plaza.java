package modelo.gestoresplazas.huecos;

import modelo.vehiculos.Vehiculo;

public class Plaza {
   private int numero;
   private Vehiculo vehiculo;

   public Plaza(int i) {
      this.numero = i;
   }

   public Vehiculo getVehiculo() {
      return this.vehiculo;
   }

   public void setVehiculo(Vehiculo vehiculo) {
      this.vehiculo = vehiculo;
   }

   public String toString() {
      return "(" + this.numero + "-" + this.vehiculo + ")";
   }

   public int getNumero() {
      return this.numero;
   }

   public boolean equals(Object o) {
      return this.numero == ((Plaza)o).numero;
   }
}
