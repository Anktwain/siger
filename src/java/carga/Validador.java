package carga;

import beans.FilaBean;
import dto.carga.Fila;
import dto.Producto;
import dto.Subproducto;
import java.util.List;
import nuevaImplementacionDAO.ProductoDAO;
import nuevaImplementacionDAO.SubproductoDAO;
import nuevaImplementacionIMPL.ProductoIMPL;
import nuevaImplementacionIMPL.SubproductoIMPL;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Validador {

  /**
   * Método que valida una conjunto de filas dado.
   *
   * @param filas Conjunto de filas que va a ser validado.
   * @return true si todas la filas fueron válidas, false en caso contrario.
   */
  public static Fila validarFilas(List<Fila> filas) {
    Session session = null;
    Transaction transaction = null;

    ProductoDAO productoDao = new ProductoIMPL();
    SubproductoDAO subproductoDao = new SubproductoIMPL();
    
    List<Producto> listaProductos = null;
    List<Subproducto> listaSubproductos = null;

    FilaBean filaBean = new FilaBean();

    /* Abre la BD y consulta los objetos guardados */
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      listaProductos = productoDao.getAll(session);
      listaSubproductos = subproductoDao.getAll(session);

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

    /* Valida las filas */
    for (Fila fila : filas) {
      filaBean.setFilaActual(fila);
      try {
        /* Validaciones simples: una validación simple únicamente revisa un elemento
         a la vez. */
        filaBean.validarNumCred(); /* Valida número de crédito. */

        filaBean.validarNombreRazonSoc(); /* Valida nombre o razón social. */

        filaBean.validarRefCobro(); /* Valida referencia de cobro. */

        filaBean.validarEstatus(); /* Valida estatus. */

        filaBean.validarMesesVencidos(); /* Valida meses vencidos. */

        filaBean.validarFechaInicio(); /* Valida fecha de inicio del crédito. */

        filaBean.validarFechaFin(); /* Valida fecha de vencimiento del crédito. */

        filaBean.validarMonto(); /* Valida monto o disposición. */

        filaBean.validarMensualidad(); /* Valida mensualidad. */

        filaBean.validarSaldoIns(); /* Valida saldo (insoluto?). */

        filaBean.validarSaldoVen(); /* Valida saldo vencido. */

        filaBean.validarTasaInt(); /* Valida tasa de interés. */

        filaBean.validarNumCta(); /* Valida número de cuenta. */

        filaBean.validarFechaUltP(); /* Valida fecha de último pago. */

        filaBean.validarRfc(); /* Valida RFC del deudor. */
        /* Valida marcaje. El marcaje es un dato de control interno, por lo tanto no
         aparece en el archivo de remesa. Si no es especifica ningún marcaje, entonces
         se supone que los créditos vienen "sin marcaje". */

        filaBean.validarMarcaje();
        filaBean.validarEmail(); /* Valida dirección de correo electrónico. */

        /* Validaciones complejas: una validación compleja es aquella que involucra
         más de un campo, o que requiere acceso a la base de datos para poder llevarse
         a cabo. */

        filaBean.validarIdProducto(listaProductos); /* Valida producto. */

        filaBean.validarIdSubproducto(listaSubproductos); /* Valida subproducto. */
        /* Valida la dirección de crédito. Esta validación incluye el estado, municipio,
         colonia y CP. */

        filaBean.validarDireccion();

        /* Si la fila pasó todas las validaciones, entonces se dice que la fila es
         "válida" y se coloca la cadena "NO" en el campo "Error" del objeto Fila. */
        fila.setError("NO");

      } catch (Exception ex) {
        /* Envía mensaje de error al usuario, además registra este error en el log. */
        fila.setError("Ha ocurrido un error al validar el crédito " + fila.getCredito());
        fila.setDetalleError(ex.getMessage());
        Logs.log.error(fila.getError() + ": " + fila.getDetalleError());
        return fila;
      }
    }

    return null;
  }

  /**
   * Método que valida una fila dada.
   *
   * @param fila Objeto Fila que corresponde a la fila que va a ser validada.
   */
  public static void validarFila(Fila fila) {
    FilaBean filaBean = new FilaBean(fila);

    try {
      /* Validaciones simples: una validación simple únicamente revisa un elemento
       a la vez. */
      filaBean.validarNumCred(); /* Valida número de crédito. */

      filaBean.validarNombreRazonSoc(); /* Valida nombre o razón social. */

      filaBean.validarRefCobro(); /* Valida referencia de cobro. */

      filaBean.validarEstatus(); /* Valida estatus. */

      filaBean.validarMesesVencidos(); /* Valida meses vencidos. */

      filaBean.validarFechaInicio(); /* Valida fecha de inicio del crédito. */

      filaBean.validarFechaFin(); /* Valida fecha de vencimiento del crédito. */

      filaBean.validarMonto(); /* Valida monto o disposición. */

      filaBean.validarMensualidad(); /* Valida mensualidad. */

      filaBean.validarSaldoIns(); /* Valida saldo (insoluto?). */

      filaBean.validarSaldoVen(); /* Valida saldo vencido. */

      filaBean.validarTasaInt(); /* Valida tasa de interés. */

      filaBean.validarNumCta(); /* Valida número de cuenta. */

      filaBean.validarFechaUltP(); /* Valida fecha de último pago. */

      filaBean.validarRfc(); /* Valida RFC del deudor. */

      /* Valida marcaje. El marcaje es un dato de control interno, por lo tanto no
       aparece en el archivo de remesa. Si no es especifica ningún marcaje, entonces
       se supone que los créditos vienen "sin marcaje". */
      filaBean.validarMarcaje();
//      filaBean.validarFacs();
      filaBean.validarEmail(); /* Valida dirección de correo electrónico. */

      /* Validaciones complejas: una validación compleja es aquella que involucra
       más de un campo, o que requiere acceso a la base de datos para poder llevarse
       a cabo. */
      filaBean.validarDespacho(); /* Valida despacho. */

      filaBean.validarIdProducto(); /* Valida producto. */

      filaBean.validarIdSubproducto(); /* Valida subproducto. */

      /* Valida la dirección de crédito. Esta validación incluye el estado, municipio,
       colonia y CP. */
      filaBean.validarDireccion();

      /* Si la fila pasó todas las validaciones, entonces se dice que la fila es
       "válida" y se coloca la cadena "NO" en el campo "Error" del objeto Fila. */
      fila.setError("NO");
    } catch (Exception ex) {
      /* Envía mensaje de error al usuario, además registra este error en el log. */
      fila.setError("Ha ocurrido un error al validar el crédito " + fila.getCredito());
      fila.setDetalleError(ex.getMessage());
      Logs.log.error(fila.getError() + ": " + fila.getDetalleError());
    }

  }
}
