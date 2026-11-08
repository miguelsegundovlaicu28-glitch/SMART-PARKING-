package modelo.gestoresplazas.huecos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Hueco {
   private LocalDateTime tI;
   private LocalDateTime tF;
   private Plaza plaza;

   public Hueco(LocalDateTime tI, LocalDateTime tF, Plaza plaza) {
      this.tI = tI;
      this.tF = tF;
      this.plaza = plaza;
   }

   public String toString() {
      return "(" + this.tI.toLocalTime() + ", " + this.tF.toLocalTime() + ", " + this.plaza + ")";
   }

   public LocalDateTime gettI() {
      return this.tI;
   }

   public LocalDateTime gettF() {
      return this.tF;
   }

   public long duracion() {
      return this.tI.until(this.tF, ChronoUnit.MINUTES);
   }

   public Plaza getPlaza() {
      return this.plaza;
   }

   public void settI(LocalDateTime tI) {
      this.tI = tI;
   }

   public void settF(LocalDateTime tF) {
      this.tF = tF;
   }

   public boolean equals(Object o) {
      Hueco h = (Hueco)o;
      return h.plaza.equals(this.plaza) && h.tI.equals(this.tI) && h.tF.equals(this.tF);
   }
}
