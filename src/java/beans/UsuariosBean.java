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
 * La clase {@code UsuariosBean} permite ... y es el bean correspondiente a la
 * vista {@code usuarios.xhtml}
 *
 * @author
 * @author
 * @author Cofradia
 * @since SigerWeb2.0
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

    /**
     *
     *
     * @return
     */
    public List<Usuario> getTodosUsuario() {
        return todosUsuario;
    }

    /**
     *
     *
     * @return
     */
    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    /**
     *
     *
     * @param
     */
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    /**
     *
     *
     * @param
     */
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

    /**
     *
     *
     * @return
     */
    public SesionBean getSesion() {
        return sesion;
    }

    /**
     *
     *
     * @param
     */
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

    /**
     *
     *
     * @return
     */
    private boolean nombreLoginEsUnico() {
        if (usuarioDao.buscarNombreLogin(nombreLogin) != null) {
            // Error: El usuario ya existe
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @return
     */
    private boolean correoEsUnico() {
        if (usuarioDao.buscarCorreo(correo) != null) {
            return false;
        }
        return true;
    }

    /**
     *
     *
     *
     */
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

    /**
     *
     *
     *
     */
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

    /**
     *
     *
     * @return
     */
    private boolean validarCorreo() {
        // Compila la cadena PATRON_EMAIL como un patrón
        Pattern patron = Pattern.compile(Patrones.PATRON_EMAIL);
        // Compara el correo con el patrón dado
        Matcher matcher = patron.matcher(correo);
        return matcher.matches();
    }

    /**
     *
     *
     * @return
     */
    private boolean passwordsCoinciden() {
        return password.equals(confirmePassword);
    }

    /**
     *
     *
     * @param
     */
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

    /**
     *
     *
     * @param
     */
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

    /**
     *
     *
     * @param
     */
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

    /**
     *
     *
     * @param
     */
    public void setSesion(SesionBean sesion) {
        this.sesion = sesion;
    }

    /**
     *
     *
     * @return
     */
    public List<Usuario> getGestorNoConfirmados() {
        return gestoresNoConfirmados;
    }

    /**
     *
     *
     * @param
     */
    public void setGestorNoConfirmados(List<Usuario> gestoresNoConfirmados) {
        this.gestoresNoConfirmados = gestoresNoConfirmados;
    }

    /**
     *
     *
     * @return
     */
    public List<Usuario> getUsuarioEncontrados() {
        return usuariosEncontrados;
    }

    /**
     *
     *
     * @param
     */
    public void setUsuarioEncontrados(List<Usuario> usuariosEncontrados) {
        this.usuariosEncontrados = usuariosEncontrados;
    }

    /**
     *
     *
     * @return
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     *
     *
     * @return
     */
    public List<Usuario> getUsuarioSeleccionados() {
        return usuariosSeleccionados;
    }

    /**
     *
     *
     * @param
     */
    public void setUsuarioSeleccionados(List<Usuario> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }

    /**
     *
     *
     * @param
     */
    public void onRowSelect(SelectEvent event) {
        System.out.println("Se selecciono un registro");
    }

    /**
     *
     *
     * @param
     */
    public void onRowUnselect(UnselectEvent event) {
        System.out.println("Se deselecciono un registro");
    }

    /**
     *
     *
     * @param
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     *
     *
     * @return
     */
    public int getPerfil() {
        return perfil;
    }

    /**
     *
     *
     * @param
     */
    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    /**
     *
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     *
     * @param
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     *
     * @return
     */
    public String getPaterno() {
        return paterno;
    }

    /**
     *
     *
     * @param
     */
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    /**
     *
     *
     * @return
     */
    public String getMaterno() {
        return materno;
    }

    /**
     *
     *
     * @param
     */
    public void setMaterno(String materno) {
        this.materno = materno;
    }

    /**
     *
     *
     * @return
     */
    public String getNombreLogin() {
        return nombreLogin;
    }

    /**
     *
     *
     * @param
     */
    public void setNombreLogin(String nombreLogin) {
        this.nombreLogin = nombreLogin;
    }

    /**
     *
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     *
     * @param
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     *
     * @return
     */
    public String getConfirmePassword() {
        return confirmePassword;
    }

    /**
     *
     *
     * @param
     */
    public void setConfirmePassword(String confirmePassword) {
        this.confirmePassword = confirmePassword;
    }

    /**
     *
     *
     * @return
     */
    public String getCorreo() {
        return correo;
    }

    /**
     *
     *
     * @param
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     *
     *
     * @return
     */
    public String getImagenPerfil() {
        return imagenPerfil;
    }

    /**
     *
     *
     * @param
     */
    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    /**
     *
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     *
     *
     * @param
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
}
