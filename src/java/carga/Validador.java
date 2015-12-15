package carga;

import beans.FilaBean;
import dto.Fila;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Validador {
   public static void validarFila(Fila fila){
    FilaBean filaBean = new FilaBean(fila);
    
    try {
      // Validaciones simples:
      filaBean.validarNumCred();
      filaBean.validarNombreRazonSoc();
      filaBean.validarRefCobro();
      filaBean.validarEstatus();
      filaBean.validarMesesVencidos();
      filaBean.validarFechaInicio();
      filaBean.validarFechaFin();
      filaBean.validarMonto();
      filaBean.validarMensualidad();
      filaBean.validarSaldoIns();
      filaBean.validarSaldoVen();
      filaBean.validarTasaInt();
      filaBean.validarNumCta();
      filaBean.validarFechaUltP();
      filaBean.validarRfc();

      filaBean.validarMarcaje();
//      filaBean.validarFacs();
      filaBean.validarEmail();
      
      // Validaciones complejas
      filaBean.validarDespacho();
      filaBean.validarIdProducto();
      filaBean.validarIdSubproducto();
      filaBean.validarDireccion();
      
      fila.setError("NO");
    } catch (Exception ex) {
      //Logger.getLogger(Validador.class.getName()).log(Level.SEVERE, null, ex);
      fila.setError("Ha ocurrido un error al validar el crédito " + fila.getCredito());
      fila.setDetalleError(ex.getMessage());
      Logs.log.error(fila.getError() + ": " + fila.getDetalleError());

    }

  }
}
