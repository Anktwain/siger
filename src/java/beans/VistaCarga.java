/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class VistaCarga implements Serializable {
  
  // Controles de la vista
  private String nombreArchivo;
  private boolean btnSigPaso2Disabled;
  private boolean btnSigPaso3Disabled;
  
  // Conjunto de filas obtenidas del archivo de Excel
  // private Fila filas;
  
  private int numeroCreditosEnLaRemesa;
  
  // Otros beans
  @ManagedProperty(value = "#{cargaBean}")
  private CargaBean cargaBean;
  
  public VistaCarga() {
    btnSigPaso2Disabled = true;
    btnSigPaso3Disabled = true;
  }
  
  public void subirArchivo(FileUploadEvent e) throws IOException {
    if(cargaBean.subirArchivo(e)) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Carga exitosa",
              "Se cargó el archivo para continuar la carga de la remesa."));
      setBtnSigPaso2Disabled(false);
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo cargar el archivo",
              "Comunique esta situación a Soporte Técnico."));
    }
  }
  
  public void leerArchivoExcel() {
    numeroCreditosEnLaRemesa = cargaBean.leerArchivoExcel();
    if(numeroCreditosEnLaRemesa > 0) {
      RequestContext.getCurrentInstance().execute("PF('dlgNumeroDeFilas').show();");
      setBtnSigPaso3Disabled(false);
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo leer la remesa",
              "Comunique esta situación a Soporte Técnico"));
    }
  }
  
  public void crearArchivoSql() {
    if(cargaBean.crearArchivoSql()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se crearon las consultas",
              "Verifique salida"));
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo crear archivo sql",
              "Comunique esta situación a Soporte Técnico"));
    }
  }
  
  public String getNombreArchivo() {
    return nombreArchivo;
  }
  
  public void setNombreArchivo(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }
  
  public boolean isBtnSigPaso2Disabled() {
    return btnSigPaso2Disabled;
  }
  
  public void setBtnSigPaso2Disabled(boolean btnSigPaso2Disabled) {
    this.btnSigPaso2Disabled = btnSigPaso2Disabled;
  }

  public boolean isBtnSigPaso3Disabled() {
    return btnSigPaso3Disabled;
  }

  public void setBtnSigPaso3Disabled(boolean btnSigPaso3Disabled) {
    this.btnSigPaso3Disabled = btnSigPaso3Disabled;
  }

  public CargaBean getCargaBean() {
    return cargaBean;
  }

  public void setCargaBean(CargaBean cargaBean) {
    this.cargaBean = cargaBean;
  }

  public int getNumeroCreditosEnLaRemesa() {
    return numeroCreditosEnLaRemesa;
  }

  public void setNumeroCreditosEnLaRemesa(int numeroCreditosEnLaRemesa) {
    this.numeroCreditosEnLaRemesa = numeroCreditosEnLaRemesa;
  }

}
