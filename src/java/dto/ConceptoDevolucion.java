package dto;
// Generated 4/02/2016 12:05:50 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * ConceptoDevolucion generated by hbm2java
 */
public class ConceptoDevolucion  implements java.io.Serializable {


     private Integer idConceptoDevolucion;
     private String concepto;
     private Set devolucions = new HashSet(0);
     private Set motivoDevolucions = new HashSet(0);

    public ConceptoDevolucion() {
    }

	
    public ConceptoDevolucion(String concepto) {
        this.concepto = concepto;
    }
    public ConceptoDevolucion(String concepto, Set devolucions, Set motivoDevolucions) {
       this.concepto = concepto;
       this.devolucions = devolucions;
       this.motivoDevolucions = motivoDevolucions;
    }
   
    public Integer getIdConceptoDevolucion() {
        return this.idConceptoDevolucion;
    }
    
    public void setIdConceptoDevolucion(Integer idConceptoDevolucion) {
        this.idConceptoDevolucion = idConceptoDevolucion;
    }
    public String getConcepto() {
        return this.concepto;
    }
    
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    public Set getDevolucions() {
        return this.devolucions;
    }
    
    public void setDevolucions(Set devolucions) {
        this.devolucions = devolucions;
    }
    public Set getMotivoDevolucions() {
        return this.motivoDevolucions;
    }
    
    public void setMotivoDevolucions(Set motivoDevolucions) {
        this.motivoDevolucions = motivoDevolucions;
    }




}


