package beans;

import dao.UsuarioDAO;
import dto.Usuario;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import util.CaptchaView;
import util.MD5;
import util.log.Logs;

/**
 * La clase {@code RecuperarContrasenaBean} permite ... y es el bean
 * correspondiente a la vista {@code recuperarContrasena.xhtml}
 *
 * @author
 * @author
 * @author antonio
 * @since SigerWeb2.0
 */
@ManagedBean
@ViewScoped
public class RecuperarContrasenaBean implements Serializable {

  private String usuarioLogin;
  private String correo;
  Usuario usuario;
  private final UsuarioDAO usuarioDao;

  /**
   *
   *
   *
   */
  public RecuperarContrasenaBean() {
    usuario = new Usuario();
    usuarioDao = new UsuarioIMPL();
  }

  /**
   *
   *
   */
  public void recuperar() {
    // verifica que el usuario y correo existan en la BD
    usuario = usuarioDao.buscarPorCorreo(usuarioLogin, correo);
    //CaptchaView.submit();

    if (usuario != null) {
      crearCorreo();
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/SigerWeb");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir al index.");
        Logs.log.error(ioe);
      }
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "NO SE PUEDE RECUPERAR CONTRASEÑA",
              "Los datos proporcionados no son correctos"));
    }
  }

  /**
   *
   *
   *
   */
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
      Transport t = sesion.getTransport("smtp");
      t.connect("servicios.cofradia@gmail.com", "@Cofradia&");
      t.sendMessage(mensaje, mensaje.getAllRecipients());

      // Cambia la contraseña
      usuario.setPassword(MD5.encriptar(generarNuevoPassword()));
      usuarioDao.editar(usuario);

      FacesContext context = FacesContext.getCurrentInstance();
      ExternalContext externalContext = context.getExternalContext();
      context.addMessage(null, new FacesMessage("Operacion exitosa",
              "Revise su correo electrónico, se ha enviado un mensaje para la recuperación de su contraseña"));
      externalContext.getFlash().setKeepMessages(true);
      FacesContext.getCurrentInstance().getExternalContext().redirect("/SigerWeb");
      // Se cierra
      t.close();
    } catch (MessagingException me) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage("ERROR", "Hubo un error al envíar el correo"));
      Logs.log.error("No se pudo enviar el correo de recuperacion de contraseña.");
      Logs.log.error(me);
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir al index.");
      Logs.log.error(ioe);
    }
    /*
     catch(IOException ioe){
     Logs.log.error("No se pudo redirigir al index.");
     Logs.log.error(ioe);
     }
     */
  }

  /**
   *
   *
   * @return
   */
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

  /**
   *
   *
   * @return
   */
  private String generarNuevoPassword() {
    return usuario.getPassword().substring(0, 10);
  }

  /**
   *
   *
   * @return
   */
  public String getUsuarioLogin() {
    return usuarioLogin;
  }

  /**
   *
   *
   * @param usuarioLogin
   */
  public void setUsuarioLogin(String usuarioLogin) {
    this.usuarioLogin = usuarioLogin;
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
   * @param correo
   */
  public void setCorreo(String correo) {
    this.correo = correo;
  }

}
