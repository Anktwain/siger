package dto;
// Generated 2/12/2015 01:29:58 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Gestion generated by hbm2java
 */
public class Gestion  implements java.io.Serializable {


     private Integer idGestion;
     private Credito credito;
     private EstatusInformativo estatusInformativo;
     private Usuario usuario;
     private Date fecha;
     private String tipoGestion;
     private String lugarGestion;
     private String asuntoGestion;
     private String descripcionGestion;
     private String tipoSujetoGestion;
     private String sujetoGestion;
     private String gestion;

    public Gestion() {
    }

    public Gestion(Credito credito, EstatusInformativo estatusInformativo, Usuario usuario, Date fecha, String tipoGestion, String lugarGestion, String asuntoGestion, String descripcionGestion, String tipoSujetoGestion, String sujetoGestion, String gestion) {
       this.credito = credito;
       this.estatusInformativo = estatusInformativo;
       this.usuario = usuario;
       this.fecha = fecha;
       this.tipoGestion = tipoGestion;
       this.lugarGestion = lugarGestion;
       this.asuntoGestion = asuntoGestion;
       this.descripcionGestion = descripcionGestion;
       this.tipoSujetoGestion = tipoSujetoGestion;
       this.sujetoGestion = sujetoGestion;
       this.gestion = gestion;
    }
   
    public Integer getIdGestion() {
        return this.idGestion;
    }
    
    public void setIdGestion(Integer idGestion) {
        this.idGestion = idGestion;
    }
    public Credito getCredito() {
        return this.credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    public EstatusInformativo getEstatusInformativo() {
        return this.estatusInformativo;
    }
    
    public void setEstatusInformativo(EstatusInformativo estatusInformativo) {
        this.estatusInformativo = estatusInformativo;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getTipoGestion() {
        return this.tipoGestion;
    }
    
    public void setTipoGestion(String tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public String getLugarGestion() {
        return this.lugarGestion;
    }
    
    public void setLugarGestion(String lugarGestion) {
        this.lugarGestion = lugarGestion;
    }
    public String getAsuntoGestion() {
        return this.asuntoGestion;
    }
    
    public void setAsuntoGestion(String asuntoGestion) {
        this.asuntoGestion = asuntoGestion;
    }
    public String getDescripcionGestion() {
        return this.descripcionGestion;
    }
    
    public void setDescripcionGestion(String descripcionGestion) {
        this.descripcionGestion = descripcionGestion;
    }
    public String getTipoSujetoGestion() {
        return this.tipoSujetoGestion;
    }
    
    public void setTipoSujetoGestion(String tipoSujetoGestion) {
        this.tipoSujetoGestion = tipoSujetoGestion;
    }
    public String getSujetoGestion() {
        return this.sujetoGestion;
    }
    
    public void setSujetoGestion(String sujetoGestion) {
        this.sujetoGestion = sujetoGestion;
    }
    public String getGestion() {
        return this.gestion;
    }
    
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }




}


