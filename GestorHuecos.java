package modelo.gestoresplazas.huecos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import list.ArrayList;
import list.IList;

public class GestorHuecos {
   private IList<HuecosIgualInicio> huecosPorHora = new ArrayList();
   private static int intervaloHoras = 3;
   public static final LocalDateTime HORAINICIO = LocalDateTime.of(2021, 10, 5, 0, 0);
   public static final LocalDateTime HORAFIN;

   static {
      HORAFIN = HORAINICIO.plusHours((long)intervaloHoras);
   }

   public GestorHuecos(Plaza[] plazas) {
      this.huecosPorHora.add(0, new HuecosIgualInicio(HORAINICIO));

      for(int j = 0; j < plazas.length; ++j) {
         Hueco hueco = new Hueco(HORAINICIO, HORAFIN, plazas[j]);
         ((HuecosIgualInicio)this.huecosPorHora.get(0)).add(j, hueco);
      }

   }

   private int buscarInicioMasCercano(LocalDateTime tI) {
      int primero = 0;
      int ultimo = this.huecosPorHora.size() - 1;
      int centroAnt = -1;

      while(primero < ultimo) {
         int centro = (primero + ultimo) / 2;
         if (((HuecosIgualInicio)this.huecosPorHora.get(centro)).gettI().isAfter(tI)) {
            ultimo = centro - 1;
            centroAnt = -1;
         } else if (((HuecosIgualInicio)this.huecosPorHora.get(centro)).gettI().isBefore(tI)) {
            primero = centro + 1;
            centroAnt = centro;
         } else {
            primero = centro;
            ultimo = centro;
         }
      }

      if (primero == ultimo && ((HuecosIgualInicio)this.huecosPorHora.get(primero)).gettI().equals(tI)) {
         return primero;
      } else {
         return centroAnt < 0 ? 0 : centroAnt;
      }
   }

   private int insertarOrdenado(Hueco nuevoHueco, int desdePos) {
      int i;
      for(i = desdePos; i < this.huecosPorHora.size() && ((HuecosIgualInicio)this.huecosPorHora.get(i)).gettI().isBefore(nuevoHueco.gettI()); ++i) {
      }

      if (i == this.huecosPorHora.size() || !((HuecosIgualInicio)this.huecosPorHora.get(i)).gettI().equals(nuevoHueco.gettI())) {
         this.huecosPorHora.add(i, new HuecosIgualInicio(nuevoHueco.gettI()));
      }

      ((HuecosIgualInicio)this.huecosPorHora.get(i)).add(0, nuevoHueco);
      return i;
   }

   public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
      if (this.huecosPorHora.size() != 0 && !tI.isBefore(((HuecosIgualInicio)this.huecosPorHora.get(0)).gettI())) {
         int pos = this.buscarInicioMasCercano(tI);
         int i = 0;
         boolean fin = false;
         boolean encontrado = false;

         while(pos >= 0 && !fin) {
            for(i = 0; i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size() && !encontrado; ++i) {
               if (((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).gettF().isAfter(tF)) {
                  encontrado = true;
               }
            }

            if (i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size()) {
               fin = true;
            } else {
               --pos;
            }
         }

         return encontrado;
      } else {
         return false;
      }
   }

   public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
      if (this.huecosPorHora.size() != 0 && !tI.isBefore(((HuecosIgualInicio)this.huecosPorHora.get(0)).gettI())) {
         int pos = this.buscarInicioMasCercano(tI);
         Hueco mejorHueco = null;
         long mejorDuracion = Long.MAX_VALUE;
         int mejorPosInt = -1;
         int mejorPosExt = -1;
         int i = 0;
         boolean fin = false;

         while(pos >= 0 && !fin) {
            for(i = 0; i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size() && ((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).gettI().until(tF, ChronoUnit.MINUTES) < mejorDuracion; ++i) {
               if (!((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).gettF().isBefore(tF) && mejorDuracion > ((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).duracion()) {
                  mejorHueco = ((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i);
                  mejorDuracion = mejorHueco.duracion();
                  mejorPosInt = i;
                  mejorPosExt = pos;
               }
            }

            if (i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size()) {
               fin = true;
            } else {
               --pos;
            }
         }

         if (mejorPosExt == -1) {
            return null;
         } else {
            Hueco huecoReservado = new Hueco(tI, tF, mejorHueco.getPlaza());
            if (mejorHueco.gettF().isAfter(tF)) {
               Hueco huecoPost = new Hueco(tF, mejorHueco.gettF(), mejorHueco.getPlaza());
               this.insertarOrdenado(huecoPost, pos + 1);
            }

            if (mejorHueco.gettI().isBefore(tI)) {
               mejorHueco.settF(tI);
            } else {
               ((HuecosIgualInicio)this.huecosPorHora.get(mejorPosExt)).removeElementAt(mejorPosInt);
            }

            return huecoReservado;
         }
      } else {
         return null;
      }
   }

   public void liberarHueco(Hueco hueco) {
      LocalDateTime tI = hueco.gettI();
      LocalDateTime tF = hueco.gettF();
      int pos1 = this.buscarInicioMasCercano(tI);
      int i = 0;
      Hueco nuevoHueco = hueco;
      boolean hayUnionAnt = false;

      for(int pos = pos1; pos >= 0 && !hayUnionAnt; --pos) {
         for(i = 0; i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size() && (!((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).gettF().equals(tI) || ((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i).getPlaza() != hueco.getPlaza()); ++i) {
         }

         if (i < ((HuecosIgualInicio)this.huecosPorHora.get(pos)).size()) {
            nuevoHueco = ((HuecosIgualInicio)this.huecosPorHora.get(pos)).get(i);
            nuevoHueco.settF(tF);
            tI = nuevoHueco.gettI();
            hayUnionAnt = true;
         }
      }

      int var12 = pos1 + 1;

      for(boolean hayUnionDesp = false; var12 < this.huecosPorHora.size() && !hayUnionDesp; ++var12) {
         int j;
         for(j = 0; j < ((HuecosIgualInicio)this.huecosPorHora.get(var12)).size() && (!((HuecosIgualInicio)this.huecosPorHora.get(var12)).get(j).gettI().equals(tF) || ((HuecosIgualInicio)this.huecosPorHora.get(var12)).get(j).getPlaza() != hueco.getPlaza()); ++j) {
         }

         if (j < ((HuecosIgualInicio)this.huecosPorHora.get(var12)).size()) {
            hayUnionDesp = true;
            if (!hayUnionAnt) {
               nuevoHueco = hueco;
            }

            nuevoHueco.settF(((HuecosIgualInicio)this.huecosPorHora.get(var12)).get(j).gettF());
            ((HuecosIgualInicio)this.huecosPorHora.get(var12)).removeElementAt(j);
            if (((HuecosIgualInicio)this.huecosPorHora.get(var12)).size() == 0) {
               this.huecosPorHora.removeElementAt(var12);
            }
         }
      }

      if (!hayUnionAnt) {
         this.insertarOrdenado(hueco, pos1);
      }

   }

   public String toString() {
      return this.huecosPorHora.toString();
   }
}
