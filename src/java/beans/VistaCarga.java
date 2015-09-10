/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.constantes.Directorios;
import util.log.Logs;

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
  
  // Otros beans
  @ManagedProperty(value = "#{filaBean}")
  private FilaBean filaBean;
  
  public VistaCarga() {
    btnSigPaso2Disabled = true;
    btnSigPaso3Disabled = true;
  }
  
  public void subirArchivo(FileUploadEvent e) throws IOException {
    UploadedFile archivoRecibido = e.getFile();
    
    nombreArchivo = nombrarArchivo(archivoRecibido.getFileName());
    
    byte[] bytes = null;
    
    try {
      if (archivoRecibido != null) {
        bytes = archivoRecibido.getContents();
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(nombreArchivo)));
        stream.write(bytes);
        stream.close();
      }
      
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Carga exitosa",
              "Se cargó el archivo: " + archivoRecibido.getFileName()));
      setBtnSigPaso2Disabled(false);
    } catch (IOException ioe) {
      Logs.log.error("No se pudo cargar el archivo de la remesa");
      Logs.log.error(ioe.getMessage());
    }
  }
  
  public void crearArchivoSql() {
    
  }
  
  private String nombrarArchivo(String elArchivo) {
    Calendar calendario = new GregorianCalendar();
    
    return Directorios.RUTA_REMESAS + "remesa"
            + Integer.toString(calendario.get(Calendar.YEAR))
            + Integer.toString(1 + calendario.get(Calendar.MONTH))
            + Integer.toString(calendario.get(Calendar.DATE))
            + Integer.toString(calendario.get(Calendar.HOUR_OF_DAY))
            + Integer.toString(calendario.get(Calendar.MINUTE))
            + Integer.toString(calendario.get(Calendar.SECOND)) + "."
            + FilenameUtils.getExtension(elArchivo);
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
  
}
