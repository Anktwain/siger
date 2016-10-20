/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.IndexBean;
import dao.ConceptoDevolucionDAO;
import dao.CreditoDAO;
import dao.DeudorDAO;
import dao.DevolucionDAO;
import dao.GestorDAO;
import dao.MotivoDevolucionDAO;
import dao.SujetoDAO;
import dto.Credito;
import dto.Despacho;
import dto.Deudor;
import dto.Devolucion;
import dto.Gestor;
import dto.Producto;
import dto.Subproducto;
import dto.Sujeto;
import dto.carga.FilaCreditoExcel;
import dto.carga.FilaCreditoExcel.Ajustes;
import dto.carga.FilaCreditoExcel.Facs;
import impl.ActualizacionIMPL;
import impl.ConceptoDevolucionIMPL;
import impl.CreditoIMPL;
import impl.DespachoIMPL;
import impl.DeudorIMPL;
import impl.DevolucionIMPL;
import impl.GestorIMPL;
import impl.MotivoDevolucionIMPL;
import impl.ProductoIMPL;
import impl.RemesaIMPL;
import impl.SubproductoIMPL;
import impl.SujetoIMPL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import util.constantes.Campanas;
import util.constantes.Devoluciones;
import util.constantes.Marcajes;
import util.constantes.Sujetos;
import util.constantes.TipoCreditos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
// REESTRUCTURACION DEL MODULO DE CARGA
public class CreadorConsultasCargaSQL {

  // ***************************************************************************
  // LLAMADA A OTROS BEANS
  // ***************************************************************************
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private final SujetoDAO sujetoDao;
  private final DeudorDAO deudorDao;
  private final CreditoDAO creditoDao;
  private final GestorDAO gestorDao;
  private final DevolucionDAO devolucionDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private final MotivoDevolucionDAO motivoDevolucionDao;
  private final List<AsignacionCreditoGestor> asignacion;
  private final List<CreditoGestor> relacionCreditoGestor;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public CreadorConsultasCargaSQL() {
    sujetoDao = new SujetoIMPL();
    deudorDao = new DeudorIMPL();
    creditoDao = new CreditoIMPL();
    gestorDao = new GestorIMPL();
    devolucionDao = new DevolucionIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    asignacion = new ArrayList();
    relacionCreditoGestor = new ArrayList();
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA ACTUALIZAR LOS CREDITOS CONSERVADOS
  // ***************************************************************************
  public void prepararConservados(List<FilaCreditoExcel> conservados, Connection conn) {
    for (int i = 0; i < (conservados.size()); i++) {
      if (!actualizar(conservados.get(i), conn)) {
        break;
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR LOS NUEVOS CREDITOS
  // ***************************************************************************
  public void prepararNuevosCreditos(List<FilaCreditoExcel> nuevosCreditos, Connection conn) {
    for (int i = 0; i < (nuevosCreditos.size()); i++) {
      Deudor d = deudorDao.buscar(nuevosCreditos.get(i).getNumeroDeudor());
      if (d != null) {
        // SE BUSCAN EL GESTOR DE LOS CREDITOS ANTERIORES
        // PARA ASIGNAR EL NUEVO CREDITO
        Credito c = creditoDao.buscarPorSujeto(d.getSujeto().getIdSujeto());
        if (insertarCredito(nuevosCreditos.get(i), d.getIdDeudor(), c.getGestor().getIdGestor(), conn)) {
          c = creditoDao.buscar(nuevosCreditos.get(i).getNumeroCredito());
          if (c != null) {
            if (!insertarActualizacion(nuevosCreditos.get(i), c.getIdCredito(), conn)) {
              Logs.log.error("No se inserto la actualizacion para el nuevo credito " + nuevosCreditos.get(i).getNumeroCredito());
              break;
            }
          } else {
            Logs.log.error("No se encontro el nuevo credito recien insertado");
          }
        } else {
          Logs.log.error("No se inserto el nuevo credito");
        }
      } else {
        Logs.log.error("No se pudo asignar el deudor para el nuevo credito");
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR LOS NUEVOS TOTALES
  // ***************************************************************************
  public void prepararNuevosTotales(List<FilaCreditoExcel> nuevosTotales, Connection conn) {
    for (int i = 0; i < (nuevosTotales.size()); i++) {
      if (insertarSujeto(nuevosTotales.get(i).getNombreDeudor(), nuevosTotales.get(i).getRfc(), conn)) {
        Sujeto s;
        if (nuevosTotales.get(i).getRfc() != null) {
          if (nuevosTotales.get(i).getRfc().endsWith(" ")) {
            s = sujetoDao.buscarPorRFC(nuevosTotales.get(i).getRfc().substring(0, nuevosTotales.get(i).getRfc().length()));
          } else {
            s = sujetoDao.buscarPorRFC(nuevosTotales.get(i).getRfc());
          }
        } else {
          s = sujetoDao.buscarPorNombre(nuevosTotales.get(i).getNombreDeudor());
        }
        if (s != null) {
          if (insertarDeudor(nuevosTotales.get(i).getNumeroDeudor(), s.getIdSujeto(), conn)) {
            Deudor d = deudorDao.buscar(nuevosTotales.get(i).getNumeroDeudor());
            if (d != null) {
              // TO FIX:
              // SE DEBEN SORTEAR LOS GESTORES
              if (insertarCredito(nuevosTotales.get(i), d.getIdDeudor(), 9, conn)) {
                Credito c = creditoDao.buscar(nuevosTotales.get(i).getNumeroCredito());
                if (c != null) {
                  if (insertarActualizacion(nuevosTotales.get(i), c.getIdCredito(), conn)) {
                    Logs.log.info("Se inserto el credito " + nuevosTotales.get(i).getNumeroCredito() + " de forma satisfactoria");
                  } else {
                    Logs.log.error("No se inserto la actualizacion.");
                    break;
                  }
                } else {
                  Logs.log.error("No se ha encontrado el credito " + nuevosTotales.get(i).getNumeroCredito());
                  break;
                }
              } else {
                Logs.log.error("No se inserto el credito.");
                break;
              }
            } else {
              Logs.log.error("No se ha encontrado el deudor con el numero " + nuevosTotales.get(i).getNumeroDeudor());
              break;
            }
          } else {
            Logs.log.error("No se inserto el deudor.");
            break;
          }
        } else {
          Logs.log.error("No se ha encontrado el sujeto con el RFC " + nuevosTotales.get(i).getRfc());
          break;
        }
      } else {
        Logs.log.error("No se inserto el sujeto con el RFC " + nuevosTotales.get(i).getRfc());
        break;
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA REACTIVAR LOS CREDITOS
  // ***************************************************************************
  public void prepararReactivados(List<FilaCreditoExcel> reactivados, Connection conn) {
    for (int i = 0; i < (reactivados.size()); i++) {
      Devolucion d = devolucionDao.buscarDevolucionPorNumeroCredito(reactivados.get(i).getNumeroCredito());
      if (d != null) {
        Locale local = new Locale("es", "MX");
        Calendar cal = new GregorianCalendar(local);
        String fechaAsignacion = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, local) + " " + cal.get(Calendar.YEAR);
        d.setEstatus(Devoluciones.CONSERVADO);
        d.setObservaciones("Reactivada asignacion " + fechaAsignacion);
        if (!devolucionDao.editar(d)) {
          Logs.log.error("No se logro reactivar el credito " + reactivados.get(i).getNumeroCredito());
        }
      } else {
        Logs.log.error("No se logro reactivar el credito " + reactivados.get(i).getNumeroCredito() + ". No se encontro la devolucion en el sistema.");
      }
      if (!actualizar(reactivados.get(i), conn)) {
        break;
      }
    }
  }

  // ***************************************************************************
  // METODO QUE MANDA A BANDEJA DE DEVOLUCION LOS CREDITOS RETIRADOS
  // ***************************************************************************
  public void prepararRetirados(List<Credito> retirados) {
    for (int i = 0; i < (retirados.size()); i++) {
      Credito c = creditoDao.buscar(retirados.get(i).getNumeroCredito());
      if (c != null) {
        Locale local = new Locale("es", "MX");
        Calendar cal = new GregorianCalendar(local);
        String fechaAsignacion = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, local) + " " + cal.get(Calendar.YEAR);
        Devolucion d = devolucionDao.buscarDevolucionPorNumeroCredito(retirados.get(i).getNumeroCredito());
        if (d != null) {
          d.setFecha(cal.getTime());
          d.setObservaciones("Descodificada asignacion " + fechaAsignacion);
          d.setEstatus(Devoluciones.DEVUELTO);
          if (!devolucionDao.editar(d)) {
            Logs.log.error("No se retiro el credito " + retirados.get(i).getNumeroCredito());
          }
        } else {
          d = new Devolucion();
          d.setCredito(c);
          d.setConceptoDevolucion(conceptoDevolucionDao.buscarPorId(11));
          d.setMotivoDevolucion(motivoDevolucionDao.buscarPorId(23));
          d.setFecha(cal.getTime());
          d.setSolicitante("inbursa");
          d.setRevisor(indexBean.getUsuario().getNombreLogin());
          d.setObservaciones("Descodificada asignacion " + fechaAsignacion);
          d.setEstatus(Devoluciones.DEVUELTO);
          if (!devolucionDao.insertar(d)) {
            Logs.log.error("No se retiro el credito " + retirados.get(i).getNumeroCredito());
          }
        }
      } else {
        Logs.log.error("No se retiro el credito " + retirados.get(i).getNumeroCredito() + ". No se encontro el credito en el sistema.");
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA ACTUALIZAR DATOS
  // ***************************************************************************
  public void asignarCreditos(List<FilaCreditoExcel> filas) {
    List<Gestor> gestores = gestorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    List<AsignacionCreditoGestor> listaAsignacionGestor = new ArrayList();
    for (int i = 0; i < (gestores.size()); i++) {
      AsignacionCreditoGestor acg = new AsignacionCreditoGestor();
      acg.setGestor(gestores.get(i));
      acg.setCuentasAsignadas(creditoDao.buscarCreditosPorGestor(gestores.get(i).getUsuario().getIdUsuario()).size());
      acg.setTotalAsignadas(new ActualizacionIMPL().obtenerMontoPrometidoGestor(gestores.get(i).getIdGestor()));
      listaAsignacionGestor.add(acg);
    }
    Collections.sort(filas, new ComparadorSaldoVencido());
    // TO FIX:
    // DE MANERA TEMPORAL SE ESTARAN ASIGNANDO TODOS LOS CREDITOS AL GESTOR LETICIA
    // ELLA ELEGIRA MANUALMENTE A QUIEN SE LO ASIGNARA
    for (int i = 0; i < (filas.size()); i++) {
      CreditoGestor cg = new CreditoGestor();
      cg.setCredito(filas.get(i).getNumeroCredito());
      cg.setGestor(gestorDao.buscar(9));
      relacionCreditoGestor.add(cg);
    }
    /*
     for (int i = 0; i < (listaAsignacionGestor.size()); i++) {
     for (int j = 0; j < (filas.size()); j++) {
     // TO FIX:
     // ASIGNAR DE MANERA EQUITATIVA, O LO MAS CERCA QUE SE PUEDA DE LA EQUITATIVIDAD
     // AUMENTAR LA ASIGNACION
     // BUSCAR EL MONTO EN LA LISTA
     // CREAR OTRA VEZ LA LISTA Y LA CLASE CREDITOGESTOR PARA GUARDAR LOS CREDITOS REPARTIDOS
     }
     }
     */
  }

  // ***************************************************************************
  // METODO QUE ASIGNA UNA CUENTA A UN GESTOR DETERMINADO
  // ***************************************************************************
  public Gestor asignacionGestores(String numeroCredito) {
    Gestor g = null;
    for (int i = 0; i < (asignacion.size()); i++) {
      // TO FIX:
      // REPARAR METODO
      /*
       if (asignacion.get(i).getNumeroCredito().equals(numeroCredito)) {
       g = asignacion.get(i).gestor;
       }
       */
    }
    return g;
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA ACTUALIZAR DATOS
  // ***************************************************************************
  public boolean actualizar(FilaCreditoExcel fila, Connection conn) {
    String consulta;
    if (fila.getFechaQuebranto() != null) {
      consulta = "UPDATE credito SET fecha_quebranto = '" + fila.getFechaQuebranto() + "', saldo_insoluto = " + fila.getSaldoInsoluto() + ", mensualidad = " + fila.getMensualidad() + " WHERE numero_credito ='" + fila.getNumeroCredito() + "';";
    } else {
      consulta = "UPDATE credito SET saldo_insoluto = " + fila.getSaldoInsoluto() + ", mensualidad = " + fila.getMensualidad() + " WHERE numero_credito ='" + fila.getNumeroCredito() + "';";
    }
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    if (!ejecutor.ejecutor(consulta, conn)) {
      Logs.log.error("Error en la fila del credito " + fila.getNumeroCredito() + ". Revertir la carga.");
      return false;
    } else {
      consulta = "UPDATE actualizacion SET estatus = '" + fila.getEstatusCuenta() + "', meses_vencidos = " + fila.getMesesVencidos() + ", saldo_vencido = " + fila.getSaldoVencido();
      if (fila.getFechaUltimoPago() != null) {
        consulta = consulta + ", fecha_ultimo_pago = '" + fila.getFechaUltimoPago() + "'";
      }
      if (fila.getFechaUltimoVencimientoPagado() != null) {
        consulta = consulta + ", fecha_ultimo_vencimiento_pagado = '" + fila.getFechaUltimoVencimientoPagado() + "'";
      }
      consulta = consulta + " WHERE id_credito = (SELECT id_credito FROM credito WHERE numero_credito = '" + fila.getNumeroCredito() + "');";
      if (!ejecutor.ejecutor(consulta, conn)) {
        Logs.log.error("Error en la fila del credito " + fila.getNumeroCredito() + ". Revertir la carga.");
        return false;
      } else {
        if (!fila.getListaAjustes().isEmpty()) {
          insertarAjustes(fila.getListaAjustes(), fila.getNumeroCredito(), conn);
        }
        if (!fila.getListaFacs().isEmpty()) {
          insertarFacs(fila.getListaFacs(), fila.getNumeroCredito(), conn);
        }
        return true;
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR UN SUJETO
  // ***************************************************************************
  public boolean insertarSujeto(String nombreDeudor, String rfc, Connection conn) {
    String consulta = "INSERT INTO sujeto (nombre_razon_social, eliminado";
    if (rfc != null) {
      consulta = consulta + ", rfc";
    }
    consulta = consulta + ") VALUES ('" + nombreDeudor + "', '" + Sujetos.ACTIVO;
    if (rfc != null) {
      rfc = rfc.replace("  ", " ");
      if (rfc.endsWith(" ")) {
        String aux = rfc.substring(0, rfc.length() - 1);
        rfc = aux;
      }
      consulta = consulta + "', '" + rfc;
    }
    consulta = consulta + "');";
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    if (!ejecutor.ejecutor(consulta, conn)) {
      Logs.log.error("No se logro insertar el sujeto con el RFC " + rfc);
      return false;
    } else {
      return true;
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR UN DEUDOR
  // ***************************************************************************
  public boolean insertarDeudor(String numeroDeudor, int idSujeto, Connection conn) {
    String consulta = "INSERT INTO deudor (id_sujeto, id_calificacion, numero_deudor) VALUES ('" + idSujeto + "', '1', '" + numeroDeudor + "');";
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    if (!ejecutor.ejecutor(consulta, conn)) {
      Logs.log.error("No se logro insertar el deudor numero " + numeroDeudor);
      return false;
    } else {
      return true;
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR UN CREDITO
  // ***************************************************************************
  public boolean insertarCredito(FilaCreditoExcel fila, int idDeudor, int idGestor, Connection conn) {
    boolean ok;
    if (creditoDao.numeroCreditoExiste(fila.getNumeroCredito())) {
      Logs.log.error("Error en la fila del credito " + fila.getNumeroCredito() + ". El credito que intenta insertar ya existe en el sistema. Revertir la carga.");
      ok = false;
    } else {
      String consulta = "INSERT INTO credito (id_deudor, id_despacho, id_gestor, id_producto, id_campana, id_marcaje, numero_credito, tipo_credito";
      if (fila.getSubproducto() != null) {
        consulta = consulta + ", id_subproducto";
      }
      if (fila.getFechaInicioCredito() != null) {
        consulta = consulta + ", fecha_inicio";
      }
      if (fila.getFechaVencimientoCredito() != null) {
        consulta = consulta + ", fecha_fin";
      }
      if (fila.getFechaQuebranto() != null) {
        consulta = consulta + ", fecha_quebranto";
      }
      if (fila.getMontoCredito() != null) {
        consulta = consulta + ", monto";
      }
      if (fila.getSaldoInsoluto() != null) {
        consulta = consulta + ", saldo_insoluto";
      }
      if (fila.getMensualidad() != null) {
        consulta = consulta + ", mensualidad";
      }
      if (fila.getTasaInteres() != null) {
        consulta = consulta + ", tasa_interes";
      }
      consulta = consulta + ", dias_mora";
      if (fila.getCuentaCredito() != null) {
        consulta = consulta + ", numero_cuenta";
      }
      consulta = consulta + ") VALUES ('" + idDeudor + "', '" + asignadorDespacho(fila.getDespachoAsignado()) + "', '" + idGestor + "', '" + asignadorProducto(fila.getProducto()) + "', '" + Campanas.NUEVA_REMESA + "', '" + Marcajes.SIN_MARCAJE + "', '" + fila.getNumeroCredito() + "', '" + TipoCreditos.LINEA_TELEFONICA;
      if (fila.getSubproducto() != null) {
        consulta = consulta + "', '" + asignadorSubproducto(fila.getSubproducto());
      }
      if (fila.getFechaInicioCredito() != null) {
        consulta = consulta + "', '" + fila.getFechaInicioCredito();
      }
      if (fila.getFechaVencimientoCredito() != null) {
        consulta = consulta + "', '" + fila.getFechaVencimientoCredito();
      }
      if (fila.getFechaQuebranto() != null) {
        consulta = consulta + "', '" + fila.getFechaQuebranto();
      }
      if (fila.getMontoCredito() != null) {
        consulta = consulta + "', '" + fila.getMontoCredito();
      }
      if (fila.getSaldoInsoluto() != null) {
        consulta = consulta + "', '" + fila.getSaldoInsoluto();
      }
      if (fila.getMensualidad() != null) {
        consulta = consulta + "', '" + fila.getMensualidad();
      }
      consulta = consulta + "', '0";
      if (fila.getTasaInteres() != null) {
        consulta = consulta + "', '" + fila.getTasaInteres();
      }
      if (fila.getCuentaCredito() != null) {
        consulta = consulta + "', '" + fila.getCuentaCredito();
      }
      consulta = consulta + "');";
      EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
      if (!ejecutor.ejecutor(consulta, conn)) {
        Logs.log.error("Error en la fila del credito " + fila.getNumeroCredito() + ". Revertir la carga.");
        ok = false;
      } else {
        ok = true;
      }
    }
    return ok;
  }

  // ***************************************************************************
  // METODO QUE BUSCA EL DESPACHO AL QUE PERTENECEN LOS CREDITOS
  // ***************************************************************************
  public int asignadorDespacho(String despacho) {
    Despacho des = new DespachoIMPL().buscar(despacho);
    if (despacho != null) {
      return des.getIdDespacho();
    } else {
      return -1;
    }
  }

  // ***************************************************************************
  // METODO QUE BUSCA EL PRODUCTO DEL CREDITO
  // ***************************************************************************
  public int asignadorProducto(String producto) {
    producto = producto.replace("  ", " ");
    if (producto.endsWith(" ")) {
      String aux = producto.substring(0, producto.length() - 1);
      producto = aux;
    }
    Producto p = new ProductoIMPL().buscar(producto);
    if (p != null) {
      return p.getIdProducto();
    } else {
      return -1;
    }
  }

  // ***************************************************************************
  // METODO QUE BUSCA EL SUBPRODUCTO
  // ***************************************************************************
  public int asignadorSubproducto(String subproducto) {
    subproducto = subproducto.replace("  ", " ");
    if (subproducto.endsWith(" ")) {
      subproducto = subproducto.substring(0, subproducto.length() - 1);
    }
    Subproducto sub = new SubproductoIMPL().buscar(subproducto);
    if (sub != null) {
      return sub.getIdSubproducto();
    } else {
      return -1;
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR LAS ACTUALIZACIONES
  // ***************************************************************************
  public boolean insertarActualizacion(FilaCreditoExcel fila, int idCredito, Connection conn) {
    String consulta = "INSERT INTO actualizacion (id_actualizacion, id_credito, id_remesa, meses_vencidos, saldo_vencido, estatus";
    if (fila.getFechaUltimoPago() != null) {
      consulta = consulta + ", fecha_ultimo_pago";
    }
    if (fila.getFechaUltimoVencimientoPagado() != null) {
      consulta = consulta + ", fecha_ultimo_vencimiento_pagado";
    }
    consulta = consulta + ") VALUES ('" + idCredito + "', '" + idCredito + "', '" + new RemesaIMPL().buscarRemesaActual() + "', '" + fila.getMesesVencidos() + "', '" + fila.getSaldoVencido() + "', '" + fila.getEstatusCuenta();
    if (fila.getFechaUltimoPago() != null) {
      consulta = consulta + "', '" + fila.getFechaUltimoPago();
    }
    if (fila.getFechaUltimoVencimientoPagado() != null) {
      consulta = consulta + "', '" + fila.getFechaUltimoVencimientoPagado();
    }
    consulta = consulta + "');";
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    if (!ejecutor.ejecutor(consulta, conn)) {
      Logs.log.error("No se logro insertar la actualizacion para el credito " + fila.getNumeroCredito());
      return false;
    } else {
      return true;
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR LAS RESPUESTAS DE TELMEX
  // ***************************************************************************
  public void insertarFacs(List<Facs> facs, String numeroCredito, Connection conn) {
    String consulta;
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    for (int i = 0; i < (facs.size()); i++) {
      consulta = "INSERT INTO fac (id_actualizacion, mes, anio, fac_por, fac_mes) SELECT id_actualizacion, '" + facs.get(i).getMes() + "', '" + facs.get(i).getAnio() + "', '" + facs.get(i).getMonto() + "', '" + facs.get(i).getAnio() + "' FROM actualizacion WHERE id_credito = (SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "')";
      if (!ejecutor.ejecutor(consulta, conn)) {
        Logs.log.error("Error en la fila del credito " + numeroCredito + ". Revertir la carga.");
        return;
      }
    }
  }

  // ***************************************************************************
  // METODO QUE CREA LAS SENTENCIAS SQL PARA INSERTAR LOS AJUSTES DEL BANCO
  // ***************************************************************************
  public void insertarAjustes(List<Ajustes> ajustes, String numeroCredito, Connection conn) {
    EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
    String consulta;
    for (int i = 0; i < (ajustes.size()); i++) {
      consulta = "INSERT INTO ajuste (id_actualizacion, tipo, ajuste) SELECT id_actualizacion, '" + ajustes.get(i).getTipo() + "', '" + ajustes.get(i).getAjuste() + "' FROM actualizacion WHERE id_credito = (SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "')";
      if (!ejecutor.ejecutor(consulta, conn)) {
        Logs.log.error("Error en la fila del credito " + numeroCredito + ". Revertir la carga.");
        return;

      }
    }
  }

  // ***************************************************************************
  // CLASE COMPARADOR PARA ORDENAR CREDITOS POR SALDO VENCIDO
  // ***************************************************************************
  public class ComparadorSaldoVencido implements Comparator<FilaCreditoExcel> {

    @Override
    public int compare(FilaCreditoExcel fila1, FilaCreditoExcel fila2) {
      float v1 = new Float(fila1.getSaldoVencido());
      float v2 = new Float(fila2.getSaldoVencido());
      return Float.compare(v2, v1);
    }

  }

  // ***************************************************************************
// CLASE MIEMBRO QUE FUNCIONA PARA GUARDAR LAS ASIGNACIONES DE CADA GESTOR
// ***************************************************************************
  public static class AsignacionCreditoGestor {

    // VARIABLES DE CLASE
    private Gestor gestor;
    private int cuentasAsignadas;
    private int cuentasNuevas;
    private float totalAsignadas;
    private float totalNuevas;

    // CONSTRUCTOR
    public AsignacionCreditoGestor() {
    }

    // GETTERS & SETTERS
    public Gestor getGestor() {
      return gestor;
    }

    public void setGestor(Gestor gestor) {
      this.gestor = gestor;
    }

    public int getCuentasAsignadas() {
      return cuentasAsignadas;
    }

    public void setCuentasAsignadas(int cuentasAsignadas) {
      this.cuentasAsignadas = cuentasAsignadas;
    }

    public int getCuentasNuevas() {
      return cuentasNuevas;
    }

    public void setCuentasNuevas(int cuentasNuevas) {
      this.cuentasNuevas = cuentasNuevas;
    }

    public float getTotalAsignadas() {
      return totalAsignadas;
    }

    public void setTotalAsignadas(float totalAsignadas) {
      this.totalAsignadas = totalAsignadas;
    }

    public float getTotalNuevas() {
      return totalNuevas;
    }

    public void setTotalNuevas(float totalNuevas) {
      this.totalNuevas = totalNuevas;
    }

  }

  // ***************************************************************************
// CLASE MIEMBRO QUE GUARDA A QUE GESTOR SE LE ASIGNO CADA CREDITO
// ***************************************************************************
  public static class CreditoGestor {

    // VARIABLES DE CLASE
    private Gestor gestor;
    private String credito;

    // CONSTRUCTOR
    public CreditoGestor() {
    }

    //GETTERS & SETTERS
    public Gestor getGestor() {
      return gestor;
    }

    public void setGestor(Gestor gestor) {
      this.gestor = gestor;
    }

    public String getCredito() {
      return credito;
    }

    public void setCredito(String credito) {
      this.credito = credito;
    }

  }

}
