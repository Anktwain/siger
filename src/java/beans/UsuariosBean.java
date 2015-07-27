package beans;

import dao.AdministradorDAO;
import dao.GestorDAO;
import dao.UsuarioDAO;
import dto.Administrativo;
import dto.Gestor;
import dto.Usuario;
import impl.AdministradorIMPL;
import impl.GestorIMPL;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.Patrones;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 *
 * @author Cofradia
 */
@ManagedBean(name = "usuariosBean")
@ViewScoped
public class UsuariosBean implements Serializable {

    private Usuario usuario;
    private Usuario usuarioSeleccionado;
    private UsuarioDAO usuarioDao;
    private int perfil;
    private String nombre;
    private String paterno;
    private String materno;
    private String nombreLogin;
    private String password;
    private String confirmePassword;
    private String correo;
    private String imagenPerfil;
    private String extension;
    private SesionBean sesion;
    private List<Usuario> gestoresNoConfirmados;
    private List<Usuario> usuariosEncontrados;
    private List<Usuario> usuariosSeleccionados;
    private List<Usuario> todosUsuario;

    public List<Usuario> getTodosUsuario() {
        return todosUsuario;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    
    public void setTodosUsuario(List<Usuario> todosUsuario) {
        this.todosUsuario = todosUsuario;
    }

    public UsuariosBean() {
        usuarioDao = new UsuarioIMPL();
        usuario = new Usuario();
        perfil = Perfiles.GESTOR_NO_CONFIRMADO;
        imagenPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png";
        gestoresNoConfirmados = usuarioDao.buscarUsuariosNoConfirmados();
        todosUsuario = usuarioDao.buscarTodo();
        System.out.println("\n*************************** todosUsuario:");
        int i = 0;
        for (Usuario usuarioActual : todosUsuario) {
            System.out.println("***U." + (++i) + ": " + usuarioActual.getNombre() + " " + usuarioActual.getPaterno() + " " + usuarioActual.getMaterno());
        }
    }

    public SesionBean getSesion() {
        return sesion;
    }

    public void insertar() throws IOException {
        // Valida correo
        if (!validarCorreo()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo agregar el usuario", "El formato de correo electrónico no es válido."));
        } else if (!passwordsCoinciden()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo agregar el usuario", "Las contraseñas no coinciden, vuelva a escribirlas."));
        } else if (!nombreLoginEsUnico()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo agregar el usuario", "El nombre de usuario ya existe, elija otro."));
        } else if (!correoEsUnico()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo agregar el usuario", "La dirección de correo electrónico ya se encuentra registrada."));
        } else {
            insertarUsuario();
        }
    }

    private boolean nombreLoginEsUnico() {
        if (usuarioDao.buscarNombreLogin(nombreLogin) != null) {
            // Error: El usuario ya existe
            return false;
        }
        return true;
    }

    private boolean correoEsUnico() {
        if (usuarioDao.buscarCorreo(correo) != null) {
            return false;
        }
        return true;
    }

    private void insertarUsuario() throws IOException {
        // Crea objeto de tipo Usuario:
        usuario.setNombre(nombre);
        usuario.setPaterno(paterno);
        usuario.setMaterno(materno);
        usuario.setNombreLogin(nombreLogin);
        usuario.setPassword(MD5.encriptar(password));
        usuario.setPerfil(perfil);
        usuario.setCorreo(correo);
        usuario.setImagenPerfil(imagenPerfil);
        // Guarda el objeto en la BD
        if (usuarioDao.insertar(usuario) == 0) { // error al guardar
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                            "Error al guardar en BD, notifíquelo a un administrador."));
        } else {
            insertarUsuarioPorPerfil();
        }
    }

    private void insertarUsuarioPorPerfil() {
        Gestor gestor = new Gestor();
        Administrativo administrador = new Administrativo();
        GestorDAO gestorDao = new GestorIMPL();
        AdministradorDAO administradorDao = new AdministradorIMPL();
        // Busca al usuario recién ingresado y guarda un objeto de tipo Gestor o Administrador, según corresponda
        usuario = usuarioDao.buscarPorCorreo(usuario.getNombreLogin(), usuario.getCorreo());
        if (usuario.getPerfil() == Perfiles.ADMINISTRADOR) {
            administrador.setUsuario(usuario);
            administradorDao.insertar(administrador);
            Logs.log.info("Administrador ha agregado al administrador: " + usuario.getIdUsuario());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo usuario",
                            "Se ha agregó correctamente: " + usuario.getNombre()
                            + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
        }
        if (usuario.getPerfil() == Perfiles.GESTOR) {
            gestor.setUsuario(usuario);
            gestor.setExtension(extension);
            gestorDao.insertar(gestor);
            Logs.log.info("Administrador ha agregado al gestor: " + usuario.getIdUsuario());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo usuario",
                            "Se ha agregó correctamente: " + usuario.getNombre()
                            + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
        }
    }

    private boolean validarCorreo() {
        // Compila la cadena PATRON_EMAIL como un patrón
        Pattern patron = Pattern.compile(Patrones.PATRON_EMAIL);
        // Compara el correo con el patrón dado
        Matcher matcher = patron.matcher(correo);
        return matcher.matches();
    }

    private boolean passwordsCoinciden() {
        return password.equals(confirmePassword);
    }

    public void confirmarGestor(List<Usuario> usuariosSeleccionados) {
        for (int i = 0; i < (usuariosSeleccionados.size()); i++) {
            // CAMBIAR SU PERFIL EN LA BASE DE DATOS. DE -2 A 2            
            usuario = usuariosSeleccionados.get(i);
            usuario.setPerfil(Perfiles.GESTOR);
            usuarioDao.editar(usuario);
            if (usuarioDao.editar(usuario) == false) { // error al guardar
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo confirmar el usuario",
                                "Error al guardar en BD, notifíquelo a un administrador."));
            } else {
                // SE INSERTA EL GESTOR EN LA TABLA GESTORES
                insertarGestor(usuario);
                // SE BORRA EL CONFIRMADO DE LA LISTA PARA CONFIRMAR
                gestoresNoConfirmados.remove(usuario);
                RequestContext.getCurrentInstance().update("noConfirmados");
                RequestContext.getCurrentInstance().update("circulito");
                //LOG
                Logs.log.info("Se confirmo al gestor con id " + usuario.getIdUsuario());
                // MENSAJE EN VISTA
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Se confirmo un nuevo usuario",
                                "Se ha confirmado al usuario: " + usuario.getNombre()
                                + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
                // AVISO
                System.out.println("************ CONSOLA SIGERWEB ****************");
                System.out.println("Se ha confirmado al usuario: " + usuariosSeleccionados.get(i).getNombreLogin());
                RequestContext.getCurrentInstance().update("noConfirmados");
                RequestContext.getCurrentInstance().update("circulito");
            }
        }
    }

    public void eliminarGestor(List<Usuario> usuariosSeleccionados) {
        for (int i = 0; i < (usuariosSeleccionados.size()); i++) {
            usuario = usuariosSeleccionados.get(i);
            usuarioDao.eliminar(usuario);
            if (usuarioDao.eliminar(usuario) == false) { // error al borrar
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo borrar el usuario",
                                "Error al guardar en BD, notifíquelo a un administrador."));
            } else {
                RequestContext.getCurrentInstance().update("noConfirmados");
                RequestContext.getCurrentInstance().update("circulito");
                //LOG
                Logs.log.info("Se elimino al gestor con id " + usuario.getIdUsuario());
                // MENSAJE EN VISTA
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Se elimino el usuario",
                                "Se ha eliminado al usuario: " + usuario.getNombre()
                                + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
                // AVISO
                System.out.println("************ CONSOLA SIGERWEB ****************");
                System.out.println("Se ha eliminado al usuario: " + usuariosSeleccionados.get(i).getNombreLogin());
                RequestContext.getCurrentInstance().update("noConfirmados");
                RequestContext.getCurrentInstance().update("circulito");
            }
        }
    }

    private void insertarGestor(Usuario usuario) {
        Gestor gestor = new Gestor();
        GestorDAO gestorDao = new GestorIMPL();
        gestor.setUsuario(usuario);
        gestor.setExtension(extension);
        gestorDao.insertar(gestor);
        Logs.log.info("Administrador ha agregado al gestor: " + usuario.getIdUsuario());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo gestor",
                        "Se ha agregado correctamente."));
    }

    public void setSesion(SesionBean sesion) {
        this.sesion = sesion;
    }

    public List<Usuario> getGestorNoConfirmados() {
        return gestoresNoConfirmados;
    }

    public void setGestorNoConfirmados(List<Usuario> gestoresNoConfirmados) {
        this.gestoresNoConfirmados = gestoresNoConfirmados;
    }

    public List<Usuario> getUsuarioEncontrados() {
        return usuariosEncontrados;
    }

    public void setUsuarioEncontrados(List<Usuario> usuariosEncontrados) {
        this.usuariosEncontrados = usuariosEncontrados;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Usuario> getUsuarioSeleccionados() {
        return usuariosSeleccionados;
    }

    public void setUsuarioSeleccionados(List<Usuario> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("Se selecciono un registro");
    }

    public void onRowUnselect(UnselectEvent event) {
        System.out.println("Se deselecciono un registro");
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getNombreLogin() {
        return nombreLogin;
    }

    public void setNombreLogin(String nombreLogin) {
        this.nombreLogin = nombreLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmePassword() {
        return confirmePassword;
    }

    public void setConfirmePassword(String confirmePassword) {
        this.confirmePassword = confirmePassword;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
