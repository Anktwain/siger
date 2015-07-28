package beans;

import dao.UsuarioDAO;
import dto.Usuario;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.Patrones;
import util.constantes.Perfiles;

/**
 * La clase {@code AltaGestorBean} permite ... y es el bean correspondiente a la
 * vista {@code AltaGestor.xhtml}
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
@ManagedBean
@ViewScoped
public class AltaGestorBean {
    /* Se indica que el gestor se da de alta pero debe esperar a ser confirmado
     por un administrador
     */

    private int perfil;

    private String nombre;
    private String paterno;
    private String materno;
    private String nombreLogin;
    private String password;
    private String confirmePassword;
    private String correo;
    private String imagenPerfil;

    private Usuario usuario;

    private UsuarioDAO usuarioDao;

    public AltaGestorBean() {
        usuarioDao = new UsuarioIMPL();
        usuario = new Usuario();
        perfil = Perfiles.GESTOR_NO_CONFIRMADO;
        imagenPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png";
    }

    public void insertar() throws IOException {
        // Valida correo
        if (!validarCorreo()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                    "El formato de correo electrónico no es válido."));
        } else if (!passwordsCoinciden()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                    "Las contraseñas no coinciden, vuelva a escribirlas."));
        } else if (!nombreLoginEsUnico()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                    "El nombre de usuario ya existe, elija otro."));
        } else if (!correoEsUnico()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                    "La dirección de correo electrónico ya se encuentra registrada."));
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
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "No se pudo agregar el usuario",
                    "Error al guardar en BD, repórtese con Soporte Técnico."));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            context.addMessage(null, new FacesMessage("SE AGREGÓ NUEVO USUARIO",
                    "Se ha agregado un nuevo gestor,"
                    + " sin embargo no podrá ingresar al sistema hasta "
                    + "que sea confirmado por un Administrador"));
            externalContext.getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
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

}
