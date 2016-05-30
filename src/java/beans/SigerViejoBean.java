/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "sigerViejoBean")
@ViewScoped
public class SigerViejoBean implements Serializable {

  // VARIABLES DE CLASE
  // TO FIX:
  // PERMITIR CONEXIONES REMOTAS EN PABLITO'S SERVER
  private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  private final String DB_URL = "jdbc:mysql://10.0.0.52:3306/sigerweb";
  private final String USER = "root";
  private final String PASS = "root";
  private List<GestionSigerViejo> listaGestiones;
  private List<DireccionSigerViejo> listaDirecciones;

  // CONSTRUCTOR
  public SigerViejoBean() {
    listaGestiones = new ArrayList();
    listaDirecciones = new ArrayList();
  }

  // METODO QUE BUSCARA LAS GESTIONES ANTERIORES
  public void obtenerGestionesAnteriores(String credito) {
    Connection conn;
    Statement stmt;
    String consulta = "SELECT g.Gestion_Fecha, g.Nota_Importante, g.Gestion, t.Tipo_Gest_Desc FROM gestiones g JOIN cat_tipo_gestion t WHERE t.Tipo_Gest_Clv = g.Tipo_Gest_Clv AND g.Credito_SF_LT_NT_CT_CreditosII = '" + credito + "' ORDER BY g.Gestion_Fecha DESC;";
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(consulta);
      while (rs.next()) {
        GestionSigerViejo g = new GestionSigerViejo();
        g.setFechaGestion(rs.getDate("g.Gestion_Fecha"));
        String tipo = rs.getString("t.Tipo_Gest_Desc");
        String aux = rs.getString("g.Gestion") + ". " + rs.getString("g.Nota_Importante");
        if (tipo.contains("VISITA DOMICILIARIA")) {
          g.setGestion("VISITA DOMICILIARIA, " + aux);
        } else if (tipo.contains("TELEFONIA")) {
          g.setGestion("TELEF, " + aux);
        } else if (tipo.contains("LLAMADA ENTRANTE")) {
          g.setGestion("LLAMADA ENTRANTE, " + aux);
        } else if (tipo.contains("CORPORATIVO")) {
          g.setGestion(aux);
        } else {
          g.setGestion(aux);
        }
        listaGestiones.add(g);
      }
      rs.close();
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error(consulta);
      Logs.log.error(e);
    }
  }

  // METODO QUE BUSCARA LAS DIRECCIONES DEL SIGER VIEJO
  public void obtenerDireccionesAnteriores(String credito) {
    Connection conn;
    Statement stmt;
    String consulta = "SELECT DomDeu, ColDeu, Entdeu, EdoDeu, CodPos FROM direcciones WHERE Datos_primarios_Folio = (SELECT Datos_primarios_Folio FROM credito_sf_lt_nt_ct WHERE CreditosII = '" + credito + "');";
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(consulta);
      while (rs.next()) {
        DireccionSigerViejo d = new DireccionSigerViejo();
        d.setCalleNum("DomDeu");
        d.setColonia("ColDeu");
        d.setMunicipio("Entdeu");
        d.setEstado("EdoDeu");
        d.setCp("CodPos");
        listaDirecciones.add(d);
      }
      rs.close();
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error(consulta);
      Logs.log.error(e);
    }
  }

  // GETTERS & SETTERS
  public List<GestionSigerViejo> getListaGestiones() {
    return listaGestiones;
  }

  public void setListaGestiones(List<GestionSigerViejo> listaGestiones) {
    this.listaGestiones = listaGestiones;
  }

  public List<DireccionSigerViejo> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<DireccionSigerViejo> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  // CLASE MIEMBRO PARA OBTENER LAS GESTIONES ANTERIORES
  public static class GestionSigerViejo {

    // VARIABLES DE CLASE
    private Date fechaGestion;
    private String gestion;

    // CONSTRUCTOR
    public GestionSigerViejo() {
    }

    // GETTERS & SETTERS
    public Date getFechaGestion() {
      return fechaGestion;
    }

    public void setFechaGestion(Date fechaGestion) {
      this.fechaGestion = fechaGestion;
    }

    public String getGestion() {
      return gestion;
    }

    public void setGestion(String gestion) {
      this.gestion = gestion;
    }

  }

  // CLASE MIEMBRO PARA OBTENER LAS DIRECCIONES ANTERIORES
  public static class DireccionSigerViejo {

    // VARIABLES DE CLASE
    public String calleNum;
    public String colonia;
    public String municipio;
    public String estado;
    public String cp;

    // CONSTRUCTOR
    public DireccionSigerViejo() {
    }
    
    // GETTERS & SETTERS
    public String getCalleNum() {
      return calleNum;
    }

    public void setCalleNum(String calleNum) {
      this.calleNum = calleNum;
    }

    public String getColonia() {
      return colonia;
    }

    public void setColonia(String colonia) {
      this.colonia = colonia;
    }

    public String getMunicipio() {
      return municipio;
    }

    public void setMunicipio(String municipio) {
      this.municipio = municipio;
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public String getCp() {
      return cp;
    }

    public void setCp(String cp) {
      this.cp = cp;
    }
  }
}
