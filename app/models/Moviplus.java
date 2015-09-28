package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Moviplus {
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	/**
	 * Metodo que corre la simulaci�n de los vecinos m�s cercanos
	 */
	public SimulacionVecinos simulacionVecinos(){
		SimulacionVecinos simulacion=new SimulacionVecinos();
		Double r=simulacion.simulacionVecinosCercanos();
		System.out.println("Tiempo espera total: "+r);
		int co=0;
		for(int i=0;i<simulacion.pasajerosFinales.size();i++){
			Pasajero pas=simulacion.pasajerosFinales.get(i);
			double p=pas.getHoraSolicitud();
			Conductor c=simulacion.asignacion.get(simulacion.pasajerosFinales.get(i));
			if(c!=null){
				System.out.println("El pasajero: "+pas.getId()+" fue asignado al conductor: "+c.getId());
			}
			else{
				co++;
				//System.out.println("El pasajero: "+pas.getId()+" tiene tiempo: "+p+" - NO ENTROOOO");
			}
		}
		System.out.println(co);
        return simulacion;
	}
	
	/**
	 * Metodo que llama la interfaz para realizar la simulaci�n simple
	 * @param tiempo
	 * @param id
	 */
	public File simulacionOptimizacion(Double tiempo,int id){
        File file = new File("/tmp/resultados.xls");
        try {

            String content = "This is the content to write into file";

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.close();

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }



            WorkbookSettings wbSettings = new WorkbookSettings();

			    wbSettings.setLocale(new Locale("en", "EN"));

			    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
			    workbook.createSheet("Simulacion"+id, id);
			    WritableSheet excelSheet = workbook.getSheet(id);
		SimulacionOptimizacion s=new SimulacionOptimizacion(tiempo);
		s.simulacionOptimizacion(excelSheet);

	    workbook.write();
	    workbook.close();
	    JOptionPane.showMessageDialog (null, "Ya se realizó la simulación. \nPor favor consulte los resultados en la ruta que especific�.", "Sus resultados estan listos!", JOptionPane.INFORMATION_MESSAGE);

			} catch (WriteException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Uppss! no se pudo guardar bien tu archivo, vuelve a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

		}
        return new File("/tmp/resultados.xls");
	}
	
	/**
	 * Metodo auxiliar para realizar la simulaci�n multiple
	 * @param tiempo
	 * @param excelSheet
	 * @param simulaciones
	 */
	public void simulacionOptimizacionM(Double tiempo,WritableSheet excelSheet,List<SimulacionOptimizacion>simulaciones ){
		
			
		SimulacionOptimizacion s=new SimulacionOptimizacion(tiempo);
		s.simulacionOptimizacion(excelSheet);
		simulaciones.add(s);
			

		
	}
	
	/**
	 * METODO QUE ES LLAMADO POR LA INTERFAZ PARA REALIZAR LA SIMULACI�N M�LTIPLE
	 * @param superior
	 * @param inferior
	 * @param veces
	 */
	public File simulacionMultiple(double superior, double inferior, double veces){
        File file = new File("/tmp/resultados.xls");
        try{
            String content = "This is the content to write into file";
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.close();


            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }




            WorkbookSettings wbSettings = new WorkbookSettings();

		    wbSettings.setLocale(new Locale("en", "EN"));

		    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

		
		double tamVentanas=(superior-inferior)/(veces-1);
		Double[]ventanas=new Double[(int)veces];
		List<SimulacionOptimizacion>simulaciones=new ArrayList<SimulacionOptimizacion>();
		for(int i=0;i<veces;i++){
		    workbook.createSheet("Simulacion"+i, i);
		    WritableSheet excelSheet = workbook.getSheet(i);
		    if(i==0){
			ventanas[i]=inferior;
		    }
		    else{
		    	ventanas[i]=tamVentanas+ventanas[i-1];
		    }
			simulacionOptimizacionM(ventanas[i],excelSheet,simulaciones);
		}
		workbook.createSheet("ResultadosFinales",(int)veces);
	    WritableSheet excel = workbook.getSheet((int)veces);
	    SimulacionOptimizacion nn=new SimulacionOptimizacion(3.3);
	    nn.createLabel();
	    
	    nn.addCaption(excel,1,1,"Simulaci�n");
	    nn.addCaption(excel,2,1,"Ventana");
	    nn.addCaption(excel,3,1,"Tiempo promedio total");
	    nn.addCaption(excel,4,1,"Nivel Servicio");
	    String nombre="";
	    Double ventana=0.0;
	    Double tiempo=999999999999.0;
	    Double nivel=0.0;
	    for(int i=0;i<simulaciones.size();i++){
	    	nn.addLabel(excel,1,(i+2),"Simulaci�n"+i);
	    	nn.addNumber(excel,2,(i+2),simulaciones.get(i).ventanaTiempo);
	    	nn.addNumber(excel,3,(i+2),simulaciones.get(i).tiempoPromedioTotal);
	    	nn.addNumber(excel,4,(i+2),simulaciones.get(i).nivelServicio);
	    	if(tiempo>simulaciones.get(i).tiempoPromedioTotal){
	    		tiempo=simulaciones.get(i).tiempoPromedioTotal;
	    		nombre="Simulaci�n"+i;
	    		ventana=simulaciones.get(i).ventanaTiempo;
	    		nivel=simulaciones.get(i).nivelServicio;
	    	}
	    }
	    nn.addCaption(excel,1,(simulaciones.size()+3),"La mejor ventana es:");
	    nn.addLabel(excel,1,(simulaciones.size()+4),"Simulaci�n");
	    nn.addLabel(excel,2,(simulaciones.size()+4),nombre);
	    nn.addLabel(excel,1,(simulaciones.size()+5),"Ventana");
	    nn.addNumber(excel,2,(simulaciones.size()+5),ventana);
	    nn.addLabel(excel,1,(simulaciones.size()+6),"Tiempo promedio total");
	    nn.addNumber(excel,2,(simulaciones.size()+6),tiempo);
	    nn.addLabel(excel,1,(simulaciones.size()+7),"Nivel servicio");
	    nn.addNumber(excel,2,(simulaciones.size()+7),nivel);
	    workbook.write();
	    workbook.close();
	    JOptionPane.showMessageDialog (null, "Ya se realiz� la simulaci�n. \nPor favor consulte los resultados en la ruta que especific�.", "Sus resultados estan listos!", JOptionPane.INFORMATION_MESSAGE);

		} catch (RowsExceededException e1) {
            e1.printStackTrace();
        } catch (WriteException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();


	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, "Uppss! no se pudo guardar bien tu archivo, vuelve a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

	}
        return  new File("/tmp/resultados.xls");
	}
	
}
