/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuariosDAO;
import dto.Usuarios;
import impl.UsuariosIMPL;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import util.MD5;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class RecuperarContrasenaBean {

    private String usuarioLogin;
    private String correo;

    Usuarios usuario;
    UsuariosDAO usuarioDao;

    public RecuperarContrasenaBean() {
        usuario = new Usuarios();
        usuarioDao = new UsuariosIMPL();
    }

    public void recuperar() {
        // verifica que el usuario y correo existan en la BD
        usuario = usuarioDao.buscarPorCorreo(usuarioLogin, correo);

        if (usuario != null) {
            crearCorreo();
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("ERROR", "El usuario y/o contraseña no existen"));
        }
    }

    private void crearCorreo() {
        try {
            // Propiedades de la conexión
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.user", "servicios.cofradia@gmail.com");
            propiedades.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesión
            Session sesion = Session.getDefaultInstance(propiedades);

            // Construimos el mensaje
            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress("servicios.cofradia@gmail.com"));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getCorreo()));
//            mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress("eduardo.chavez@corporativodelrio.com"));
            mensaje.setSubject("Recuperar Contraseña");
            mensaje.setText(generarMensaje(), "ISO-8859-1", "html");

            // Se envía el correo
            System.out.println("... Se envía el correo a " + usuario.getCorreo());
            Transport t = sesion.getTransport("smtp");
            t.connect("servicios.cofradia@gmail.com", "@Cofradia&");
            t.sendMessage(mensaje, mensaje.getAllRecipients());

            // Cambia la contraseña
            usuario.setPassword(MD5.encriptar(generarNuevoPassword()));
            usuarioDao.editar(usuario);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("ENVIANDO MENSAJE",
                    "Revise su correo electrónico, se ha enviado un mensaje para la recuperación de su contraseña"));

            // Se cierra
            t.close();
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("ERROR", "Hubo un error al envíar el correo"));
            e.printStackTrace();
        }
    }

    private String generarMensaje() {
        String mensaje;
        mensaje = "<font color=\"black\"><i>Usted recibe este mensaje porque ha olvidado su contraseña "
                + "para ingresar a SigerWeb. Si usted no ha hecho ninguna "
                + "solicitud para recuperar su contraseña, póngase en contacto "
                + "con el administrador del sistema.</i><br/><br />";
        mensaje += "Su nueva contraseña es: </font><font color=\"blue\">" + generarNuevoPassword() + "</font><br/><br />";
        mensaje += "<font size=2 color =\"red\"><b>IMPORTANTE:</b></font><br/>";
        mensaje += "<font color=\"red\">Esta contraseña es provisional, cámbiela inmediatamente "
                + "desde el menú <i>Configuración</i> de SigerWeb.<br /></font>";

        return mensaje;
    }

    private String generarNuevoPassword() {
        return usuario.getPassword().substring(0, 10);
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
