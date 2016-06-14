/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.CreditoDAO;
import dao.GestionDAO;
import dao.HistorialDAO;
import dao.PagoDAO;
import dao.PromesaPagoDAO;
import dto.Campana;
import dto.Credito;
import dto.Gestion;
import dto.Historial;
import dto.Pago;
import dto.PromesaPago;
import impl.CampanaIMPL;
import impl.CreditoIMPL;
import impl.GestionIMPL;
import impl.HistorialIMPL;
import impl.PagoIMPL;
import impl.PromesaPagoIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.constantes.Campanas;
import util.constantes.Marcajes;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cuentasGestorBean")
@SessionScoped
public class CuentasGestorBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private boolean habilitaCampanas;
  private List<CreditoCampana> listaCreditosCampanas;
  List<Credito> creditosCampana;
  private CreditoCampana seleccion;
  private final CreditoDAO creditoDao;
  private final CampanaDAO campanaDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;
  private final PromesaPagoDAO promesaPagoDao;
  private final HistorialDAO historialDao;
  private int posicion;

  //CONSTRUCTOR
  public CuentasGestorBean() {
    habilitaCampanas = false;
    posicion = 0;
    seleccion = new CreditoCampana();
    listaCreditosCampanas = new ArrayList();
    creditosCampana = new ArrayList();
    creditoDao = new CreditoIMPL();
    campanaDao = new CampanaIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
    promesaPagoDao = new PromesaPagoIMPL();
    historialDao = new HistorialIMPL();
  }

  // METODO QUE CARGA LAS CAMPAÑAS DESPUES DE INICIAR SESION
  public void cargar() {
    cambiarCampanas();
    obtenerListas();
    habilitaCampanas = true;
  }

  // METODO QUE OBTIENE LAS LISTAS INICIALES
  public final void obtenerListas() {
    listaCreditosCampanas = new ArrayList();
    List<Campana> listaCampanas = campanaDao.buscarTodas();
    for (int i = 0; i < (listaCampanas.size()); i++) {
      List<Credito> listaCredito = creditoDao.buscarCreditosPorCampanaGestor(listaCampanas.get(i).getIdCampana(), indexBean.getUsuario().getIdUsuario());
      if (!listaCredito.isEmpty()) {
        CreditoCampana c = new CreditoCampana();
        c.setIdCampana(listaCampanas.get(i).getIdCampana());
        c.setNombreCampana(listaCampanas.get(i).getNombre());
        c.setCuentasEnCampana(listaCredito.size());
        int[] arreglo = checarCampana(listaCredito);
        c.setNuevasEnCampana(arreglo[0]);
        c.setProgresoCampana(arreglo[1]);
        c.setVerdes(arreglo[2]);
        c.setAmarillas(arreglo[3]);
        c.setRojas(arreglo[4]);
        listaCreditosCampanas.add(c);
      }
    }
  }

  //METODO QUE CALCULA LOS CREDITOS QUE CAMBIARON DE CAMPAÑA HOY
  public int[] checarCampana(List<Credito> creditos) {
    int[] arreglo = new int[5];
    int cambios = 0;
    int progreso = 0;
    int verdes = 0;
    int amarillas = 0;
    int rojas = 0;
    for (int i = 0; i < (creditos.size()); i++) {
      if (historialDao.verificarCampioCampañaCredito(creditos.get(i).getIdCredito())) {
        cambios = cambios + 1;
      }
      if (gestionDao.buscarGestionHoy(creditos.get(i).getIdCredito())) {
        progreso = progreso + 1;
      }
      int dias = gestionDao.checarDiasSinGestionar(creditos.get(i).getIdCredito()).intValue();
      if (dias <= 3) {
        verdes = verdes + 1;
      }
      if ((dias > 3) && (dias <= 7)) {
        amarillas = amarillas + 1;
      }
      if (dias > 7) {
        rojas = rojas + 1;
      }
    }
    arreglo[0] = cambios;
    arreglo[1] = progreso;
    arreglo[2] = verdes;
    arreglo[3] = amarillas;
    arreglo[4] = rojas;
    return arreglo;
  }

  // METODO QUE VERIFICA SI LA CAMPAÑA EN LA LISTA ES LA CAMPAÑA QUE SE ESTA GESTIONANDO
  public String verificarCampanaEnCurso(CreditoCampana campana) {
    if (campana == seleccion) {
      return "En curso";
    } else {
      return "";
    }
  }

  // METODO QUE CAMBIA LAS CUENTAS DE CAMPAÑA SEGUN CORRESPONDA
  public final void cambiarCampanas() {
    // SE OBTIENE TODOS LOS CREDITOS ASIGNADOS A ESTE GESTOR
    List<Credito> creditosEnGestion = creditoDao.buscarCreditosPorGestor(indexBean.getUsuario().getIdUsuario());
    for (int i = 0; i < (creditosEnGestion.size()); i++) {
      // SE OBTIENEN LAS CAMPANAS
      int campana = creditosEnGestion.get(i).getCampana().getIdCampana();
      int campanaActual = campana;
      // SE OBTIENEN LAS LISTAS DE PROMESAS DE PAGO
      List<PromesaPago> promesasFuturas = promesaPagoDao.buscarPromesasFuturas(creditosEnGestion.get(i).getIdCredito());
      List<PromesaPago> promesasHoy = promesaPagoDao.promesasPagoHoy(creditosEnGestion.get(i).getIdCredito());
      List<PromesaPago> promesasPasadas = promesaPagoDao.buscarPromesasAnterioresCredito(creditosEnGestion.get(i).getIdCredito());
      List<Pago> pagos = pagoDao.buscarPagosPorCredito(creditosEnGestion.get(i).getIdCredito());
      // SE OBTIENE LA LISTA DE GESTIONES
      List<Gestion> gestionesCredito = gestionDao.buscarGestionesCredito(creditosEnGestion.get(i).getIdCredito());
      // CHECAR MARCAJES
      if (creditosEnGestion.get(i).getMarcaje().getIdMarcaje() != 9) {
        if (creditosEnGestion.get(i).getMarcaje().getIdMarcaje() == Marcajes.LOCALIZACION) {
          campana = Campanas.LOCALIZACION;
        } else if (creditosEnGestion.get(i).getMarcaje().getIdMarcaje() == Marcajes.URGENTE) {
          campana = Campanas.URGENTE;
        }
      }
      // CHECAR PROMESAS FUTURAS
      if (!promesasFuturas.isEmpty()) {
        // PROMESAS EN ESPERA DE CUMPLIRSE
        for (int j = 0; j < (promesasFuturas.size()); j++) {
          Calendar fecha = Calendar.getInstance();
          fecha.add(Calendar.DATE, 4);
          Date fechaFutura = fecha.getTime();
          if (promesasFuturas.get(j).getFechaPrometida().after(fechaFutura)) {
            campana = Campanas.CONVENIO_EN_ESPERA_DE_CUMPLIRSE;
            break;
          } else {
            campana = Campanas.CONVENIO_ANTICIPA_FECHA;
          }
        }
      }
      // CHECAR PROMESAS HOY
      if (!promesasHoy.isEmpty()) {
        campana = Campanas.CONVENIO_MISMA_FECHA;
      }
      // CHECAR PROMESAS ANTERIORES
      if (!promesasPasadas.isEmpty()) {
        // CHECAR PAGOS
        if (!pagos.isEmpty()) {
          for (int j = 0; j < (pagos.size()); j++) {
            if (pagos.get(j).getPromesaPago().getCantidadPrometida() >= pagos.get(j).getMontoPago()) {
              campana = Campanas.CONVENIO_CUMPLIDO;
              break;
            } else {
              campana = Campanas.CONVENIO_INCOMPLETO;
            }
          }
        } else {
          campana = Campanas.CONVENIO_INCUMPLIDO;
        }
      }
      // CHECAR GESTIONES
      if (!gestionesCredito.isEmpty()) {
        // CHECAR ULTIMA GESTION
        Gestion ultima = gestionesCredito.get(gestionesCredito.size() - 1);
        if ((ultima.getDescripcionGestion().getCalificacion() != null) && (ultima.getDescripcionGestion().getCalificacion() == 2)) {
          // CHECAR PAGOS
          if (!pagos.isEmpty()) {
            campana = Campanas.CON_CONTACTO_CON_PAGOS;
          } else {
            campana = Campanas.CON_CONTACTO;
          }
        } else if ((ultima.getDescripcionGestion().getCalificacion() != null) && (ultima.getDescripcionGestion().getCalificacion() == 3)) {
          // CHECAR PAGOS
          if (!pagos.isEmpty()) {
            campana = Campanas.SIN_CONTACTO_CON_PAGOS;
          } else {
            // CHECAR ULTIMAS 7 GESTIONES
            if (gestionesCredito.size() >= 7) {
              boolean ok = true;
              for (int j = (gestionesCredito.size() - 7); j < (gestionesCredito.size()); j++) {
                ok = ok & ((gestionesCredito.get(j).getDescripcionGestion().getCalificacion() != null) && (gestionesCredito.get(j).getDescripcionGestion().getCalificacion() == 3));
              }
              if (ok) {
                campana = Campanas.SIN_CONTACTO_TOTAL;
              } else {
                campana = Campanas.SIN_CONTACTO_PARCIAL;
              }
            } else {
              campana = Campanas.SIN_CONTACTO_PARCIAL;
            }
          }
        }
      }
      if (campana != campanaActual) {
        creditosEnGestion.get(i).setCampana(campanaDao.buscarPorId(campana));
        if (creditoDao.editar(creditosEnGestion.get(i))) {
          Historial h = new Historial();
          h.setCredito(creditosEnGestion.get(i));
          h.setEvento("Automatico. Cambio de campaña.");
          h.setFecha(new Date());
          if (historialDao.insertar(h)) {
            Logs.log.info("Credito " + creditosEnGestion.get(i).getNumeroCredito() + " cambio de campaña.");
          } else {
            Logs.log.error("El cambio de campaña no fue registrado en el historial");
          }
        } else {
          Logs.log.error("Credito " + creditosEnGestion.get(i).getNumeroCredito() + " no cambio de campaña");
        }
      }
    }
  }

// METODO QUE PREPARA LA GESTION DE CAMPAÑA SEGUN LA ELECCION EN LA VISTA CUENTAS GESTOR
  public void preparaCampana() {
    List<Credito> listaRojas = new ArrayList();
    List<Credito> listaAmarillas = new ArrayList();
    List<Credito> listaVerdes = new ArrayList();
    List<Credito> lista = creditoDao.buscarCreditosPorCampanaGestor(seleccion.getIdCampana(), indexBean.getUsuario().getIdUsuario());
    for (int i = 0; i < (lista.size()); i++) {
      int dias = gestionDao.checarDiasSinGestionar(lista.get(i).getIdCredito()).intValue();
      if (dias <= 3) {
        listaVerdes.add(lista.get(i));
      }
      if ((dias > 3) && (dias <= 7)) {
        listaAmarillas.add(lista.get(i));
      }
      if (dias > 7) {
        listaRojas.add(lista.get(i));
      }
    }
    posicion = 0;
    creditosCampana = new ArrayList();
    creditosCampana.addAll(listaRojas);
    creditosCampana.addAll(listaAmarillas);
    creditosCampana.addAll(listaVerdes);
    if (!creditosCampana.isEmpty()) {
      for (int i = 0; i < (creditosCampana.size()); i++) {
        if (!gestionDao.buscarGestionHoy(creditosCampana.get(i).getIdCredito())) {
          posicion = i;
          break;
        }
      }
      creditoActualBean.setCreditoActual(creditosCampana.get(posicion));
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCampanaActual.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista de la campaña actual.");
        Logs.log.error(ioe);
      }
    }
  }
  
  // METODO QUE PREPARA LA GESTION DE CAMPAÑA DE LOS CREDITOS FILTRADOS EN LA VISTA BUSQUEDA
  public void preparaCampanaBusqueda(List<Credito> creditos) {
    CreditoCampana c = new CreditoCampana();
    c.setCuentasEnCampana(creditos.size());
    c.setNombreCampana("Campaña especial busqueda creditos");
    int progreso = 0;
    for (int i = 0; i < (creditos.size()); i++) {
      if (gestionDao.buscarGestionHoy(creditos.get(i).getIdCredito())) {
        progreso = progreso + 1;
      }
    }
    c.setProgresoCampana(progreso);
    seleccion = c;
    List<Credito> listaRojas = new ArrayList();
    List<Credito> listaAmarillas = new ArrayList();
    List<Credito> listaVerdes = new ArrayList();
    for (int i = 0; i < (creditos.size()); i++) {
      int dias = gestionDao.checarDiasSinGestionar(creditos.get(i).getIdCredito()).intValue();
      if (dias <= 3) {
        listaVerdes.add(creditos.get(i));
      }
      if ((dias > 3) && (dias <= 7)) {
        listaAmarillas.add(creditos.get(i));
      }
      if (dias > 7) {
        listaRojas.add(creditos.get(i));
      }
    }
    posicion = 0;
    creditosCampana = new ArrayList();
    creditosCampana.addAll(listaRojas);
    creditosCampana.addAll(listaAmarillas);
    creditosCampana.addAll(listaVerdes);
    if (!creditosCampana.isEmpty()) {
      for (int i = 0; i < (creditosCampana.size()); i++) {
        if (!gestionDao.buscarGestionHoy(creditosCampana.get(i).getIdCredito())) {
          posicion = i;
          break;
        }
      }
      creditoActualBean.setCreditoActual(creditosCampana.get(posicion));
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCampanaActual.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista de la campaña actual.");
        Logs.log.error(ioe);
      }
    }
  }

  // METODO QUE ACTUALIZA LAS CAMPAÑAS SIN NECESIDAD DE CERRAR SESION
  public void actualizarCampanas() {
    cambiarCampanas();
    obtenerListas();
  }

  // GETTERS & SETTERS
  public List<CreditoCampana> getListaCreditosCampanas() {
    return listaCreditosCampanas;
  }

  public void setListaCreditosCampanas(List<CreditoCampana> listaCreditosCampanas) {
    this.listaCreditosCampanas = listaCreditosCampanas;
  }

  public CreditoCampana getSeleccion() {
    return seleccion;
  }

  public void setSeleccion(CreditoCampana seleccion) {
    this.seleccion = seleccion;
  }

  public List<Credito> getCreditosCampana() {
    return creditosCampana;
  }

  public void setCreditosCampana(List<Credito> creditosCampana) {
    this.creditosCampana = creditosCampana;
  }

  public int getPosicion() {
    return posicion;
  }

  public void setPosicion(int posicion) {
    this.posicion = posicion;
  }

  public boolean isHabilitaCampanas() {
    return habilitaCampanas;
  }

  public void setHabilitaCampanas(boolean habilitaCampanas) {
    this.habilitaCampanas = habilitaCampanas;

  }

  // CLASE MIEMBRO PARA PODER LLENAR LA TABLA
  public static class CreditoCampana {

    // VARIABLES DE CLASE
    private int idCampana;
    private String nombreCampana;
    private int cuentasEnCampana;
    private int nuevasEnCampana;
    private int progresoCampana;
    private int verdes;
    private int amarillas;
    private int rojas;

    // CONSTRUCTOR
    public CreditoCampana() {
    }

    // GETTERS & SETTERS
    public int getIdCampana() {
      return idCampana;
    }

    public void setIdCampana(int idCampana) {
      this.idCampana = idCampana;
    }

    public String getNombreCampana() {
      return nombreCampana;
    }

    public void setNombreCampana(String nombreCampana) {
      this.nombreCampana = nombreCampana;
    }

    public int getCuentasEnCampana() {
      return cuentasEnCampana;
    }

    public void setCuentasEnCampana(int cuentasEnCampana) {
      this.cuentasEnCampana = cuentasEnCampana;
    }

    public int getProgresoCampana() {
      return progresoCampana;
    }

    public void setProgresoCampana(int progresoCampana) {
      this.progresoCampana = progresoCampana;
    }

    public int getNuevasEnCampana() {
      return nuevasEnCampana;
    }

    public void setNuevasEnCampana(int nuevasEnCampana) {
      this.nuevasEnCampana = nuevasEnCampana;
    }

    public int getVerdes() {
      return verdes;
    }

    public void setVerdes(int verdes) {
      this.verdes = verdes;
    }

    public int getAmarillas() {
      return amarillas;
    }

    public void setAmarillas(int amarillas) {
      this.amarillas = amarillas;
    }

    public int getRojas() {
      return rojas;
    }

    public void setRojas(int rojas) {
      this.rojas = rojas;
    }

  }

}
