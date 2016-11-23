/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ColoniaDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.DireccionTextoDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dto.Colonia;
import dto.Credito;
import dto.Direccion;
import dto.DireccionTexto;
import dto.EstadoRepublica;
import dto.Municipio;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.DireccionTextoIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "validacionEspecialDireccionesBean")
@ViewScoped
public class ValidacionEspecialDireccionesBean implements Serializable {

  // VARIABLES DE CLASE
  private boolean habilitaColonias;
  private int posicion;
  private int totalCuentas;
  private int idDireccionDirecta;
  private String coloniaBusqueda;
  private String cpBusqueda;
  private Direccion direccionSeleccionada;
  private EstadoRepublica estadoBuscadorColonias;
  private Municipio municipioBuscadorColonias;
  private Colonia coloniaSeleccionadaBusqueda;
  private List<Direccion> listaDirecciones;
  private List<DireccionTexto> listaDireccionesTexto;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Colonia> listaColonias;
  private List<Colonia> listaColoniasBusqueda;
  private final DireccionDAO direccionDao;
  private final ColoniaDAO coloniaDao;
  private final MunicipioDAO municipioDao;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final DireccionTextoDAO direccionTextoDao;
  private final CreditoDAO creditoDao;

  // CONSTRUCTOR
  public ValidacionEspecialDireccionesBean() {
    habilitaColonias = false;
    estadoBuscadorColonias = new EstadoRepublica();
    municipioBuscadorColonias = new Municipio();
    coloniaSeleccionadaBusqueda = new Colonia();
    direccionDao = new DireccionIMPL();
    coloniaDao = new ColoniaIMPL();
    municipioDao = new MunicipioIMPL();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    direccionTextoDao = new DireccionTextoIMPL();
    creditoDao = new CreditoIMPL();
    direccionSeleccionada = new Direccion();
    listaDirecciones = direccionDao.buscarTodo();
    totalCuentas = listaDirecciones.size();
    listaEstados = estadoRepublicaDao.buscarTodo();
    listaDireccionesTexto = new ArrayList();
    listaMunicipios = new ArrayList();
    listaColonias = new ArrayList();
    listaColoniasBusqueda = new ArrayList();
    inicializarDireccion();
  }

  public final void inicializarDireccion() {
    direccionSeleccionada.setCalle("");
    direccionSeleccionada.setColonia(coloniaDao.buscar(1));
    direccionSeleccionada.setEstadoRepublica(estadoRepublicaDao.buscar(1));
    direccionSeleccionada.setExterior("");
    direccionSeleccionada.setIdDireccion(0);
    direccionSeleccionada.setInterior("");
    direccionSeleccionada.setLatitud(BigDecimal.ZERO);
    direccionSeleccionada.setLongitud(BigDecimal.ZERO);
    direccionSeleccionada.setMunicipio(municipioDao.buscar(1));
  }

  public void anterior() {
    if (posicion == 0) {
      direccionSeleccionada = listaDirecciones.get(listaDirecciones.size() - 1);
      posicion = listaDirecciones.size();
    } else {
      posicion = posicion - 1;
      direccionSeleccionada = listaDirecciones.get(posicion);
    }
    prepararDireccion();
  }

  public void siguiente() {
    if (posicion == (listaDirecciones.size() - 1)) {
      direccionSeleccionada = listaDirecciones.get(0);
      posicion = listaDirecciones.size();
    } else {
      posicion = posicion + 1;
      direccionSeleccionada = listaDirecciones.get(posicion);
    }
    prepararDireccion();
  }

  public void obtenerMunicipios(EstadoRepublica estado) {
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(estado.getIdEstado());
  }

  public void obtenerColonias(Municipio municipio) {
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(municipio.getIdMunicipio());
  }

  public void prepararDireccion() {
    posicion = listaDirecciones.indexOf(direccionSeleccionada);
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(direccionSeleccionada.getEstadoRepublica().getIdEstado());
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(direccionSeleccionada.getMunicipio().getIdMunicipio());
    List<Credito> creditos = creditoDao.buscarCreditosPorSujeto(direccionSeleccionada.getSujeto().getIdSujeto());
    listaDireccionesTexto = new ArrayList();
    for (int i = 0; i < (creditos.size()); i++) {
      listaDireccionesTexto.addAll(direccionTextoDao.buscarPorCredito(creditos.get(i).getNumeroCredito()));
    }
  }

  public void editarDireccion() {
    if (direccionDao.editar(direccionSeleccionada)) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa", "Se edito la direccion " + direccionSeleccionada.getIdDireccion()));
    } else {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se edito la direccion. Contacte al equipo de sistemas"));
    }
  }

  public void editarDireccionBuscador(int idColonia) {
    direccionSeleccionada.setColonia(coloniaSeleccionadaBusqueda);
    direccionSeleccionada.setMunicipio(coloniaSeleccionadaBusqueda.getMunicipio());
    direccionSeleccionada.setEstadoRepublica(coloniaSeleccionadaBusqueda.getMunicipio().getEstadoRepublica());
    if (direccionDao.editar(direccionSeleccionada)) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa", "Se edito la direccion " + direccionSeleccionada.getIdDireccion()));
    } else {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se edito la direccion. Contacte al equipo de sistemas"));
    }
  }

  public void buscarColonias() {
    String consulta = "SELECT * FROM colonia WHERE";
    // SI SE ELIGIO UN ESTADO
    if (estadoBuscadorColonias.getIdEstado() != 0) {
      consulta = consulta + " id_municipio IN (SELECT id_municipio FROM municipio WHERE id_estado = " + estadoBuscadorColonias.getIdEstado() + ")";
      // SI SE ELIGIO ALGUN MUNICIPIO
      if (municipioBuscadorColonias.getIdMunicipio() != 0) {
        consulta = consulta + " AND id_municipio = " + municipioBuscadorColonias.getIdMunicipio();
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      } // SI NO SE ELIGIO NINGUN MUNICIPIO
      else {
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      }
    } // SI NO SE ELIGIO NINGUN ESTADO
    else {
      // SI SE ELIGIO ALGUN MUNICIPIO
      if (municipioBuscadorColonias.getIdMunicipio() != 0) {
        consulta = consulta + " AND id_municipio = " + municipioBuscadorColonias.getIdMunicipio();
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      } // SI NO SE ELIGIO NINGUN MUNICIPIO
      else {
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " codigo_postal = '" + cpBusqueda + "'";
          }
        }
      }
    }
    if (!consulta.isEmpty()) {
      consulta = consulta + ";";
      listaColoniasBusqueda = coloniaDao.busquedaEspecialColonias(consulta);
      habilitaColonias = true;
    }
  }

  // METODO QUE ABRE UNA DIRECCION EN LA CUAL HAN ESPECIFICADO EL ID
  public void abrirDireccionEspecifica() {
    int contador = 0;
    for (int i = 0; i < (listaDirecciones.size()); i++) {
      if (listaDirecciones.get(i).getIdDireccion() == idDireccionDirecta) {
        direccionSeleccionada = listaDirecciones.get(i);
        break;
      } else {
        contador++;
      }
    }
    if (contador == (listaDirecciones.size())) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "la direccion con el id solicitado no existe en el sistema."));
    } else {
      prepararDireccion();
      RequestContext.getCurrentInstance().update("editarDireccionValidacionEspecialForm");
      RequestContext.getCurrentInstance().execute("PF('dialogoEditarDireccionValidacionEspecial').show();");
    }
  }

  //GETTERS & SETTERS
  public Direccion getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  public List<Direccion> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Direccion> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public int getPosicion() {
    return posicion;
  }

  public void setPosicion(int posicion) {
    this.posicion = posicion;
  }

  public int getTotalCuentas() {
    return totalCuentas;
  }

  public void setTotalCuentas(int totalCuentas) {
    this.totalCuentas = totalCuentas;
  }

  public List<EstadoRepublica> getListaEstados() {
    return listaEstados;
  }

  public void setListaEstados(List<EstadoRepublica> listaEstados) {
    this.listaEstados = listaEstados;
  }

  public List<Municipio> getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(List<Municipio> listaMunicipios) {
    this.listaMunicipios = listaMunicipios;
  }

  public List<Colonia> getListaColonias() {
    return listaColonias;
  }

  public void setListaColonias(List<Colonia> listaColonias) {
    this.listaColonias = listaColonias;
  }

  public List<DireccionTexto> getListaDireccionesTexto() {
    return listaDireccionesTexto;
  }

  public void setListaDireccionesTexto(List<DireccionTexto> listaDireccionesTexto) {
    this.listaDireccionesTexto = listaDireccionesTexto;
  }

  public EstadoRepublica getEstadoBuscadorColonias() {
    return estadoBuscadorColonias;
  }

  public void setEstadoBuscadorColonias(EstadoRepublica estadoBuscadorColonias) {
    this.estadoBuscadorColonias = estadoBuscadorColonias;
  }

  public Municipio getMunicipioBuscadorColonias() {
    return municipioBuscadorColonias;
  }

  public void setMunicipioBuscadorColonias(Municipio municipioBuscadorColonias) {
    this.municipioBuscadorColonias = municipioBuscadorColonias;
  }

  public Colonia getColoniaSeleccionadaBusqueda() {
    return coloniaSeleccionadaBusqueda;
  }

  public void setColoniaSeleccionadaBusqueda(Colonia coloniaSeleccionadaBusqueda) {
    this.coloniaSeleccionadaBusqueda = coloniaSeleccionadaBusqueda;
  }

  public String getColoniaBusqueda() {
    return coloniaBusqueda;
  }

  public void setColoniaBusqueda(String coloniaBusqueda) {
    this.coloniaBusqueda = coloniaBusqueda;
  }

  public String getCpBusqueda() {
    return cpBusqueda;
  }

  public void setCpBusqueda(String cpBusqueda) {
    this.cpBusqueda = cpBusqueda;
  }

  public List<Colonia> getListaColoniasBusqueda() {
    return listaColoniasBusqueda;
  }

  public void setListaColoniasBusqueda(List<Colonia> listaColoniasBusqueda) {
    this.listaColoniasBusqueda = listaColoniasBusqueda;
  }

  public boolean isHabilitaColonias() {
    return habilitaColonias;
  }

  public void setHabilitaColonias(boolean habilitaColonias) {
    this.habilitaColonias = habilitaColonias;
  }

  public int getIdDireccionDirecta() {
    return idDireccionDirecta;
  }

  public void setIdDireccionDirecta(int idDireccionDirecta) {
    this.idDireccionDirecta = idDireccionDirecta;
  }

}
