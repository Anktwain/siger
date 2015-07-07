package util;

/**
 *
 * @author Pablo
 */
public class Usuario {
    
    public Usuario(){
    }

    public Usuario(Integer idUsuario, String nombre, String paterno, 
            String materno, String nombreLogin, int perfil, String correo, 
            String imagenPerfil) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.nombreLogin = nombreLogin;
        this.perfil = perfil;
        this.correo = correo;
        this.imagenPerfil = imagenPerfil;
    }
    
    private Integer idUsuario;
    private String nombre;
    private String paterno;
    private String materno;
    private String nombreLogin;
    private int perfil;
    private String correo;
    private String imagenPerfil;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
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
