package dto;
// Generated 4/11/2015 10:38:46 AM by Hibernate Tools 4.3.1



/**
 * MotivoDevolucion generated by hbm2java
 */
public class MotivoDevolucion  implements java.io.Serializable {


     private Integer idMotivoDevolucion;
     private String motivo;
     private String descripcion;
     private int idConceptoDevolucion;

    public MotivoDevolucion() {
    }

    public MotivoDevolucion(String motivo, String descripcion, int idConceptoDevolucion) {
       this.motivo = motivo;
       this.descripcion = descripcion;
       this.idConceptoDevolucion = idConceptoDevolucion;
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
    public int getIdConceptoDevolucion() {
        return this.idConceptoDevolucion;
    }
    
    public void setIdConceptoDevolucion(int idConceptoDevolucion) {
        this.idConceptoDevolucion = idConceptoDevolucion;
    }

  @Override
  public String toString() {
    return motivo;
  }

}


