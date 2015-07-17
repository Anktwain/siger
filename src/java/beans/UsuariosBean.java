package beans;

import dao.UsuariosDAO;
import dto.Usuarios;
import impl.UsuariosIMPL;
import java.io.IOException;
import java.io.Serializable;
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
 * @author Pablo
 */
@ManagedBean
@ViewScoped
public class UsuariosBean implements Serializable {

    private Usuarios usuario;
    private UsuariosDAO usuarioDao;

    private int perfil;
    private String nombre;
    private String paterno;
    private String materno;
    private String nombreLogin;
    private String password;
    private String confirmePassword;
    private String correo;
    private String imagenPerfil;

    private SesionBean sesion;
    private List<Usuarios> gestoresNoConfirmados;
    private List<Usuarios> usuariosEncontrados;
    
    private List<Usuarios> usuariosSeleccionados;

    public UsuariosBean() {
        usuarioDao = new UsuariosIMPL();
        usuario = new Usuarios();
        perfil = Perfiles.GESTOR_NO_CONFIRMADO;
        imagenPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png";
        gestoresNoConfirmados = usuarioDao.buscarUsuariosNoConfirmados();
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
        if (usuarioDao.insertar(usuario) == false) { // error al guardar
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                            "Error al guardar en BD, notifíquelo a un administrador." ));
        } else {
            Logs.log.info("Administrador ha agregado al gestor " + usuario.getIdUsuario());
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
    
    public void confirmarGestores(List<Usuarios> usuariosSeleccionados){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        for (int i = 0; i < (usuariosSeleccionados.size()); i++) {
            // CAMBIAR SU PERFIL EN LA BASE DE DATOS. DE -2 A 2
            // MOSTRAR EN CONSOLA LO QUE SE CAMBIO
            System.out.println("SE CONFIRMO AL USUARIO: " + usuariosSeleccionados.get(i).toString());
        }
    }

    public void setSesion(SesionBean sesion) {
        this.sesion = sesion;
    }

    public List<Usuarios> getGestoresNoConfirmados() {
        return gestoresNoConfirmados;
    }

    public void setGestoresNoConfirmados(List<Usuarios> gestoresNoConfirmados) {
        this.gestoresNoConfirmados = gestoresNoConfirmados;
    }

    public List<Usuarios> getUsuariosEncontrados() {
        return usuariosEncontrados;
    }

    public void setUsuariosEncontrados(List<Usuarios> usuariosEncontrados) {
        this.usuariosEncontrados = usuariosEncontrados;
    }

    public Usuarios getUsuario() {
        return usuario;
    }
    
    public List<Usuarios> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }
 
    public void setUsuariosSeleccionados(List<Usuarios> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }
    
    public void onRowSelect(SelectEvent event) {
        System.out.println("Se selecciono un registro");
    }
 
    public void onRowUnselect(UnselectEvent event) {
        System.out.println("Se deselecciono un registro");
    }

    public void setUsuario(Usuarios usuario) {
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

}
