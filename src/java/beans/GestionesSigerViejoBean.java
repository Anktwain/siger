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
@ManagedBean(name = "gestionesSigerViejoBean")
@ViewScoped
public class GestionesSigerViejoBean implements Serializable{

  // VARIABLES DE CLASE
  private List<GestionSigerViejo> listaGestiones;
  
  // CONSTRUCTOR
  public GestionesSigerViejoBean() {
    listaGestiones = new ArrayList();
  }
  
  // METODO QUE BUSCARA LAS GESTIONES ANTERIORES
  public void obtenerGestionesAnteriores(String credito) {
    // TO FIX:
    // LA CONEXION A BINAH YA NO EXISTE
    // REEMPLAZARLA POR LA QUE VAYA A QUEDAR VIGENTE
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://10.0.0.52:3306/sigerweb";
    final String USER = "root";
    final String PASS = "root";
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
        if(tipo.contains("VISITA DOMICILIARIA")){
          g.setGestion("VISITA DOMICILIARIA, " + aux);
        }
        else if(tipo.contains("TELEFONIA")){
          g.setGestion("TELEF, " + aux);
        }else if(tipo.contains("LLAMADA ENTRANTE")){
          g.setGestion("LLAMADA ENTRANTE, " + aux);
        }else if(tipo.contains("CORPORATIVO")){
          g.setGestion(aux);
        }else{
          g.setGestion(aux);
        }
        listaGestiones.add(g);
      }
      rs.close();
    } catch (ClassNotFoundException cnfe) {
      Logs.log.error(consulta);
      Logs.log.error(cnfe);
    } catch (SQLException sqle) {
      Logs.log.error(consulta);
      Logs.log.error(sqle);
    }
  }
  
  // GETTERS & SETTERS
  public List<GestionSigerViejo> getListaGestiones() {
    return listaGestiones;
  }

  public void setListaGestiones(List<GestionSigerViejo> listaGestiones) {
    this.listaGestiones = listaGestiones;
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
}
