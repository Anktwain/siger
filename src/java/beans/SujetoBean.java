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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.constantes.Patrones;
import util.constantes.Sujetos;

/**
 *
 * @author antonio
 */
@ManagedBean(name = "sujetoBean")
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
    sujeto = new Sujeto();
    sujeto.setNombreRazonSocial(nombreRazonSocial);
    sujeto.setRfc(rfc);
    sujeto.setEliminado(eliminado);

    // Lo envía a la BD y regresa el resultado
    return sujetoDao.insertar(sujeto);
  }
  
  public boolean editar(Sujeto sujeto){
    sujeto.setNombreRazonSocial(nombreRazonSocial);
    sujeto.setRfc(rfc);
    // se manda el sujeto para editar
    boolean ok = sujetoDao.editar(sujeto);
    // si se logro regresar 1
    if (ok){
      return true;
    }
    // si no se logro regresar 0
    else{
      return false;
    }
  }

  // VALIDACIONES
  public boolean nombreEsValido() {
    return (nombreRazonSocial != null) && (!nombreRazonSocial.equals(""))
            && (nombreRazonSocial.length() <= Sujetos.LONGITUD_NOMBRE)
            && (!nombreRazonSocial.matches("[.*\\s*]*"));
  }
  
  // funcion para validar el rfc de una nueva empresa
  public boolean validarRfc() {
    // pasamos el rfc a mayusculas para poder comparar
    rfc = rfc.toUpperCase();
    Pattern patron = Pattern.compile(Patrones.PATRON_RFC_MORAL);
    Matcher matcher = patron.matcher(rfc);
    return matcher.matches();
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

  public Sujeto getSujeto() {
    return sujeto;
  }

  public void setSujeto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

  public SujetoDAO getSujetoDao() {
    return sujetoDao;
  }

  public void setSujetoDao(SujetoDAO sujetoDao) {
    this.sujetoDao = sujetoDao;
  }

}
