package models;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Etapa {
	//-------------------------------------------------------------------------------
	//Atributos----------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	private Double ventanaTiempo;
	public HashMap<Pasajero,Conductor>asignacion;
	public List<Pasajero>pasajeros;
	public List<Conductor>conductores;
	private Long id; //identificador de la etapa
	private Double horaInicial;
	private Double horaFinal;
	private Integer vehiculosDisponiblesInicio;
	private Integer serviciosFinalizados;
	private Integer nuevasSolicitudes;
	private Integer solicitudesAsignadas;
	private Integer solicitudesNoAsignadas;
	private Integer clientesPerdidos;
	private Double tiempoEsperaPromedio;
	
	public List<Pasajero>pasajerosSinAsignar;
	public List<Conductor>conductoresSinAsignar;
	private List<Nodo>nodosPasajeros;
	private List<Nodo>nodosConductores;
	public List<Pasajero>listaPerdidos; //lista pasajeros que ya cumplieron tiempo de impaciencia
	public Arco[][]arcos;
    public double[][]costos;
	//public GRBVar[][]variables;
	//-------------------------------------------------------------------------------
	//Constructor--------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Etapa(Double ventanaTiempo,Long id,Double horaInicial,Double horaFinal){
		this.ventanaTiempo=ventanaTiempo;
		asignacion=new HashMap<Pasajero,Conductor>();
		pasajeros=new ArrayList<Pasajero>();
		conductores=new ArrayList<Conductor>();
		nodosPasajeros=new ArrayList<Nodo>();
		nodosConductores=new ArrayList<Nodo>();
		pasajerosSinAsignar=new ArrayList<Pasajero>();
		conductoresSinAsignar=new ArrayList<Conductor>();
		listaPerdidos=new ArrayList<Pasajero>();
		this.id=id;
		this.horaInicial=horaInicial;
		this.horaFinal=horaFinal;
		vehiculosDisponiblesInicio=null;
		serviciosFinalizados=null;
		nuevasSolicitudes=null;
		solicitudesAsignadas=null;
		solicitudesNoAsignadas=null;
		clientesPerdidos=null;
		tiempoEsperaPromedio=null;
		
	}
	
	

	//-------------------------------------------------------------------------------
	//Getters and Setters------------------------------------------------------------
	//-------------------------------------------------------------------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(Double horaInicial) {
		this.horaInicial = horaInicial;
	}

	public Double getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Double horaFinal) {
		this.horaFinal = horaFinal;
	}

	public Integer getVehiculosDisponiblesInicio() {
		return vehiculosDisponiblesInicio;
	}

	public void setVehiculosDisponiblesInicio(Integer vehiculosDisponiblesInicio) {
		this.vehiculosDisponiblesInicio = vehiculosDisponiblesInicio;
	}

	public Integer getServiciosFinalizados() {
		return serviciosFinalizados;
	}

	public void setServiciosFinalizados(Integer serviciosFinalizados) {
		this.serviciosFinalizados = serviciosFinalizados;
	}

	public Integer getNuevasSolicitudes() {
		return nuevasSolicitudes;
	}

	public void setNuevasSolicitudes(Integer nuevasSolicitudes) {
		this.nuevasSolicitudes = nuevasSolicitudes;
	}

	public Integer getSolicitudesAsignadas() {
		return solicitudesAsignadas;
	}

	public void setSolicitudesAsignadas(Integer solicitudesAsignadas) {
		this.solicitudesAsignadas = solicitudesAsignadas;
	}

	public Integer getSolicitudesNoAsignadas() {
		return solicitudesNoAsignadas;
	}

	public void setSolicitudesNoAsignadas(Integer solicitudesNoAsignadas) {
		this.solicitudesNoAsignadas = solicitudesNoAsignadas;
	}

	public Integer getClientesPerdidos() {
		return clientesPerdidos;
	}

	public void setClientesPerdidos(Integer clientesPerdidos) {
		this.clientesPerdidos = clientesPerdidos;
	}

	public Double getTiempoEsperaPromedio() {
		return tiempoEsperaPromedio;
	}

	public void setTiempoEsperaPromedio(Double tiempoEsperaPromedio) {
		this.tiempoEsperaPromedio = tiempoEsperaPromedio;
	}

	
	public Double getVentanaTiempo() {
		return ventanaTiempo;
	}

	public void setVentanaTiempo(Double ventanaTiempo) {
		this.ventanaTiempo = ventanaTiempo;
	}

	public HashMap<Pasajero, Conductor> getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(HashMap<Pasajero, Conductor> asignacion) {
		this.asignacion = asignacion;
	}

	public List<Pasajero> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<Pasajero> pasajeros) {
		this.pasajeros = pasajeros;
	}

	public List<Conductor> getConductores() {
		return conductores;
	}

	public void setConductores(List<Conductor> conductores) {
		this.conductores = conductores;
	}

	//-------------------------------------------------------------------------------
	//Metodos------------------------------------------------------------------------
	//-------------------------------------------------------------------------------
	/**
	 * Este m�todo genera el modelo de la aplicaci�n, usando gurobi
	 */
	public void generarModelo(){
		crearNodos();
		int numeroFicticeos=pasajeros.size()-conductores.size();
		if(numeroFicticeos>0){
			agregarConductoresFicticeos(Math.abs(numeroFicticeos));
		}
		else if(numeroFicticeos<0){
			agregarPasajerosFicticeos(Math.abs(numeroFicticeos));
		}
		//ya estan niveladas las variables de oferta y demanda
		//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHH NP: "+nodosPasajeros.size()+" NC: "+nodosConductores.size());
		crearArcos();
		 //GRBEnv env;
		try {
            costos=new double[nodosPasajeros.size()][nodosConductores.size()];
            for(int i=0;i<nodosPasajeros.size();i++) {
                for (int j = 0; j < nodosConductores.size(); j++) {
                   costos[i][j]= arcos[i][j].c;
                }
            }
            HungarianAlgorithm ha = new HungarianAlgorithm(costos);
            
            int k = 0;
            for(Integer i : ha.execute()) {
                System.out.println((k + 1) + " -> " + (i + 1));
                k++;
            }
            
             int i=0;
			for(Integer j:ha.execute()){
				//for(int j=0;j<nodosConductores.size();j++){
					//if(variables[i][j].get(GRB.DoubleAttr.X)>0)
					//System.out.println(variables[i][j].get(GRB.StringAttr.VarName)+ " " +variables[i][j].get(GRB.DoubleAttr.X));
					//if(variables[i][j].get(GRB.DoubleAttr.X)>0.0){
						if(nodosPasajeros.get(i).pasajero!=null&&nodosConductores.get(j).conductor!=null){
							//asignacion.put(nodosPasajeros.get(i).pasajero, nodosConductores.get(j).conductor);
							int distanciaC=Math.abs(nodosPasajeros.get(i).pasajero.getCalleInicial()-nodosConductores.get(j).conductor.getCalle());
							int distanciaCr=Math.abs(nodosPasajeros.get(i).pasajero.getCarreraInicial()-nodosConductores.get(j).conductor.getCarrera());
							Double dist=(double) (distanciaC+distanciaCr);
							Double tiempoE=dist*Simulacion.DISTANCIA_CUADRAS/Simulacion.VELOCIDAD_CONDUCTORES;
							Double tiempo2=horaFinal-nodosPasajeros.get(i).pasajero.getHoraSolicitud();
							nodosPasajeros.get(i).pasajero.setTiempoEspera(nodosPasajeros.get(i).pasajero.getTiempoEspera()+tiempoE+tiempo2);
							asignacion.put(nodosPasajeros.get(i).pasajero, nodosConductores.get(j).conductor);
							Conductor c=new Conductor(nodosPasajeros.get(i).pasajero.getCalleFinal(),nodosPasajeros.get(i).pasajero.getCarreraFinal(),nodosConductores.get(j).conductor.getId());
							int distanciaC2=Math.abs(nodosPasajeros.get(i).pasajero.getCalleInicial()-nodosPasajeros.get(i).pasajero.getCalleFinal());
							int distanciaCr2=Math.abs(nodosPasajeros.get(i).pasajero.getCarreraInicial()-nodosPasajeros.get(i).pasajero.getCarreraFinal());
							Double dist2=(double) (distanciaC2+distanciaCr2);
							Double tiempoE2=dist2*Simulacion.DISTANCIA_CUADRAS/Simulacion.VELOCIDAD_CONDUCTORES;
							Double tiempoDispCN=tiempoE2+tiempoE;	
							c.setTiempoDisponible(ventanaTiempo*(id)+tiempoDispCN);
							conductoresSinAsignar.add(c);
							
						}
					//}
					
				//}
                i++;
			}
			
			actualizarInformacionEtapa();

		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}
	
	/**
	 * Este m�todo actualiza la informaci�n de la etapa de acuerdo a los resultados obtenidos luego de correr el modelo de optimizaci�n
	 */
	public void actualizarInformacionEtapa(){
		vehiculosDisponiblesInicio=conductores.size();
		//serviciosFinalizados se actualiza en las demas etapas
		nuevasSolicitudes=pasajeros.size();
		solicitudesAsignadas=asignacion.keySet().size();
		clientesPerdidos=0;
		if(solicitudesAsignadas<nuevasSolicitudes){
			solicitudesNoAsignadas=nuevasSolicitudes-solicitudesAsignadas;
			for(int i=0;i<pasajeros.size();i++){
				Conductor c=asignacion.get(pasajeros.get(i));
				if(c==null){
					//System.out.println(" - "+pasajeros.get(i).getHoraSolicitud());
					Double tiempo=horaFinal-pasajeros.get(i).getHoraSolicitud();
					pasajeros.get(i).setTiempoEspera(pasajeros.get(i).getTiempoEspera()+tiempo);
					if(pasajeros.get(i).getTiempoEspera()>=Simulacion.TIEMPO_IMPACIENCIA){
						clientesPerdidos++;
						listaPerdidos.add(pasajeros.get(i));
					}else{
						pasajerosSinAsignar.add(pasajeros.get(i));
					}
					
				}
			}
			
		}else{
			solicitudesNoAsignadas=0;
		}
		Collection<Conductor>condAsign=asignacion.values();
		
		for(int i=0;i<conductores.size();i++){
			int c=0;
			for(Conductor co:condAsign){
				if(conductores.get(i).getId()==co.getId()){
					c++;
				}
			}
			if(c==0){
				conductores.get(i).setTiempoDisponible(ventanaTiempo*(id));
				conductoresSinAsignar.add(conductores.get(i));
			}
		}
		tiempoEsperaPromedio=tiempoEsperaPromedio();
		
	}
	/**
	 * Este metodo calcula el tiempo de espera promedio de la etapa
	 * @return el tiempo de espera promedio
	 */
	public Double tiempoEsperaPromedio(){
		Double respuesta=0.0;
		Set<Pasajero>p1=asignacion.keySet();
		
		for(Pasajero p:p1){
			respuesta+=p.getTiempoEspera();
		}
		for(int i=0;i<pasajerosSinAsignar.size();i++){
			respuesta+=pasajerosSinAsignar.get(i).getTiempoEspera();
		}
		if((((double)(p1.size()+pasajerosSinAsignar.size())))==0.0){
			return respuesta=0.0;
		}
		int c=0;
		if(clientesPerdidos>0){
			c=1;
		}
		return (respuesta/((double)(p1.size()+pasajerosSinAsignar.size())))+c*Simulacion.TIEMPO_IMPACIENCIA;
	}
	/**
	 * Este metodo crea los nodos a usar en el modelo de optimizaci�n para resolver el problema de asignacion
	 */
	public void crearNodos(){
		for(int i=0; i<pasajeros.size();i++){
			Nodo n=new Nodo(-1.0,pasajeros.get(i).getId()+"",pasajeros.get(i),null);
			nodosPasajeros.add(n);
		}
		
		for(int i=0; i<conductores.size();i++){
			Nodo n=new Nodo(1.0,conductores.get(i).getId()+"",null,conductores.get(i));
			nodosConductores.add(n);
		}
	}
	/**
	 * Este metodo agraga pasajeros ficticios en caso de que hayan m�s conductores
	 * @param numeroFicticeos
	 */
	public void agregarPasajerosFicticeos(int numeroFicticeos){
		for(int i=0;i<numeroFicticeos;i++){
			Nodo n=new Nodo(-1.0,"NodoFiciticeoP_"+i,null,null);
			nodosPasajeros.add(n);
		}
	}
	/**
	 * Este metodo agraga conductores ficticios en caso de que hayan m�s pasajeros
	 * @param numeroFicticeos
	 */
	public void agregarConductoresFicticeos(int numeroFicticeos){
		for(int i=0;i<numeroFicticeos;i++){
			Nodo n=new Nodo(1.0,"NodoFiciticeoC_"+i,null,null);
			nodosConductores.add(n);
		}
	}
	
	/**
	 * Este m�todo crea los arcos y su informaci�n para ser usados en el modelo de optimizaci�n,
	 */
	public void crearArcos(){
		arcos=new Arco[nodosPasajeros.size()][nodosConductores.size()];
		for(int i=0;i<nodosPasajeros.size();i++){
			for(int j=0;j<nodosConductores.size();j++){
				Double costo=null;
				if(nodosPasajeros.get(i).conductor==null && nodosPasajeros.get(i).pasajero==null){
					costo=0.0;
				}
				else if(nodosConductores.get(j).conductor==null && nodosConductores.get(j).pasajero==null){
					costo=0.0;
				}
				else{
					int distanciaC=Math.abs(nodosPasajeros.get(i).pasajero.getCalleInicial()-nodosConductores.get(j).conductor.getCalle());
					int distanciaCr=Math.abs(nodosPasajeros.get(i).pasajero.getCarreraInicial()-nodosConductores.get(j).conductor.getCarrera());
					
					costo=(double)distanciaC+distanciaCr;
				}
				arcos[i][j]=new Arco(costo,nodosPasajeros.get(i),nodosConductores.get(j));
			}
		}
	}
//	 public static void main(String[] args) {
//		 try {
//			 GRBEnv    env   = new GRBEnv("mip1.log");
//			 GRBModel  model = new GRBModel(env);
//
//			 // Create variables
//
//			 GRBVar x = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x");
//			 GRBVar y = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "y");
//			 GRBVar z = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "z");
//
//			 // Integrate new variables
//
//			 model.update();
//
//			 // Set objective: maximize x + y + 2 z
//
//			 GRBLinExpr expr = new GRBLinExpr();
//			 expr.addTerm(1.0, x); expr.addTerm(1.0, y); expr.addTerm(2.0, z);
//			 model.setObjective(expr, GRB.MAXIMIZE);
//
//			 // Add constraint: x + 2 y + 3 z <= 4
//
//			 expr = new GRBLinExpr();
//			 expr.addTerm(1.0, x); expr.addTerm(2.0, y); expr.addTerm(3.0, z);
//			 model.addConstr(expr, GRB.LESS_EQUAL, 4.0, "c0");
//
//			 // Add constraint: x + y >= 1
//
//			 expr = new GRBLinExpr();
//			 expr.addTerm(1.0, x); expr.addTerm(1.0, y);
//			 model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c1");
//
//			 // Optimize model
//
//			 model.optimize();
//
//			 System.out.println(x.get(GRB.StringAttr.VarName)
//					 + " " +x.get(GRB.DoubleAttr.X));
//			 System.out.println(y.get(GRB.StringAttr.VarName)
//					 + " " +y.get(GRB.DoubleAttr.X));
//			 System.out.println(z.get(GRB.StringAttr.VarName)
//					 + " " +z.get(GRB.DoubleAttr.X));
//
//			 System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));
//
//			 // Dispose of model and environment
//
//			 model.dispose();
//			 env.dispose();
//
//		 } catch (GRBException e) {
//			 System.out.println("Error code: " + e.getErrorCode() + ". " +
//					 e.getMessage());
//		 }
//		  }	

}
