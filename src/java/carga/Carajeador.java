package carga;

import dto.Actualizacion;
import dto.Credito;
import dto.Despacho;
import dto.Deudor;
import dto.Email;
import dto.Fila;
import dto.Gestor;
import dto.Institucion;
import dto.Linea;
import dto.Producto;
import dto.Remesa;
import dto.Subproducto;
import dto.Sujeto;
import dto.Telefono;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import nuevaImplementacionDAO.ActualizacionDAO;
import nuevaImplementacionDAO.CreditoDAO;
import nuevaImplementacionDAO.DespachoDAO;
import nuevaImplementacionDAO.DeudorDAO;
import nuevaImplementacionDAO.EmailDAO;
import nuevaImplementacionDAO.FacDAO;
import nuevaImplementacionDAO.GestorDAO;
import nuevaImplementacionDAO.InstitucionDAO;
import nuevaImplementacionDAO.LineaDAO;
import nuevaImplementacionDAO.ProductoDAO;
import nuevaImplementacionDAO.RemesaDAO;
import nuevaImplementacionDAO.SubproductoDAO;
import nuevaImplementacionDAO.SujetoDAO;
import nuevaImplementacionDAO.TelefonoDAO;
import nuevaImplementacionIMPL.ActualizacionIMPL;
import nuevaImplementacionIMPL.CreditoIMPL;
import nuevaImplementacionIMPL.DespachoIMPL;
import nuevaImplementacionIMPL.DeudorIMPL;
import nuevaImplementacionIMPL.EmailIMPL;
import nuevaImplementacionIMPL.FacIMPL;
import nuevaImplementacionIMPL.GestorIMPL;
import nuevaImplementacionIMPL.InstitucionIMPL;
import nuevaImplementacionIMPL.LineaIMPL;
import nuevaImplementacionIMPL.ProductoIMPL;
import nuevaImplementacionIMPL.RemesaIMPL;
import nuevaImplementacionIMPL.SubproductoIMPL;
import nuevaImplementacionIMPL.SujetoIMPL;
import nuevaImplementacionIMPL.TelefonoIMPL;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.BautistaDeArchivos;
import util.Fecha;
import util.HibernateUtil;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Carajeador {

  private static Session session;
  private static Transaction transaction;

  public static void carajear(int idDespacho, List<Fila> filas,
          int mes, int totalCreditos, float totalSaldoVencido) {
    String query = "";
    String linea = null;
    String archivoSql = Directorios.RUTA_REMESAS + BautistaDeArchivos.bautizar("script", BautistaDeArchivos.PREFIJO, "sql");
    String archivoPlano = Directorios.RUTA_REMESAS + BautistaDeArchivos.bautizar("direccionar", BautistaDeArchivos.PREFIJO, "txt");

    try {
      Fecha fecha = new Fecha();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      
      DespachoDAO despachoDao = new DespachoIMPL();

      SujetoDAO sujetoDao = new SujetoIMPL();
      Sujeto sujeto;

      DeudorDAO deudorDao = new DeudorIMPL();
      Deudor deudor;

      GestorDAO gestorDao = new GestorIMPL();
      Gestor gestor;

      SubproductoDAO subproductoDao = new SubproductoIMPL();
      Subproducto subproducto;

      ProductoDAO productoDao = new ProductoIMPL();
      Producto producto;
      
      LineaDAO lineaDao = new LineaIMPL();

      InstitucionDAO institucionDao = new InstitucionIMPL();
      Institucion institucion;

      CreditoDAO creditoDao = new CreditoIMPL();
      Credito credito;
      
      ActualizacionDAO actualizacionDao = new ActualizacionIMPL();
      Actualizacion actualizacion;

      FacDAO facDao = new FacIMPL();

      RemesaDAO remesaDao = new RemesaIMPL();
      // Remesa remesa;

      TelefonoDAO telefonoDao = new TelefonoIMPL();

      EmailDAO emailDao = new EmailIMPL();

      session = HibernateUtil.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      /* Despacho al cual se le cargarán los créditos de la remesa. */
      Despacho despacho = despachoDao.getById(session, idDespacho);
      
      /* Objeto remesa */
      Remesa remesa = new Remesa(mes, Calendar.getInstance().get(Calendar.YEAR),
              totalCreditos, totalSaldoVencido, new Date(), 0, null);

      remesaDao.insert(session, remesa);
      
      for (Fila f : filas) {
        /* Crea un nuevo sujeto */
        sujeto = sujetoDao.insert(session, new Sujeto(f.getNombre(), f.getRfc(), 0));
        
        /* Nuevo deudor asociado al sujeto */
        deudor = deudorDao.insert(session, new Deudor(sujeto, f.getIdCliente(), null, null, null, null));
        
        /* Asigna todos los emails encontrados para ese sujeto */
        for(String email : f.getCorreos()) {
          emailDao.insert(session, new Email(sujeto, email, "Remesa"));
        }

        /* Asigna todos los teléfonos encontrados para ese sujeto */
        for(String telefono : f.getTelsAdicionales()) {
          telefonoDao.insert(session, new Telefono(sujeto, telefono, "Remesa", null, null, "ND"));
        }
        
        /* Gestor al cual se asignará el crédito */
        gestor = gestorDao.getById(session, f.getIdGestor());
  
        /* Obtiene subproducto y producto, el primero no es obligatorio */
//        subproducto = subproductoDao.getById(session, f.getIdSubproducto());
//        producto = productoDao.getById(session, f.getIdProducto());
        
        /* Crea un nuevo crédito */
        credito = new Credito(despacho, deudor, gestor, f.getProductoDTO(), f.getSubproductoDTO(),
                f.getCredito(), simpleDateFormat.parse(fecha.convertirFormatoMySQL(f.getFechaInicioCredito())),
                simpleDateFormat.parse(fecha.convertirFormatoMySQL(f.getFechaVencimientoCred())), null,
                f.getDisposicionFloat(), Float.parseFloat(f.getMensualidad()), (float)0.0,
                0, f.getCuenta(), 1, null, null, null, null, null, null, null);
        
        creditoDao.insert(session, credito);
        
        /* Crea objeto Linea para el crédito */
        // hacer las correcciones necesarias para que el id sea autonumérico
        //lineaDao.insert(session, new Linea(mes, credito, linea))
        
        /* Crea la actualización */
        System.out.println(credito);
        System.out.println(remesa);
        System.out.println(f.getMesesVencidos());
        System.out.println(f.getSaldoVencido());
        System.out.println(f.getEstatus());
        System.out.println(f.getFechaUltimoPago());
        System.out.println(f.getFechaUltimoVencimientoPagado());
        actualizacion = new Actualizacion(credito, remesa,
                Integer.parseInt(f.getMesesVencidos()), Float.parseFloat(f.getSaldoVencido()),
                f.getEstatus(), simpleDateFormat.parse(fecha.convertirFormatoMySQL(f.getFechaUltimoPago())),
                simpleDateFormat.parse(fecha.convertirFormatoMySQL(f.getFechaUltimoVencimientoPagado())), null);
        
        actualizacionDao.insert(session, actualizacion);
        
//        /* Crea los facs asociados a la actualización */
//        for(Fac fac : f.getFacs()) {
//          facDao.insert(session, new dto.Fac(actualizacion, fac.getMes(), fac.getAnio(),
//                  Float.parseFloat(fac.getFacPor()), fac.getFacMes()));
//        }
//
//        if (f.getIdColonia() != 0) {
//          query += "INSERT INTO `sigerbd`.`direccion` (`calle`, `id_sujeto`, `id_municipio`, `id_estado`, `id_colonia`) "
//                  + "VALUES ('" + f.getCalle() + "', '" + sujeto.getIdSujeto() + "', '" + f.getIdMunicipio() + "', '" + f.getIdEstado() + "', '" + f.getIdColonia() + "');\n";
//          guardarQueryEnArchivo(query, archivoSql);
//          query = "";
//        } else {
//          linea = sujeto.getIdSujeto() + ";" + f.getCredito() + ";" + f.getCalle() + ";" + f.getColonia()
//                  + ";" + f.getMunicipio() + ";" + f.getEstado() + ";" + f.getCp() + "\n";
//          guardarQueryEnArchivo(linea, archivoPlano);
//          query = "";
//        }
        throw new Exception("Me voy al carajo!!!");

      }

      /* Sólo si todo fue correcto se hace commit */
      transaction.commit();
    } catch (Exception ex) {
      if (transaction != null) {
        transaction.rollback();
      }

      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
      Logs.log.error(ex.getMessage());
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  /**
   * Coloca el valor "0" en caso de que el número dado sea null o una cadena
   * vacía. Esto evita errores de punteros nulos.
   *
   * @param numero Un String que representa un número
   * @return Un String que el mismo número o u 0 en caso de que el valor de
   * entrada sea null o cadena vacía.
   */
  private static String numerador(String numero) {
    if (numero == null || numero.isEmpty()) {
      return "0";
    } else {
      return numero;
    }
  }

  /**
   * Guarda la sentencia en un archivo sql.
   *
   * @param query Un String que contiene la sentencia sql.
   * @param nombreArchivoSql Un String que contiene el nombre y ruta del archivo
   * en que se guardará la sentencia.
   */
  private static void guardarQueryEnArchivo(String query, String nombreArchivoSql) {
    // SE CREA UN ARCHIVO "VIRTUAL" PARA COMPROBAR SU EXISTENCIA
    File fichero = new File(nombreArchivoSql);
    // VERIFICAMOS SI EL ARCHIVO YA EXISTE
    if (fichero.exists()) {
      // SE ESCRIBE AL FINAL DEL ARCHIVO
      try {
        // SE CREA UN ARCHIVO DEL TIPO FILEWRITER CON LA OPCION TRUE PARA PODER AGREGAR DATOS AL FINAL DEL ARCHIVO Y NO SOBREESCRIBIRLO
        FileWriter fileWriter = new FileWriter(nombreArchivoSql, true);
        // SE ENVIA EL TEXTO PARA AGREGAR
        fileWriter.append("\n" + query);
        // CIERRE DEL ARCHIVO
        fileWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    } else {
      // SE CREA EL ARCHIVO
      try {
        // SE CREA EL ARCHIVO DE TEXTO
        fichero.createNewFile();
        // SE CREAN VARIABLES (ESCRITOR Y BUFFER) PARA PODER ESCRIBIR EN EL NUEVO FICHERO
        FileWriter fileWriter = new FileWriter(fichero);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        // SE ESCRIBE EN EL ARCHIVO
        printWriter.write(query);
        // CIERRE DEL ARCHIVO
        printWriter.close();
        bufferedWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}
