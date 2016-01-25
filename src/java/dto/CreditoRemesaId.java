package dto;
// Generated 25/01/2016 12:05:14 PM by Hibernate Tools 4.3.1



/**
 * CreditoRemesaId generated by hbm2java
 */
public class CreditoRemesaId  implements java.io.Serializable {


     private int idRemesa;
     private int idCredito;

    public CreditoRemesaId() {
    }

    public CreditoRemesaId(int idRemesa, int idCredito) {
       this.idRemesa = idRemesa;
       this.idCredito = idCredito;
    }
   
    public int getIdRemesa() {
        return this.idRemesa;
    }
    
    public void setIdRemesa(int idRemesa) {
        this.idRemesa = idRemesa;
    }
    public int getIdCredito() {
        return this.idCredito;
    }
    
    public void setIdCredito(int idCredito) {
        this.idCredito = idCredito;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CreditoRemesaId) ) return false;
		 CreditoRemesaId castOther = ( CreditoRemesaId ) other; 
         
		 return (this.getIdRemesa()==castOther.getIdRemesa())
 && (this.getIdCredito()==castOther.getIdCredito());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdRemesa();
         result = 37 * result + this.getIdCredito();
         return result;
   }   


}


