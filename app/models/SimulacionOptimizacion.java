package models;

import java.io.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controllers.routes;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class SimulacionOptimizacion extends Simulacion {
	
	//-------------------------------------------------------------------------------
	//Atributos----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	private List<Conductor>conductoresIniciales;
	private PriorityQueue<Pasajero>pasajerosIniciales;
	public List<Etapa>etapas;
	public Double ventanaTiempo;
	private WritableCellFormat timesBoldUnderline; //ATRIBUTO PARA IMPRIMIR EN ARCHIVO EXCEL
	private WritableCellFormat times; //ATRIBUTO PARA IMPRIMIR EN ARCHIVO EXCEL
	private String inputFile; //ATRIBUTO PARA IMPRIMIR EN ARCHIVO EXCEL
	public Double tiempoPromedioTotal;
	public Double nivelServicio;
	//-------------------------------------------------------------------------------
	//Constructor--------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public SimulacionOptimizacion(Double tiempo) {
		
		conductoresIniciales=new ArrayList<Conductor>();
		pasajerosIniciales=new PriorityQueue<Pasajero>();
		etapas=new ArrayList<Etapa>();
		ventanaTiempo=tiempo;
		tiempoPromedioTotal=0.0;
		nivelServicio=0.0;
	}
	
	//-------------------------------------------------------------------------------
	//Getters and Setters------------------------------------------------------------
	//-------------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	/**
	 * Carga la informaci�n inicial del archivo de excel suministrado
	 * @throws Exception
	 */
	private void cargarInformacionInicial() throws Exception{

		InputStream archivoDatos1=Moviplus.class.getResourceAsStream("/public/datos.xls");
		Simulacion.cargarPasajeros(archivoDatos1, pasajerosIniciales);
		InputStream archivoDatos2=Moviplus.class.getResourceAsStream("/public/datos.xls");
		Simulacion.cargarConductores(archivoDatos2, conductoresIniciales);
	}
	
	/**
	 * Asigna los pasajeros a las diferentes etapas dependiendo de su hora de llegada y la ventana de tiempo
	 * @throws Exception
	 */
	public void asignarPasajerosAEtapas() throws Exception{
		cargarInformacionInicial();
		//System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAA pasaj inic: "+pasajerosIniciales.size());
		PriorityQueue<Pasajero> q=new PriorityQueue(pasajerosIniciales);
		Pasajero ultimo=null;
		int tam=q.size();
		for(int i=0;i<tam;i++){
			ultimo=q.poll();
		}
		//System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAA pasaj inic: "+pasajerosIniciales.size());
		Double duracionTotal=ultimo.getHoraSolicitud();
		Integer numeroEtapas=(int) Math.ceil((duracionTotal/ventanaTiempo));
		Double horaInicio=0.0;
		Double horaFin=ventanaTiempo;
		for(int i=0;i<numeroEtapas;i++){
			Long id=(long) (i+1);
			Etapa etapa=new Etapa(ventanaTiempo,id,horaInicio,horaFin);
			int ttt=pasajerosIniciales.size();
			for(int j=0;j<ttt;j++){
				Pasajero pa=pasajerosIniciales.poll();
				if(pa.getHoraSolicitud()<=etapa.getHoraFinal()){
					etapa.pasajeros.add(pa);
					
				}
				else{
					pasajerosIniciales.add(pa);
					
					break;
				}
			}
			etapas.add(etapa);
			horaInicio=horaFin;
			horaFin+=ventanaTiempo;
			
		}
		for(int i=0;i<numeroEtapas;i++){
			Etapa etapa=etapas.get(i);
			etapa.setNuevasSolicitudes(etapa.pasajeros.size());
			System.out.println("Etapa "+i+" num pas: "+etapa.pasajeros.size()+" Inicio: "+etapa.getHoraInicial()+" fin: "+etapa.getHoraFinal());
		}
		
	}
	
	/**
	 * Obtiene el promedio de tiempo de espera total de la simulacion. No cuenta las etapas q tienen promedio cero
	 * @return
	 */
	public Double obtenerPromedioTotal(){
		Double respuesta=0.0;
		int cont =0;
		for(int i=0;i<etapas.size();i++){
			if(etapas.get(i).tiempoEsperaPromedio()==0){
				cont++;
			}
			respuesta+=etapas.get(i).tiempoEsperaPromedio();
		}
		if(etapas.size()!=0){
			respuesta=respuesta/(etapas.size()-cont);
		}
		return respuesta;
	}
	
	/**
	 * Reparte los conductores al finalizar una etapa
	 * @param conduct conductores
	 * @param id identificador de la �ltima etapa q se llev� a cabo
	 */
	public void reparticionConductoresEtapas(List<Conductor>conduct,int id){
		for(Conductor c:conduct){
			
			
			for(int i=1;i<etapas.size();i++){
				if(etapas.get(i).getServiciosFinalizados()==null){
					etapas.get(i).setServiciosFinalizados(0);
				}
				
				if(c.getTiempoDisponible()>etapas.get(i).getHoraInicial()&&c.getTiempoDisponible()<etapas.get(i).getHoraFinal()){
					etapas.get(i).setServiciosFinalizados(1+etapas.get(i).getServiciosFinalizados());
				}
				if(c.getTiempoDisponible()>=etapas.get(i-1).getHoraInicial()&&c.getTiempoDisponible()<=etapas.get(i).getHoraInicial()){
					etapas.get(i).conductores.add(c);
					//etapas.get(i).setServiciosFinalizados(1+etapas.get(i).getServiciosFinalizados());
					i=etapas.size()+2;
				}
			}
		}
	}
	
	/**
	 * Metodo que corre la simulaci�n completa
	 * @param excelSheet
	 */
	public void simulacionOptimizacion(WritableSheet excelSheet) {
		   
			try{

	    createLabel();
	 
		addCaption(excelSheet,1,1,"Intervalo (Etapa)");
		addCaption(excelSheet,2,1,"Hora inicio (segundos)");
		addCaption(excelSheet,3,1,"Hora fin (segundos)");
		addCaption(excelSheet,4,1,"Vehiculos disponibles inicio etapa");
		addCaption(excelSheet,5,1,"Servicios Finalizados");
		addCaption(excelSheet,6,1,"Nuevas solicitudes");
		addCaption(excelSheet,7,1,"Solicitudes asignadas");
		addCaption(excelSheet,8,1,"Solicitudes no asignadas en la etapa");
		addCaption(excelSheet,9,1,"Clientes perdidos");
		addCaption(excelSheet,10,1,"Tiempo de espera promedio (segundos)");
		
			asignarPasajerosAEtapas();
			etapas.get(0).conductores=conductoresIniciales;
			etapas.get(0).setServiciosFinalizados(0);
			for(int i=0;i<etapas.size();i++){
				etapas.get(i).generarModelo();
				reparticionConductoresEtapas(etapas.get(i).conductoresSinAsignar, i);
				if(i<etapas.size()-1){
					if(etapas.get(i).pasajerosSinAsignar.size()>0){
						etapas.get(i+1).pasajeros.addAll(etapas.get(i).pasajerosSinAsignar);
					}
				}
				addLabel(excelSheet, 1, (i+2), i+"");
				addNumber(excelSheet, 2, (i+2), etapas.get(i).getHoraInicial()); 
				addNumber(excelSheet, 3, (i+2), etapas.get(i).getHoraFinal());
				addNumber(excelSheet, 4, (i+2), (double)etapas.get(i).getVehiculosDisponiblesInicio());
				addNumber(excelSheet, 5, (i+2), (double)etapas.get(i).getServiciosFinalizados());
				addNumber(excelSheet, 6, (i+2), (double)etapas.get(i).getNuevasSolicitudes());
				addNumber(excelSheet, 7, (i+2), (double)etapas.get(i).getSolicitudesAsignadas());
				addNumber(excelSheet, 8, (i+2), (double)etapas.get(i).getSolicitudesNoAsignadas());
				addNumber(excelSheet, 9, (i+2), (double)etapas.get(i).getClientesPerdidos());
				addNumber(excelSheet, 10, (i+2), etapas.get(i).getTiempoEsperaPromedio());
				nivelServicio+=(double)etapas.get(i).getClientesPerdidos();
				addCaption(excelSheet, ((1+i)*2+12),0,"Asignaci�n etapa"+i);
				//addCaption(excelSheet, ((1+i)*2+13),0," etapa"+i);
				addCaption(excelSheet, ((1+i)*2+12),1,"Pasajero");
				addCaption(excelSheet, ((1+i)*2+13),1,"Conductor");
				HashMap<Pasajero,Conductor> asign=etapas.get(i).asignacion;
				Set<Pasajero> pasajAsign=asign.keySet();
				int contador=0;
				for(Pasajero pa:pasajAsign){
					Conductor cond=asign.get(pa);
					addLabel(excelSheet,((1+i)*2+12) , (contador+2), "Pasajero "+pa.getId());
					addLabel(excelSheet,((1+i)*2+13) , (contador+2), "Conductor "+cond.getId());
					contador++;
				}
				System.out.println("Nuevas solicitudes: "+etapas.get(i).getNuevasSolicitudes());
				System.out.println("Clientes perdidos: "+etapas.get(i).getClientesPerdidos());
				System.out.println("Servicios finalizados: "+etapas.get(i).getServiciosFinalizados());
				System.out.println("Solicitudes asignadas: "+etapas.get(i).getSolicitudesAsignadas());
				System.out.println("Solicitudes no asignadas: "+etapas.get(i).getSolicitudesNoAsignadas());
				System.out.println("Tiempo promedio: "+etapas.get(i).getTiempoEsperaPromedio());
				System.out.println("Conductores disponibles iniciales: "+etapas.get(i).getVehiculosDisponiblesInicio());
			}
			addCaption(excelSheet,9,etapas.size()+2,"Tiempo promedio total");
			addNumber(excelSheet,10,etapas.size()+2,obtenerPromedioTotal());

			nivelServicio=1-(nivelServicio/317.0);
			tiempoPromedioTotal=obtenerPromedioTotal();
			addCaption(excelSheet,9,etapas.size()+3,"Nivel servicio");
			addNumber(excelSheet,10,etapas.size()+3,nivelServicio);
		} catch (Exception e) {
			e.printStackTrace();
			//JOptionPane.showMessageDialog (null, "llllllllll! no se pudo guardar bien tu archivo, vuelve a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	/**
	 * Metodo para imprimir en archivo Excel
	 * @throws WriteException
	 */
	  public void createLabel() throws WriteException {
		    // Lets create a times font
		    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		    // Define the cell format
		    times = new WritableCellFormat(times10pt);
		    // Lets automatically wrap the cells
		    times.setWrap(true);

		    // create create a bold font with unterlines
		    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,UnderlineStyle.SINGLE);
		    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		    // Lets automatically wrap the cells
		    timesBoldUnderline.setWrap(true);

		    CellView cv = new CellView();
		    cv.setFormat(times);
		    cv.setFormat(timesBoldUnderline);
		    cv.setAutosize(true);

		    

		  }
	  
	  /**
		 * Metodo para imprimir en archivo Excel
		 * @throws WriteException
		 */
	  private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
    // Write a few number
    
    // Lets calculate the sum of it
    StringBuffer buf = new StringBuffer();
    buf.append("SUM(A2:A10)");
    Formula f = new Formula(0, 10, buf.toString());
    sheet.addCell(f);
    buf = new StringBuffer();
    buf.append("SUM(B2:B10)");
    f = new Formula(1, 10, buf.toString());
    sheet.addCell(f);

    // now a bit of text
    for (int i = 12; i < 20; i++) {
      // First column
      addLabel(sheet, 0, i, "Boring text " + i);
      // Second column
      addLabel(sheet, 1, i, "Another text");
    }
  }
	  
	  /**
	   * Metodo para imprimir en archivo Excel
	   * @param sheet
	   * @param column
	   * @param row
	   * @param s
	   * @throws RowsExceededException
	   * @throws WriteException
	   */
	  public void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
		    Label label;
		    label = new Label(column, row, s, timesBoldUnderline);
		    sheet.addCell(label);
		  }
	  /**
	   * Metodo para imprimir en archivo Excel
	   * @param sheet
	   * @param column
	   * @param row
	   * @throws RowsExceededException
	   * @throws WriteException
	   */
	  public void addNumber(WritableSheet sheet, int column, int row,Double num) throws WriteException, RowsExceededException {
		  Number number;
		    number = new Number(column, row, num, times);
		    sheet.addCell(number);
		  }
	  /**
	   * Metodo para imprimir en archivo Excel
	   * @param sheet
	   * @param column
	   * @param row
	   * @param s
	   * @throws RowsExceededException
	   * @throws WriteException
	   */
	  public void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
		    Label label;
		    label = new Label(column, row, s, times);
		    sheet.addCell(label);
		  }


//	public static void main(String[] args) {
//		SimulacionOptimizacion s=new SimulacionOptimizacion(30.0*60.0);
//		s.simulacionOptimizacion();
//	}
	
	
	
//	
//	public static void main(String[] args) {
//		int a=9;
//		int b=a;
//		b=b-2;
//		System.out.println(""+a+" b: "+b);
//		System.out.println(Math.ceil(101.0/50.0));
//		SimulacionOptimizacion sim=new SimulacionOptimizacion(1.0);
//		try {
//			sim.asignarPasajerosAEtapas();
//			for(int i=0;i<sim.etapas.size();i++){
//				Etapa e=sim.etapas.get(i);
//				System.out.println("Hora final: "+e.getHoraFinal()+" Hora inicial "+e.getHoraInicial());
//				for(int j=0;j<e.pasajeros.size();j++){
//					System.out.println(e.pasajeros.size());
//					System.out.println("Pasajero: "+e.pasajeros.get(j).getHoraSolicitud());
//				}
//			}
//			System.out.println(sim.etapas);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

	 
}
