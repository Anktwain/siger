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
  private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  private final String DB_URL = "jdbc:mysql://10.0.0.52:3306/sigerweb";
  private final String USER = "root";
  private final String PASS = "root";
  private List<GestionSigerViejo> listaGestiones;
  private List<DireccionSigerViejo> listaDirecciones;
  private List<TelefonoSigerViejo> listaTelefonos;
  private List<ContactoSigerViejo> listaContactos;

  // CONSTRUCTOR
  public SigerViejoBean() {
    listaGestiones = new ArrayList();
    listaDirecciones = new ArrayList();
  }

  // METODO QUE BUSCARA LAS GESTIONES ANTERIORES
  public void obtenerGestionesAnteriores(String credito) {
    listaGestiones = new ArrayList();
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
    listaDirecciones = new ArrayList();
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
        d.setCalleNum(rs.getString("DomDeu"));
        d.setColonia(rs.getString("ColDeu"));
        d.setMunicipio(rs.getString("Entdeu"));
        d.setEstado(rs.getString("EdoDeu"));
        d.setCp(rs.getString("CodPos"));
        listaDirecciones.add(d);
      }
      rs.close();
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error(consulta);
      Logs.log.error(e);
    }
  }

  // METODO QUE BUSCARA LOS DATOS DE CONTACTO ANTERIORES
  public void obtenerDatosContactoAnteriores(String credito) {
    obtenerTelefonosAnteriores(credito);
    obtenerContactosAnteriores(credito);
  }

  // METODO QUE BUSCARA LOS TELEFONOS DEL SIGER VIEJO
  public void obtenerTelefonosAnteriores(String credito) {
    listaTelefonos = new ArrayList();
    Connection conn;
    Statement stmt;
    String consulta = "SELECT Numero, Lugar, Tipo, Extencion, Lada, Horario FROM telefonos WHERE Datos_primarios_Folio = (SELECT Datos_primarios_Folio FROM credito_sf_lt_nt_ct WHERE CreditosII = '" + credito + "');";
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(consulta);
      while (rs.next()) {
        TelefonoSigerViejo t = new TelefonoSigerViejo();
        t.setNumero(rs.getString("Numero"));
        t.setLugar(rs.getString("Lugar"));
        t.setTipo(rs.getString("Tipo"));
        t.setExtension(rs.getString("Extencion"));
        t.setLada(rs.getString("Lada"));
        t.setHorario(rs.getString("Horario"));
        listaTelefonos.add(t);
      }
      rs.close();
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error(consulta);
      Logs.log.error(e);
    }
  }

  // METODO QUE BUSCARA LOS CORREOS DEL SIGER VIEJO
  public void obtenerContactosAnteriores(String credito) {
    listaContactos = new ArrayList();
    Connection conn;
    Statement stmt;
    String consulta = "SELECT NomCont, Observaciones, TipoDeContacto FROM contactos WHERE Datos_primarios_Folio = (SELECT Datos_primarios_Folio FROM credito_sf_lt_nt_ct WHERE CreditosII = '" + credito + "');";
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(consulta);
      while (rs.next()) {
        ContactoSigerViejo c = new ContactoSigerViejo();
        c.setNombre(rs.getString("NomCont"));
        c.setObservaciones(rs.getString("Observaciones"));
        c.setTipo(rs.getString("TipoDeContacto"));
        listaContactos.add(c);
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

  public List<TelefonoSigerViejo> getListaTelefonos() {
    return listaTelefonos;
  }

  public void setListaTelefonos(List<TelefonoSigerViejo> listaTelefonos) {
    this.listaTelefonos = listaTelefonos;
  }

  public List<ContactoSigerViejo> getListaContactos() {
    return listaContactos;
  }

  public void setListaContactos(List<ContactoSigerViejo> listaContactos) {
    this.listaContactos = listaContactos;
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

  // CLASE MIEMBRO PARA OBTENER LOS TELEFONOS ANTERIORES
  public static class TelefonoSigerViejo {

    // VARIABLES DE CLASE
    private String numero;
    private String lugar;
    private String tipo;
    private String extension;
    private String lada;
    private String horario;

    // CONSTRUCTOR
    public TelefonoSigerViejo() {
    }

    // GETTERS & SETTERS
    public String getNumero() {
      return numero;
    }

    public void setNumero(String numero) {
      this.numero = numero;
    }

    public String getLugar() {
      return lugar;
    }

    public void setLugar(String lugar) {
      this.lugar = lugar;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public String getExtension() {
      return extension;
    }

    public void setExtension(String extension) {
      this.extension = extension;
    }

    public String getLada() {
      return lada;
    }

    public void setLada(String lada) {
      this.lada = lada;
    }

    public String getHorario() {
      return horario;
    }

    public void setHorario(String horario) {
      this.horario = horario;
    }

  }

  // CLASE MIEMBRO PARA OBTENER LOS CONTACTOS ANTERIORES
  public static class ContactoSigerViejo {

    // VARIABLES DE CLASE
    private String nombre;
    private String tipo;
    private String observaciones;

    // CONSTRUCTOR
    public ContactoSigerViejo() {
    }

    // GETTERS & SETTERS
    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public String getObservaciones() {
      return observaciones;
    }

    public void setObservaciones(String observaciones) {
      this.observaciones = observaciones;
    }
    
  }

}
