package models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class SimulacionVecinos {
	
	//-------------------------------------------------------------------------------
	//Constantes----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	//public final static InputStream archivoDatos1=Moviplus.class.getResourceAsStream("datos.xls");
	//public final static InputStream archivoDatos2=Moviplus.class.getResourceAsStream("datos.xls");
	
	//-------------------------------------------------------------------------------
	//Atributos----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public List<Conductor>conductoresIniciales;
	public PriorityQueue<Pasajero>pasajerosIniciales;
	public HashMap<Pasajero,Conductor>asignacion;
	public List<Pasajero>pasajerosFinales;
	public int clientesPerdidos;
	
	//-------------------------------------------------------------------------------
	//Constructor--------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public SimulacionVecinos(){
		conductoresIniciales=new ArrayList<Conductor>();
		pasajerosIniciales=new PriorityQueue<Pasajero>();
		pasajerosFinales=new ArrayList<Pasajero>();
		asignacion= new HashMap<Pasajero,Conductor>();
	}
	
	
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	/**
	 * Metodo que ejecuta la simulaci�n de vecinos mas cercanos completa
	 * @return tiempo de espera total
	 */
	public Double simulacionVecinosCercanos(){
		Double respuesta=null;
		try{
			cargarInformacionInicial();
			asignarConductoresAPasajeros();
			respuesta=tiempoEsperaTotal();
		}catch(Exception e){
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Metodo que carga la informaci�n inicial del archivo de excel dado
	 * @throws Exception
	 */
	private void cargarInformacionInicial() throws Exception{
		InputStream archivoDatos1=Moviplus.class.getResourceAsStream("/data/datos.xls");
		InputStream archivoDatos2=Moviplus.class.getResourceAsStream("/data/datos.xls");
		Simulacion.cargarPasajeros(archivoDatos1, pasajerosIniciales);
		Simulacion.cargarConductores(archivoDatos2, conductoresIniciales);
	}
	/**
	 * Metodo que calcula el tiempo de espera total
	 * @return
	 */
	private Double tiempoEsperaTotal(){
		Double tiempoTotal=0.0;
		clientesPerdidos=0;
		for(int i=0;i<pasajerosFinales.size();i++){
			if(pasajerosFinales.get(i).getTiempoEspera()>=20.0*60.0){
				clientesPerdidos++;
			}
			tiempoTotal+=pasajerosFinales.get(i).getTiempoEspera();
		}
		
		JOptionPane.showMessageDialog (null, "El tiempo de espera total fue: "+tiempoTotal+"\nEl tiempo promedio fue: "+(tiempoTotal/pasajerosFinales.size())+"\nLos clientes perdidos fueron: "+clientesPerdidos, "Pol�tica anterior", JOptionPane.INFORMATION_MESSAGE);

		return tiempoTotal;
	}
	/**
	 * Metodo que asigna los conductores a los pasajeros
	 */
	private void asignarConductoresAPasajeros(){
		int tamanio=pasajerosIniciales.size();
		for(int i=0;i<tamanio;i++){
			Pasajero pasajero=pasajerosIniciales.poll();
			Conductor conductor=conductorMasCercano(pasajero);
			if(conductor.getTiempoDisponible()-pasajero.getHoraSolicitud()>=Simulacion.TIEMPO_IMPACIENCIA){
				pasajero.setTiempoEspera(Simulacion.TIEMPO_IMPACIENCIA);
				pasajerosFinales.add(pasajero);
			}
			else if(pasajero.getHoraSolicitud()>=conductor.getTiempoDisponible()){
				pasajero.setTiempoEspera(0.0);
				conductoresIniciales.remove(conductor);
				double distanciaX1=Math.abs(pasajero.getCalleInicial()-conductor.getCalle());
				double distanciaY1=Math.abs(pasajero.getCarreraInicial()-conductor.getCarrera());
				double distanciaPasajero=distanciaX1+distanciaY1;
				double distanciaX2=Math.abs(pasajero.getCalleInicial()-pasajero.getCalleFinal());
				double distanciaY2=Math.abs(pasajero.getCarreraInicial()-pasajero.getCarreraFinal());
				double distanciaDestino=distanciaX2+distanciaY2;
				double tiempo=(distanciaPasajero+distanciaDestino)*Simulacion.DISTANCIA_CUADRAS/Simulacion.VELOCIDAD_CONDUCTORES;
				Conductor c=new Conductor(pasajero.getCalleFinal(),pasajero.getCarreraFinal(),conductor.getId());
				c.setTiempoDisponible(tiempo+conductor.getTiempoDisponible());
				conductoresIniciales.add(c);
				asignacion.put(pasajero, conductor);
				pasajerosFinales.add(pasajero);
			}
			else if(pasajero.getHoraSolicitud()<conductor.getTiempoDisponible()){
				pasajero.setTiempoEspera(conductor.getTiempoDisponible()-pasajero.getHoraSolicitud());
				conductoresIniciales.remove(conductor);
				double distanciaX1=Math.abs(pasajero.getCalleInicial()-conductor.getCalle());
				double distanciaY1=Math.abs(pasajero.getCarreraInicial()-conductor.getCarrera());
				double distanciaPasajero=distanciaX1+distanciaY1;
				double distanciaX2=Math.abs(pasajero.getCalleInicial()-pasajero.getCalleFinal());
				double distanciaY2=Math.abs(pasajero.getCarreraInicial()-pasajero.getCarreraFinal());
				double distanciaDestino=distanciaX2+distanciaY2;
				double tiempo=(distanciaPasajero+distanciaDestino)*Simulacion.DISTANCIA_CUADRAS/Simulacion.VELOCIDAD_CONDUCTORES;
				Conductor c=new Conductor(pasajero.getCalleFinal(),pasajero.getCarreraFinal(),conductor.getId());
				c.setTiempoDisponible(tiempo+conductor.getTiempoDisponible());
				conductoresIniciales.add(c);
				asignacion.put(pasajero, conductor);
				pasajerosFinales.add(pasajero);
			}
		}	
	}
	
	/**
	 * M�todo que busca dado un pasajero, su conductor m�s cercano
	 * @param pasajero
	 * @return
	 */
	private Conductor conductorMasCercano(Pasajero pasajero){
		Conductor respuesta=null;
		boolean hayConductoresDisponibles=false;
		for(int i=0;i<conductoresIniciales.size();i++){
			if(pasajero.getHoraSolicitud()>=conductoresIniciales.get(i).getTiempoDisponible()){
				hayConductoresDisponibles=true;
			}
		}
		if(hayConductoresDisponibles){
			double minimo=999999999.0;
			Conductor conductorMinimo=null;
			for(int i=0;i<conductoresIniciales.size();i++){
				if(pasajero.getHoraSolicitud()>=conductoresIniciales.get(i).getTiempoDisponible()){
					double distanciaX=Math.abs(pasajero.getCalleInicial()-conductoresIniciales.get(i).getCalle());
					double distanciaY=Math.abs(pasajero.getCarreraInicial()-conductoresIniciales.get(i).getCarrera());
					double distancia=distanciaX+distanciaY;
//					if(pasajero.getId()==38L){
//						System.out.println("Distancia: "+distancia+" Minimo: "+minimo+"---"+(distancia<minimo)+" Conductor: "+conductoresIniciales.get(i).getId());
//					}
					if(distancia<minimo){
						minimo=distancia;
						conductorMinimo=conductoresIniciales.get(i);
					}
				}
			}
			respuesta=conductorMinimo;
		}
		else{
			double minimo=999999999.0;
			Conductor conductorMinimo=null;
			for(int i=0;i<conductoresIniciales.size();i++){
				if(conductoresIniciales.get(i).getTiempoDisponible()<minimo){
					minimo=conductoresIniciales.get(i).getTiempoDisponible();
					conductorMinimo=conductoresIniciales.get(i);
				}
			}
			respuesta=conductorMinimo;
		}
		return respuesta;
	}
	
//	public static void main(String[] args) {
//		SimulacionVecinos simulacion=new SimulacionVecinos();
//		Double r=simulacion.simulacionVecinosCercanos();
//		System.out.println("Tiempo espera total: "+r);
//		for(int i=0;i<simulacion.pasajerosFinales.size();i++){
//			Pasajero pas=simulacion.pasajerosFinales.get(i);
//			double p=pas.getHoraSolicitud();
//			Conductor c=simulacion.asignacion.get(simulacion.pasajerosFinales.get(i));
//			if(c!=null){
//				System.out.println("El pasajero: "+pas.getId()+" tiene tiempo: "+p+"\t el conductor: "+c.getId()+" tiene tiempo: "+c.getTiempoDisponible());
//			}
//			else{
//				System.out.println("El pasajero: "+pas.getId()+" tiene tiempo: "+p+" - NO ENTROOOO");
//			}
//		}
//	}

}
