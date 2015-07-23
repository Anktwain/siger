/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuariosDAO;
import dto.Usuarios;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class FotoBean implements Serializable{
    private String imagenPerfil;
    
    private Usuarios usuario;
    private UsuariosDAO usuarioDao;
    
    private UploadedFile archivo;
    
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        System.out.println("Archivo cargado...");
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }
    
    
}
