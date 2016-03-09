/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioDAO;
import dto.Usuario;
import impl.UsuarioIMPL;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "usuariosBean")
@ViewScoped
public class UsuariosBean {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private Usuario usuarioSeleccionado;
  private final UsuarioDAO usuarioDao;
  private List<Usuario> usuariosNoConfirmados;
  private List<Usuario> usuariosActuales;

  // CONSTRUCTOR
  public UsuariosBean() {
    usuarioSeleccionado = new Usuario();
    usuariosNoConfirmados = new ArrayList();
    usuariosActuales = new ArrayList();
    usuarioDao = new UsuarioIMPL();
    obtenerListas();
  }

  // METODO QUE OBTIENE LAS LISTAS QUE SE DEBEN PRECARGAR
  public final void obtenerListas() {
    usuariosNoConfirmados = usuarioDao.buscarUsuariosNoConfirmados(indexBean.getUsuario().getDespacho().getIdDespacho());
    usuariosActuales = usuarioDao.buscarUsuariosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  // GETTERS & SETTERS
  public List<Usuario> getUsuariosNoConfirmados() {
    return usuariosNoConfirmados;
  }

  public void setUsuariosNoConfirmados(List<Usuario> usuariosNoConfirmados) {
    this.usuariosNoConfirmados = usuariosNoConfirmados;
  }

  public Usuario getUsuarioSeleccionado() {
    return usuarioSeleccionado;
  }

  public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
    this.usuarioSeleccionado = usuarioSeleccionado;
  }

  public List<Usuario> getUsuariosActuales() {
    return usuariosActuales;
  }

  public void setUsuariosActuales(List<Usuario> usuariosActuales) {
    this.usuariosActuales = usuariosActuales;
  }

}
