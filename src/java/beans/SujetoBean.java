/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.SujetoDAO;
import dto.Sujeto;
import impl.SujetoIMPL;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.constantes.Sujetos;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class SujetoBean implements Serializable {

  // Objeto que gestiona este bean

  private Sujeto sujeto;

  // Atributos del objeto Sujeto
  private String nombreRazonSocial;
  private String rfc;
  private int eliminado;

  // DAO, para acceso a la BD
  private SujetoDAO sujetoDao;

  // Construyendo...
  public SujetoBean() {
    sujeto = new Sujeto();
    sujetoDao = new SujetoIMPL();
    eliminado = Sujetos.ACTIVO;
  }

  // GESTIÓN DE USUARIOS
  public Sujeto insertar() {
    // Crea el objeto Sujeto
    sujeto.setNombreRazonSocial(nombreRazonSocial);
    sujeto.setRfc(rfc);
    sujeto.setEliminado(eliminado);

    // Lo envía a la BD y regresa el resultado
    return sujetoDao.insertar(sujeto);
  }

  // VALIDACIONES
  public boolean nombreEsValido() {
    return (nombreRazonSocial != null) && (!nombreRazonSocial.equals(""))
            && (nombreRazonSocial.length() <= Sujetos.LONGITUD_NOMBRE)
            && (!nombreRazonSocial.matches("[.*\\s*]*"));
  }
  
  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    nombreRazonSocial = null;
    rfc = null;
  }
  
  // SETTERS & GETTERS

  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public int getEliminado() {
    return eliminado;
  }

  public void setEliminado(int eliminado) {
    this.eliminado = eliminado;
  }
  
}
