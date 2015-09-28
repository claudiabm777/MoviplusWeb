package models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class InterfazMoviplus extends JFrame implements ActionListener {
//Esta clase representa la interfaz de la aplicaci�n
	private Moviplus mundo;
	private JTextField txtSencilla;
	private JLabel lblSencilla;
	private JButton btnSencilla;
	
	private JTextField txtMultipleInferior;
	private JLabel lblMultipleInferior;
	private JTextField txtMultipleISuperior;
	private JLabel lblMultipleSuperior;
	private JTextField txtMultipleINumero;
	private JLabel lblMultipleNumero;
	private JButton btnMultiple;
	
	private JButton btnVecinos;
	private JLabel lblVecinos;
	
	public InterfazMoviplus(){
		setTitle("Simulaciones Moviplus");
		setSize(450,276);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		mundo=new Moviplus();
		JPanel panelSimSencilla=new JPanel();
		panelSimSencilla.setLayout(new GridLayout(2,2));
		panelSimSencilla.setPreferredSize(new Dimension(0,75));
		TitledBorder border=BorderFactory.createTitledBorder("Realizar simulaci�n sencilla");
		border.setTitleColor(Color.GRAY);
		panelSimSencilla.setBorder(border);
		
		JPanel panelSimMultiple=new JPanel();
		panelSimMultiple.setLayout(new GridLayout(4,2));
		panelSimMultiple.setPreferredSize(new Dimension(0,150));
		TitledBorder border2=BorderFactory.createTitledBorder("Realizar simulaci�n m�ltiple");
		border2.setTitleColor(Color.GRAY);
		panelSimMultiple.setBorder(border2);
		
		
		JPanel panelSimVecinos=new JPanel();
		panelSimVecinos.setLayout(new GridLayout(1,2));
		panelSimVecinos.setPreferredSize(new Dimension(0,50));
		TitledBorder border3=BorderFactory.createTitledBorder("Realizar simulaci�n vecinos m�s cercanos");
		border3.setTitleColor(Color.GRAY);
		panelSimVecinos.setBorder(border3);
		
		txtSencilla=new JTextField("10");
		lblSencilla=new JLabel("Ventana de tiempo (min):") ;
		btnSencilla=new JButton("Comenzar!");
		btnSencilla.setActionCommand("SENCILLA");
		btnSencilla.addActionListener(this);
		panelSimSencilla.add(lblSencilla);
		panelSimSencilla.add(txtSencilla);
		panelSimSencilla.add(new JLabel(""));
		panelSimSencilla.add(btnSencilla);
		
		txtMultipleInferior=new JTextField("10");
		lblMultipleInferior=new JLabel("Ventana de tiempo inferior (min):");
		txtMultipleISuperior=new JTextField("100");
		lblMultipleSuperior=new JLabel("Ventana de tiempo superior (min):");
		txtMultipleINumero=new JTextField("5");
		lblMultipleNumero=new JLabel("N�mero de simulaciones:");
		btnMultiple=new JButton("Comenzar!");
		btnMultiple.setActionCommand("MULTIPLE");
		btnMultiple.addActionListener(this);
		panelSimMultiple.add(lblMultipleInferior);
		panelSimMultiple.add(txtMultipleInferior);
		panelSimMultiple.add(lblMultipleSuperior);
		panelSimMultiple.add(txtMultipleISuperior);
		panelSimMultiple.add(lblMultipleNumero);
		panelSimMultiple.add(txtMultipleINumero);
		panelSimMultiple.add(new JLabel(""));
		panelSimMultiple.add(btnMultiple);
		
		lblVecinos=new JLabel("Simular pol�tica anterior:");
		btnVecinos=new JButton("Ver resultados");
		btnVecinos.setActionCommand("VECINOS");
		btnVecinos.addActionListener(this);
		panelSimVecinos.add(lblVecinos);
		panelSimVecinos.add(btnVecinos);
		
		
		add(panelSimSencilla,BorderLayout.NORTH);
		add(panelSimMultiple,BorderLayout.CENTER);
		add(panelSimVecinos,BorderLayout.SOUTH);
		
	}
	
	public static void main(String[] args) {
		InterfazMoviplus interfaz=new InterfazMoviplus();
		interfaz.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		if(comando.equals("SENCILLA")){
			try{
			Double t=Double.parseDouble(txtSencilla.getText().trim())*60.0;
			if(t<=0.0){
				JOptionPane.showMessageDialog (null, "La ventana de tiempo no puede ser cero o menos. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}else{
			mundo.simulacionOptimizacion(t, 0);
			
			}
			}catch(Exception ex){
				JOptionPane.showMessageDialog (null, "Debe ingresar un n�mero. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}
			
		}
		else if(comando.equalsIgnoreCase("MULTIPLE")){
			try{
			Double ti=Double.parseDouble(txtMultipleInferior.getText().trim())*60.0;
			Double ts=Double.parseDouble(txtMultipleISuperior.getText().trim())*60.0;
			Double n=Double.parseDouble(txtMultipleINumero.getText().trim());
			if(ti<=0.0||ts<=0.0||n<=1){
				JOptionPane.showMessageDialog (null, "Ning�n parametro puede ser cero o menor. El n�mero de simulaciones debe ser mayor a 1. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}else{
			mundo.simulacionMultiple(ts, ti, n);
			
			}
			}catch(Exception ex){
				JOptionPane.showMessageDialog (null, "Debe ingresar un n�mero. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}
		}
		else if(comando.equals("VECINOS")){
			mundo.simulacionVecinos();
		}
	}

}
