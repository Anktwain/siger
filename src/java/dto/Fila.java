package dto;

import java.io.Serializable;
import java.util.ArrayList;
import util.Fecha;
import util.constantes.Patrones;

/**
 *
 * @author Cofradia
 */
public class Fila implements Serializable, Comparable<Fila> {

  private int idDespacho;
  private int idProducto;
  private int idSubproducto;
  private int idEstado;
  private int idMunicipio;
  private int idColonia;
  private int idGestor;
  private int idSujeto;
  private int idDeudor;

  private String credito;
  private String nombre;
  private String refCobro;
  private String linea;
  private String tipoCredito;
  private String estatus;
  private String mesesVencidos;
  private String despacho;
  private String fechaInicioCredito;
  private String fechaVencimientoCred;
  private String disposicion;
  private String mensualidad;
  private String saldoInsoluto;
  private String saldoVencido;
  private String tasa;
  private String cuenta;
  private String fechaUltimoPago;
  private String fechaUltimoVencimientoPagado;
  private String idCliente;
  private String rfc;
  private String calle;
  private String colonia;
  private String estado;
  private String municipio;
  private String cp;
  private String numeroInterior;
  private String numeroExterior;

  private ArrayList<String> anio;
  private ArrayList<String> mes;
  private ArrayList<String> facMes;
  private ArrayList<String> monto;

  private ArrayList<carga.Fac> facs;

  private ArrayList<String> refsAdicionales;

  private ArrayList<String> correos;

  private ArrayList<String> telsAdicionales;

  private ArrayList<String> direcsAdicionales;

  private String marcaje;
  private String fechaQuebranto;

  private Fecha fecha = new Fecha();
  private String error;
  private String detalleError;
  private int clasificacion;

  public Fila() {
    refsAdicionales = new ArrayList<String>();
    correos = new ArrayList<String>();
    telsAdicionales = new ArrayList<String>();
    direcsAdicionales = new ArrayList<String>();

    facs = new ArrayList<>();
    clasificacion = 0;
  }

  private String corregirFecha(String fecha) {
    if (fecha == null || fecha.equals("-") || fecha.equals("")) {
      return "";
    } else {
      return this.fecha.calcularFecha(Integer.parseInt(fecha));
    }
  }

  // METODO QUE CREA LA CONSULTA SQL CON LOS PARAMETROS DEL DTO
  public String crearSQL() {
    // CREAMOS LA CADENA QUE GUARDARA LA CONSULTA
    String consulta;
    // PRIMERO CREAMOS EL SUJETO  ***EL SUJETO YA EXISTE...
    consulta = "INSERT INTO sujeto (nombre_razon_social, rfc, eliminado) VALUES ('" + nombre + "', '" + rfc + "', 1);\n";
    // GUARDAMOS EL ID DEL SUJETO INSERTADO
    consulta = consulta + "SET @idSujeto = (SELECT LAST_INSERT_ID());\n";
    // CREAMOS AL CLIENTE QUE SERA DUEÑO DEL CREDITO
    consulta = consulta + "INSERT INTO cliente (numero_cliente, sujetos_id_sujeto) VALUES ('" + idCliente + "', @idSujeto);\n";
    // BUSCAMOS AL CLIENTE
    consulta = consulta + "SET @idCliente = (SELECT id_cliente FROM cliente WHERE sujetos_id_sujeto = @idSujeto);\n";
    // BUSCAMOS A LA EMPRESA
    consulta = consulta + "SET @idEmpresa = (SELECT id_empresa FROM empresa WHERE id_empresa = 1);\n";
    // BUSCAMOS EL PRODUCTO
    consulta = consulta + "SET @idProducto = (SELECT id_producto FROM producto WHERE id_producto = 1);\n";
    // BUSCAMOS EL SUBPRODUCTO
    consulta = consulta + "SET @idSubproducto = (SELECT id_subproducto FROM subproducto WHERE id_subproducto = 1);\n";
    // CREAMOS EL CREDITO
    consulta = consulta + "INSERT INTO credito (numero_credito, fecha_inicio, fecha_fin, fecha_quebranto, monto, mensualidad, tasa_interes, dias_mora, numero_cuenta, tipo_credito, empresas_id_empresa, productos_id_producto, subproductos_id_subproducto, clientes_id_cliente, gestores_id_gestor) VALUES ('" + credito + "', '" + fechaInicioCredito + "', '" + fechaVencimientoCred + "', '" + fechaQuebranto + "', " + disposicion + ", " + mensualidad + ", " + tasa + ", 0, " + cuenta + ", 1, @idEmpresa, @idProducto, @idSubproducto, @idCliente, 7);\n";
    // CREAMOS LA DIRECCION DE ESTE DEUDOR
    consulta = consulta + "INSERT INTO direccion (calle, sujetos_id_sujeto, municipio_id_municipio, estado_republica_id_estado, colonia_id_colonia) VALUES ('" + calle + "', @idSujeto, " + municipio + ", " + estado + ", " + colonia + ");\n";
    // CREAMOS EL TELEFONO PARA EL DEUDOR
    consulta = consulta + "INSERT INTO telefono (numero, tipo, sujetos_id_sujeto) VALUES ('" + refCobro + "', 'Referencia', @idSujeto);\n";
    // CREAMOS EL CORREO ELECTRONICO DEL DEUDOR
    //consulta = consulta + "INSERT INTO email (direccion, tipo, sujetos_id_sujeto) VALUES ('" + correos.get(0) + "', 'Referencia', @idSujeto);";

    return consulta;
  }

  @Override
  public int compareTo(Fila o) {
    Float miSaldoVencido = new Float(this.saldoVencido);
    Float suSaldoVencido = new Float(o.getSaldoVencido());
    return miSaldoVencido.compareTo(suSaldoVencido);
  }

  @Override
  public String toString() {
    if (idColonia == 0) {
      return "Fila{" + "credito=" + credito + ", nombre=" + nombre
              + ", refCobro=" + refCobro + ", linea=" + idSubproducto
              + ", tipoCredito=" + idProducto + ", estatus=" + estatus
              + ", mesesVencidos=" + mesesVencidos + ", despacho=" + idDespacho
              + ", fechaInicioCredito=" + fechaInicioCredito + ", fechaVencimientoCred="
              + fechaVencimientoCred + ", disposicion=" + disposicion + ", mensualidad="
              + mensualidad + ", saldoInsoluto=" + saldoInsoluto + ", saldoVencido="
              + saldoVencido + ", tasa=" + tasa + ", cuenta=" + cuenta + ", fechaUltimoPago="
              + fechaUltimoPago + ", fechaUltimoVencimientoPagado=" + fechaUltimoVencimientoPagado
              + ", idCliente=" + idCliente + ", rfc=" + rfc + ", calle=" + calle + ", colonia="
              + colonia + ", estado=" + estado + ", municipio=" + municipio + ", cp=" + cp + ", gestor=" + idGestor
              + ", numeroInterior=" + numeroInterior + ", numeroExterior=" + numeroExterior
              + ", facs=" + facs + ", refsAdicionales=" + refsAdicionales + ", correos=" + correos
              + ", telsAdicionales=" + telsAdicionales + ", direcsAdicionales=" + direcsAdicionales
              + ", marcaje=" + marcaje + ", fechaQuebranto=" + fechaQuebranto + '}';
    } else {
      return "Fila{" + "credito=" + credito + ", nombre=" + nombre
              + ", refCobro=" + refCobro + ", linea=" + idSubproducto
              + ", tipoCredito=" + idProducto + ", estatus=" + estatus
              + ", mesesVencidos=" + mesesVencidos + ", despacho=" + idDespacho
              + ", fechaInicioCredito=" + fechaInicioCredito + ", fechaVencimientoCred="
              + fechaVencimientoCred + ", disposicion=" + disposicion + ", mensualidad="
              + mensualidad + ", saldoInsoluto=" + saldoInsoluto + ", saldoVencido="
              + saldoVencido + ", tasa=" + tasa + ", cuenta=" + cuenta + ", fechaUltimoPago="
              + fechaUltimoPago + ", fechaUltimoVencimientoPagado=" + fechaUltimoVencimientoPagado
              + ", idCliente=" + idCliente + ", rfc=" + rfc + ", calle=" + calle + ", colonia="
              + idColonia + ", estado=" + idEstado + ", municipio=" + idMunicipio + ", cp=" + cp + ", gestor=" + idGestor
              + ", numeroInterior=" + numeroInterior + ", numeroExterior=" + numeroExterior
              + ", facs=" + facs + ", refsAdicionales=" + refsAdicionales + ", correos=" + correos
              + ", telsAdicionales=" + telsAdicionales + ", direcsAdicionales=" + direcsAdicionales
              + ", marcaje=" + marcaje + ", fechaQuebranto=" + fechaQuebranto + '}';
    }
  }

  public carga.Fac buscarFac(int mes) {
    if (facs.size() > 0) {
      for (carga.Fac fac : facs) {
        if (fac.getMes() == mes) {
          return fac;
        }
      }
    }
    return null;
  }

  /**
   * @return {@code credito} Número de crédito. Este número debe ser único ya
   * que funciona como identificador del crédito.
   */
  public String getCredito() {
    return credito;
  }

  public void setCredito(String credito) {
    this.credito = credito;
  }

  /**
   * @return {@code nombre} El nombre del deudor, o razón social, si se trata de
   * una empresa.
   */
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * @return {@code refCobro} Referencia de cobro. Es una cadena de números que
   * representa un número telefónico asociado al crédito.
   */
  public String getRefCobro() {
    return refCobro;
  }

  public void setRefCobro(String refCobro) {
    this.refCobro = refCobro;
  }

  /**
   * @return {@code linea} Línea de crédito. Una clasificación que hace el banco
   * sobre sus productos de crédito.
   */
  public String getLinea() {
    return linea;
  }

  public void setLinea(String linea) {
    this.linea = linea;
  }

  /**
   * @return {@code tipoCredito} Tipo de crédito. Clasificación realizada por el
   * banco.
   */
  public String getTipoCredito() {
    return tipoCredito;
  }

  public void setTipoCredito(String tipoCredito) {
    this.tipoCredito = tipoCredito;
  }

  /**
   * @return {@code estatus} Estatus del crédito. Es una clave que utiliza el
   * banco para indicar la situcaión actual del crédito en cuestión. El estatus
   * puede ser: MV (Meses vencidos), etc.
   */
  public String getEstatus() {
    return estatus;
  }

  public void setEstatus(String estatus) {
    this.estatus = estatus;
  }

  /**
   * @return {@code mesesVencidos} El número de meses vencidos que presenta ese
   * crédito en el momento de hacer la carga.
   */
  public String getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(String mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  /**
   * @return {@code despacho} Es una cadena que representa el nombre corto del
   * despacho al cual está asignado el crédito en cuestión.
   */
  public String getDespacho() {
    return despacho;
  }

  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }

  /**
   * @return {@code fechaInicioCredito} Fecha de inicio del crédito.
   */
  public String getFechaInicioCredito() {

    return fechaInicioCredito;
  }

  public void setFechaInicioCredito(String fechaInicioCredito) {
    this.fechaInicioCredito = corregirFecha(fechaInicioCredito);
    //this.fechaInicioCredito = fechaInicioCredito;
  }

  /**
   * @return {@code fechaVencimientoCred} Fecha de vencimiento del crédito.
   */
  public String getFechaVencimientoCred() {
    return fechaVencimientoCred;
  }

  public void setFechaVencimientoCred(String fechaVencimientoCred) {
    this.fechaVencimientoCred = corregirFecha(fechaVencimientoCred);
//    this.fechaVencimientoCred = fechaVencimientoCred;
  }

  /**
   * @return {@code disposicion} Disposición o monto. Es la cantidad original
   * que el banco prestó al deudor. Este dato está representado por una cadena
   * de texto.
   */
  public String getDisposicion() {
    return disposicion;
  }

  public void setDisposicion(String disposicion) {
    this.disposicion = disposicion;
  }

    /**
   * @return {@code disposicion} Disposición o monto. Dato recuperado como tipo
   * Float.
   */
  public Float getDisposicionFloat() {
    return Float.parseFloat(disposicion);
  }

  /**
   * @return {@code mensualidad} La mensualidad que debe pagar el deudor. Se
   * representa mediante una cadena de texto.
   */
  public String getMensualidad() {
    return mensualidad;
  }

  public void setMensualidad(String mensualidad) {
    this.mensualidad = mensualidad;
  }

  /**
   * @return {@code saldoInsoluto} El saldo insoluto?. Es la cantidad que resta
   * el deudor por pagar.
   */
  public String getSaldoInsoluto() {
    return saldoInsoluto;
  }

  public void setSaldoInsoluto(String saldoInsoluto) {
    this.saldoInsoluto = saldoInsoluto;
  }

  /**
   * @return {@code saldoVencido} Es el saldo que tiene vencido el deudor, y que
   * por lo tanto hace que su crédito se encuentre en cobranza.
   */
  public String getSaldoVencido() {
    return saldoVencido;
  }

  /**
   * @return {@code saldoVencido} Es el saldo que tiene vencido el deudor. Este
   * dato es recuperado como un tipo Float.
   */
  public Float getSaldoVencidoFloat() {
    return Float.parseFloat(saldoVencido);
  }

  public void setSaldoVencido(String saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  /**
   * @return {@code tasa} La tasa de interés para el crédito en cuestión.
   */
  public String getTasa() {
    return tasa;
  }

  public void setTasa(String tasa) {
    this.tasa = tasa;
  }

  /**
   * @return {@code cuenta} Cuenta. Es una cadena de texto formada por dígitos que
   * representa un número de cuenta? en donde el deudor deposita su pago.   */
  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  /**
   * @return {@code fechaUltimoPago} La fecha en que el deudor hizo el último pago
   * de su crédito.
   */
  public String getFechaUltimoPago() {
    return fechaUltimoPago;
  }

  public void setFechaUltimoPago(String fechaUltimoPago) {
    this.fechaUltimoPago = corregirFecha(fechaUltimoPago);
//    this.fechaUltimoPago = fechaUltimoPago;
  }

  /**
   * @return {@code fechaUltimoVencimientoPagado} El último vencimiento pagado
   * del crédito.
   */
  public String getFechaUltimoVencimientoPagado() {
    return fechaUltimoVencimientoPagado;
  }

  public void setFechaUltimoVencimientoPagado(String fechaUltimoVencimientoPagado) {
    this.fechaUltimoVencimientoPagado = corregirFecha(fechaUltimoVencimientoPagado);
//    this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
  }

  /**
   * @return {@code idCliente} Una cadena única, generalmente compuesta de dígitos,
   * que representa el identificador para un deudor.
   */
  public String getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(String idCliente) {
    this.idCliente = idCliente;
  }

  /**
   * @return {@code rfc} El RFC del deudor
   */
  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /**
   * @return {@code calle} Calle. Forma parte del domicilio del deudor.
   */
  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  /**
   * @return {@code colonia} Colonia. Forma parte del domicilio del deudor.
   */
  public String getColonia() {
    return colonia;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  /**
   * @return {@code estado} Estado. Forma parte del domicilio del deudor.
   */
  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  /**
   * @return {@code municipio} Municipio. Forma parte del domicilio del deudor.
   */
  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  /**
   * @return {@code cp} Código postal. Forma parte del domicilio del deudor.
   */
  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  /**
   * @return {@code anio} Año. Conjunto de años que forman parte de un objeto Fac.
   */
  public ArrayList<String> getAnio() {
    return anio;
  }

  public void setAnio(ArrayList<String> anio) {
    this.anio = anio;
  }

  /**
   * @return {@code mes} Meses. Conjunto de meses que forman parte de un objeto Fac.
   */
  public ArrayList<String> getMes() {
    return mes;
  }

  public void setMes(ArrayList<String> mes) {
    this.mes = mes;
  }

  /**
   * @return {@code facMes} Fac Mes. Conjunto de Fac mes que forman parte de un objeto Fac.
   */
  public ArrayList<String> getFacMes() {
    return facMes;
  }

  public void setFacMes(ArrayList<String> facMes) {
    this.facMes = facMes;
  }

  /**
   * @return {@code facMes} Montos. Conjunto de montos que forman parte de un objeto Fac.
   */
  public ArrayList<String> getMonto() {
    return monto;
  }

  public void setMonto(ArrayList<String> monto) {
    this.monto = monto;
  }

  /**
   * @return {@code refsAdicionales} Conjunto de referencias adicionales asociadas
   * a un crédito.
   */
  public ArrayList<String> getRefsAdicionales() {
    return refsAdicionales;
  }

  public void setRefsAdicionales(ArrayList<String> refsAdicionales) {
    this.refsAdicionales = refsAdicionales;
  }

  /**
   * @return {@code correos} Conjunto de correos electrónicos del deudor.
   */
  public ArrayList<String> getCorreos() {
    return correos;
  }

  public void setCorreos(ArrayList<String> correos) {
    this.correos = correos;
  }

  /**
   * @return {@code telsAdicionales} Teléfonos adicionales proporcionados por el
   * deudor para hacer contacto con él.
   */
  public ArrayList<String> getTelsAdicionales() {
    return telsAdicionales;
  }

  public void setTelsAdicionales(ArrayList<String> telsAdicionales) {
    this.telsAdicionales = telsAdicionales;
  }

  /**
   * @return {@code correos} Direcciones adicionales proporcionados por el deudor
   * para visitas domiciliarias.
   */
  public ArrayList<String> getDirecsAdicionales() {
    return direcsAdicionales;
  }

  public void setDirecsAdicionales(ArrayList<String> direcsAdicionales) {
    this.direcsAdicionales = direcsAdicionales;
  }

  /**
   * @return {@code marcaje} Marcaje. Es un dato interno que sirve para conocer
   * la situación de un crédito respecto a su gestión. Por default los créditos
   * no tienen marcaje "Sin Marcaje".
   */
  public String getMarcaje() {
    return marcaje;
  }

  public void setMarcaje(String marcaje) {
    this.marcaje = marcaje;
  }

  /**
   * @return {@code fechaQuebranto} Fecha de quebranto del crédito. Si es el caso.
   */
  public String getFechaQuebranto() {
    return fechaQuebranto;
  }

  public void setFechaQuebranto(String fechaQuebranto) {
    this.fechaQuebranto = corregirFecha(fechaQuebranto);
//    this.fechaQuebranto = fechaQuebranto;
  }

  public String getNumeroInterior() {
    return numeroInterior;
  }

  public void setNumeroInterior(String numeroInterior) {
    this.numeroInterior = numeroInterior;
  }

  public String getNumeroExterior() {
    return numeroExterior;
  }

  public void setNumeroExterior(String numeroExterior) {
    this.numeroExterior = numeroExterior;
  }

  public void setTelefonoAdicional(String telefono) {
    if (telefono != null) {
      if (!telefono.equals("")) {
        this.telsAdicionales.add(telefono);
      }
    }
  }

  public void setCorreoAdicional(String correo) {
    if (correo != null) {
      if (!correo.equals("") && correo.matches(Patrones.PATRON_EMAIL)) {
        this.correos.add(correo);
      }
    }
  }

  public void setDireccionAdicional(String direccion) {
    if (direccion != null) {
      if (!direccion.equals("")) {
        this.direcsAdicionales.add(direccion);
      }
    }
  }

  public void setReferenciaAdicional(String referencia) {
    if (referencia != null) {
      if (!referencia.equals("")) {
        this.refsAdicionales.add(referencia);
      }
    }
  }

  public void setFacAdicional(carga.Fac fac) {
    this.facs.add(fac);
  }

  public ArrayList<carga.Fac> getFacs() {
    return facs;
  }

  public void setFacs(ArrayList<carga.Fac> facs) {
    this.facs = facs;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getDetalleError() {
    return detalleError;
  }

  public void setDetalleError(String detalleError) {
    this.detalleError = detalleError;
  }

  public int getIdDespacho() {
    return idDespacho;
  }

  public void setIdDespacho(int idDespacho) {
    this.idDespacho = idDespacho;
  }

  public int getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(int idProducto) {
    this.idProducto = idProducto;
  }

  public int getIdSubproducto() {
    return idSubproducto;
  }

  public void setIdSubproducto(int idSubproducto) {
    this.idSubproducto = idSubproducto;
  }

  public int getIdEstado() {
    return idEstado;
  }

  public void setIdEstado(int idEstado) {
    this.idEstado = idEstado;
  }

  public int getIdMunicipio() {
    return idMunicipio;
  }

  public void setIdMunicipio(int idMunicipio) {
    this.idMunicipio = idMunicipio;
  }

  public int getIdColonia() {
    return idColonia;
  }

  public void setIdColonia(int idColonia) {
    this.idColonia = idColonia;
  }

  public int getClasificacion() {
    return clasificacion;
  }

  public void setClasificacion(int clasificacion) {
    this.clasificacion = clasificacion;
  }

  public int getIdGestor() {
    return idGestor;
  }

  public void setIdGestor(int idGestor) {
    this.idGestor = idGestor;
  }

  public int getIdSujeto() {
    return idSujeto;
  }

  public void setIdSujeto(int idSujeto) {
    this.idSujeto = idSujeto;
  }

  public int getIdDeudor() {
    return idDeudor;
  }

  public void setIdDeudor(int idDeudor) {
    this.idDeudor = idDeudor;
  }

}
