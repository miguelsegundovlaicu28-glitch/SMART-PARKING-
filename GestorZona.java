package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.IList;
import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

public class GestorZona {
	private int iZona;
	private int jZona;
	private Plaza[] plazas;
	private double precio;
	private IList<SolicitudReservaAnticipada> listaEspera;
	private GestorHuecos gestorHuecos;
	private IList<Hueco> huecosReservados;
	
	public int getI() {
		return iZona;
	}
	
	public int getJ() {
		return jZona;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public String getId() {
		return "z" + iZona + ":" + jZona;
	}
	
	public String getEstadoHuecosLibres() {
		return this.gestorHuecos.toString();
	}
	
	public String getEstadoHuecosReservados() {
		return this.huecosReservados.toString();
	}
	
	public String getListaEspera() {
		return this.listaEspera.toString();
	}
	
	public String getPlazas() {
		return Arrays.toString(this.plazas);
	}
	
	public String toString() {
		return getId() + ": " + getEstadoHuecosReservados();
	}
	
	//TO-DO alumno obligatorios
	
	public GestorZona(int i, int j, int noPlazas, double precio) {
		this.iZona =i;
		this.jZona =j;
		this.plazas=new Plaza[noPlazas];
		this.precio= precio;
		this.listaEspera= new ArrayList<SolicitudReservaAnticipada>();
		this.huecosReservados=new ArrayList<Hueco>();
	}
	
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		if(existeHueco(tI,tF)) {
			Hueco nuevo=new Hueco(tI,tF);
			huecosReservados.add(nuevo);
			return nuevo;
		}
		else {
		return null;}
	}
	
	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		int size =huecosReservados.size();
		for (int i=0;i<size ;i++) {
			if(huecosReservados.get(i).existeHueco()) {
				return true;
			}
		}
		return false;
	}
	
	
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {
		listaEspera.add(solicitud);
	}
	
	public boolean existeHuecoReservado(Hueco hueco) {
		for(int i=0; i<huecosReservados.size();i++) {
			if(hueco.esIgual(huecosReservados.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	//TO-DO alumno opcionales
	
	public void liberarHueco(Hueco hueco) {
		huecosReservados.remove(hueco);
	}

	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		return listaEspera;
	}

	


}
