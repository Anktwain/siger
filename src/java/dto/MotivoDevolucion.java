package dto;
// Generated 29/03/2016 04:35:49 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * MotivoDevolucion generated by hbm2java
 */
public class MotivoDevolucion  implements java.io.Serializable {


     private Integer idMotivoDevolucion;
     private ConceptoDevolucion conceptoDevolucion;
     private String motivo;
     private String descripcion;
     private Set devolucions = new HashSet(0);

    public MotivoDevolucion() {
    }

	
    public MotivoDevolucion(ConceptoDevolucion conceptoDevolucion, String motivo, String descripcion) {
        this.conceptoDevolucion = conceptoDevolucion;
        this.motivo = motivo;
        this.descripcion = descripcion;
    }
    public MotivoDevolucion(ConceptoDevolucion conceptoDevolucion, String motivo, String descripcion, Set devolucions) {
       this.conceptoDevolucion = conceptoDevolucion;
       this.motivo = motivo;
       this.descripcion = descripcion;
       this.devolucions = devolucions;
    }
   
    public Integer getIdMotivoDevolucion() {
        return this.idMotivoDevolucion;
    }
    
    public void setIdMotivoDevolucion(Integer idMotivoDevolucion) {
        this.idMotivoDevolucion = idMotivoDevolucion;
    }
    public ConceptoDevolucion getConceptoDevolucion() {
        return this.conceptoDevolucion;
    }
    
    public void setConceptoDevolucion(ConceptoDevolucion conceptoDevolucion) {
        this.conceptoDevolucion = conceptoDevolucion;
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
    public Set getDevolucions() {
        return this.devolucions;
    }
    
    public void setDevolucions(Set devolucions) {
        this.devolucions = devolucions;
    }




}


