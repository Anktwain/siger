/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.envioGestiones;

import dao.EmailDAO;
import dto.Email;
import impl.EmailIMPL;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class EnviarCorreoGestiones {

  // VARIABLES DE CLASE
  private final EmailDAO emailDao;

  // CONSTRUCTOR
  public EnviarCorreoGestiones() {
    emailDao = new EmailIMPL();
  }

  // METODO QUE ENVIA EL CORREO CON LOS DATOS ADJUNTOS DE LAS GESTIONES
  public boolean enviarCorreoInbursa(String rutaCT, String rutaNoCT) {
    return crearCorreo(rutaCT, rutaNoCT);
  }

  // METODO QUE CREA Y ENVIA EL CORREO ELECTRONICO A INBURSA
  // BUG:
  // PARA PRUEBAS SOLO LO ENVIARA A NOSOTROS
  public boolean crearCorreo(String rutaCT, String rutaNoCT) {
    boolean ok;
    try {
      Properties props = new Properties();
      props.put("mail.smtp.host", "mail.corporativodelrio.com");
      props.setProperty("mail.smtp.starttls.enable", "true");
      props.setProperty("mail.smtp.port", "587");
      props.setProperty("mail.smtp.user", "eduardo.chavez@corporativodelrio.com");
      props.setProperty("mail.smtp.auth", "true");
      Session sesion = Session.getDefaultInstance(props, null);
      MimeMessage mensaje = new MimeMessage(sesion);
      mensaje.setFrom(new InternetAddress("eduardo.chavez@corporativodelrio.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("bvalenciab@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("elugoc@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("cavilam@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("smendozas@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("lfloresm@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("savilesh@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("vmendozac@inbursa.com"));
      mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress("eduardo.chavez@corporativodelrio.com"));
      mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress("lilia.delrio@corporativodelrio.com"));
      mensaje.setSubject("REPORTE SEMANAL DE GESTIONES");
      MimeMultipart multiParte = new MimeMultipart();
      BodyPart texto = new MimeBodyPart();
      texto.setText("HOLA A TODOS\n\nLES ENVÍO LAS GESTIONES\n\nSALUDOS");
      multiParte.addBodyPart(texto);
      BodyPart adjunto = new MimeBodyPart();
      adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaCT)));
      adjunto.setFileName(rutaCT.replace(Directorios.RUTA_WINDOWS_REPORTES_GESTIONES, ""));
      multiParte.addBodyPart(adjunto);
      adjunto = new MimeBodyPart();
      adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaNoCT)));
      adjunto.setFileName(rutaNoCT.replace(Directorios.RUTA_WINDOWS_REPORTES_GESTIONES, ""));
      multiParte.addBodyPart(adjunto);
      mensaje.setContent(multiParte);
      Transport t = sesion.getTransport("smtp");
      // BUG:
      // proteger contraseña
      t.connect("eduardo.chavez@corporativodelrio.com", "009-94-92");
      t.sendMessage(mensaje, mensaje.getAllRecipients());
      ok = true;
      Logs.log.info("Se envio reporte de gestiones al banco.");
    } catch (Exception e) {
      Logs.log.error("No se pudo enviar reporte de gestiones al banco.");
      Logs.log.error(e.getMessage());
      ok = false;
    }
    return ok;
  }
  // GETTERS & SETTERS

}
