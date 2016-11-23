package dto;
// Generated 20/10/2016 11:15:34 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DireccionTexto generated by hbm2java
 */
public class DireccionTexto  implements java.io.Serializable {


     private Integer idDireccionTexto;
     private Date fecha;
     private String numeroCredito;
     private String calle;
     private String exterior;
     private String interior;
     private String colonia;
     private String municipio;
     private String estado;
     private String codigoPostal;
     private int validada;

    public DireccionTexto() {
    }

	
    public DireccionTexto(Date fecha, String numeroCredito, String calle, String colonia, String municipio, String estado, String codigoPostal, int validada) {
        this.fecha = fecha;
        this.numeroCredito = numeroCredito;
        this.calle = calle;
        this.colonia = colonia;
        this.municipio = municipio;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
    }
    public DireccionTexto(Date fecha, String numeroCredito, String calle, String exterior, String interior, String colonia, String municipio, String estado, String codigoPostal, int validada) {
       this.fecha = fecha;
       this.numeroCredito = numeroCredito;
       this.calle = calle;
       this.exterior = exterior;
       this.interior = interior;
       this.colonia = colonia;
       this.municipio = municipio;
       this.estado = estado;
       this.codigoPostal = codigoPostal;
    }
   
    public Integer getIdDireccionTexto() {
        return this.idDireccionTexto;
    }
    
    public void setIdDireccionTexto(Integer idDireccionTexto) {
        this.idDireccionTexto = idDireccionTexto;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getNumeroCredito() {
        return this.numeroCredito;
    }
    
    public void setNumeroCredito(String numeroCredito) {
        this.numeroCredito = numeroCredito;
    }
    public String getCalle() {
        return this.calle;
    }
    
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public String getExterior() {
        return this.exterior;
    }
    
    public void setExterior(String exterior) {
        this.exterior = exterior;
    }
    public String getInterior() {
        return this.interior;
    }
    
    public void setInterior(String interior) {
        this.interior = interior;
    }
    public String getColonia() {
        return this.colonia;
    }
    
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getMunicipio() {
        return this.municipio;
    }
    
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCodigoPostal() {
        return this.codigoPostal;
    }
    
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    public int getValidada() {
        return validada;
    }

    public void setValidada(int validada) {
        this.validada = validada;
    }




}


