package dto;
// Generated 2/12/2015 01:29:58 PM by Hibernate Tools 4.3.1



/**
 * CreditoRemesa generated by hbm2java
 */
public class CreditoRemesa  implements java.io.Serializable {


     private CreditoRemesaId id;
     private Credito credito;
     private Remesa remesa;

    public CreditoRemesa() {
    }

    public CreditoRemesa(CreditoRemesaId id, Credito credito, Remesa remesa) {
       this.id = id;
       this.credito = credito;
       this.remesa = remesa;
    }
   
    public CreditoRemesaId getId() {
        return this.id;
    }
    
    public void setId(CreditoRemesaId id) {
        this.id = id;
    }
    public Credito getCredito() {
        return this.credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    public Remesa getRemesa() {
        return this.remesa;
    }
    
    public void setRemesa(Remesa remesa) {
        this.remesa = remesa;
    }




}


