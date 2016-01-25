package carga;

import dto.Credito;
import dto.CreditoRemesa;
import dto.CreditoRemesaId;
import dto.Despacho;
import dto.Deudor;
import dto.Fila;
import dto.Gestor;
import dto.Institucion;
import dto.Producto;
import dto.Remesa;
import dto.Subproducto;
import dto.Sujeto;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import nuevaImplementacionDAO.CreditoDAO;
import nuevaImplementacionDAO.CreditoRemesaDAO;
import nuevaImplementacionDAO.DespachoDAO;
import nuevaImplementacionDAO.DeudorDAO;
import nuevaImplementacionDAO.FacDAO;
import nuevaImplementacionDAO.GestorDAO;
import nuevaImplementacionDAO.InstitucionDAO;
import nuevaImplementacionDAO.ProductoDAO;
import nuevaImplementacionDAO.RemesaDAO;
import nuevaImplementacionDAO.SubproductoDAO;
import nuevaImplementacionDAO.SujetoDAO;
import nuevaImplementacionIMPL.CreditoIMPL;
import nuevaImplementacionIMPL.CreditoRemesaIMPL;
import nuevaImplementacionIMPL.DespachoIMPL;
import nuevaImplementacionIMPL.DeudorIMPL;
import nuevaImplementacionIMPL.FacIMPL;
import nuevaImplementacionIMPL.GestorIMPL;
import nuevaImplementacionIMPL.InstitucionIMPL;
import nuevaImplementacionIMPL.ProductoIMPL;
import nuevaImplementacionIMPL.RemesaIMPL;
import nuevaImplementacionIMPL.SubproductoIMPL;
import nuevaImplementacionIMPL.SujetoIMPL;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Carajeador {

  private static Session session;
  private static Transaction transaction;

  public static void carajear(int idDespacho, List<Fila> filas) {
    try {
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

      InstitucionDAO institucionDao = new InstitucionIMPL();
      Institucion institucion;

      CreditoDAO creditoDao = new CreditoIMPL();
      Credito credito;
      
      CreditoRemesaDAO creditoRemesaDao = new CreditoRemesaIMPL();
      
      FacDAO facDao = new FacIMPL();

      RemesaDAO remesaDao = new RemesaIMPL();
      Remesa remesa;

      session = HibernateUtil.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      Despacho despacho = despachoDao.getById(session, idDespacho);

      for (Fila f : filas) {
        sujeto = sujetoDao.insert(session, new Sujeto(f.getNombre(), f.getRfc(), 0));
        deudor = deudorDao.insert(session, new Deudor(sujeto, f.getIdCliente(), null, null, null, null));

        gestor = gestorDao.getById(session, f.getIdGestor());
//        subproducto = subproductoDao.getById(session, f.getIdSubproducto());
//        producto = productoDao.getById(session, f.getIdProducto());
//        institucion = institucionDao.getById(session, producto.getInstitucion().getIdInstitucion());
        
//        credito = new Credito(despacho, deudor, gestor, f.getProductoDTO(), f.getSubproductoDTO(),
//                        f.getCredito(), null, null, null, Float.parseFloat(f.getDisposicion()),
//                        Float.parseFloat(f.getMensualidad()), Float.parseFloat(numerador(f.getTasa())), 0,
//                        f.getCuenta(), 1, f.getProductoDTO().getInstitucion().getIdInstitucion(),
//                        null, null, null, null, null, null, null);
//        
//        remesa = new Remesa(Integer.parseInt(f.getMesesVencidos()),
//                Float.parseFloat(f.getSaldoVencido()), f.getEstatus(),
//                null, null, null, null, null); 

//        creditoDao.insert(session, credito);
//        
//        remesaDao.insert(session, remesa);
        
//        creditoRemesaDao.insert(session,
//                new CreditoRemesa(new CreditoRemesaId(remesa.getIdRemesa(), credito.getIdCredito()), credito, remesa));
//        
//        for(Fac fac : f.getFacs()) {
//          facDao.insert(session, new dto.Fac(remesa, fac.getMes(), fac.getAnio(),
//                  Float.parseFloat(numerador(fac.getFacPor())), fac.getFacMes()));
//        }

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
}
