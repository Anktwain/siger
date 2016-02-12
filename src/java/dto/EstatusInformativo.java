package dto;
// Generated 9/02/2016 02:44:04 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * EstatusInformativo generated by hbm2java
 */
public class EstatusInformativo  implements java.io.Serializable {


     private Integer idEstatusInformativo;
     private String estatus;
     private Set gestions = new HashSet(0);

    public EstatusInformativo() {
    }

	
    public EstatusInformativo(String estatus) {
        this.estatus = estatus;
    }
    public EstatusInformativo(String estatus, Set gestions) {
       this.estatus = estatus;
       this.gestions = gestions;
    }
   
    public Integer getIdEstatusInformativo() {
        return this.idEstatusInformativo;
    }
    
    public void setIdEstatusInformativo(Integer idEstatusInformativo) {
        this.idEstatusInformativo = idEstatusInformativo;
    }
    public String getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    public Set getGestions() {
        return this.gestions;
    }
    
    public void setGestions(Set gestions) {
        this.gestions = gestions;
    }




}


