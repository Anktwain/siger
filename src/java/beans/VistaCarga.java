package beans;

import carga.Asignador;
import carga.Balanceador;
import carga.Carajeador;
import carga.Cargador;
import carga.Clasificador;
import carga.Confirmador;
import carga.CreadorSQL;
import carga.Validador;
import dao.DespachoDAO;
import dto.Asignacion;
import dto.Despacho;
import dto.Fila;
import dto.Gestor;
import impl.DespachoIMPL;
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
import nuevaImplementacionDAO.GestorDAO;
import nuevaImplementacionIMPL.GestorIMPL;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class VistaCarga implements Serializable {
  /* Pasos que conforman el proceso de carga. Cada paso indica el tab que mostrará el
   componente TabView de la vista. */

  private static final int INICIO = 1;
  private static final int SUBIR_ARCHIVO = 2;
  private static final int LEER_ARCHIVO = 3;
  private static final int VALIDAR_DATOS = 4;
  private static final int CLASIFICAR_CREDITOS = 5;
  private static final int ASIGNAR_CREDITOS = 6;
  private static final int BALANCE_CARGA = 7;
  private static final int TERMINAR = 8;

  /* Activa controles de la vista que permiten saber en qué paso del proceso de carga nos
   encontramos en un determinado momento. */
  private boolean paso1;
  private boolean paso2;
  private boolean paso3;
  private boolean paso4;
  private boolean paso5;
  private boolean paso6;

  /* Control tabView de la vista, sirve para mostrar un determinado tab que se corresponde con el
   paso en que se encuentra el proceso. */
  private TabView tabView;

  /* La lista "filas" almacena el total de filas recuperadas del archivo de carga. El resto de listas
   almacenan objetos fila de acuerdo a su clasificación. */
  private List<Fila> filas;
  private List<Fila> enLaFiesta;
  private List<Fila> estabanEnLaFiesta;
  private List<Fila> nuevosCreditos;
  private List<Fila> nuevosTotales;

  /* Se refiere al objeto fila que contiene el crédito con el cual se está trabajando en un momento
   determinado. Por ejemplo, si seleccionamos un crédito de una tabla, los datos de ese crédito se
   almacenan en este objeto a fin de poder acceder a ellos. */
  private Fila creditoActual;

  /* Lista de gestores que pueden ser seleccionados para participar de la carga. Los gestores que pueden
   ser seleccionados deben cumplir lo siguiente:
   1. Deben pertenecer al despacho al cual pertenece el administrador que realiza la carga, o en su defecto,
   deben pertenecer al despacho para el cual se hace la carga. 
   2. El gestor debe estar activo, es decir, no deberá estar eliminado de la BD.*/
  private List<Gestor> gestores;

  /* Lista de los IDs que corresponden a los gestores que participarán de la carga.*/
  private List<String> gestoresSeleccionados;

  /* Clase que permite cargar un archivo y leerlo */
  private Cargador cargador;

  /* Criterio de asignación. Puede ser:
   1. "montos"
   2. "zonas" */
  private String criterioDeAsignacion;

  /* Un objeto Asignación contiene las asignaciones de un gestor determinado. El objeto
   "asignacionActual" se refiere a un determinado gestor seleccionado de alguna tabla. */
  private Asignacion asignacionActual;

  /* Lista de todas las asignaciones: */
  private List<Asignacion> asignaciones;

  /* ID del gestor al cual se le va a reasignar el crédito cuyos datos se encuentran en
   el objeto "creditoActual" */
  private int gestorAReasignar;

  /* Nombre del archivo sql que va a contener el script que se ejecutará para realizar la carga. */
  private String archivoSql;

  /* Mes al cual corresponde la carga */
  private int mesCarga;

  /* Despacho encargado de la gestión*/
  private List<Despacho> despachos;
  private int idDespacho;

  /* Para interactuar con la BD */
  private Session session;
  private Transaction transaction;

  /**
   * Método que inicializa a las variables de instancia para dar comienzo a la
   * carga.
   *
   */
  public void iniciarCarga() {
    cargador = new Cargador(mesCarga);
    tabView.setActiveIndex(INICIO); // Comienza mostrando el primer tab
    enLaFiesta = new ArrayList<>();
    estabanEnLaFiesta = new ArrayList<>();
    nuevosCreditos = new ArrayList<>();
    nuevosTotales = new ArrayList<>();
    gestoresSeleccionados = new ArrayList<>();
    asignaciones = new ArrayList<>();
    criterioDeAsignacion = "";

    System.out.println(FacesContext.class.getPackage().getImplementationVersion());
  } // Fin del método inicarCarga.

  public void reiniciarCarga() {
    paso1 = paso2 = paso3 = paso4 = paso5 = paso6 = false;
  } // Fin del método reiniciarCarga.

  /**
   * Método que sube un archivo al servidor.
   *
   * @param evento el evento generado por el usuario en la vista.
   */
  public void subirArchivo(FileUploadEvent evento) throws IOException, BiffException {
    FacesContext context = FacesContext.getCurrentInstance();

    // Guarda el nombre original del archivo en el String archivoSubido.
    String archivoSubido = cargador.subirArchivo(evento);

    if (archivoSubido != null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "El archivo de la remesa se ha cargado exitosamente:",
              archivoSubido));
      paso1 = true;
      tabView.setActiveIndex(SUBIR_ARCHIVO); // Muestra siguiente tab
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo cargar el archivo.",
              "Comunique esta situación a Soporte Técnico."));
    }
  } // Fin del método subirArchivo.

  /**
   * Método que lee un archivo del servidor. El nombre del archivo a leer está
   * contenido en un objeto de la clase Cargador, creado anteriormente.
   *
   */
  public void leerArchivo() throws IOException, BiffException {
    FacesContext context = FacesContext.getCurrentInstance();

    /* El resultado de la lectura del archivo es una lista de objetos Fila, que
     se guarda en "filas". */
    filas = cargador.leerArchivo();

    /* Obtiene el número de créditos contenidos en la remesa, ésto es igual al
     número de objetos Fila. */
    int numeroDeCreditos = filas.size();

    /* Verifica que se haya creado al menos un objeto Fila, en caso contrario
     envía un mensaje de error. */
    if (numeroDeCreditos > 0) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se ha leído el archivo de la remesa:",
              "Se encontraron " + numeroDeCreditos + " créditos."));
      paso2 = true;
      tabView.setActiveIndex(LEER_ARCHIVO); // Muestra siguiente tab
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo leer el archivo.",
              "Comunique esta situación a Soporte Técnico."));
    }
  } // Fin del método leerArchivo.

  /**
   * Método que valida los datos provenientes del archivo de carga. Estos datos
   * fueron guardados previamente como objetos Fila en el arreglo "filas".
   *
   */
  public void validarDatos() {
    FacesContext context = FacesContext.getCurrentInstance();

    Fila fila = Validador.validarFilas(filas);
    if (fila != null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fila.getError(), fila.getDetalleError()));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se han validado los datos: ",
              "todos los datos son correctos"));
      paso3 = true;
      tabView.setActiveIndex(VALIDAR_DATOS); // Muestra siguiente tab.
    }
//
//    /* Variable que determina si se encontró algún error en la validación. */
//    boolean continuar = true;
//    /* La validación de las filas se realiza sólo si "filas" contiene al menos
//     un elemento. */
//    if (filas.size() > 0) {
//
//      /* Validación de las filas. */
//      for (Fila f : filas) {
//        Validador.validarFila(f); /* Valida la fila actual */
//
//        if (!f.getError().equals("NO")) { // Ocurrió un error
//          context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                  f.getError(), f.getDetalleError()));
//          /* Puesto que ocurrió un error, entonces ya no continuar con la
//           ejecución del ciclo. */
//          continuar = false;
//          break; /* Rompe ciclo for.*/
//
//        }
//      }
//
//      /* Si no ocurrió ningún error, se informa al usuario que la operación fue
//       exitosa. */
//      if (continuar) {
//        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
//                "Se han validado los datos: ",
//                "todos los datos son correctos"));
//        paso3 = true;
//        tabView.setActiveIndex(VALIDAR_DATOS); // Muestra siguiente tab.
//      }
//    }

  } // Fin del método validarDatos.

  /**
   * Método que agrega objetos necesarios a cada objeto Fila, haciendo una mejor
   * implementación del patrón de diseño DAO. Para efectos de emplear de mejor
   * manera este patrón, se crearon los paquetes nuevaImplementacionIMPL y
   * nuevaImplementacionDAO, cuyas clases e interfaces son usadas en este
   * método.
   *
   */
  private void agregarObjetos() {
    this.session = null;
    this.transaction = null;
    try {
      nuevaImplementacionDAO.DespachoDAO despachoDao = new nuevaImplementacionIMPL.DespachoIMPL();
      GestorDAO gestorDao = new GestorIMPL();

      this.session = HibernateUtil.getSessionFactory().openSession();
      this.transaction = session.beginTransaction();

      // Obteniendo objetos...
      Despacho despacho = despachoDao.getById(session, idDespacho);

      // Agregando objetos
      for (Fila f : filas) {
        f.setDespachoDTO(despacho);
        f.setGestorDTO(gestorDao.getById(session, f.getIdGestor()));
      }

    } catch (Exception ex) {

    } finally {

    }
  }

  /**
   * Método que clasifica los créditos de acuerdo a su inclusión o no en la Base
   * de Datos. Un crédito puede clasificarse como: "Nuevo crédito", "Nuevo
   * total", "En la fiesta", "Estaba en la fiesta" o "Retirado".
   *
   */
  public void clasificarCreditos() {
    FacesContext context = FacesContext.getCurrentInstance();

    /* Clasifica los créditos contenidos en "filas". */
    Clasificador.clasificar(filas);

    /* Una vez clasificados los créditos mediante el objeto Clasificar, divide la
     lista "filas" en listas más pequeñas que almacenarán los créditos de acuerdo
     a su clasificación. */
    separarPorClasificacion();

    paso4 = true;
    tabView.setActiveIndex(CLASIFICAR_CREDITOS); // Muestra siguiente tab.
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Se han clasificado todos los créditos.", "La tabla muestra más detalle"));

    /* Busca a los gestores del Corp correspondiente. */
    gestores = Asignador.getGestores(idDespacho);

  } // Fin del método clasificarCreditos

  /**
   * Método que asigna los créditos a sus respectivos gestores. Los créditos a
   * asignar son únicamente los que se encuantran en la lista nuevosTotales, el
   * resto de créditos ya tienen un gestor asignado porque ya se han gestionado
   * anteriormente o bien son créditos pertenecientes a un deudor que ya tenía
   * créditos y éstos su respectivo gestor, en cuyo caso el nuevo crédito se
   * asignará al gestor que ya estaba trabajando con algún otro crédito de ese
   * deudor.
   *
   */
  public void asignarCreditos() {
    FacesContext context = FacesContext.getCurrentInstance();

    String mensaje = "Se realizará la asignación por " + criterioDeAsignacion;

    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            mensaje, null));

    /* Realiza la asignación valiéndose de la clase Asignador y enviándole los
     necesarios. Obsérvese que se envía la lista nuevosTotales. */
    Asignador.asignar(nuevosTotales, gestoresSeleccionados, criterioDeAsignacion);

    /* Crea un objeto Asignacion que contendrá información relacionada con cada
     asignación de créditos. */
    Asignacion asignacion;

    /* Recorre la lista que contiene los ids de los gestores seleccionados por el
     usuario para participar de la asignación. */
    for (String g : gestoresSeleccionados) {
      asignacion = new Asignacion();
      /* Agrega el id del gestor actual a la nueva asignación. */
      asignacion.setGestor(Integer.parseInt(g));

      /* Ahora recorre la lista de objetos Fila contenidos en nuevosTotales a fin
       de buscar todos los créditos que fueron asignados previamente al gestor actual. */
      for (Fila f : nuevosTotales) {
        if (f.getIdGestor() == Integer.parseInt(g)) {
          /* Si la fila actual contiene un crédito que fue asignado al gestor
           actual, entonces agrega esa fila al objeto Asignacion */
          asignacion.setCredito(f);
        }
      }

      /* Finalmente agrega el objeto Asignacion a la lista asignaciones. Agregará
       tantos objetos Asignacion como gestores hayan sido seleccionados para
       participar de la asignación de créditos. */
      asignaciones.add(asignacion);

    } // Fin del bucle que recorre la lista de gestores seleccionados.

    /* Después de asignar, toma la primera asignación y la guarda en asignacionActual,
     además eliminar esa asignación de la lista "asignaciones". */
    asignacionActual = asignaciones.get(0);
    asignaciones.remove(0);

    paso5 = true;
    tabView.setActiveIndex(ASIGNAR_CREDITOS); // Muestra siguiente tab.

  } // Fin del método asignarCreditos.

  /**
   * Método que reasigna un crédito seleccionado a un gestor seleccionado. El
   * crédito a reasignar es el que se encuentra almacenado en creditoActual, y
   * se asignará al gestor cuyo id se encuentra almacenado en gestorAReasignar.
   *
   */
  public void reasignar() {
    FacesContext context = FacesContext.getCurrentInstance();

    /* Reasigna valiéndose del objeto Balanceador. */
    Balanceador.reasignar(creditoActual, gestorAReasignar, asignaciones, asignacionActual);
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Reasignación Exitosa", "Se Reasignó crédito: " + creditoActual.getCredito()));
  } // Fin del método reasignar.

  public void mandarAlCarajo() {
    paso6 = true;
    tabView.setActiveIndex(BALANCE_CARGA);

    Carajeador.carajear(idDespacho, nuevosTotales, mesCarga, filas.size(), Asignador.getTotalSaldoVencido(filas));
  }

  /**
   * Método que crea un archivo sql que contiene las sentencias sql a ejecutar
   * para realizar la carga. El archivo se guarda en el servidor para
   * posteriormente ser ejecutado y consumar la carga.
   *
   */
  public void crearSQL() {
    paso6 = true;
    tabView.setActiveIndex(BALANCE_CARGA); // Muestra siguiente tab.

    /* En primer lugar crea los objetos Sujeto que servirán para ser asociados a
     los deudores contenidos en los objetos Fila. */
    CreadorSQL.crearSujetos(nuevosTotales);
    archivoSql = CreadorSQL.crearSQL(nuevosTotales);
  } // Fin del método crearSQL.

  /**
   * Método que ejecuta el script sql creado previamente.
   *
   */
  public void confirmarCarga() throws IOException, SQLException {
    /* Ejecuta el método correspondiente de la clase Confirmador */
    Confirmador.ejecutarScriptSql(archivoSql);
  } // Fin del método confirmarCarga.

  private void separarPorClasificacion() {
    for (Fila f : filas) {
      switch (f.getClasificacion()) {
        case Clasificador.EN_LA_FIESTA:
          enLaFiesta.add(f);
          break;
        case Clasificador.ESTABA_EN_LA_FIESTA:
          estabanEnLaFiesta.add(f);
          break;
        case Clasificador.NUEVO_CREDITO:
          nuevosCreditos.add(f);
          break;
        case Clasificador.NUEVO_TOTAL:
          nuevosTotales.add(f);
          break;
      }
    }
  } // Fin del método separarPorClasificacion

  public void onAsignacionSelect(SelectEvent evento) {
    asignaciones.add(asignacionActual);
    setAsignacionActual((Asignacion) evento.getObject());
    asignaciones.remove(asignacionActual);
  } // Fin del método onAsignacionSelect.

  public void onCreditoSelect(SelectEvent evento) {
    setCreditoActual((Fila) evento.getObject());
  } // Fin del método onCreditoSelect.

  public boolean isPaso1() {
    return paso1;
  }

  public boolean isPaso2() {
    return paso2;
  }

  public boolean isPaso3() {
    return paso3;
  }

  public boolean isPaso4() {
    return paso4;
  }

  public boolean isPaso5() {
    return paso5;
  }

  public boolean isPaso6() {
    return paso6;
  }

  public TabView getTabView() {
    return tabView;
  }

  public void setTabView(TabView tabView) {
    this.tabView = tabView;
  }

  public int getTotalCreditos() {
    return (filas != null) ? filas.size() : 0;
  }

  public List<Fila> getEnLaFiesta() {
    return enLaFiesta;
  }

  public List<Fila> getEstabanEnLaFiesta() {
    return estabanEnLaFiesta;
  }

  public List<Fila> getNuevosCreditos() {
    return nuevosCreditos;
  }

  public List<Fila> getNuevosTotales() {
    return nuevosTotales;
  }

  public int getTotalEnLaFiesta() {
    return (enLaFiesta != null) ? enLaFiesta.size() : 0;
  }

  public int getTotalEstabanEnLaFiesta() {
    return (estabanEnLaFiesta != null) ? estabanEnLaFiesta.size() : 0;
  }

  public int getTotalNuevosTotales() {
    return (nuevosTotales != null) ? nuevosTotales.size() : 0;
  }

  public int getTotalNuevosCreditos() {
    return (nuevosCreditos != null) ? nuevosCreditos.size() : 0;
  }

  public List<Gestor> getGestores() {
    return gestores;
  }

  public List<String> getGestoresSeleccionados() {
    return gestoresSeleccionados;
  }

  public void setGestoresSeleccionados(List<String> gestoresSeleccionados) {
    this.gestoresSeleccionados = gestoresSeleccionados;
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

  public Fila getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Fila creditoActual) {
    this.creditoActual = creditoActual;
  }

  public void setGestorAReasignar(int gestorAReasignar) {
    this.gestorAReasignar = gestorAReasignar;
  }

  public int getGestorAReasignar() {
    return gestorAReasignar;
  }

  public int getMesCarga() {
    return mesCarga;
  }

  public void setMesCarga(int mesCarga) {
    this.mesCarga = mesCarga;
  }

  public int getIdDespacho() {
    return idDespacho;
  }

  public void setIdDespacho(int idDespacho) {
    this.idDespacho = idDespacho;
  }

  public List<Despacho> getDespachos() {
    DespachoDAO despachoDao = new DespachoIMPL();
    despachos = despachoDao.getAll();
    return despachos;
  }

  public void setDespachos(List<Despacho> despachos) {
    this.despachos = despachos;
  }

}
