/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class PanelGestorBean {
    @ManagedProperty("#{indexBean}")
    private IndexBean indexBean;
    
    String nombreUsuario = indexBean.getNombreUsuario();

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

}
