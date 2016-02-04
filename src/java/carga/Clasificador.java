package carga;

import dto.Credito;
import dto.Deudor;
import dto.Fila;
import dto.Remesa;

import java.util.List;
import nuevaImplementacionDAO.CreditoDAO;
import nuevaImplementacionDAO.DeudorDAO;
import nuevaImplementacionDAO.RemesaDAO;
import nuevaImplementacionIMPL.CreditoIMPL;
import nuevaImplementacionIMPL.DeudorIMPL;
import nuevaImplementacionIMPL.RemesaIMPL;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
public class Clasificador {
  /* Permiten la clasificación de los créditos de acuerdo a su situación:
   En la fiesta: Créditos gestionados durante el periodo anterior, por lo tanto se encuentran en la BD.
   Estaba en la fiesta: Créditos que se encuentran en la BD, pero no se gestionaron en el periodo anterior.
   Nuevo crédito: Es un nuevo crédito para un deudor que ya se encuentra en la BD porque tiene otros créditos.
   Nuevo total: Tanto el crédito como el deudor no se encuentran en la BD. */

  public static final int EN_LA_FIESTA = 1;
  public static final int ESTABA_EN_LA_FIESTA = 2;
  public static final int NUEVO_CREDITO = 3;
  public static final int NUEVO_TOTAL = 4;

  //private CreditoDAO creditoDao;
  public Clasificador() {
    //creditoDao = new CreditoIMPL();
  }

  /**
   * Método que clasifica los créditos contenidos en una lista de objetos Fila.
   *
   * @param filas La lista de objetos Fila cuyos elementos van a ser
   * clasificados.
   */
  public static void clasificar(List<Fila> filas) {
    Session session = null;
    Transaction transaction = null;

    /* Lista de Créditos y deudores existentes en la BD, respectivamente */
    List<String> creditosEnBD = null;
    List<String> deudoresEnBD = null;

    /* Objetos credito y deudor. El crédito y el deudor representados por un objeto
     Fila en un momento determinado. */
    Credito credito;
    Deudor deudor;
    Remesa remesaActual;

    /* Objetos de acceso a la BD. */
    CreditoDAO creditoDao = new CreditoIMPL();
    DeudorDAO deudorDao = new DeudorIMPL();
    RemesaDAO remesaDao = new RemesaIMPL();

    /* Acceso a la BD para obtener lista de numeros de credito e ID de deudores */
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      creditosEnBD = creditoDao.getAllNoCredito(session);
      deudoresEnBD = deudorDao.getAllNoDeudor(session);

      remesaActual = remesaDao.getUltimaRemesa(session);

      transaction.commit();
    } catch (Exception ex) {
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      if (session.isOpen()) {
        session.close();
      }
    }

    int remesaActualCredito;
    String numeroDeCredito;

    /* Recorre todos los elementos Fila contenidos en "filas" */
    try {
      for (Fila f : filas) {
        if (creditosEnBD.contains(f.getCredito())) {
          f.setClasificacion(ESTABA_EN_LA_FIESTA);
          // CONTINUAR AQUÍ.........................
        } else {
          if (deudoresEnBD.contains(f.getIdCliente())) {
            f.setClasificacion(NUEVO_CREDITO);
          } else {
            f.setClasificacion(NUEVO_TOTAL);
          }
        }
      }
    } catch (NullPointerException npe) {
      System.out.println("Nullpointer...");
    }

//      
//      /* En primer lugar, toma el número de crédito de la fila actual y lo busca
//       en la Base de Datos. */
//      credito = creditoDao.buscar(f.getCredito());
//
//      /* Si encuentra ese número de crédito en la Base de Datos, entonces la
//       clasificación puede caer en cualquiera de estos dos casos: "En la fiesta"
//       o "Estaba en la fiesta". */
//      if (credito != null) {
//        remesaActualCredito = creditoRemesaDao.buscarRemesaActual(credito.getNumeroCredito());
//        if (remesaActualCredito == remesaActual) {
//          f.setClasificacion(EN_LA_FIESTA);
//        } else {
//          f.setClasificacion(ESTABA_EN_LA_FIESTA);
//        }
//      } else {
//        /* Si no encuentra ese número de crédito en la Base de Datos, entonces la
//         clasificación puede caer en cualquiera de estos dos casos: "Nuevo crédito"
//         o "Nuevo total". */
//
//        deudor = deudorDao.buscar(f.getIdCliente()); /* Ahora busca el deudor en la Base de Datos */
//        /* Si el deudor sí se encuentra en la base de datos, entonces se trata de un
//         nuevo crédito. */
//
//        if (deudor != null) {
//          f.setClasificacion(NUEVO_CREDITO); // No encuentra crédito ni cliente
//        } /* Si el deudor no se encuentra en la base de datos, entonces se trata de un
//         nuevo total. */ else {
//          f.setClasificacion(NUEVO_TOTAL); // No encuentra crédito, sí cliente
//        }
//      }
    //}
  }

}
