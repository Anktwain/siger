/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.IndexBean;
import beans.ValidarDireccionesBean;
import dao.ColoniaDAO;
import dto.Colonia;
import dto.Credito;
import dto.carga.DireccionPorValidar;
import dto.carga.FilaDireccionExcel;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import util.GestionAutomatica;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ValidadorDirecciones {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  ValidarDireccionesBean validarDireccionesBean = (ValidarDireccionesBean) elContext.getELResolver().getValue(elContext, null, "validarDireccionesBean");

  // VARIABLES DE CLASE
  private final String DB_URL = "jdbc:mysql://localhost:3306/sigerbd";
  private final String USER = "root";
  private final String PASS = "root";
  private final ColoniaDAO coloniaDao;

  // CONSTRUCTOR
  public ValidadorDirecciones() {
    coloniaDao = new ColoniaIMPL();
  }

  // METODO QUE INTENTA VALIDAR AUTOMATICAMENTE LAS DIRECCIONES
  // ESTE METODO VALIDA EN VARIAS ETAPAS LA DIRECCION PARA DARLE LA MAYOR COHERENCIA
  public List<DireccionPorValidar> validacionAutomatica(List<FilaDireccionExcel> direcciones) {
    int contador = 0;
    List<DireccionPorValidar> noValidadas = new ArrayList();
    List<String> cambiadas = new ArrayList();
    for (int i = 0; i < (direcciones.size()); i++) {
      List<Colonia> listaColonias = coloniaDao.buscarPorCodigoPostal(direcciones.get(i).getCp());
      if (!listaColonias.isEmpty()) {
        // SI SOLO EXISTE UNA COLONIA, VALIDACION POR UNICA COLONIA
        if (listaColonias.size() == 1) {
          contador++;
          if (!insertarDireccion(direcciones.get(i), listaColonias.get(0), 0)) {
            Logs.log.error("No se pudo insertar la direccion validada.");
          }
        } else {
          for (int j = 0; j < (listaColonias.size()); j++) {
            String compuesta = retiraEspeciales(listaColonias.get(j).getTipo().toLowerCase()) + " " + retiraEspeciales(listaColonias.get(j).getNombre().toLowerCase());
            // SI LAS DOS COLONIAS COINCIDEN, VALIDACION POR COINCIDENCIA EXACTA
            if (retiraEspeciales(listaColonias.get(j).getNombre().toLowerCase()).compareTo(retiraEspeciales(direcciones.get(i).getColonia().toLowerCase())) == 0) {
              contador++;
              if (insertarDireccion(direcciones.get(i), listaColonias.get(j), 1)) {
                if (!generarGestionAutomatica(direcciones.get(i).getNumeroCredito())) {
                  Logs.log.error("No se pudo insertar la gestion automatica.");
                }
              } else {
                Logs.log.error("No se pudo insertar la direccion validada.");
              }
              break;
            }
            // SI LA COLONIA DE LA REMESA COINCIDE CON LA COLONIA COMPUESTA (TIPO DE COLONIA + NOMBRE), VALIDACION POR COINCIDENCIA EXACTA COLONIA COMPUESTA
            if (compuesta.compareTo(retiraEspeciales(direcciones.get(i).getColonia().toLowerCase())) == 0) {
              contador++;
              if (!insertarDireccion(direcciones.get(i), listaColonias.get(j), 0)) {
                Logs.log.error("No se pudo insertar la direccion validada.");
              }
              break;
            }
            // SI SE LLEGA A LA ULTIMA COLONIA Y NO EXISTEN COINCIDENCIAS AUN
            if (j == listaColonias.size() - 1) {
              for (int k = 0; k < (listaColonias.size()); k++) {
                // SI LA COLONIA EN LA BASE DE DATOS CONTIENE LA COLONIA DE LA REMESA, VALIDACION POR CONTENER COLONIA
                if (retiraEspeciales(listaColonias.get(k).getNombre().toLowerCase()).contains(retiraEspeciales(direcciones.get(i).getColonia().toLowerCase()))) {
                  contador++;
                  if (!insertarDireccion(direcciones.get(i), listaColonias.get(k), 0)) {
                    Logs.log.error("No se pudo insertar la direccion validada.");
                  }
                  break;
                }
                // SI SE RECORRIO NUEVAMENTE LA LISTA DE COLONIAS Y NO HAY COINCIDENCIA, VALIDACION PRIMER COLONIA DEL CODIGO POSTAL
                if (k == (listaColonias.size()) - 1) {
                  contador++;
                  cambiadas.add(direcciones.get(i).getNumeroCredito());
                  if (!insertarDireccion(direcciones.get(i), listaColonias.get(0), 0)) {
                    Logs.log.error("No se pudo insertar la direccion validada.");
                  }
                }
              }
              break;
            }
          }
        }
      } else {
        DireccionPorValidar d = new DireccionPorValidar();
        d.setId(i);
        d.setNumeroCredito(direcciones.get(i).getNumeroCredito());
        d.setCalle(direcciones.get(i).getCalle());
        d.setExterior(direcciones.get(i).getExterior());
        d.setInterior(direcciones.get(i).getInterior());
        d.setColonia(direcciones.get(i).getColonia());
        d.setMunicipio(direcciones.get(i).getMunicipio());
        d.setEstado(direcciones.get(i).getEstado());
        d.setCp(direcciones.get(i).getCp());
        noValidadas.add(d);
      }
    }
    if (!cambiadas.isEmpty()) {
      if (!enviarCorreoDireccionesCambiadas(cambiadas)) {
        Logs.log.error("No se envio el reporte de carga de direcciones.");
      }
    }
    validarDireccionesBean.setDireccionesValidadas("Se validaron automaticamente " + contador + " direcciones.");
    Logs.log.info(validarDireccionesBean.getDireccionesValidadas());
    return noValidadas;
  }

  // METODO QUE RETIRA CARACTERES ESPECIALES DE ESCRITURA Y REGRESA UNA CADENA LIMPIA
  public String retiraEspeciales(String colonia) {
    String originales = "áéíóúü";
    String reemplazos = "aeiouu";
    char[] arreglo = colonia.toCharArray();
    for (int i = 0; i < arreglo.length; i++) {
      int posicion = originales.indexOf(arreglo[i]);
      if (posicion > -1) {
        arreglo[i] = reemplazos.charAt(posicion);
      }
    }
    return new String(arreglo);
  }

  // METODO QUE INSERTA LA DIRECCION EN LA BASE DE DATOS
  public boolean insertarDireccion(FilaDireccionExcel f, Colonia col, int validada) {
    String consulta = "INSERT INTO direccion (id_sujeto, id_municipio, id_estado, id_colonia, calle, exterior";
    if (f.getInterior() != null) {
      consulta = consulta + ", interior";
    }
    consulta = consulta + ", latitud, longitud, validada) SELECT id_sujeto, '" + col.getMunicipio().getIdMunicipio() + "', '" + col.getMunicipio().getEstadoRepublica().getIdEstado() + "', '" + col.getIdColonia() + "', '" + f.getCalle() + "', '" + f.getExterior();
    if (f.getInterior() != null) {
      consulta = consulta + "', '" + f.getInterior();
    }
    consulta = consulta + "', '0.000000', '0.000000', '" + validada + "' FROM deudor WHERE id_deudor = (SELECT id_deudor FROM credito WHERE numero_credito = '" + f.getNumeroCredito() + "');";
    try {
      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
        return ejecutor.ejecutor(consulta, conn);
      }
    } catch (SQLException sqle) {
      Logs.log.error("No se pudo crear la conexion.");
      Logs.log.error(sqle.getMessage());
      return false;
    }
  }

  // METODO QUE GENERA LA GESTION AUTOMATICA DE VALIDACION DE DIRECCIONES
  public boolean generarGestionAutomatica(String numeroCredito) {
    GestionAutomatica ga = new GestionAutomatica();
    Credito c = new CreditoIMPL().buscar(numeroCredito);
    if (c != null) {
      return ga.generarGestionAutomatica("4DOMI", c, indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL DEUDOR " + c.getDeudor().getSujeto().getNombreRazonSocial());
    } else {
      Logs.log.info("No se encontro el credito " + numeroCredito);
      return false;
    }
  }

  // METODO QUE CREA UN CORREO PARA ALERTAR AL ADMINISTRADOR DE LOS CREDITOS CUYA COLONIA CAMBIO
  public boolean enviarCorreoDireccionesCambiadas(List<String> cambiadas) {
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
      // BUG:
      // SE DEBE AGREGAR EL CORREO DE LOS LICS
      //mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("lilia.delrio@corporativodelrio.com"));
      //mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress("carlos.delrio@corporativodelrio.com"));
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("eduardo.chavez@corporativodelrio.com"));
      mensaje.setSubject("REPORTE DE CARGA DE DIRECCIONES AL SISTEMA");
      MimeMultipart multiParte = new MimeMultipart();
      String cuerpo = "BUEN DIA LIC.\n\n";
      cuerpo = cuerpo + "LAS DIRECCIONES DE LOS SIGUIENTES CREDITOS FUERON MODIFICADAS DEBIDO A QUE LA COLONIA Y EL CODIGO POSTAL OBTENIDOS EN LA REMESA NO COINCIDEN.\n\n";
      cuerpo = cuerpo + "SE LES HA ASIGNADO DE FORMA AUTOMATICA LA PRIMER COLONIA QUE CORRESPONDA AL CODIGO POSTAL OBTENIDO EN LA REMESA.\n\n";
      cuerpo = cuerpo + "SALUDOS.\n\n\n";
      for (int i = 0; i < (cambiadas.size()); i++) {
        cuerpo = cuerpo + cambiadas.get(i) + "\n";
      }
      BodyPart texto = new MimeBodyPart();
      texto.setText(cuerpo);
      multiParte.addBodyPart(texto);
      mensaje.setContent(multiParte);
      Transport t = sesion.getTransport("smtp");
      // BUG:
      // proteger contraseña
      t.connect("eduardo.chavez@corporativodelrio.com", "009-94-92");
      t.sendMessage(mensaje, mensaje.getAllRecipients());
      ok = true;
      Logs.log.info("Se envio reporte de carga de direcciones.");
    } catch (Exception e) {
      Logs.log.error("No se logro enviar el reporte de carga de direcciones.");
      Logs.log.error(e.getMessage());
      ok = false;
    }
    return ok;
  }

}
