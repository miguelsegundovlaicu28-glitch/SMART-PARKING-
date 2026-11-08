package modelo.gestoresplazas;

import list.IList;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;
//TO-DO Obligatorio

public class GestorLocalidad {
	private int[][]plazas;
	private double[][] precios;
	private GestorZona[][]gestorZona;

	public GestorLocalidad(int[][] plazas, double[][] precios) {
		this.plazas=plazas;
		this.precios=precios;
		//Obtención de las filas y columnas
		int filas=plazas.length();
		int columnas =plazas[0].length();
		//Instanción de la matriz
		this.gestorZona= new GestorZona[filas][coumnas];
		for(int i=0;i<filas;i++) {
			for(int j=0;j<columnas;j++) {
				this.gestorZona[i][j]=new GestorZona(i,j,plazas[i][j],precios[i][j]);
			}
		}
		
	}
	
	public int getRadioMaxI() {
		return gestorZona.length-1;
	}
	
	public int getRadioMaxJ() {
		return gestorZona[0].length-1;
	}
	
	public boolean existeZona(int i, int j) {
		
		return i>=0 && i<=getRadioMaxI() && j>=0 && j<=getRadioMaxJ();
	}

	public boolean existeHuecoReservado(Hueco hueco, int i, int j) {

		return gestorZona[i][j].existeHuecoReservado(hueco);
	}

	public GestorZona getGestorZona(int i, int j) {
		return gestorZona[i][j];
	}
	
	//TO-DO alumno opcional
	
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera(int i, int j) {
		//TO-DO
		return gestorZona[i][j].getSolicitudesAtendidasListaEspera();
	}
	
}
