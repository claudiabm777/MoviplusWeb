package models;

public class Conductor {
	
	//-------------------------------------------------------------------------------
	//Atributos----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	private Integer calle;
	private Integer carrera;
	private Long id;
	private Double tiempoDisponible;
	
	//-------------------------------------------------------------------------------
	//Constructor--------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Conductor(Integer calle, Integer carrera,Long id){
		this.calle=calle;
		this.carrera=carrera;
		this.id=id;
		this.tiempoDisponible=0.0;
	}
	
	//-------------------------------------------------------------------------------
	//Getters and Setters------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Integer getCalle() {
		return calle;
	}

	public void setCalle(Integer calle) {
		this.calle = calle;
	}

	public Integer getCarrera() {
		return carrera;
	}

	public void setCarrera(Integer carrera) {
		this.carrera = carrera;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTiempoDisponible() {
		return tiempoDisponible;
	}

	public void setTiempoDisponible(Double tiempoDisponible) {
		this.tiempoDisponible = tiempoDisponible;
	}
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------

}
