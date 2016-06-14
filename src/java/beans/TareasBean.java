/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Date;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
  private boolean todoElDia;
  private String diaSeleccionado;
  private ScheduleModel modelo;
  private ScheduleEvent evento;

  // CONSTRUCTOR
  public TareasBean() {
    todoElDia = false;
    modelo = new DefaultScheduleModel();
    evento = new DefaultScheduleEvent();
  }

  // METODO QUE AGREGA EL EVENTO
  public void agregarEvento(){
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

  public boolean isTodoElDia() {
    return todoElDia;
  }

  public void setTodoElDia(boolean todoElDia) {
    this.todoElDia = todoElDia;
  }

  public String getDiaSeleccionado() {
    return diaSeleccionado;
  }

  public void setDiaSeleccionado(String diaSeleccionado) {
    this.diaSeleccionado = diaSeleccionado;
  }
  
  // CLASE MIEMBRO QUE SIMULARA LOS EVENTOS HASTA QUER ESTEN EN LA BASE DE DATOS
  public static class Evento {

    // VARIABLES DE CLASE
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;

    //CONSTRUCTOR
    public Evento() {
    }

    //GETTERS & SETTERS
    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public Date getFechaInicio() {
      return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
      this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
      return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
      this.fechaFin = fechaFin;
    }

  }

}
