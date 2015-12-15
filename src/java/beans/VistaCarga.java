package beans;

import carga.Asignador;
import carga.Balanceador;
import carga.Cargador;
import carga.Clasificador;
import carga.Confirmador;
import carga.CreadorSQL;
import carga.Validador;
import dao.GestorDAO;
import dao.UsuarioDAO;
import dto.Asignacion;
import dto.Fila;
import dto.Gestor;
import dto.Usuario;
import impl.GestorIMPL;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import jxl.read.biff.BiffException;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class VistaCarga implements Serializable {
  
  private static final int EN_LA_FIESTA = 1;
  private static final int ESTABA_EN_LA_FIESTA = 2;
  private static final int NUEVO_CREDITO = 3;
  private static final int NUEVO_TOTAL = 4;
  
  private boolean paso1;
  private boolean paso2;
  private boolean paso3;
  private boolean paso4;
  private boolean paso5;
  private boolean paso6;
  
  private TabView tabView;
  private Cargador cargador;
  private int numeroDeCreditos;
  private boolean verNumeroDeCreditos;
  
  private List<Fila> filas;
  private List<Fila> enLaFiesta;
  private List<Fila> estabanEnLaFiesta;
  private List<Fila> nuevosCreditos;
  private List<Fila> nuevosTotales;
  
  private int tamFiesta = 0;
  private int tamEstaban = 0;
  private int tamTotales = 0;
  private int tamCreditos = 0;
  
  private GestorDAO gestorDao;
  private List<Gestor> gestores;
  private List<String> idSeleccionados;
  
  private String criterioDeAsignacion;
  
  private List<Asignacion> asignaciones;
  private Asignacion asignacionActual;
  private Fila creditoActual;
  private int gestorAReasignar;
  
  String archivoSql;
  
  
  public void iniciarCarga() {
    cargador = new Cargador();
    setPaso1(false);
    setPaso2(false);
    setPaso3(false);
    setPaso4(false);
    setPaso5(false);
    setVerNumeroDeCreditos(false);
    tabView.setActiveIndex(1);
    enLaFiesta = new ArrayList<>();
    estabanEnLaFiesta = new ArrayList<>();
    nuevosCreditos = new ArrayList<>();
    nuevosTotales = new ArrayList<>();
    gestorDao = new GestorIMPL();
    criterioDeAsignacion = "";
  }
  
  public void subirArchivo(FileUploadEvent evento) throws IOException, BiffException {
    FacesContext context = FacesContext.getCurrentInstance();
    String archivoSubido = cargador.subirArchivo(evento);
    
    if (archivoSubido != null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "El archivo de la remesa se ha cargado exitosamente:",
              archivoSubido));
      setPaso1(true);
      tabView.setActiveIndex(2);
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo cargar el archivo.",
              "Comunique esta situación a Soporte Técnico."));
    }
  }
  
  public void leerArchivo() throws IOException, BiffException {
    FacesContext context = FacesContext.getCurrentInstance();
    filas = cargador.leerArchivo();
    numeroDeCreditos = filas.size();
    
    if (numeroDeCreditos > 0) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se ha leído el archivo de la remesa:",
              "Se encontraron " + numeroDeCreditos + " créditos."));
      setPaso2(true);
      setVerNumeroDeCreditos(true); // ??????????????
      tabView.setActiveIndex(3);
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo leer el archivo.",
              "Comunique esta situación a Soporte Técnico."));
    }
  }
  
  public void validarDatos() {
    FacesContext context = FacesContext.getCurrentInstance();
    
    boolean continuar = true;
    
    if (numeroDeCreditos > 0) {
      for (Fila f : filas) {
        Validador.validarFila(f);
        if (!f.getError().equals("NO")) { // Ocurrió un error
          context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                  f.getError(), f.getDetalleError()));
          continuar = false;
          break;
        }
      }
    }
    
    if (continuar) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se han validado los datos: ",
              "todos los datos son correctos"));
      setPaso3(true);
      tabView.setActiveIndex(4);
    }
  }
  
  public void clasificarCreditos() {
    FacesContext context = FacesContext.getCurrentInstance();
    
    filas = Clasificador.clasificar(filas);
    clasificar();
    tamFiesta = enLaFiesta.size();
    tamEstaban = estabanEnLaFiesta.size();
    tamCreditos = nuevosCreditos.size();
    tamTotales = nuevosTotales.size();

    setPaso4(true);
    tabView.setActiveIndex(5);
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Se han clasificado todos los créditos.", "La tabla muestra más detalle"));
    gestores = gestorDao.buscarPorDespacho(2);// Busca a los gestores del Corp correspondiente
    idSeleccionados = new ArrayList<>();
  }
  
  public void asignarCreditos() {
    FacesContext context = FacesContext.getCurrentInstance();
    asignaciones = new ArrayList<>();
    
    String mensaje = "Se realizará la asignación por " + criterioDeAsignacion;
    String mensaje2 = ": ";
    for (String i : idSeleccionados) {
      mensaje2 += i + " ";
    }
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            mensaje, mensaje2));
    
    Asignador.asignar(nuevosTotales, idSeleccionados, criterioDeAsignacion);
    //***************************************************
    Asignacion asignacion;
    
    for (String g : idSeleccionados) {
      asignacion = new Asignacion();
      asignacion.setGestor(Integer.parseInt(g));
      for (Fila f : nuevosTotales) {
        if (f.getIdGestor() == Integer.parseInt(g)) {
          asignacion.setCredito(f);
        }
      }
      asignaciones.add(asignacion);
    }
    //***************************************************
    balancearCarga();
    
    setPaso5(true);
    tabView.setActiveIndex(6);

  }
  
  public void balancearCarga() {
    asignacionActual = asignaciones.get(0);
    asignaciones.remove(0);
  }
  
  public void reasignar() {
    FacesContext context = FacesContext.getCurrentInstance();
    Balanceador.reasignar(creditoActual, gestorAReasignar, asignaciones, asignacionActual);
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Reasignación Exitosa", "Se Reasignó crédito: " + creditoActual.getCredito()));
  }
  
  public void crearSQL() {
    setPaso6(true);
    tabView.setActiveIndex(7);
    imprimirTodo();
    
    CreadorSQL.crearSujetos(nuevosTotales);
    archivoSql = CreadorSQL.crearSQL(nuevosTotales);
  }
  
  public void confirmarCarga() throws IOException, SQLException {
    Confirmador.ejecutarScriptSql(archivoSql);
  }
  
  private void clasificar() {
    for (Fila f : filas) {
      switch (f.getClasificacion()) {
        case EN_LA_FIESTA:
          enLaFiesta.add(f);
          break;
        case ESTABA_EN_LA_FIESTA:
          estabanEnLaFiesta.add(f);
          break;
        case NUEVO_CREDITO:
          nuevosCreditos.add(f);
          break;
        case NUEVO_TOTAL:
          nuevosTotales.add(f);
          break;
      }
    }
  }
  
  private void imprimirTodo() {
    for (Fila fila : filas) {
      System.out.println(fila);
    }
  }
  
  public void onAsignacionSelect(SelectEvent evento) {
    asignaciones.add(asignacionActual);
    setAsignacionActual((Asignacion) evento.getObject());
    asignaciones.remove(asignacionActual);
  }
  
  public void onCreditoSelect(SelectEvent evento) {
    setCreditoActual((Fila) evento.getObject());
  }
  
  public boolean isPaso1() {
    return paso1;
  }
  
  public void setPaso1(boolean paso1) {
    this.paso1 = paso1;
  }
  
  public boolean isPaso2() {
    return paso2;
  }
  
  public void setPaso2(boolean paso2) {
    this.paso2 = paso2;
  }
  
  public boolean isPaso3() {
    return paso3;
  }
  
  public void setPaso3(boolean paso3) {
    this.paso3 = paso3;
  }
  
  public boolean isPaso4() {
    return paso4;
  }
  
  public void setPaso4(boolean paso4) {
    this.paso4 = paso4;
  }
  
  public boolean isPaso5() {
    return paso5;
  }
  
  public void setPaso5(boolean paso5) {
    this.paso5 = paso5;
  }
  
  public boolean isPaso6() {
    return paso6;
  }
  
  public void setPaso6(boolean paso6) {
    this.paso6 = paso6;
  }
  
  public TabView getTabView() {
    return tabView;
  }
  
  public void setTabView(TabView tabView) {
    this.tabView = tabView;
  }
  
  public int getNumeroDeCreditos() {
    return numeroDeCreditos;
  }
  
  public void setNumeroDeCreditos(int numeroDeCreditos) {
    this.numeroDeCreditos = numeroDeCreditos;
  }
  
  public boolean isVerNumeroDeCreditos() {
    return verNumeroDeCreditos;
  }
  
  public void setVerNumeroDeCreditos(boolean verNumeroDeCreditos) {
    this.verNumeroDeCreditos = verNumeroDeCreditos;
  }
  
  public List<Fila> getEnLaFiesta() {
    return enLaFiesta;
  }
  
  public void setEnLaFiesta(List<Fila> enLaFiesta) {
    this.enLaFiesta = enLaFiesta;
  }
  
  public List<Fila> getEstabanEnLaFiesta() {
    return estabanEnLaFiesta;
  }
  
  public void setEstabanEnLaFiesta(List<Fila> estabanEnLaFiesta) {
    this.estabanEnLaFiesta = estabanEnLaFiesta;
  }
  
  public List<Fila> getNuevosCreditos() {
    return nuevosCreditos;
  }
  
  public void setNuevosCreditos(List<Fila> nuevosCreditos) {
    this.nuevosCreditos = nuevosCreditos;
  }
  
  public List<Fila> getNuevosTotales() {
    return nuevosTotales;
  }
  
  public void setNuevosTotales(List<Fila> nuevosTotales) {
    this.nuevosTotales = nuevosTotales;
  }
  
  public int getTamFiesta() {
    return tamFiesta;
  }
  
  public int getTamEstaban() {
    return tamEstaban;
  }
  
  public int getTamTotales() {
    return tamTotales;
  }
  
  public int getTamCreditos() {
    return tamCreditos;
  }

  public List<Gestor> getGestores() {
    return gestores;
  }

  public void setGestores(List<Gestor> gestores) {
    this.gestores = gestores;
  }
  
  public List<String> getIdSeleccionados() {
    return idSeleccionados;
  }
  
  public void setIdSeleccionados(List<String> idSeleccionados) {
    this.idSeleccionados = idSeleccionados;
  }
  
  public String getCriterioDeAsignacion() {
    return criterioDeAsignacion;
  }
  
  public void setCriterioDeAsignacion(String criterioDeAsignacion) {
    this.criterioDeAsignacion = criterioDeAsignacion;
  }

  public Asignacion getAsignacionActual() {
    return asignacionActual;
  }

  public void setAsignacionActual(Asignacion asignacionActual) {
    this.asignacionActual = asignacionActual;
  }

  public List<Asignacion> getAsignaciones() {
    return asignaciones;
  }

  public void setAsignaciones(List<Asignacion> asignaciones) {
    this.asignaciones = asignaciones;
  }

  public Fila getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Fila creditoActual) {
    this.creditoActual = creditoActual;
  }

  public int getGestorAReasignar() {
    return gestorAReasignar;
  }

  public void setGestorAReasignar(int gestorAReasignar) {
    this.gestorAReasignar = gestorAReasignar;
  }
  
}
