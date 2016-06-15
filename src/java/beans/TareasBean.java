/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EventoDAO;
import dto.Evento;
import impl.EventoIMPL;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "tareasBean")
@ViewScoped
public class TareasBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private ScheduleModel modelo;
  private ScheduleEvent evento;
  private final EventoDAO eventoDao;

  // CONSTRUCTOR
  public TareasBean() {
    modelo = new DefaultScheduleModel();
    evento = new DefaultScheduleEvent();
    eventoDao = new EventoIMPL();
    cargarEventos();
  }
  
  // METODO QUE OBTIENE LOS EVENTOS QUE SE HAN CREADO EN LA BASE DE DATOS
  public final void cargarEventos(){
    List<Evento> eventos = eventoDao.buscarEventosVigentes(indexBean.getUsuario().getIdUsuario());
    for (int i = 0; i <(eventos.size()); i++) {
      modelo.addEvent(new DefaultScheduleEvent(eventos.get(i).getTitulo(), eventos.get(i).getFechaInicio(), eventos.get(i).getFechaFin()));
    }
  }

  // METODO QUE OBTIENE LOS DATOS DEL DIA SELECCIONADO
  public void seleccionDeDia(SelectEvent eventoSeleccionado) {
    evento = new DefaultScheduleEvent("", (Date) eventoSeleccionado.getObject(), (Date) eventoSeleccionado.getObject());
  }
  
  // METODO QUE OBTIENE LOS DATOS DEL EVENTO SELECCIONADO
  public void seleccionDeEvento(SelectEvent eventoSeleccionado) {
        evento = (ScheduleEvent) eventoSeleccionado.getObject();
    }

  // METODO QUE AGREGA EL EVENTO
  public void agregarEvento() {
    // AGREGAR EL EVENTO A LA BD
    //RECARGAR LOS EVENTOS
    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "En construccion.", "Disculpe las molestias."));
  }

  // GETTERS & SETTERS
  public ScheduleModel getModelo() {
    return modelo;
  }

  public void setModelo(ScheduleModel modelo) {
    this.modelo = modelo;
  }

  public ScheduleEvent getEvento() {
    return evento;
  }

  public void setEvento(ScheduleEvent evento) {
    this.evento = evento;
  }

}
