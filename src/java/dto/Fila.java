package dto;

import dto.Despacho;
import dto.Deudor;
import dto.Gestor;
import dto.Institucion;
import dto.Producto;
import dto.Subproducto;
import java.io.Serializable;
import java.util.ArrayList;
import util.Fecha;
import util.constantes.Patrones;

/**
 *
 * @author Cofradia
 */
public class Fila implements Serializable, Comparable<Fila> {

  /* Guarda los IDs correspondientes a los objetos utilizados, en el archivo de
   Excel estos datos no aparecen como claves, sino como cadenas de texto. */
  private int idDespacho;
  private int idProducto;
  private int idSubproducto;
  private int idEstado;
  private int idMunicipio;
  private int idColonia;
  private int idGestor;
  private int idSujeto;
  private int idDeudor;
  
  /* Objetos que contiene un Credito */
  private Institucion institucionDTO; // Institución que otorgó el crédito
  private Producto productoDTO; // Producto crédito
  private Subproducto subproductoDTO; // Subproducto crédito
  private Deudor deudorDTO; // Deudor asociado al crédito
  private Gestor gestorDTO; // Gestor al cual será asignado el crédito
  private Despacho despachoDTO; // Despacho que gestionará

  /* Datos que se toman del archivo Excel */
  private String credito; // Número de crédito.
  private String nombre; // Nombre o razón social del deudor.
  private String refCobro; // Referencia de cobro: una línea telefónica asociada a crédito.
  private String linea; // La línea de crédito. Esta dato lo maneja el banco.
  private String tipoCredito; // El tipo de crédito.
  private String estatus; // Estatus del crédito, puede ser MV, VV, etc.
  private String mesesVencidos; // Número de meses vencidos que presenta el crédito.
  private String despacho; // Despacho al cual se ha asignado el crédito para su gestión.
  private String fechaInicioCredito; // Fecha de inicio del crédito.
  private String fechaVencimientoCred; // Fecha de vencimiento de crédito.
  private String disposicion; // Monto o disposición de crédito.
  private String mensualidad; // Mensualidad.
  private String saldoInsoluto; // Saldo insoluto que presenta el crédito.
  private String saldoVencido; // Saldo vencido.
  private String tasa; // Tasa de interés
  private String cuenta; // Número de cuenta a la cual se hacen depósitos.
  private String fechaUltimoPago; // Fecha de último pago.
  private String fechaUltimoVencimientoPagado; // Fecha del último vencimiento pagado.
  private String idCliente; // ID o número de deudor.
  private String rfc; // RFC del deudor.
  private String calle; // Calle en donde se encuentra el domicilio del deudor.
  private String colonia; // Colonia en donde se encuentra el domicilio del deudor.
  private String estado; // Estado en donde se encuentra el domicilio del deudor.
  private String municipio; // Municipio en donde se encuentra el domicilio del deudor.
  private String cp; // El código postal.
  private String numeroInterior; // Número unterior.
  private String numeroExterior; // Número exterior.
  private String fechaQuebranto; // La fecha de quebranto.

  // Conjunto de los "Facs" que se incluyen en el archivo de carga mensual
  private ArrayList<carga.Fac> facs;

  // Los siguientes datos también son conjuntos da datos de contacto del deudor.
  private ArrayList<String> refsAdicionales; // Referencias adicionales.
  private ArrayList<String> correos; // Correos adicionales.
  private ArrayList<String> telsAdicionales; // Teléfonos adicionales.
  private ArrayList<String> direcsAdicionales; // Direcciones adicionales.

  /* El marcaje del crédito es un dato que se maneja internamente para conocer
   la situación de un crédito. */
  private String marcaje;

  /* Objeto auxiliar que sirve para hacer correcciones de fechas. */
  private Fecha fecha = new Fecha();

  /* En caso de ocurrir algún error con alguna fila, el error y sus detalles se
   guardan en las siguientes variables. */
  private String error;
  private String detalleError;

  /* Asigna un valor entero a una Fila de acuerdo a la clasificación del crédito:
   NUEVO CRÉDITO, NUEVO TOTAL, EN LA FIESTA o ESTABA EN LA FIESTA. */
  private int clasificacion;

  public Fila() {
    refsAdicionales = new ArrayList<String>();
    correos = new ArrayList<String>();
    telsAdicionales = new ArrayList<String>();
    direcsAdicionales = new ArrayList<String>();

    facs = new ArrayList<>();
    clasificacion = 0;
  }

  /**
   * Método que hace conversiones de fecha. En muchos casos se dan problemas
   * debido a los diversos formatos de fecha que usan las herramientas de
   * desarrollo y los procesadores de hojas de cálculo. Este problema se
   * resuelve haciendo un procesamiento de este tipo de datos.
   *
   * @param fecha La fecha tomada del archivo. Esta fecha en el archivo Excel
   * deberá hallarse de manera que apareca como un entero. Luego se hace el
   * procesamiento adecuado.
   *
   * @return La fecha, en forma de String, ya procesada y arreglado el problema.
   */
  private String corregirFecha(String fecha) {
    if (fecha == null || fecha.equals("-") || fecha.equals("")) {
      return "";
    } else {
      return this.fecha.calcularFecha(Integer.parseInt(fecha));
    }
  }

  /**
   * Compara saldos vencidos. La comparación de los saldos vencidos permite
   * ordenar los créditos de acuerdo con este criterio.
   *
   * @param o El objeto Fila con el cual se comparará el objeto actual.
   *
   * @return Número positivo si la cadena dada es menor, número negativo si la
   * cadena actual es menor, 0 si son iguales.
   */
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

  /* Setters y Getters */
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
   * @return {@code cuenta} Cuenta. Es una cadena de texto formada por dígitos
   * que representa un número de cuenta? en donde el deudor deposita su pago.
   */
  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  /**
   * @return {@code fechaUltimoPago} La fecha en que el deudor hizo el último
   * pago de su crédito.
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
   * @return {@code idCliente} Una cadena única, generalmente compuesta de
   * dígitos, que representa el identificador para un deudor.
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
   * @return {@code fechaQuebranto} Fecha de quebranto del crédito. Si es el
   * caso.
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

  public ArrayList<String> getRefsAdicionales() {
    return refsAdicionales;
  }

  public void setRefsAdicionales(ArrayList<String> refsAdicionales) {
    this.refsAdicionales = refsAdicionales;
  }

  public ArrayList<String> getCorreos() {
    return correos;
  }

  public void setCorreos(ArrayList<String> correos) {
    this.correos = correos;
  }

  public ArrayList<String> getTelsAdicionales() {
    return telsAdicionales;
  }

  public void setTelsAdicionales(ArrayList<String> telsAdicionales) {
    this.telsAdicionales = telsAdicionales;
  }

  public ArrayList<String> getDirecsAdicionales() {
    return direcsAdicionales;
  }

  public void setDirecsAdicionales(ArrayList<String> direcsAdicionales) {
    this.direcsAdicionales = direcsAdicionales;
  }

  public Institucion getInstitucionDTO() {
    return institucionDTO;
  }

  public void setInstitucionDTO(Institucion institucionDTO) {
    this.institucionDTO = institucionDTO;
  }

  public Producto getProductoDTO() {
    return productoDTO;
  }

  public void setProductoDTO(Producto productoDTO) {
    this.productoDTO = productoDTO;
  }

  public Subproducto getSubproductoDTO() {
    return subproductoDTO;
  }

  public void setSubproductoDTO(Subproducto subproductoDTO) {
    this.subproductoDTO = subproductoDTO;
  }

  public Deudor getDeudorDTO() {
    return deudorDTO;
  }

  public void setDeudorDTO(Deudor deudorDTO) {
    this.deudorDTO = deudorDTO;
  }

  public Gestor getGestorDTO() {
    return gestorDTO;
  }

  public void setGestorDTO(Gestor gestorDTO) {
    this.gestorDTO = gestorDTO;
  }

  public Despacho getDespachoDTO() {
    return despachoDTO;
  }

  public void setDespachoDTO(Despacho despachoDTO) {
    this.despachoDTO = despachoDTO;
  }

  
}
