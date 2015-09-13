package beans;

import dto.Fila;
import java.io.Serializable;
import util.constantes.Constantes;
import util.constantes.Errores;
import util.constantes.Patrones;

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
  public void validarNumCred() throws Exception {
    if (!this.filaActual.getCredito().isEmpty()) {
      if (this.filaActual.getCredito().length() > Constantes.limCREDITO_numero_credito) {
        throw new Exception("Error en el campo \"numero_credito\". "
                + Errores.CAMPO_DESBORDADO + " (máx: " + Constantes.limCREDITO_numero_credito + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"numero_credito\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El nombre del deudor es obligatorio.
   */
  public void validarNombreRazonSoc() throws Exception {
    if (!this.filaActual.getNombre().isEmpty()) {
      if (this.filaActual.getNombre().length() > Constantes.limSUJETO_nombre_razon_social) {
        throw new Exception("Error en el campo \"nombre_razon_social\". "
                + Errores.CAMPO_DESBORDADO + " (máx: " + Constantes.limSUJETO_nombre_razon_social + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"nombre_razon_social\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * La refCobro (referencia de cobro) es obligatoria.
   */
  public void validarRefCobro() throws Exception {
    if (!this.filaActual.getRefCobro().isEmpty()) {
      if (this.filaActual.getRefCobro().length() > Constantes.limLINEA_telefono) {
        throw new Exception("Error en el campo \"linea_telefono\". "
                + Errores.CAMPO_DESBORDADO + " (máx: " + Constantes.limLINEA_telefono + ".)");
      }
    } else {
      throw new Exception("Error en el campo \"linea_telefono\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * La linea(producto) es obligatoria, deberá comprobarse que existe el
   * producto en la tabla 'producto'
   */
  public void validarIdProducto() throws Exception {
    if (!this.filaActual.getLinea().isEmpty()) {
      try {
        Integer.parseInt(this.filaActual.getLinea());
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"productos_id_producto\". " + Errores.CAMPO_ENTERO_REQUERIDO);
      }
    } else {
      throw new Exception("Error en el campo \"productos_id_producto\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El tipoCredito(subproducto) deberá existir en la tabla 'subproducto', es un
   * id entero, sin embargo este campo no es obligatorio.
   */
  public void validarIdSubproducto() throws Exception {
    if (!this.filaActual.getTipoCredito().isEmpty()) {
      try {
        Integer.parseInt(this.filaActual.getTipoCredito());
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"subproductos_id_subproducto\". " + Errores.CAMPO_ENTERO_REQUERIDO);
      }
    }
  }

  /**
   * El estatus no es obligatorio.
   */
  public void validarEstatus() throws Exception {
    if (!this.filaActual.getEstatus().isEmpty()) {
      if (this.filaActual.getEstatus().length() > Constantes.limREMESA_estatus) {
        throw new Exception("Error en el campo \"estatus\". "
                + Errores.CAMPO_DESBORDADO + " (máx: " + Constantes.limREMESA_estatus + ".)");
      }
    }
  }

  /**
   * Los meses vencidos deben ser números enteros, sin embargo este campo no es
   * obligatorio.
   */
  public void validarMesesVencidos() throws Exception {
    if (!this.filaActual.getMesesVencidos().isEmpty()) {
      try {
        Integer.parseInt(this.filaActual.getMesesVencidos());
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"meses_vencidos\". " + Errores.CAMPO_ENTERO_REQUERIDO);
      }
    }
  }

  /**
   * Validador genérico de fechas. Es llamado por todos los validadores
   * particulares de fechas. Valida que la fecha cumpla todos los siguiente
   * criterios:
   *
   *
   * Año con base en las
   * <a href="https://support.office.com/es-mx/article/Especificaciones-y-l%C3%ADmites-de-Excel-16c69c74-3d6a-4aaf-ba35-e6eb276e8eaa">
   * Especificaciones y límites de Excel</a>
   */
  public void validarFecha(String fecha, String campo) throws Exception {
    if (!fecha.isEmpty()) {
      if (fecha.matches(Patrones.PATRON_FECHA_DDMMAAAA)) {
        if ((Integer.parseInt(fecha.substring(0, 2)) > 31)
                || (Integer.parseInt(fecha.substring(3, 5)) > 12)
                || (Integer.parseInt(fecha.substring(6, 10)) < 1900)
                || (Integer.parseInt(fecha.substring(6, 10)) > 9999)) {
          throw new Exception("Error en el campo \"" + campo + "\". " + Errores.CAMPO_FECHA_INVALIDO);
        }
      } else {
        throw new Exception("Error en el campo \"" + campo + "\". " + Errores.CAMPO_DDMMAAA_REQUERIDO);
      }
    }
  }

  /**
   * La fechaInicioCredito no es obligatoria, la fecha debe ser correcta
   */
  public void validarFechaInicio() throws Exception {
    if (!this.filaActual.getFechaInicioCredito().isEmpty()) {
      validarFecha(this.filaActual.getFechaInicioCredito(), "fecha_inicio");
    }
  }

  /**
   * La fechaVencimientoCredito no es obligatoria, la fecha debe ser correcta
   */
  public void validarFechaFin() throws Exception {
    if (!this.filaActual.getFechaVencimientoCred().isEmpty()) {
      validarFecha(this.filaActual.getFechaVencimientoCred(), "fecha_fin");
    }
  }

  /**
   * Conversor de genérico de fechas. Es llamado por todos los conversores
   * particulares de fechas. Convierte una cadena que contiene una fecha de la
   * forma dd/mm/aaaa en una una cadena con una fecha en la forma aaaa-mm-dd
   *
   * @param fecha
   * @return
   */
  public String convierteFechaToSql(String fecha, String campo) {
    StringBuilder fechaSQL = new StringBuilder();
    fechaSQL.append(fecha.substring(6, 10));
    fechaSQL.append("-");
    fechaSQL.append(fecha.substring(3, 5));
    fechaSQL.append("-");
    fechaSQL.append(fecha.substring(0, 2));
    return fechaSQL.toString();
  }

  /**
   * La fechaInicioCredito no es obligatoria, la fecha debe ser correcta
   *
   * @return una cadena en la forma aaaa-mm-dd
   */
  public String convierteFechaInicio() {
    return convierteFechaToSql(this.filaActual.getFechaInicioCredito(), "fecha_inicio");
  }

  /**
   * La fechaVencimientoCredito no es obligatoria, la fecha debe ser correcta //
   * credito.fecha_fin
   *
   * @return una cadena en la forma aaaa-mm-dd
   */
  public String convierteFechaVencimiento() {
    return convierteFechaToSql(this.filaActual.getFechaVencimientoCred(), "fecha_fin");
  }
  

}
