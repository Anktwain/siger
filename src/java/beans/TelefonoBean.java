/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.TelefonoDAO;
import dto.Sujeto;
import dto.Telefono;
import impl.TelefonoIMPL;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class TelefonoBean implements Serializable {

  // Objeto Telefono, sus propiedades y acceso a BD
  private Telefono telefono;

  private String numero;
  private String tipo;
  private String extension;
  private String lada;
  private String horario;

  private TelefonoDAO telefonoDao;

  // Construyendo...
  public TelefonoBean() {
    telefono = new Telefono();
    telefonoDao = new TelefonoIMPL();
  }

  public boolean editar(Telefono tel) {
    FacesContext context = FacesContext.getCurrentInstance();
    boolean ok = false;
    
    ok = telefonoDao.editar(tel); 
    if (ok) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Operación Exitosa",
              "Se modificó el registro seleccionado"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación Exitosa",
              "No se pudo editar el registro seleccionado."));
    }
    
    return ok;
  }
  
  public boolean eliminar(Telefono tel) {
    FacesContext context = FacesContext.getCurrentInstance();
    boolean ok = false;
    
    ok = telefonoDao.eliminar(tel);
    if (ok) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Operación Exitosa",
              "Se eliminó el registro seleccionado"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación Exitosa",
              "No se pudo eliminar el registro seleccionado."));
    }
    
    return ok;
  }  

  public void agregar(Sujeto sujeto) {
    telefono.setNumero(numero);
    telefono.setTipo(tipo);
    telefono.setExtension(extension);
    telefono.setLada(lada);
    telefono.setHorario(horario);
    telefono.setSujeto(sujeto);

    if (telefonoDao.insertar(telefono)) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage("Operación Exitosa",
              "Se agregó un nuevo teléfono: " + numero + " para: "
              + " " + sujeto.getNombreRazonSocial()));
      limpiarEntradas();
      Logs.log.info("Se agregó objeto: Telefono");
    } else {
      Logs.log.error("No se pudo agregar objeto: Telefono.");
    }
  }

  public void limpiarEntradas() {
    numero = null;
    tipo = null;
    extension = null;
    lada = null;
    horario = null;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getLada() {
    return lada;
  }

  public void setLada(String lada) {
    this.lada = lada;
  }

  public String getHorario() {
    return horario;
  }

  public void setHorario(String horario) {
    this.horario = horario;
  }

}
