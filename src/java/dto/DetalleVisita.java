package dto;
// Generated 20/10/2016 11:15:34 AM by Hibernate Tools 4.3.1



/**
 * DetalleVisita generated by hbm2java
 */
public class DetalleVisita  implements java.io.Serializable {


     private Integer idDetalleVisita;
     private BloqueVisita bloqueVisita;
     private Impresion impresion;

    public DetalleVisita() {
    }

    public DetalleVisita(BloqueVisita bloqueVisita, Impresion impresion) {
       this.bloqueVisita = bloqueVisita;
       this.impresion = impresion;
    }
   
    public Integer getIdDetalleVisita() {
        return this.idDetalleVisita;
    }
    
    public void setIdDetalleVisita(Integer idDetalleVisita) {
        this.idDetalleVisita = idDetalleVisita;
    }
    public BloqueVisita getBloqueVisita() {
        return this.bloqueVisita;
    }
    
    public void setBloqueVisita(BloqueVisita bloqueVisita) {
        this.bloqueVisita = bloqueVisita;
    }
    public Impresion getImpresion() {
        return this.impresion;
    }
    
    public void setImpresion(Impresion impresion) {
        this.impresion = impresion;
    }




}


