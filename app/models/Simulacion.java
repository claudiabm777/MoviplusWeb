package models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class Simulacion {
//esta clase es usada por las demas con el fin de ahorrar c�digo
	//-------------------------------------------------------------------------------
	//Constantes---------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	
	//Todo esta en metros y en segundos
	public final static Double DISTANCIA_CUADRAS=80.0;
	public final static Double TIEMPO_IMPACIENCIA=1200.0;
	public final static Double VELOCIDAD_CONDUCTORES=40000.0/3600.0;
	
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	/**
	 * Metodo para cargar los pasajeros del archivo dado
	 * @param archivoDatos1
	 * @param pasajerosIniciales
	 * @throws Exception
	 */
	public static void cargarPasajeros(InputStream archivoDatos1,PriorityQueue<Pasajero>pasajerosIniciales) throws Exception{
		Workbook workbook = Workbook.getWorkbook(archivoDatos1);
		Sheet sheet = workbook.getSheet("Servicios");
		Cell[]horas=sheet.getColumn(1);
		Cell[]callesIniciales=sheet.getColumn(2);
		Cell[]carrerasIniciales=sheet.getColumn(3);
		Cell[]callesFinales=sheet.getColumn(4);
		Cell[]carrerasFinales=sheet.getColumn(5);
		Double t1=null;
		for(int i =2;i<horas.length-1;i++){
			String tiempo=horas[i].getContents().split(" ")[0];
			String apm=horas[i].getContents().split(" ")[1];
			Double h=Double.parseDouble(tiempo.split(":")[0]);
			Double m=Double.parseDouble(tiempo.split(":")[1]);
			Double s=Double.parseDouble(tiempo.split(":")[2]);
			if(apm.startsWith("P")){
				h+=12.0;
			}
			Double tiempoTotal=h*60.0*60.0+m*60.0+s;
			if(i==2){
				t1=tiempoTotal;
			}
			Long id=(long) i-1;
			Pasajero p=new Pasajero(tiempoTotal-t1,Integer.parseInt(callesIniciales[i].getContents()),Integer.parseInt(carrerasIniciales[i].getContents()),Integer.parseInt(callesFinales[i].getContents()),Integer.parseInt(carrerasFinales[i].getContents()),id);
			pasajerosIniciales.add(p);
		}
	}
	/**
	 * Metodo para cargar los conductores del archivo dado
	 * @param archivoDatos2
	 * @param conductoresIniciales
	 * @throws Exception
	 */
	public static void cargarConductores(InputStream archivoDatos2, List<Conductor>conductoresIniciales) throws Exception{
		Workbook workbook = Workbook.getWorkbook(archivoDatos2);
		Sheet sheet = workbook.getSheet("Conductores");
		Cell[]calles=sheet.getColumn(1);	
		Cell[]carreras=sheet.getColumn(2);
		for(int i =1;i<calles.length;i++){
			Long id=(long) i;
			Conductor c=new Conductor(Integer.parseInt(calles[i].getContents()),Integer.parseInt(carreras[i].getContents()),id);
			conductoresIniciales.add(c);
			//System.out.println(c.getCalle()+" --- "+c.getCarrera()+" -- "+c.getId());
		}
	}
//	public static void main(String[] args) {
//		try {
//			PriorityQueue<Pasajero>in=new PriorityQueue<Pasajero>();
//			InputStream archivoDatos1=Moviplus.class.getResourceAsStream("datos.xls");
//			Simulacion.cargarPasajeros(archivoDatos1,in);
//			int uuu=in.size();
//			for(int i=0;i<uuu;i++){
//				Pasajero p=in.poll();
//				System.out.println(p.getHoraSolicitud()+" ----- "+p.getId());
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
