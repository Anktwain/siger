/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.IndexBean;
import beans.ValidarDireccionesBean;
import dao.ColoniaDAO;
import dao.CreditoDAO;
import dto.Colonia;
import dto.Credito;
import dto.carga.DireccionPorValidar;
import dto.carga.FilaDireccionExcel;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import util.constantes.Campanas;
import util.constantes.Direcciones;
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
  private final CreditoDAO creditoDao;

  // CONSTRUCTOR
  public ValidadorDirecciones() {
    coloniaDao = new ColoniaIMPL();
    creditoDao = new CreditoIMPL();
  }

  // METODO QUE INTENTA VALIDAR AUTOMATICAMENTE LAS DIRECCIONES
  // ESTE METODO VALIDA EN VARIAS ETAPAS LA DIRECCION PARA DARLE LA MAYOR COHERENCIA
  public List<DireccionPorValidar> validacionAutomatica(List<FilaDireccionExcel> direcciones) {
    int contadorValidadas = 0;
    int contadorPorValidar = 0;
    for (int i = 0; i < (direcciones.size()); i++) {
      // SE VALIDA QUE EL CREDITO EXISTA EN EL SISTEMA
      Credito c = creditoDao.buscar(direcciones.get(i).getNumeroCredito());
      if (c != null) {
        // SI EL CREDITO ESTA EN NUEVA REMESA, SE BUSCARA VALIDAR LA DIRECCION AUTOMATICAMENTE
        if (c.getCampana().getIdCampana() == Campanas.NUEVA_REMESA) {
          // SE BUSCAN LAS COLONIAS DE ESE CODIGO POSTAL
          List<Colonia> listaColonias = coloniaDao.buscarPorCodigoPostal(direcciones.get(i).getCp());
          if (!listaColonias.isEmpty()) {
            String compuesta;
            for (int j = 0; j < (listaColonias.size()); j++) {
              compuesta = retiraEspeciales(listaColonias.get(j).getTipo().toLowerCase()) + " " + retiraEspeciales(listaColonias.get(j).getNombre().toLowerCase());
              // SI LAS DOS COLONIAS COINCIDEN, VALIDACION POR COINCIDENCIA EXACTA
              if (retiraEspeciales(listaColonias.get(j).getNombre().toLowerCase()).compareTo(retiraEspeciales(direcciones.get(i).getColonia().toLowerCase())) == 0) {
                contadorValidadas++;
                if (insertarDireccion(direcciones.get(i), listaColonias.get(j))) {
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
                contadorValidadas++;
                if (!insertarDireccion(direcciones.get(i), listaColonias.get(j))) {
                  Logs.log.error("No se pudo insertar la direccion validada.");
                }
                break;
              }
              // SI SE LLEGA A LA ULTIMA COLONIA Y NO EXISTEN COINCIDENCIAS AUN
              if (j == listaColonias.size() - 1) {
                contadorPorValidar++;
                if (!insertarDireccionTexto(direcciones.get(i), Direcciones.SIN_VALIDAR)) {
                  Logs.log.error("No se pudo insertar la direccion texto para el credito " + direcciones.get(i).getNumeroCredito() + ".");
                }
              }
            }
          } // SI NO EXISTEN COLONIAS EN ESTE CODIGO POSTAL
          else {
            contadorPorValidar++;
            if (!insertarDireccionTexto(direcciones.get(i), Direcciones.SIN_VALIDAR)) {
              Logs.log.error("No se pudo insertar la direccion texto para el credito " + direcciones.get(i).getNumeroCredito() + ".");
            }
          }
        } // SI ES UN NUEVO CREDITO, CONSERVADO O REACTIVADO, SOLO SE AÑADIRA LA DIRECCION A LA LISTA DIRECCIONES TEXTO
        else {
          contadorPorValidar++;
          if (!insertarDireccionTexto(direcciones.get(i), Direcciones.VALIDADA)) {
            Logs.log.error("No se pudo insertar la direccion texto para el credito " + direcciones.get(i).getNumeroCredito() + ".");
          }
        }
      } else {
        Logs.log.error("Se intento ingresar una direccion para un credito que no existe en el sistema.");
      }
    }
    validarDireccionesBean.setDireccionesValidadas("Se validaron automaticamente " + contadorValidadas + " direcciones, se insertaron " + contadorPorValidar + " direcciones para su posterior validacion (en el modulo de validacion de direcciones del panel administrativo)");
    Logs.log.info(validarDireccionesBean.getDireccionesValidadas());
    // BUG:
    // el metodo no deberia regresar nada, refactorizar
    return new ArrayList();
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
  public boolean insertarDireccion(FilaDireccionExcel f, Colonia col) {
    String consulta = "INSERT INTO direccion (id_sujeto, id_municipio, id_estado, id_colonia, calle, exterior";
    if (f.getInterior() != null) {
      consulta = consulta + ", interior";
    }
    consulta = consulta + ", latitud, longitud, principal) SELECT id_sujeto, '" + col.getMunicipio().getIdMunicipio() + "', '" + col.getMunicipio().getEstadoRepublica().getIdEstado() + "', '" + col.getIdColonia() + "', '" + f.getCalle() + "', '" + f.getExterior();
    if (f.getInterior() != null) {
      consulta = consulta + "', '" + f.getInterior();
    }
    consulta = consulta + "', '0.000000', '0.000000', 0 FROM deudor WHERE id_deudor = (SELECT id_deudor FROM credito WHERE numero_credito = '" + f.getNumeroCredito() + "');";
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

  // METODO QUE INSERTA UNA DIRECCION DE TEXTO EN LA BASE DE DATOS PARA QUE POSTERIORMENTE SEA EDITADA O VALIDADA
  public boolean insertarDireccionTexto(FilaDireccionExcel f, int validada) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String consulta = "INSERT INTO direccion_texto (fecha, numero_credito, calle";
    if (f.getExterior() != null) {
      consulta = consulta + ", exterior";
    }
    if (f.getInterior() != null) {
      consulta = consulta + ", interior";
    }
    consulta = consulta + ", colonia, municipio, estado, codigo_postal, validada) VALUES ('" + df.format(new Date()) + "', '" + f.getNumeroCredito() + "', '" + f.getCalle();
    if (f.getExterior() != null) {
      consulta = consulta + "', '" + f.getExterior();
    }
    if (f.getInterior() != null) {
      consulta = consulta + "', '" + f.getInterior();
    }
    consulta = consulta + "', '" + f.getColonia() + "', '" + f.getMunicipio() + "', '" + f.getEstado() + "', '" + f.getCp() + "', " + validada + ");";
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
  // DEPRECATED:
  // este metodo no se utilizara mas
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
      // SE DEBE AGREGAR EL CORREO DE LOS JEFES DE JEFES (LICS)
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
