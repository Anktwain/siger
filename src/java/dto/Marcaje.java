package dto;
// Generated 9/03/2016 10:01:38 AM by Hibernate Tools 4.3.1



/**
 * Marcaje generated by hbm2java
 */
public class Marcaje  implements java.io.Serializable {


     private Integer idMarcaje;
     private String marcaje;
     private String descripcion;

    public Marcaje() {
    }

	
    public Marcaje(String marcaje) {
        this.marcaje = marcaje;
    }
    public Marcaje(String marcaje, String descripcion) {
       this.marcaje = marcaje;
       this.descripcion = descripcion;
    }
   
    public Integer getIdMarcaje() {
        return this.idMarcaje;
    }
    
    public void setIdMarcaje(Integer idMarcaje) {
        this.idMarcaje = idMarcaje;
    }
    public String getMarcaje() {
        return this.marcaje;
    }
    
    public void setMarcaje(String marcaje) {
        this.marcaje = marcaje;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}


