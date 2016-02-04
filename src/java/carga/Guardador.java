//package carga;
//
//import dao.ColoniaDAO;
//import dao.CreditoDAO;
//import dao.DeudorDAO;
//import dao.DireccionDAO;
//import dao.EmailDAO;
//import dao.RemesaDao;
//import dao.SujetoDAO;
//import dao.TelefonoDAO;
//import dto.Credito;
//import dto.Deudor;
//import dto.Email;
//import dto.Fila;
//import dto.Remesa;
//import dto.Sujeto;
//import dto.Telefono;
//import impl.ColoniaIMPL;
//import impl.CreditoIMPL;
//import impl.DeudorIMPL;
//import impl.DireccionIMPL;
//import impl.EmailIMPL;
//import impl.RemesaIMPL;
//import impl.SujetoIMPL;
//import impl.TelefonoIMPL;
//import java.util.List;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import util.HibernateUtil;
//
///**
// *
// * @author brionvega
// */
//public class Guardador {
//  
//  public static void main(String[] args) {
//    Session session = null;
//    Transaction transaction = null;
//    try {
//      nuevaImplementacionDAO.SujetoDAO sujetoDao = new nuevaImplementacionIMPL.SujetoIMPL();
//      
//      session=HibernateUtil.getSessionFactory().openSession();
//      transaction=session.beginTransaction();
//      
//      Sujeto sujeto = sujetoDao.insert(session, new Sujeto("Alejandrina Rios Juarez", "RIJA250814C05", 0));
//      
//      nuevaImplementacionDAO.DeudorDAO deudorDao = new nuevaImplementacionIMPL.DeudorIMPL();
//      deudorDao.insert(session, new Deudor(sujeto, "dd4510009", "RIJA250814MOCSRL05", "1234567890", null, null));
//      
//      transaction.commit();
//    } catch(Exception e) {
//      if(transaction!= null) {
//        transaction.rollback();
//      }
//    } finally {
//      if(session!=null) {
//        session.close();
//      }
//      System.out.println("Termina la operación, revise la BD...");
//    }
//  }
//  
//  public static String guardar(List<Fila> filas) {
//    String query = "";
//    String linea = null;
//    String archivoSql = null;
//
//    Sujeto sujeto;
//    Deudor deudor;
//    Credito credito;
//    Remesa remesa;
//
//    SujetoDAO sujetoDao = new SujetoIMPL();
//    DeudorDAO deudorDao = new DeudorIMPL();
//    CreditoDAO creditoDao = new CreditoIMPL();
//    EmailDAO emailDao = new EmailIMPL();
//    TelefonoDAO telefonoDao = new TelefonoIMPL();
//    DireccionDAO direccionDao = new DireccionIMPL();
//    ColoniaDAO coloniaDao = new ColoniaIMPL();
//    RemesaDao remesaDao = new RemesaIMPL();
//
//    // Comenzamos!!!
//    for (Fila f : filas) {
//      // Antes que nada, creamos el sujeto :)
//      sujeto = sujetoDao.insertar(new Sujeto(f.getNombre(), f.getRfc(), 0));
//
//      // En caso de que la inserción fuere correcta, creamos el objeto Deudor
//      if (sujeto != null) {
//        deudor = deudorDao.insertar(new Deudor(sujeto, f.getIdCliente(), null, null, null, null));
//
//        // En caso de que la inserción del deudor fuere correcta, se agregan sus datos de contacto
//        if (deudor != null) {
//          // e-mails
//          if (!f.getCorreos().isEmpty()) {
//            for (String mail : f.getCorreos()) {
//              emailDao.insertar(new Email(sujeto, mail));
//            }
//          }
//
//          // teléfonos
//          if (!f.getTelsAdicionales().isEmpty()) {
//            for (String tel : f.getTelsAdicionales()) {
//              telefonoDao.insertar(new Telefono(sujeto, tel));
//            }
//          }
//          
//          // A continuación, las remesas.
//          remesa = remesaDao.insertar(new Remesa(Integer.parseInt(f.getMesesVencidos()), Float.parseFloat(f.getSaldoVencido()), f.getEstatus(), new java.util.Date(linea), null, null, null, null));
//
//          // dirección
//          if (f.getIdColonia() != 0) {
//            query += "INSERT INTO `sigerbd`.`direccion` (`calle`, `id_sujeto`, `id_municipio`, `id_estado`, `id_colonia`) "
//                    + "VALUES ('" + f.getCalle() + "', '" + sujeto.getIdSujeto() + "', '" + f.getIdMunicipio() + "', '" + f.getIdEstado() + "', '" + f.getIdColonia() + "');\n";
//          } else {
//            linea = sujeto.getIdSujeto() + ";" + f.getCredito() + ";" + f.getCalle() + ";" + f.getColonia()
//                    + ";" + f.getMunicipio() + ";" + f.getEstado() + ";" + f.getCp() + "\n";
//          }
//
//          // crédito
//          query += "INSERT INTO `sigerbd`.`credito` (`numero_credito`, `fecha_inicio`, `fecha_fin`, `fecha_quebranto`, "
//                  + "`monto`, `mensualidad`, `tasa_interes`, `dias_mora`, `numero_cuenta`, `tipo_credito`, `id_institucion`,"
//                  + " `id_producto`, `id_subproducto`, `id_deudor`, `id_gestor`, `id_despacho`) VALUES ('" + f.getCredito() + "',"
//                  + " '" + fechador(f.getFechaInicioCredito()) + "', '" + fechador(f.getFechaVencimientoCred()) + "', '" + fechador(f.getFechaQuebranto()) + "',"
//                  + " '" + f.getDisposicion() + "', '" + f.getMensualidad() + "', '" + numerador(f.getTasa()) + "', '" + numerador(null) + "', "
//                  + "'" + f.getCuenta() + "', '1', '1', '" + f.getIdProducto() + "', '" + f.getIdSubproducto() + "', '" + deudor.getIdDeudor() + "', "
//                  + "'" + f.getIdGestor() + "', '" + f.getIdDespacho() + "');\n";
//
//        }
//      }
//    }
//
//    return "carga";
//  }
//
//  /**
//   * Prepara una fecha para que se aceptada por mysql. Toma una fecha en formato
//   * dd/mm/aaaa y la regresa en formato aaaa-mm-dd.
//   *
//   * @param fecha Un String que representa la fecha en formato dd/mm/aaaa
//   * @return Un String que representa la fecha en formato aaaa-mm-dd
//   */
//  private static String fechador(String fecha) {
//    // La fecha originalmente viene en formato: dd/mm/aaaa, se regresara como: aaaa-mm-dd
//    if (fecha == null || fecha.isEmpty()) {
//      return "9999-12-31";
//    } else {
//      String[] f = fecha.split("/");
//      return f[2] + "-" + f[1] + "-" + f[0];
//    }
//  }
//
//  /**
//   * Coloca el valor "0" en caso de que el número dado sea null o una cadena
//   * vacía. Esto evita errores de punteros nulos.
//   *
//   * @param numero Un String que representa un número
//   * @return Un String que el mismo número o u 0 en caso de que el valor de
//   * entrada sea null o cadena vacía.
//   */
//  private static String numerador(String numero) {
//    if (numero == null || numero.isEmpty()) {
//      return "0";
//    } else {
//      return numero;
//    }
//  }
//
//}
