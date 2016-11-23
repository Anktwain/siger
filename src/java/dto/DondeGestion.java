package dto;
// Generated 20/10/2016 11:15:34 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * DondeGestion generated by hbm2java
 */
public class DondeGestion  implements java.io.Serializable {


     private Integer idDondeGestion;
     private TipoGestion tipoGestion;
     private String nombre;
     private Set gestions = new HashSet(0);

    public DondeGestion() {
    }

	
    public DondeGestion(TipoGestion tipoGestion, String nombre) {
        this.tipoGestion = tipoGestion;
        this.nombre = nombre;
    }
    public DondeGestion(TipoGestion tipoGestion, String nombre, Set gestions) {
       this.tipoGestion = tipoGestion;
       this.nombre = nombre;
       this.gestions = gestions;
    }
   
    public Integer getIdDondeGestion() {
        return this.idDondeGestion;
    }
    
    public void setIdDondeGestion(Integer idDondeGestion) {
        this.idDondeGestion = idDondeGestion;
    }
    public TipoGestion getTipoGestion() {
        return this.tipoGestion;
    }
    
    public void setTipoGestion(TipoGestion tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set getGestions() {
        return this.gestions;
    }
    
    public void setGestions(Set gestions) {
        this.gestions = gestions;
    }




}


