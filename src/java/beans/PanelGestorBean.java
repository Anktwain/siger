/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author brionvega
 */
@ManagedBean
@SessionScoped
public class PanelGestorBean implements Serializable{
//    @ManagedProperty(value = "indexBean")
//    private IndexBean indexBean;
    
    ELContext elContext = FacesContext.getCurrentInstance().getELContext();
//    IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
    private IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
    
    private String nombreUsuario;
    private String imagenDePerfil;
    private String nombre;
    private String correo;

    public PanelGestorBean() {
        nombre = indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno();
        nombreUsuario = indexBean.getUsuario().getNombreLogin();
        imagenDePerfil = indexBean.getUsuario().getImagenPerfil();
        correo = indexBean.getUsuario().getCorreo();
        System.out.println(nombreUsuario);
        System.out.println(imagenDePerfil);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getImagenDePerfil() {
        return imagenDePerfil;
    }

    public void setImagenDePerfil(String imagenDePerfil) {
        this.imagenDePerfil = imagenDePerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    

    public IndexBean getIndexBean() {
        return indexBean;
    }

    public void setIndexBean(IndexBean indexBean) {
        this.indexBean = indexBean;
    }
    
}
