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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class TelefonoBean implements Serializable {
  // Objeto que gestiona este bean
  private Telefono telefono;
  
  // Atributos del objeto Telefono
  private String numero;
  private String tipo;
  private String extension;
  private String lada;
  private String horario;
  
  // Acceso a la BD
  private final TelefonoDAO telefonoDao;
  
  // Construyendo...
  public TelefonoBean() {
    telefono = new Telefono();
    telefonoDao = new TelefonoIMPL();
  }
  
  
  // GESTIÓN DE TELEFONOS
  public Telefono insertar(Sujeto sujeto) {
    // Verfica que el sujeto sea válido
    if (sujeto.getIdSujeto() == null) {
      Logs.log.error("El método TelefonoBean.insertar(sujeto) recibe un sujeto null");
      return null;
    } else {
      // Crea el objeto Telefono
      telefono.setNumero(numero);
      telefono.setTipo(tipo);
      telefono.setExtension(extension);
      telefono.setLada(lada);
      telefono.setHorario(horario);
      telefono.setSujeto(sujeto);

      return telefonoDao.insertar(telefono);
    }
  }
  
  public List<Telefono> listar(int idSujeto) {
    return telefonoDao.buscarPorSujeto(idSujeto);
  }
  
  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    numero = null;
    tipo = null;
    extension = null;
    lada = null;
    horario = null;
  }  

  
  // SETTERS & GETTERS
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