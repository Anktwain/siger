package carga;

import beans.FilaBean;
import dto.Fila;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Validador {

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
