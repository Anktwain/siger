/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.AdministrativoDAO;
import dao.GestorDAO;
import dao.UsuarioDAO;
import dto.Administrativo;
import dto.Gestor;
import dto.Usuario;
import impl.AdministrativoIMPL;
import impl.GestorIMPL;
import impl.UsuarioIMPL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.Patrones;
import util.constantes.Perfiles;

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
  private final AdministrativoDAO administradorDao;
  private final GestorDAO gestorDao;
  private List<Usuario> usuariosNoConfirmados;
  private List<Usuario> usuariosActuales;
  private String nombreNuevoUsuario;
  private String paternoNuevoUsuario;
  private String maternoNuevoUsuario;
  private String loginNuevoUsuario;
  private String passwordNuevoUsuario;
  private String password2NuevoUsuario;
  private String correoNuevoUsuario;
  private String tipoUsuarioSeleccionado;
  private List<String> tipoUsuarios;

  // CONSTRUCTOR
  public UsuariosBean() {
    usuarioSeleccionado = new Usuario();
    usuarioSeleccionado.setImagenPerfil(Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png");
    usuariosNoConfirmados = new ArrayList();
    usuariosActuales = new ArrayList();
    tipoUsuarios = new ArrayList();
    usuarioDao = new UsuarioIMPL();
    administradorDao = new AdministrativoIMPL();
    gestorDao = new GestorIMPL();
    obtenerListas();
  }

  // METODO QUE OBTIENE LAS LISTAS QUE SE DEBEN PRECARGAR
  public final void obtenerListas() {
    usuariosNoConfirmados = usuarioDao.buscarUsuariosNoConfirmados(indexBean.getUsuario().getDespacho().getIdDespacho());
    usuariosActuales = usuarioDao.buscarUsuariosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    tipoUsuarios = Arrays.asList("GESTOR", "ADMINSTRADOR");
  }

  // METODO QUE CONFIRMA EL GESTOR
  public void confirmarGestor() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    usuarioSeleccionado.setPerfil(Perfiles.GESTOR);
    if (usuarioDao.editar(usuarioSeleccionado)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se confirmo al gestor seleccionado."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo confirmar al gestor. Contacte al equipo de sistemas."));
    }
    obtenerListas();
  }

  // METODO QUE RECHAZA AL GESTOR
  public void rechazarGestor() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    usuarioSeleccionado.setPerfil(Perfiles.ELIMINADO);
    if (usuarioDao.editar(usuarioSeleccionado)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se rechazo al gestor seleccionado."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo rechazar al gestor. Contacte al equipo de sistemas."));
    }
    obtenerListas();
  }

  // METODO QUE CREA UN NUEVO USUARIO
  public void crearUsuario() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (!passwordNuevoUsuario.equals(password2NuevoUsuario)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Las contraseñas no coinciden."));
    } else if (!validarCorreo(correoNuevoUsuario)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El correo electronico proporcionado no es valido."));
    } else if (usuarioDao.buscarNombreLogin(loginNuevoUsuario) != null) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El nombre de usuario ya se encuentra registrado."));
    } else if (usuarioDao.buscarCorreo(correoNuevoUsuario) != null) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El correo electronico ya se encuentra registrado."));
    } else {
      Usuario u = new Usuario();
      u.setCorreo(correoNuevoUsuario);
      u.setDespacho(indexBean.getUsuario().getDespacho());
      u.setImagenPerfil(Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png");
      u.setMaterno(maternoNuevoUsuario);
      u.setNombre(nombreNuevoUsuario);
      u.setNombreLogin(loginNuevoUsuario);
      u.setPassword(MD5.encriptar(passwordNuevoUsuario));
      u.setPaterno(paternoNuevoUsuario);
      if (tipoUsuarioSeleccionado.equals("GESTOR")) {
        u.setPerfil(Perfiles.GESTOR);
      } else {
        u.setPerfil(Perfiles.ADMINISTRADOR);
      }
      if (usuarioDao.insertar(u) != 0) {
        if (tipoUsuarioSeleccionado.equals("GESTOR")) {
          Gestor g = new Gestor();
          g.setUsuario(u);
          if (gestorDao.insertar(g)) {
            contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego a un nuevo gestor."));
          } else {
            contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar el gestor. Contacte al equipo de sistemas."));
          }
        } else {
          Administrativo a = new Administrativo();
          a.setUsuario(u);
          if (administradorDao.insertar(a)) {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevoUsuario').hide();");
            contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego a un nuevo administrador."));
          } else {
            contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar el administrador. Contacte al equipo de sistemas."));
          }
        }
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar el usuario. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE MODIFICA AL USUSARIO SELECCIONADO
  public void modificarUsuario() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (usuarioSeleccionado.getPerfil() != Perfiles.SUPER_ADMINISTRADOR) {
      if (usuarioDao.editar(usuarioSeleccionado)) {
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se guardaron los cambios. Contacte al equipo de sistemas."));
      }
    }
    RequestContext.getCurrentInstance().execute("PF('dlgDetalleUsuario').hide();");
    obtenerListas();
  }

  // METODO QUE ELIMINA EL USUARIO SELECCIONADO
  public void eliminarUsuario() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (usuarioSeleccionado.getPerfil() != Perfiles.SUPER_ADMINISTRADOR) {
      usuarioSeleccionado.setPerfil(Perfiles.ELIMINADO);
      if (usuarioDao.editar(usuarioSeleccionado)) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se elimino al usuario seleccionado."));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo eliminar al usuario. Contacte al equipo de sistemas."));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error.", "No se puede eliminar a un super administrador."));
    }
    RequestContext.getCurrentInstance().execute("PF('dlgEliminarUsuario').hide();");
    obtenerListas();
  }

  // METODO QUE VALIDA UNA DIRECCION DE CORREO ELECTRONICO
  private boolean validarCorreo(String correo) {
    // Compila la cadena PATRON_EMAIL como un patrón
    Pattern patron = Pattern.compile(Patrones.PATRON_EMAIL);
    // Compara el correo con el patrón dado
    Matcher matcher = patron.matcher(correo);
    return matcher.matches();
  }

  // METODO QUE ETIQUETA LOS PERFILES
  public String etiquetarPerfiles(int perfil) {
    String etiqueta = "";
    switch (perfil) {
      case (Perfiles.ADMINISTRADOR):
        etiqueta = "Administrador";
        break;
      case (Perfiles.ADMINISTRADOR_NO_CONFIRMADO):
        etiqueta = "Administrador no confirmado";
        break;
      case (Perfiles.ELIMINADO):
        etiqueta = "Eliminado";
        break;
      case (Perfiles.GESTOR):
        etiqueta = "Gestor";
        break;
      case (Perfiles.GESTOR_NO_CONFIRMADO):
        etiqueta = "Gestor no confirmado";
        break;
      case (Perfiles.SUPER_ADMINISTRADOR):
        etiqueta = "Super administrador";
        break;
    }
    return etiqueta;
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

  public String getNombreNuevoUsuario() {
    return nombreNuevoUsuario;
  }

  public void setNombreNuevoUsuario(String nombreNuevoUsuario) {
    this.nombreNuevoUsuario = nombreNuevoUsuario;
  }

  public String getPaternoNuevoUsuario() {
    return paternoNuevoUsuario;
  }

  public void setPaternoNuevoUsuario(String paternoNuevoUsuario) {
    this.paternoNuevoUsuario = paternoNuevoUsuario;
  }

  public String getMaternoNuevoUsuario() {
    return maternoNuevoUsuario;
  }

  public void setMaternoNuevoUsuario(String maternoNuevoUsuario) {
    this.maternoNuevoUsuario = maternoNuevoUsuario;
  }

  public String getLoginNuevoUsuario() {
    return loginNuevoUsuario;
  }

  public void setLoginNuevoUsuario(String loginNuevoUsuario) {
    this.loginNuevoUsuario = loginNuevoUsuario;
  }

  public String getPasswordNuevoUsuario() {
    return passwordNuevoUsuario;
  }

  public void setPasswordNuevoUsuario(String passwordNuevoUsuario) {
    this.passwordNuevoUsuario = passwordNuevoUsuario;
  }

  public String getPassword2NuevoUsuario() {
    return password2NuevoUsuario;
  }

  public void setPassword2NuevoUsuario(String password2NuevoUsuario) {
    this.password2NuevoUsuario = password2NuevoUsuario;
  }

  public String getCorreoNuevoUsuario() {
    return correoNuevoUsuario;
  }

  public void setCorreoNuevoUsuario(String correoNuevoUsuario) {
    this.correoNuevoUsuario = correoNuevoUsuario;
  }

  public String getTipoUsuarioSeleccionado() {
    return tipoUsuarioSeleccionado;
  }

  public void setTipoUsuarioSeleccionado(String tipoUsuarioSeleccionado) {
    this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
  }

  public List<String> getTipoUsuarios() {
    return tipoUsuarios;
  }

  public void setTipoUsuarios(List<String> tipoUsuarios) {
    this.tipoUsuarios = tipoUsuarios;
  }

}
