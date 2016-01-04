/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Files;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casiopea Server
 */
@ManagedBean
@RequestScoped
public class PhotoStreamer {
  private static StreamedContent defaultFileContent;
  private StreamedContent fileContent;

  // METODO QUE OBTIENE EL CONTENIDO DEL ARCHIVO
  public StreamedContent getFileContent() throws IOException {
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    String photoId = externalContext.getRequestParameterMap().get("photo_id");
    String sSistemaOperativo = System.getProperty("os.name");
    System.out.println("SISTEMA : " + sSistemaOperativo);
    if (photoId == null || photoId.equals("")) {
      defaul();
      fileContent = defaultFileContent;
    } else {
      if (photoId.compareToIgnoreCase("null") == 0) {
        defaul();
        fileContent = defaultFileContent;
      } else {
        if (sSistemaOperativo.compareTo("Linux") == 0) {
          /*CASO DE PRUEBAS*/
          InputStream inputStream = new FileInputStream(new File("/home/casiopea/Escritorio/comprobantes/" + photoId));
          File f = new File("/home/casiopea/Escritorio/comprobantes/" + photoId);
          fileContent = new DefaultStreamedContent(inputStream, Files.probeContentType(f.toPath()));
        } else {
          /*CASO DEL SERVIDOR*/
          InputStream inputStream = new FileInputStream(new File("C:\\\\comprobantes\\" + photoId));
          File f = new File("C:\\\\comprobantes\\" + photoId);
          fileContent = new DefaultStreamedContent(inputStream, Files.probeContentType(f.toPath()));
        }
      }
    }
    return fileContent;
  }

  // METODO QUE ESTABLECE EL CONTENIDO DEL ARCHIVO (NUNCA SE UTILIZA)
  public void setFileContent(StreamedContent fileContent) {
    this.fileContent = fileContent;
  }

  // METODO POR DEFAUL JAJAJAJAJAJA!!!
  public void defaul() throws FileNotFoundException {
    String sSistemaOperativo = System.getProperty("os.name");
    if (sSistemaOperativo.compareTo("Linux") == 0) {
      InputStream inputStream = new FileInputStream(new File("/home/brionvega/Escritorio/comprobantes/ninguno.jpg"));
      defaultFileContent = new DefaultStreamedContent(inputStream, "image/jpg");
    } else {
      InputStream inputStream = new FileInputStream(new File("C:\\\\comprobantes\\ninguno.jpg"));
      defaultFileContent = new DefaultStreamedContent(inputStream, "image/jpg");
    }
  }

}
