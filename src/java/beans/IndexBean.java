package beans;

import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import util.constantes.Constantes;
import javax.faces.context.FacesContext;
import util.MD5;
import java.util.Calendar;

@ManagedBean(name = "indexBean")
@SessionScoped

public class IndexBean implements Serializable {

    private String nombreUsuario; // viene de la vista
    private String password; // viene de la vista
    private Usuario usuario;
    private UsuarioDAO usuarioDao;
    private SesionBean beanDeSesion;

    public IndexBean() {
        usuario = new Usuario();
        usuarioDao = new UsuarioIMPL();
        beanDeSesion = new SesionBean();
    }

    /**
     * Indica si la cadena del atributo {@code nombreUsuario} del bean es válida
     * según los siguientes parámetros:
     * <ul>
     * <li>Si es una cadena no vacía.
     * <li>Si no contiene algun tipo de espacios en blanco.
     * <li>Si contiene como máximo {@code Constantes.longNombreUsuario}
     * caracteres de longitud.
     * </ul>
     *
     * @return {@code true} si la cadena es válida, {@code false} si la cadena
     * es inválida.
     */
    public boolean validarNombreUsuario() {
        return (nombreUsuario != null) && (!nombreUsuario.equals(""))
                && (nombreUsuario.length() <= Constantes.longNombreUsuario) && (!nombreUsuario.matches("[.*\\s*]*"));
    }

    /**
     * Indica si la cadena del atributo {@code password} del bean es válida
     * según los siguientes parámetros:
     * <ul>
     * <li>Si es una cadena no vacía.
     * <li>Si no contiene algun tipo de espacios en blanco.
     * <li>Si contiene como máximo {@code Constantes.longPassword} caracteres de
     * longitud.
     * </ul>
     *
     * @return {@code true} si la cadena es válida, {@code false} si la cadena
     * es inválida.
     */
    public boolean validarPassword() {
        return (password != null) && (!password.equals(""))
                && (password.length() <= Constantes.longPassword) && (!password.matches("[.*\\s*]*"));
    }

//    public static void main(String args[]) {
//        UsuarioDAO usuariosDAO = new UsuarioIMPL();
//        List<Usuario> listaUsuario = usuariosDAO.buscarTodo();
//        System.out.println("Desplegando lista de usuarios traidos de la base de datos:");
//        for (Usuario usr : listaUsuario) {
//            System.out.println(usr.toString());
//        }
//        System.out.println("Despliegue terminado.");
//    }
    /**
     * Solicita una búsqueda con las cadenas de nombre de usuario y de password
     * y devuelve el objeto usuario al que correspondan en su caso, o
     * {@code null} en otro caso.
     *
     * @throws java.io.IOException
     */
    public void ingresar() throws IOException {
        System.out.println("#################### Estamos en la la funcion ingresar\n");

        usuario = usuarioDao.buscar(nombreUsuario, MD5.encriptar(password));
        Calendar cal = Calendar.getInstance();
        if (usuario != null) {
            switch (usuario.getPerfil()) {
                case -2:
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso denegado.",
                                    usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (GESTOR_NO_CONFIRMADO) correctamente."));
                    beanDeSesion.setSesionActiva(false);
                    break;
                case 0:
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso denegado.",
                                    usuario.getNombre() + "No podrá ingresar con el perfil " + usuario.getPerfil() + " (ELIMINADO) porque ha sido desactivado."));
                    System.err.println("#################### NOT OK. ACCESO DENEGADO(U0)"); // linea de depuracion
                    beanDeSesion.setSesionActiva(false);
                    break;
                case 1:
//                    FacesContext mensaje = FacesContext.getCurrentInstance();
//                    mensaje.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.",
//                            usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (ADMINISTRADOR) correctamente."));
//                    FacesContext.getCurrentInstance().getExternalContext().redirect("faces/panelAdministrativo.xhtml");
//                    System.out.println("#################### ACCESO ADMIN CORRECTO");
//                    FacesContext.getCurrentInstance().addMessage("",
//                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.",
//                                    usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (ADMINISTRADOR) correctamente."));
                    FacesContext.getCurrentInstance().getExternalContext().redirect("faces/panelAdministrativo.xhtml");
                    System.out.println("#################### OK. ACCESO ADMIN CORRECTO"); // linea de depuracion
                    beanDeSesion.setHoraInicio(cal.getTime());
                    beanDeSesion.setHoraFin(cal.getTime());
                    beanDeSesion.setSesionActiva(true);
                    beanDeSesion.setUsuarioActivo(new Usuario(
                            usuario.getNombre(), usuario.getPaterno(), usuario.getNombreLogin(), 
                            usuario.getPassword(), usuario.getPerfil(), usuario.getCorreo()));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.",
                                    usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (GESTOR) correctamente."));
                    FacesContext.getCurrentInstance().getExternalContext().redirect("faces/panelGestor.xhtml");
                    System.out.println("#################### OK. ACCESO GESTOR CORRECTO"); // linea de depuracion
                    beanDeSesion.setHoraInicio(cal.getTime());
                    beanDeSesion.setHoraFin(cal.getTime());
                    beanDeSesion.setSesionActiva(true);
                    beanDeSesion.setUsuarioActivo(new Usuario(
                            usuario.getNombre(), usuario.getPaterno(), usuario.getNombreLogin(), 
                            usuario.getPassword(), usuario.getPerfil(), usuario.getCorreo()));
                    break;
                default:
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.",
                                    usuario.getNombre() + "Está intentando entrar con un perfil desconocido. (Perfil =" + usuario.getPerfil() + ")."));
                    System.out.println("#################### OK. ESTÁS INTENTANDO ENTRAR CON UN PERFIL DESCONOCIDO!!!"); // linea de depuracion
                    beanDeSesion.setSesionActiva(false);
                    break;
            }

        } else {
            FacesContext.getCurrentInstance().addMessage("",
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.",
                            "Verifica que los datos que has introducido son correctos y que el administrador haya dado de alta tu cuenta."));
            System.out.println("#################### OK. USUARIO NO CONFIRMADO O NOMBRE DE USUARIO O CONTRASEÑA INCORRECTOS!!!"); // linea de depuracion
            beanDeSesion.setSesionActiva(false);
        }
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nomUsuario) {
        this.nombreUsuario = nomUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
