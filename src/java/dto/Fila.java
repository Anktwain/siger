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

  public Fila() {
    refsAdicionales = new ArrayList<String>();
    correos = new ArrayList<String>();
    telsAdicionales = new ArrayList<String>();
    direcsAdicionales = new ArrayList<String>();

    facs = new ArrayList<>();
    clasificacion = 0;
  }

  private String marcaje;
  private String fechaQuebranto;

  private Fecha fecha = new Fecha();
  private String error;
  private String detalleError;
  private int clasificacion;

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
   * @return {@code credito} el campo de la columna 1 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getCredito() {
    return credito;
  }

  public void setCredito(String credito) {
    this.credito = credito;
  }

  /**
   * @return {@code nombre} el campo de la columna 2 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * @return {@code refCobro} el campo de la columna 3 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getRefCobro() {
    return refCobro;
  }

  public void setRefCobro(String refCobro) {
    this.refCobro = refCobro;
  }

  /**
   * @return {@code linea} el campo de la columna 4 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getLinea() {
    return linea;
  }

  public void setLinea(String linea) {
    this.linea = linea;
  }

  /**
   * @return {@code tipoCredito} el campo de la columna 5 en el archivo estándar
   * de excel para la carga de remesas en SigerWeb1
   */
  public String getTipoCredito() {
    return tipoCredito;
  }

  public void setTipoCredito(String tipoCredito) {
    this.tipoCredito = tipoCredito;
  }

  /**
   * @return {@code estatus} el campo de la columna 6 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getEstatus() {
    return estatus;
  }

  public void setEstatus(String estatus) {
    this.estatus = estatus;
  }

  /**
   * @return {@code mesesVencidos} el campo de la columna 7 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(String mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  /**
   * @return {@code despacho} el campo de la columna 8 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getDespacho() {
    return despacho;
  }

  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }

  /**
   * @return {@code fechaInicioCredito} el campo de la columna 9 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaInicioCredito() {

    return fechaInicioCredito;
  }

  public void setFechaInicioCredito(String fechaInicioCredito) {
    this.fechaInicioCredito = corregirFecha(fechaInicioCredito);
    //this.fechaInicioCredito = fechaInicioCredito;
  }

  /**
   * @return {@code fechaVencimientoCred} el campo de la columna 10 en el
   * archivo estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaVencimientoCred() {
    return fechaVencimientoCred;
  }

  public void setFechaVencimientoCred(String fechaVencimientoCred) {
    this.fechaVencimientoCred = corregirFecha(fechaVencimientoCred);
//    this.fechaVencimientoCred = fechaVencimientoCred;
  }

  /**
   * @return {@code disposicion} el campo de la columna 11 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getDisposicion() {
    return disposicion;
  }

  public void setDisposicion(String disposicion) {
    this.disposicion = disposicion;
  }
  
  public Float getDisposicionFloat(){
    return Float.parseFloat(disposicion);
  }

  /**
   * @return {@code mensualidad} el campo de la columna 12 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getMensualidad() {
    return mensualidad;
  }

  public void setMensualidad(String mensualidad) {
    this.mensualidad = mensualidad;
  }

  /**
   * @return {@code saldoInsoluto} el campo de la columna 13 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getSaldoInsoluto() {
    return saldoInsoluto;
  }

  public void setSaldoInsoluto(String saldoInsoluto) {
    this.saldoInsoluto = saldoInsoluto;
  }

  /**
   * @return {@code saldoVencido} el campo de la columna 14 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getSaldoVencido() {
    return saldoVencido;
  }
  
  public Float getSaldoVencidoFloat() {
    return Float.parseFloat(saldoVencido);
  }

  public void setSaldoVencido(String saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  /**
   * @return {@code tasa} el campo de la columna 15 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getTasa() {
    return tasa;
  }

  public void setTasa(String tasa) {
    this.tasa = tasa;
  }

  /**
   * @return {@code cuenta} el campo de la columna 16 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  /**
   * @return {@code fechaUltimoPago} el campo de la columna 17 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaUltimoPago() {
    return fechaUltimoPago;
  }

  public void setFechaUltimoPago(String fechaUltimoPago) {
    this.fechaUltimoPago = corregirFecha(fechaUltimoPago);
//    this.fechaUltimoPago = fechaUltimoPago;
  }

  /**
   * @return {@code fechaUltimoVencimientoPagado} el campo de la columna 18 en
   * el archivo estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaUltimoVencimientoPagado() {
    return fechaUltimoVencimientoPagado;
  }

  public void setFechaUltimoVencimientoPagado(String fechaUltimoVencimientoPagado) {
    this.fechaUltimoVencimientoPagado = corregirFecha(fechaUltimoVencimientoPagado);
//    this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
  }

  /**
   * @return {@code idCliente} el campo de la columna 19 en el archivo estándar
   * de excel para la carga de remesas en SigerWeb1
   */
  public String getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(String idCliente) {
    this.idCliente = idCliente;
  }

  /**
   * @return {@code rfc} el campo de la columna 20 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /**
   * @return {@code calle} el campo de la columna 21 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  /**
   * @return {@code colonia} el campo de la columna 22 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getColonia() {
    return colonia;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  /**
   * @return {@code estado} el campo de la columna 23 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  /**
   * @return {@code municipio} el campo de la columna 24 en el archivo estándar
   * de excel para la carga de remesas en SigerWeb1
   */
  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  /**
   * @return {@code cp} el campo de la columna 25 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  /**
   * @return {@code anio} el ArrayList que contiene los hasta 3 registros
   * (columnas 26, 30 y 34 en el archivo estándar de excel) del año
   * correspondientes a las fechas de facturacion de los créditos que se
   * facturan por un tercero.
   */
  public ArrayList<String> getAnio() {
    return anio;
  }

  public void setAnio(ArrayList<String> anio) {
    this.anio = anio;
  }

  /**
   * @return {@code mes} el ArrayList que contiene los hasta 3 registros
   * (columnas 27, 31 y 35 en el archivo estándar de excel) del mes
   * correspondientes a las fechas de facturacion de los créditos que se
   * facturan por un tercero.
   */
  public ArrayList<String> getMes() {
    return mes;
  }

  public void setMes(ArrayList<String> mes) {
    this.mes = mes;
  }

  /**
   * @return {@code facMes} el ArrayList que contiene los hasta 3 registros
   * (columnas 28, 32 y 36 en el archivo estándar de excel) fac_mes
   * correspondientes a las fechas de facturacion de los créditos que se
   * facturan por un tercero.
   */
  public ArrayList<String> getFacMes() {
    return facMes;
  }

  public void setFacMes(ArrayList<String> facMes) {
    this.facMes = facMes;
  }

  /**
   * @return {@code facMes} el ArrayList que contiene los hasta 3 registros
   * (columnas 29, 33 y 37 en el archivo estándar de excel) del monto
   * correspondientes a las fechas de facturacion de los créditos que se
   * facturan por un tercero.
   */
  public ArrayList<String> getMonto() {
    return monto;
  }

  public void setMonto(ArrayList<String> monto) {
    this.monto = monto;
  }

  /**
   * @return {@code refsAdicionales} ArrayList que contiene los hasta 3
   * registros (columnas 38, 39 y 40) de las referencias.
   */
  public ArrayList<String> getRefsAdicionales() {
    return refsAdicionales;
  }

  public void setRefsAdicionales(ArrayList<String> refsAdicionales) {
    this.refsAdicionales = refsAdicionales;
  }

  /**
   * @return {@code correos} ArrayList que contiene los correos electrónicos de
   * contacto del deudor. <strong>Sólo existe una columna (la 41) en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1.</strong>
   */
  public ArrayList<String> getCorreos() {
    return correos;
  }

  public void setCorreos(ArrayList<String> correos) {
    this.correos = correos;
  }

  /**
   * @return {@code telsAdicionales} ArrayList con los hasta 2 registros de
   * teléfonos de contacto adicionales del deudor, correspondientes a las
   * columnas 42 y 43 en el archivo estándar de excel para la carga de remesas
   * en SigerWeb1.
   */
  public ArrayList<String> getTelsAdicionales() {
    return telsAdicionales;
  }

  public void setTelsAdicionales(ArrayList<String> telsAdicionales) {
    this.telsAdicionales = telsAdicionales;
  }

  /**
   * @return {@code correos} ArrayList que contiene las direcciones adicionales
   * del deudor. <strong>Sólo existe una columna (la 44) en el archivo estándar
   * de excel para la carga de remesas en SigerWeb1.</strong>
   */
  public ArrayList<String> getDirecsAdicionales() {
    return direcsAdicionales;
  }

  public void setDirecsAdicionales(ArrayList<String> direcsAdicionales) {
    this.direcsAdicionales = direcsAdicionales;
  }

  /**
   * @return {@code marcaje} el campo de la columna 46 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1.
   */
  public String getMarcaje() {
    return marcaje;
  }

  public void setMarcaje(String marcaje) {
    this.marcaje = marcaje;
  }

  /**
   * @return {@code fechaQuebranto} el campo de la columna 47 en el archivo
   * estándar de excel para la carga de remesas en SigerWeb1
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
