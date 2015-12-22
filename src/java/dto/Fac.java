package dto;
// Generated 11/12/2015 11:59:28 AM by Hibernate Tools 4.3.1



/**
 * Fac generated by hbm2java
 */
public class Fac  implements java.io.Serializable {


     private Integer idFac;
     private Remesa remesa;
     private Integer mes;
     private Integer anio;
     private Float facPor;
     private String facMes;

    public Fac() {
    }

	
    public Fac(Remesa remesa) {
        this.remesa = remesa;
    }
    public Fac(Remesa remesa, Integer mes, Integer anio, Float facPor, String facMes) {
       this.remesa = remesa;
       this.mes = mes;
       this.anio = anio;
       this.facPor = facPor;
       this.facMes = facMes;
    }
   
    public Integer getIdFac() {
        return this.idFac;
    }
    
    public void setIdFac(Integer idFac) {
        this.idFac = idFac;
    }
    public Remesa getRemesa() {
        return this.remesa;
    }
    
    public void setRemesa(Remesa remesa) {
        this.remesa = remesa;
    }
    public Integer getMes() {
        return this.mes;
    }
    
    public void setMes(Integer mes) {
        this.mes = mes;
    }
    public Integer getAnio() {
        return this.anio;
    }
    
    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    public Float getFacPor() {
        return this.facPor;
    }
    
    public void setFacPor(Float facPor) {
        this.facPor = facPor;
    }
    public String getFacMes() {
        return this.facMes;
    }
    
    public void setFacMes(String facMes) {
        this.facMes = facMes;
    }




}


