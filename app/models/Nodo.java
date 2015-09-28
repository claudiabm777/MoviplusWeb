package models;

public class Nodo {
	//-------------------------------------------------------------------------------
	//Atributos---------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Double b;
	public String nombre;
	public Pasajero pasajero;
	public Conductor conductor;
	
	//-------------------------------------------------------------------------------
	//Constructor---------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Nodo(Double b,String nombre, Pasajero pasajero,Conductor conductor){
		this.b=b;
		this.nombre=nombre;
		this.pasajero=pasajero;
		this.conductor=conductor;
	}
}
