package beans;

import java.io.Serializable;
import dto.Usuarios;
import impl.UsuariosIMPL;
import dao.UsuariosDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class IndexBean implements Serializable {

    private String nombreUsuario; // viene de la vista
    private String password; // viene de la vista
    private Usuarios usuario;
//    private static UsuariosIMPL usuariosDAO;

    IndexBean() {
        usuario = new Usuarios();
//        usuariosDAO = new UsuariosIMPL();
    }

    /**
     * Indica si la cadena del atributo {@code nombreUsuario} del bean es válida
     * según los siguientes parámetros:
     * <ul>
     * <li>Si es una cadena no vacía.
     * <li>Si no contiene algun tipo de espacios en blanco.
     * <li>Si contiene como máximo 20 caracteres de longitud.
     * </ul>
     *
     * @return {@code true} si la cadena es válida; {@code false} si la cadena
     * es inválida.
     */
    public boolean validarNombreUsuario() {
        if ((nombreUsuario != null) && (!nombreUsuario.equals("")) && 
                (nombreUsuario.length() <= 20) && (!nombreUsuario.matches("[.*\\s*]*"))) {
            return true;
        }
        return false;
    }

    public boolean validarPassword() {
        if ((password != null) && (!password.equals(""))) {
            return true;
        }
        return false;
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
    public static void main(String args[]) {
        UsuariosDAO usuariosDAO = new UsuariosIMPL();
        List<Usuarios> listaUsuarios = usuariosDAO.buscarTodo();
        System.out.println("Desplegando lista de usuarios traidos de la base de datos:");
        for (Usuarios usr : listaUsuarios) {
            System.out.println(usr.toString());
        }
        System.out.println("Despliegue terminado.");
    }
}
