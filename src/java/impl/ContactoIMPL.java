package impl;

import dao.ContactoDAO;
import dto.Contacto;
import dto.tablas.Contactos;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Perfiles;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code ContactoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class ContactoIMPL implements ContactoDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public Contacto insertar(Contacto contacto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            sesion.save(contacto);
            tx.commit();
            Logs.log.info("Se insertó un nuevo Contacto: id = " + contacto.getIdContacto()
            + " asociado al Sujeto: id = " + contacto.getSujeto().getIdSujeto() + " que a la vez se asocia con el sujeto: id = " 
            + contacto.getDeudor().getSujeto().getIdSujeto());
        } catch (HibernateException he) {
            contacto = null;
            if (tx != null) {
                tx.rollback();
            }
            Logs.log.error("No se pudo insertar Contacto");
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        
        return contacto;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean editar(Contacto contacto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(contacto);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }

    return ok;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean eliminar(Contacto contacto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
      contacto.getSujeto().setEliminado(Perfiles.ELIMINADO);
      sesion.update(contacto.getSujeto());
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }

    return ok;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public Contacto buscar(int idContacto) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Contacto> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Contacto> buscarPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Contacto> listaContactos;

    String consulta = "select c.* from sujeto s join contacto c on s.id_sujeto = c.id_sujeto "
            + "join deudor l on l.id_deudor = c.id_deudor join sujeto s2 on s2.id_sujeto = l.id_sujeto "
            + "where s2.id_sujeto=" + idSujeto + " and s.eliminado = " + Sujetos.ACTIVO + ";";

    try {
      listaContactos = sesion.createSQLQuery(consulta).addEntity(Contacto.class).list();
    } catch (HibernateException he) {
      listaContactos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return listaContactos;
  }

  @Override
  public List<Contactos> buscarContactoPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Contactos> contactos = new ArrayList<>();
    List<Object[]> c;
    String consulta = "SELECT s.nombre_razon_social, c.observaciones FROM contacto c JOIN sujeto s WHERE s.id_sujeto = c.id_sujeto AND c.id_deudor = (SELECT id_deudor FROM deudor WHERE id_sujeto = " + idSujeto + ");";
    try {
      c = sesion.createSQLQuery(consulta).list();
      for (Object[] row : c) {
        Contactos contacto = new Contactos();
        contacto.setNombreContacto(row[0].toString());
        contacto.setDescripcion(row[1].toString());
        contactos.add(contacto);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      contactos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return contactos;
  }

  /**
   *
   *
   * @param
   */
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
