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
    IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
    
    String nombreUsuario;
    
    public void metodo() {
        System.out.println("Nombre: " + nombreUsuario);
    }

    public PanelGestorBean() {
        nombreUsuario = indexBean.getNombreUsuario();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public IndexBean getIndexBean() {
        return indexBean;
    }

    public void setIndexBean(IndexBean indexBean) {
        this.indexBean = indexBean;
    }

    
}
