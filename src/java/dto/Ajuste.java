package dto;
// Generated 20/10/2016 11:15:34 AM by Hibernate Tools 4.3.1



/**
 * Ajuste generated by hbm2java
 */
public class Ajuste  implements java.io.Serializable {


     private Integer idAjuste;
     private Actualizacion actualizacion;
     private int tipo;
     private String ajuste;

    public Ajuste() {
    }

    public Ajuste(Actualizacion actualizacion, int tipo, String ajuste) {
       this.actualizacion = actualizacion;
       this.tipo = tipo;
       this.ajuste = ajuste;
    }
   
    public Integer getIdAjuste() {
        return this.idAjuste;
    }
    
    public void setIdAjuste(Integer idAjuste) {
        this.idAjuste = idAjuste;
    }
    public Actualizacion getActualizacion() {
        return this.actualizacion;
    }
    
    public void setActualizacion(Actualizacion actualizacion) {
        this.actualizacion = actualizacion;
    }
    public int getTipo() {
        return this.tipo;
    }
    
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public String getAjuste() {
        return this.ajuste;
    }
    
    public void setAjuste(String ajuste) {
        this.ajuste = ajuste;
    }




}


