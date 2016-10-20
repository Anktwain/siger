/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.IndexBean;
import dao.CreditoDAO;
import dao.DeudorDAO;
import dto.Credito;
import dto.Deudor;
import dto.carga.FilaCreditoClasificado;
import dto.carga.FilaCreditoExcel;
import impl.CreditoIMPL;
import impl.DeudorIMPL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
// REESTRUCTURACION DEL MODULO DE CARGA
public class ComparadorCreditos {

  // ***************************************************************************
  // LLAMADA A OTROS BEANS
  // ***************************************************************************
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/sigerbd";
  private final String USER = "root";
  private final String PASS = "root";
  private final CreditoDAO creditoDao;
  private final DeudorDAO deudorDao;

  private List<Deudor> deudoresEnSistema;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public ComparadorCreditos() {
    deudoresEnSistema = new ArrayList();
    creditoDao = new CreditoIMPL();
    deudorDao = new DeudorIMPL();
  }

  // ***************************************************************************
  // METODO QUE COMPARA LOS CREDITOS Y VERIFICA QUE EXISTAN EN LA BASE DE DATOS
  // ***************************************************************************
  public FilaCreditoClasificado compararCreditos(List<FilaCreditoExcel> filas) {
    List<FilaCreditoExcel> reasignados = new ArrayList();
    List<FilaCreditoExcel> conservados = new ArrayList();
    List<FilaCreditoExcel> nuevosTotales = new ArrayList();
    List<FilaCreditoExcel> nuevosCreditos = new ArrayList();
    List<Credito> retirados = new ArrayList();
    FilaCreditoClasificado creditosClasificados = new FilaCreditoClasificado();
    // AQUI SE COMPARAN LOS CREDITOS EN BANDEJA DE DEVOLUCION CON LOS DE LA REMESA
    List<Credito> creditosEnDevolucion = creditoDao.buscarCreditosDevueltosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i < (creditosEnDevolucion.size()); i++) {
      for (int j = 0; j < (filas.size()); j++) {
        if (creditosEnDevolucion.get(i).getNumeroCredito().equals(filas.get(j).getNumeroCredito())) {
          reasignados.add(filas.get(j));
          creditosEnDevolucion.remove(i);
          filas.remove(j);
          break;
        }
      }
    }
    creditosClasificados.setReasignados(reasignados);
    // AQUI SE COMPARAN LOS CREDITOS ACTIVOS CON LOS DE LA REMESA
    List<Credito> creditosEnGestion = creditoDao.creditosEnGestionPorDespachoSinQuebrantos(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i < (creditosEnGestion.size()); i++) {
      for (int j = 0; j < (filas.size()); j++) {
        if (creditosEnGestion.get(i).getNumeroCredito().equals(filas.get(j).getNumeroCredito())) {
          conservados.add(filas.get(j));
          filas.remove(j);
          break;
        }
        //creditosEnGestion.remove(i);
      }
    }
    creditosClasificados.setConservados(conservados);
    // AQUI SE COMPARAN LOS CREDITOS CON LOS DEUDORES EXISTENTES PARA VER SI SE TRATA DE NUEVOS CREDITOS O DE NUEVOS TOTALES
    deudoresEnSistema = deudorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i < (filas.size()); i++) {
      if (verificarDeudorExistente(filas.get(i))) {
        nuevosCreditos.add(filas.get(i));
      } else {
        nuevosTotales.add(filas.get(i));
      }
    }
    creditosClasificados.setNuevosCreditos(nuevosCreditos);
    creditosClasificados.setNuevosTotales(nuevosTotales);
    // AQUI SE COMPARAN LOS CREDITOS QUE QUEDARAN COMO ACTIVOS CON LOS CREDITOS QUE ESTAN COMO ACTIVOS
    // ESTO CON LA FINALIDAD DE VER CUALES SON LOS CREDITOS RETIRADOS
    creditosClasificados.setCreditosEnGestion(creditosEnGestion.size());
    for (int i = 0; i < (creditosEnGestion.size()); i++) {
      for (int j = 0; j < (conservados.size()); j++) {
        if (creditosEnGestion.get(i).getNumeroCredito().equals(conservados.get(j).getNumeroCredito())) {
          creditosEnGestion.remove(i);
        }
      }
    }
    retirados.addAll(creditosEnGestion);
    creditosClasificados.setRetirados(retirados);
    return creditosClasificados;
  }

  // ***************************************************************************
  // METODO QUE CONFIRMA LA CARGA Y HACE OPERACIONES SOBRE LOS CREDITOS
  // SE IMPLEMENTA PARA EVITAR EL RETORNO DE LISTAS INNECESARIO
  // ***************************************************************************
  public void confirmarCarga(FilaCreditoClasificado creditosClasificados) {
    CreadorConsultasCargaSQL creador = new CreadorConsultasCargaSQL();
    if (!creditosClasificados.getReasignados().isEmpty()) {
      try {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
          Logs.log.info("Se reactivaran " + creditosClasificados.getReasignados().size() + " creditos.");
          creador.prepararReactivados(creditosClasificados.getReasignados(), conn);
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se pudo crear la conexion.");
        Logs.log.error(sqle.getMessage());
      }
    }
    if (!creditosClasificados.getRetirados().isEmpty()) {
      Logs.log.info("Se retiraran " + creditosClasificados.getRetirados().size() + " creditos.");
      creador.prepararRetirados(creditosClasificados.getRetirados());
    }
    if (!creditosClasificados.getConservados().isEmpty()) {
      try {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
          Logs.log.info("Se actualizaran " + creditosClasificados.getConservados().size() + " creditos.");
          creador.prepararConservados(creditosClasificados.getConservados(), conn);
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se pudo crear la conexion.");
        Logs.log.error(sqle.getMessage());
      }
    }
    if (!creditosClasificados.getNuevosCreditos().isEmpty()) {
      try {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
          Logs.log.info("Se cargaran al sistema " + creditosClasificados.getNuevosCreditos().size() + " nuevos creditos.");
          creador.prepararNuevosCreditos(creditosClasificados.getNuevosCreditos(), conn);
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se pudo crear la conexion.");
        Logs.log.error(sqle.getMessage());
      }
    }
    if (!creditosClasificados.getNuevosTotales().isEmpty()) {
      try {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
          Logs.log.info("Se cargaran al sistema " + creditosClasificados.getNuevosTotales().size() + " nuevos totales.");
          creador.prepararNuevosTotales(creditosClasificados.getNuevosTotales(), conn);
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se pudo crear la conexion.");
        Logs.log.error(sqle.getMessage());
      }
    }
    RequestContext.getCurrentInstance().execute("PF('dialogoReporteCarga').show()");
  }

  // ***************************************************************************
  // METODO QUE VERIFICA SI UN CREDITO PERTENECE A UN DEUDOR EXISTENTE
  // ***************************************************************************
  public boolean verificarDeudorExistente(FilaCreditoExcel fila) {
    boolean ok = false;
    for (int i = 0; i < (deudoresEnSistema.size()); i++) {
      if (deudoresEnSistema.get(i).getNumeroDeudor().equals(fila.getNumeroDeudor())) {
        ok = true;
        break;
      } else if ((deudoresEnSistema.get(i).getSujeto().getRfc() != null) && (deudoresEnSistema.get(i).getSujeto().getRfc().equals(fila.getRfc()))) {
        ok = true;
        break;
      }
    }
    return ok;
  }

}
