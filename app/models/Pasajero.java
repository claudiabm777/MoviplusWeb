package models;

public class Pasajero implements Comparable<Pasajero> {
	
	//-------------------------------------------------------------------------------
	//Atributos----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	private Double horaSolicitud;
	private Integer calleInicial;
	private Integer carreraInicial;
	private Integer calleFinal;
	private Integer carreraFinal;
	private Double tiempoEspera;
	private Long id;
	
	//-------------------------------------------------------------------------------
	//Constructor--------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Pasajero(Double horaSolicitud,Integer calleInicial,Integer carreraInicial, Integer calleFinal, Integer carreraFinal,Long id){
		this.horaSolicitud=horaSolicitud;
		this.calleInicial=calleInicial;
		this.carreraInicial=carreraInicial;
		this.calleFinal=calleFinal;
		this.carreraFinal=carreraFinal;
		this.tiempoEspera=0.0;
		this.id=id;
	}

	//-------------------------------------------------------------------------------
	//Getters and Setters------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Double getHoraSolicitud() {
		return horaSolicitud;
	}

	public void setHoraSolicitud(Double horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	public Integer getCalleInicial() {
		return calleInicial;
	}

	public void setCalleInicial(Integer calleInicial) {
		this.calleInicial = calleInicial;
	}

	public Integer getCarreraInicial() {
		return carreraInicial;
	}

	public void setCarreraInicial(Integer carreraInicial) {
		this.carreraInicial = carreraInicial;
	}

	public Integer getCalleFinal() {
		return calleFinal;
	}

	public void setCalleFinal(Integer calleFinal) {
		this.calleFinal = calleFinal;
	}

	public Integer getCarreraFinal() {
		return carreraFinal;
	}

	public void setCarreraFinal(Integer carreraFinal) {
		this.carreraFinal = carreraFinal;
	}

	public Double getTiempoEspera() {
		return tiempoEspera;
	}

	public void setTiempoEspera(Double tiempoEspera) {
		this.tiempoEspera = tiempoEspera;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public int compareTo(Pasajero other)
    {
        return Double.compare(horaSolicitud, other.getHoraSolicitud());
    }
}
