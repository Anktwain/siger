package dto;
// Generated 26/10/2015 11:55:09 AM by Hibernate Tools 4.3.1



/**
 * MotivoDevolucion generated by hbm2java
 */
public class MotivoDevolucion  implements java.io.Serializable {


     private Integer idMotivoDevolucion;
     private String motivo;
     private String descripcion;

    public MotivoDevolucion() {
    }

    public MotivoDevolucion(String motivo, String descripcion) {
       this.motivo = motivo;
       this.descripcion = descripcion;
    }
   
    public Integer getIdMotivoDevolucion() {
        return this.idMotivoDevolucion;
    }
    
    public void setIdMotivoDevolucion(Integer idMotivoDevolucion) {
        this.idMotivoDevolucion = idMotivoDevolucion;
    }
    public String getMotivo() {
        return this.motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}


