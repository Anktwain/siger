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

  /**
   *
   */
    public TelefonoBean() {
    telefono = new Telefono();
    telefonoDao = new TelefonoIMPL();
  }

  /**
   *
   * @param sujeto
   */
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

  /**
   *
   */
  public void limpiarEntradas() {
    numero = null;
    tipo = null;
    extension = null;
    lada = null;
    horario = null;
  }

  /**
   *
   * @return
   */
  public String getNumero() {
    return numero;
  }

  /**
   *
   * @param numero
   */
  public void setNumero(String numero) {
    this.numero = numero;
  }

  /**
   *
   * @return
   */
  public String getTipo() {
    return tipo;
  }

  /**
   *
   * @param tipo
   */
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   *
   * @return
   */
  public String getExtension() {
    return extension;
  }

  /**
   *
   * @param extension
   */
  public void setExtension(String extension) {
    this.extension = extension;
  }

  /**
   *
   * @return
   */
  public String getLada() {
    return lada;
  }

  /**
   *
   * @param lada
   */
  public void setLada(String lada) {
    this.lada = lada;
  }

  /**
   *
   * @return
   */
  public String getHorario() {
    return horario;
  }

  /**
   *
   * @param horario
   */
  public void setHorario(String horario) {
    this.horario = horario;
  }

}
