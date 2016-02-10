/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.TipoQuienGestionDAO;
import dto.TipoQuienGestion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class TipoQuienGestionIMPL implements TipoQuienGestionDAO {

  @Override
  public List<TipoQuienGestion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<TipoQuienGestion> tipos;
    try {
      tipos = sesion.createSQLQuery("SELECT * FROM tipo_quien_gestion;").addEntity(TipoQuienGestion.class).list();
    } catch (HibernateException he) {
      tipos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipos;
  }

  @Override
  public TipoQuienGestion buscarPorId(int idTipoQuienGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    TipoQuienGestion tipo;
    try {
      tipo = (TipoQuienGestion) sesion.createSQLQuery("SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = " + idTipoQuienGestion + ";").addEntity(TipoQuienGestion.class).uniqueResult();
    } catch (HibernateException he) {
      tipo = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipo;
  }

  @Override
  public List<TipoQuienGestion> buscarPorAsuntoDescripcion(int claveAsunto, String abreviatura) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<TipoQuienGestion> tipos;
    String consulta = "";
    switch (claveAsunto) {
      // CASO NO APLICA
      case 1:
      case 15:
      case 25:
        consulta = noAplica();
        break;
      // CASO PERSONAS(TODOS)
      case 2:
      case 6:
      case 7:
      case 8:
      case 11:
      case 12:
      case 13:
      case 17:
        consulta = personasTodos();
        break;
      // CASO PERSONAS(NO TITULAR)
      case 3:
      case 4:
      case 5:
        consulta = personasNoTitular();
        break;
      // POSITIVOS
      case 9:
        if (abreviatura.equals("1POSI")) {
          consulta = localizaTelefonos();
        }
        else if (abreviatura.equals("2POSI")) {
          consulta = localizaDomicilio();
        } else {
          consulta = noExiste();
        }
        break;
      // NEGATIVOS
      case 10:
        if ((abreviatura.equals("1NEGA")) || (abreviatura.equals("4NEGA"))) {
          consulta = noAplica();
        }
        else if (abreviatura.equals("2NEGA")) {
          consulta = personasTodos();
        }
        else if (abreviatura.equals("3NEGA")) {
          consulta = domicilioEstado();
        } else {
          consulta = noExiste();
        }
        break;
      // DIVERSO IBR
      case 14:
        if (abreviatura.equals("2DIIB")) {
          consulta = personasTodos();
        } else {
          consulta = noAplica();
        }
        break;
      // CORREO ELECTRONICO
      case 16:
        if (abreviatura.equals("1COEL")) {
          consulta = personasTodos();
        } else {
          consulta = noAplica();
        }
        break;
      // LOCALIZACION
      case 18:
        if (abreviatura.equals("SISB")) {
          consulta = localizaIntegra();
        } else {
          consulta = noAplica();
        }
        break;
    }
    try {
      tipos = sesion.createSQLQuery(consulta).addEntity(TipoQuienGestion.class).list();
    } catch (HibernateException he) {
      tipos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipos;
  }

  // CASOS
  // CASO NO APLICA
  public String noAplica() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 12;";
  }
  // CASO NO EXISTE
  public String noExiste() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 0;";
  }
  // CASO PERSONAS(TODOS)
  public String personasTodos() {
    return "SELECT * FROM tipo_quien_gestion WHERE tipo = 'PERSONAS';";
  }
  // CASO PERSONAS(NO TITULAR)
  public String personasNoTitular() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion != 1 AND tipo = 'PERSONAS';";
  }
  // CASO DOMICILIO (ESTADO)
  public String domicilioEstado() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 8;";
  }
  // CASO LOCALIZA TELEFONOS
  public String localizaTelefonos() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 9;";
  }
  // CASO LOCALIZA DOMICILIO
  public String localizaDomicilio() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 10;";
  }
  // CASO LOCALIZA INTEGRA
  public String localizaIntegra() {
    return "SELECT * FROM tipo_quien_gestion WHERE id_tipo_quien_gestion = 11;";
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
