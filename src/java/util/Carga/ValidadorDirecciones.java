/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Carga;

import beans.IndexBean;
import beans.ValidarDireccionesBean;
import dao.ColoniaDAO;
import dto.Colonia;
import dto.Credito;
import dto.carga.DireccionPorValidar;
import dto.carga.FilaDireccionExcel;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import util.GestionAutomatica;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ValidadorDirecciones {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  ValidarDireccionesBean validarDireccionesBean = (ValidarDireccionesBean) elContext.getELResolver().getValue(elContext, null, "validarDireccionesBean");

  // VARIABLES DE CLASE
  private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/sigerbd";
  private final String USER = "root";
  private final String PASS = "root";
  private final ColoniaDAO coloniaDao;

  // CONSTRUCTOR
  public ValidadorDirecciones() {
    coloniaDao = new ColoniaIMPL();
  }

  // METODO QUE INTENTA VALIDAR AUTOMATICAMENTE LAS DIRECCIONES
  public List<DireccionPorValidar> validacionAutomatica(List<FilaDireccionExcel> direcciones) {
    int contador = 0;
    for (int i = 0; i < (direcciones.size()); i++) {
      List<Colonia> listaColonias = coloniaDao.buscarPorCodigoPostal(direcciones.get(i).getCp());
      if (!listaColonias.isEmpty()) {
        for (int j = 0; j < (listaColonias.size()); j++) {
          String compuesta = listaColonias.get(j).getTipo().toLowerCase() + " " + listaColonias.get(j).getNombre().toLowerCase();
          if (listaColonias.get(j).getNombre().toLowerCase().compareTo(direcciones.get(i).getColonia().toLowerCase()) == 0) {
            if (insertarDireccion(direcciones.get(i), listaColonias.get(j))) {
              if (generarGestionAutomatica(direcciones.get(i).getNumeroCredito())) {
                contador++;
                direcciones.remove(i);
                break;
              } else {
                Logs.log.error("No se pudo insertar la gestion automatica.");
                break;
              }
            } else {
              Logs.log.error("No se pudo insertar la direccion validada.");
              break;
            }
          }
          if (compuesta.compareTo(direcciones.get(i).getColonia().toLowerCase()) == 0) {
            if (insertarDireccion(direcciones.get(i), listaColonias.get(j))) {
              if (generarGestionAutomatica(direcciones.get(i).getNumeroCredito())) {
                contador++;
                direcciones.remove(i);
                break;
              } else {
                Logs.log.error("No se pudo insertar la gestion automatica.");
                break;
              }
            } else {
              Logs.log.error("No se pudo insertar la direccion validada.");
              break;
            }
          }
        }
      }
    }
    List<DireccionPorValidar> noValidadas = new ArrayList();
    for (int i = 0; i < (direcciones.size()); i++) {
      DireccionPorValidar d = new DireccionPorValidar();
      d.setId(i);
      d.setNumeroCredito(direcciones.get(i).getNumeroCredito());
      d.setCalle(direcciones.get(i).getCalle());
      d.setExterior(direcciones.get(i).getExterior());
      d.setInterior(direcciones.get(i).getInterior());
      d.setColonia(direcciones.get(i).getColonia());
      d.setMunicipio(direcciones.get(i).getMunicipio());
      d.setEstado(direcciones.get(i).getEstado());
      d.setCp(direcciones.get(i).getCp());
      noValidadas.add(d);
    }
    Logs.log.info("Se validaron automaticamente " + contador + " direcciones.");
    validarDireccionesBean.setDireccionesValidadas("Se validaron automaticamente " + contador + " direcciones.");
    return noValidadas;
  }

  // METODO QUE INSERTA LA DIRECCION EN LA BASE DE DATOS
  public boolean insertarDireccion(FilaDireccionExcel f, Colonia col) {
    String consulta = "INSERT INTO direccion (id_sujeto, id_municipio, id_estado, id_colonia, calle, exterior";
    if (f.getInterior() != null) {
      consulta = consulta + ", interior";
    }
    consulta = consulta + ", latitud, longitud) SELECT id_sujeto, '" + col.getMunicipio().getIdMunicipio() + "', '" + col.getMunicipio().getEstadoRepublica().getIdEstado() + "', '" + col.getIdColonia() + "', '" + f.getCalle() + "', '" + f.getExterior();
    if (f.getInterior() != null) {
      consulta = consulta + "', '" + f.getInterior();
    }
    consulta = consulta + "', '0.000000', '0.000000' FROM deudor WHERE id_deudor = (SELECT id_deudor FROM credito WHERE numero_credito = '" + f.getNumeroCredito() + "')";
    try {
      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        EjecutorCargaSQL ejecutor = new EjecutorCargaSQL();
        return ejecutor.ejecutor(consulta, conn);
      }
    } catch (SQLException sqle) {
      Logs.log.error("No se pudo crear la conexion.");
      Logs.log.error(sqle.getMessage());
      return false;
    }
  }

  // METODO QUE GENERA LA GESTION AUTOMATICA DE VALIDACION DE DIRECCIONES
  public boolean generarGestionAutomatica(String numeroCredito) {
    GestionAutomatica ga = new GestionAutomatica();
    Credito c = new CreditoIMPL().buscar(numeroCredito);
    if (c != null) {
      return ga.generarGestionAutomatica("4DOMI", c, indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL DEUDOR " + c.getDeudor().getSujeto().getNombreRazonSocial());
    } else {
      Logs.log.info("No se encontro el credito " + numeroCredito);
      return false;
    }
  }

}
