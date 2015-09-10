package beans;

import dto.Fila;
import java.io.Serializable;
import util.constantes.Constantes;
import util.constantes.Errores;

/**
 *
 * @author Pablo
 */
public class FilaBean implements Serializable {

  private Fila filaActual;

  public FilaBean() {

  }

  public Fila getFilaActual() {
    return filaActual;
  }

  public void setFilaActual(Fila filaActual) {
    this.filaActual = filaActual;
  }

  /**
   * El número de crédito es obligatorio.
   */
  public void validaNumCred() throws Exception {
    if (!this.filaActual.getCredito().isEmpty()) {
      if (this.filaActual.getCredito().length() > Constantes.limCREDITO_numero_credito) {
        throw new Exception("Error en el campo \"numero_credito\". "
                + Errores.campoDesbordado + " (máx: " + Constantes.limCREDITO_numero_credito + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"numero_credito\". " + Errores.campoVacio);
    }
  }

  /**
   * El nombre del deudor es obligatorio.
   */
  public void validaNombreRazonSoc() throws Exception {
    if (!this.filaActual.getNombre().isEmpty()) {
      if (this.filaActual.getNombre().length() > Constantes.limSUJETO_nombre_razon_social) {
        throw new Exception("Error en el campo \"nombre_razon_social\". "
                + Errores.campoDesbordado + " (máx: " + Constantes.limSUJETO_nombre_razon_social + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"nombre_razon_social\". " + Errores.campoVacio);
    }
  }

  /**
   * La refCobro (referencia de cobro) es obligatoria.
   */
  public void validaRefCobro() throws Exception {
    if (!this.filaActual.getRefCobro().isEmpty()) {
      if (this.filaActual.getRefCobro().length() > Constantes.limLINEA_telefono) {
        throw new Exception("Error en el campo \"linea_telefono\". "
                + Errores.campoDesbordado + " (máx: " + Constantes.limLINEA_telefono + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"linea_telefono\". " + Errores.campoVacio);
    }
  }

  /**
   * La linea(producto) es obligatoria, deberá comprobarse que existe el
   * producto en la tabla 'producto'
   */
  public void validaIdProducto() throws Exception {
    if (!this.filaActual.getLinea().isEmpty()) {
      try {
        Integer.parseInt(this.filaActual.getLinea());
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"productos_id_producto\". " + Errores.campoEnteroRequerido);
      }
    } else {
      throw new Exception("Error en el campo \"productos_id_producto\". " + Errores.campoVacio);
    }
  }

  /**
   * El tipoCredito(subproducto) deberá existir en la tabla 'subproducto', es un
   * id entero, sin embargo este campo no es obligatorio.
   */
  public void validaIdSubproducto() throws Exception {
    if (!this.filaActual.getTipoCredito().isEmpty()) {
      try {
        Integer.parseInt(this.filaActual.getTipoCredito());
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"subproductos_id_subproducto\". " + Errores.campoEnteroRequerido);
      }
    }
  }

  /**
   * El estatus no es obligatorio. Es un VARCHAR(10) // remesa.estatus
   */
  public void validaEstatus() throws Exception {
    if (!this.filaActual.getEstatus().isEmpty()) {
      if (this.filaActual.getEstatus().length() > Constantes.limREMESA_estatus) {
        throw new Exception("Error en el campo \"estatus\". "
                + Errores.campoDesbordado + " (máx: " + Constantes.limREMESA_estatus + ".)");
      }
    }
  }
  
  

}
