package beans;

import dto.Fila;
import java.io.Serializable;
import java.util.Arrays;
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
   *
   */
  private void validarVarchar(String valorCampo, String nombreCampo, int limCaracteres) throws Exception {
    if (valorCampo.length() > limCaracteres) {
      throw new Exception("Error en el campo \"" + nombreCampo + "\"."
              + Errores.CAMPO_DESBORDADO + " (máx: " + limCaracteres + ".)");
    }

  }

  /**
   * El número de crédito es obligatorio.
   */
  public void validarNumCred() throws Exception {
    if (!this.filaActual.getCredito().isEmpty()) {
      validarVarchar(this.filaActual.getCredito(), "numero_credito", Constantes.LIM_CREDITO_numero_credito);
    } else {
      throw new Exception("Error en el campo \"numero_credito\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El nombre del deudor es obligatorio.
   */
  public void validarNombreRazonSoc() throws Exception {
    if (!this.filaActual.getNombre().isEmpty()) {
      validarVarchar(this.filaActual.getNombre(), "nombre_razon_social", Constantes.LIM_SUJETO_nombre_razon_social);
    } else {
      throw new Exception("Error en el campo \"nombre_razon_social\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * La refCobro (referencia de cobro) es obligatoria.
   */
  public void validarRefCobro() throws Exception {
    if (!this.filaActual.getRefCobro().isEmpty()) {
      validarVarchar(this.filaActual.getRefCobro(), "linea_telefono", Constantes.LIM_LINEA_telefono);
    } else {
      throw new Exception("Error en el campo \"linea_telefono\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   *
   */
  private void validarInt(String valorCampo, String nombreCampo) throws Exception {
    try {
      Integer.parseInt(valorCampo);
    } catch (NumberFormatException nfe) {
      throw new Exception("Error en el campo \"" + nombreCampo + "\". " + Errores.CAMPO_ENTERO_REQUERIDO, nfe);
    }
  }

  /**
   * La linea(producto) es obligatoria, deberá comprobarse que existe el
   * producto en la tabla 'producto'
   */
  public void validarIdProducto() throws Exception {
    if (!this.filaActual.getLinea().isEmpty()) {
      validarInt(this.filaActual.getLinea(), "productos_id_producto");
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
      validarInt(this.filaActual.getTipoCredito(), "subproductos_id_subproducto");
    }
  }

  /**
   * El estatus no es obligatorio.
   */
  public void validarEstatus() throws Exception {
    if (!this.filaActual.getEstatus().isEmpty()) {
      validarVarchar(this.filaActual.getEstatus(), "estatus", Constantes.LIM_REMESA_estatus);
    }
  }

  /**
   * Los meses vencidos deben ser números enteros, sin embargo este campo no es
   * obligatorio.
   */
  public void validarMesesVencidos() throws Exception {
    if (!this.filaActual.getMesesVencidos().isEmpty()) {
      validarInt(this.filaActual.getMesesVencidos(), "meses_vencidos");
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
  private void validarFecha(String valorCampo, String nombreCampo) throws Exception {
    if (!valorCampo.isEmpty()) {
      if (valorCampo.matches(Patrones.PATRON_FECHA_DDMMAAAA)) {
        if ((Integer.parseInt(valorCampo.substring(0, 2)) > 31)
                || (Integer.parseInt(valorCampo.substring(3, 5)) > 12)
                || (Integer.parseInt(valorCampo.substring(6, 10)) < Constantes.LIM_INF_ANIO_EXCEL)
                || (Integer.parseInt(valorCampo.substring(6, 10)) > Constantes.LIM_SUP_ANIO_EXCEL)) {
          throw new Exception("Error en el campo \"" + nombreCampo + "\". " + Errores.CAMPO_FECHA_INVALIDO);
        }
      } else {
        throw new Exception("Error en el campo \"" + nombreCampo + "\". " + Errores.CAMPO_DDMMAAA_REQUERIDO);
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
  private String convierteFechaToSql(String fecha, String campo) {
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

  /**
   *
   */
  private void validarFloat(String valorCampo, String nombreCampo) throws Exception {
    try {
      Float.valueOf(valorCampo);
    } catch (NumberFormatException nfe) {
      throw new Exception("Error en el campo \"" + nombreCampo + "\"." + Errores.CAMPO_FLOAT_REQUERIDO, nfe);
    }

  }

  /**
   * El monto o disposición no es obligado, debe ser un float
   */
  public void validarMonto() throws Exception {
    if (!this.filaActual.getDisposicion().isEmpty()) {
      validarFloat(this.filaActual.getDisposicion(), "monto");
    }
  }

  /**
   * La mensualidad no es obligatoria, debe ser un float
   */
  public void validarMensualidad() throws Exception {
    if (!this.filaActual.getMensualidad().isEmpty()) {
      validarFloat(this.filaActual.getMensualidad(), "mensualidad");
    }
  }

  /**
   * El saldo ins (saldo insoluto) deberá guardarse en la tabla remesa, sin
   * embargo todavía no existe un campo para hacerlo. Se creará ese campo
   */
  public void validarSaldoIns() {

  }

  /**
   * El SaldoVen (saldo vencido) no es obligatorio. Debe ser un float
   */
  public void validarSaldoVen() throws Exception {
    if (!this.filaActual.getSaldoVencido().isEmpty()) {
      validarFloat(this.filaActual.getSaldoVencido(), "saldo_vencido");
    }
  }

  /**
   * La TASA (tasa de interes) no es obligatoria. Debe ser un float
   */
  public void validarTasaInt() throws Exception {
    if (!this.filaActual.getTasa().isEmpty()) {
      validarFloat(this.filaActual.getTasa(), "tasa_interes");
    }
  }

  /**
   * La cuenta no es obligatoria. Es un VARCHAR(30)
   */
  public void validarNumCta() throws Exception {
    if (!this.filaActual.getCuenta().isEmpty()) {
      validarVarchar(this.filaActual.getCuenta(), "numero_cuenta", Constantes.LIM_CREDITO_numero_cuenta);
    }
  }

  /**
   * La Fec_FUP (fecha de ultimo pago) no es obligatorio.
   */
  public void validarFechaUltP() throws Exception {
    if (!this.filaActual.getFechaUltimoPago().isEmpty()) {
      validarFecha(this.filaActual.getFechaUltimoPago(), "fecha_ultimo_pago");
    }
  }

  /**
   * El Fec_UVP (fecha de ultimo vencimiento pagado)
   */
  public void validarFechaUltVP() throws Exception {
    if (!this.filaActual.getFechaUltimoVencimientoPagado().isEmpty()) {
      validarFecha(this.filaActual.getFechaUltimoVencimientoPagado(), "fecha_ultimo_vencimiento_pagado");
    }
  }

  /**
   * El NumCte (número de cliente) no es obligatorio. cliente.numero_cliente
   */
  public void validarNumCliente() throws Exception {
    if (!this.filaActual.getIdCliente().isEmpty()) {
      validarVarchar(this.filaActual.getIdCliente(), "numero_cliente", Constantes.LIM_CLIENTE_numero_cliente);
    }

  }

  /**
   * El RFC no es obligatorio.
   */
  public void validarRfc() throws Exception {
    if (!this.filaActual.getRfc().isEmpty()) {
      validarVarchar(this.filaActual.getRfc(), "rfc", Constantes.LIM_SUJETO_rfc);
    }
  }

  /**
   * La CALLE es obligatoria.
   */
  public void validarCalle() throws Exception {
    if (!this.filaActual.getCalle().isEmpty()) {
      validarVarchar(this.filaActual.getCalle(), "calle", Constantes.LIM_DIRECCION_calle);
    } else {
      throw new Exception("Error en el campo \"calle\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * La COLONIA es obligatoria.
   */
  public void validarColonia() throws Exception {
    if (!this.filaActual.getColonia().isEmpty()) {
      validarVarchar(this.filaActual.getColonia(), "colonia", Constantes.LIM_COLONIA_nombre);
    } else {
      throw new Exception("Error en el campo \"colonia\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El ESTADO es obligatorio.
   */
  public void validarEstadoRep() throws Exception {
    if (!this.filaActual.getEstado().isEmpty()) {
      validarVarchar(this.filaActual.getEstado(), "estado_republica", Constantes.LIM_ESTADO_REPUBLICA_nombre);
    } else {
      throw new Exception("Error en el campo \"estado_republica\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El MUNICIPIO es obligatorio.
   */
  public void validarMunicipio() throws Exception {
    if (!this.filaActual.getMunicipio().isEmpty()) {
      validarVarchar(this.filaActual.getMunicipio(), "municipio", Constantes.LIM_MUNICIPIO_nombre);
    } else {
      throw new Exception("Error en el campo \"municipio\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   * El CP es obligatorio.
   */
  public void validarCodPost() throws Exception {
    if (!this.filaActual.getCp().isEmpty()) {
      validarVarchar(this.filaActual.getCp(), "codigo_postal", Constantes.LIM_COLONIA_cp);
    } else {
      throw new Exception("Error en el campo \"codigo_postal\". " + Errores.CAMPO_VACIO);
    }
  }

  /**
   *
   */
  public void validarFacs() throws Exception {
    int maxIndex = Math.max(this.filaActual.getAnio().size(), this.filaActual.getMes().size());
    maxIndex = Math.max(maxIndex, this.filaActual.getMonto().size());
    int i;
    for (i = 0; i < maxIndex; i++) {
      try {
        if ((Integer.parseInt(this.filaActual.getAnio().get(i)) < Constantes.LIM_INF_ANIO_EXCEL)
                || (Integer.parseInt(this.filaActual.getAnio().get(i)) > Constantes.LIM_SUP_ANIO_EXCEL)) {
          throw new Exception("Error en el campo \"AÑO\" en el fac No." + (i + 1) + "." + Errores.ANIO_FUERA_DE_RANGO_EXCEL);
        }
      } catch (NumberFormatException nfe) {
        throw new Exception("Error en el campo \"AÑO\" en el fac No." + (i + 1) + "." + Errores.CAMPO_ENTERO_REQUERIDO, nfe);
      } catch (IndexOutOfBoundsException iobe) {
        throw new Exception("Error en el campo \"AÑO\" en el fac No." + (i + 1) + ". Se esperaba un valor, dado que alguna(s) otra(s) celda(s) del fac lo contiene(n).", iobe);
      }
      try {
        if (Arrays.binarySearch(Constantes.MESES_DEL_ANIO, this.filaActual.getFacMes().get(i).toLowerCase()) < 0) {
          throw new Exception("Error en el campo \"MES\" en el fac No." + (i + 1) + ". Se esperaba un mes del año escrito con palabra.");
        }
      } catch (IndexOutOfBoundsException iobe) {
        throw new Exception("Error en el campo \"MES\" en el fac No." + (i + 1) + ". Se esperaba un valor, dado que alguna(s) otra(s) celda(s) del fac lo contiene(n).", iobe);
      }
      try {
        if (Float.valueOf(this.filaActual.getMonto().get(i)) < 0f) {
          throw new Exception("Error en el campo \"MONTO\" en el fac No." + (i + 1) + ". Se esperaba un valor de monto no negativo.");
        }
      } catch (IndexOutOfBoundsException iobe) {
        throw new Exception("Error en el campo \"MONTO\" en el fac No." + (i + 1) + ". Se esperaba un valor, dado que alguna(s) otra(s) celda(s) del fac lo contiene(n).", iobe);
      }
    }
  }
}
