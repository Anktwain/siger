package dto;
// Generated 9/02/2016 02:44:04 PM by Hibernate Tools 4.3.1



/**
 * Auto generated by hbm2java
 */
public class Auto  implements java.io.Serializable {


     private int idAuto;
     private Credito credito;
     private String clave;
     private String marca;
     private String modelo;
     private String color;
     private Float valor;

    public Auto() {
    }

	
    public Auto(int idAuto, Credito credito) {
        this.idAuto = idAuto;
        this.credito = credito;
    }
    public Auto(int idAuto, Credito credito, String clave, String marca, String modelo, String color, Float valor) {
       this.idAuto = idAuto;
       this.credito = credito;
       this.clave = clave;
       this.marca = marca;
       this.modelo = modelo;
       this.color = color;
       this.valor = valor;
    }
   
    public int getIdAuto() {
        return this.idAuto;
    }
    
    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }
    public Credito getCredito() {
        return this.credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getMarca() {
        return this.marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return this.modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    public Float getValor() {
        return this.valor;
    }
    
    public void setValor(Float valor) {
        this.valor = valor;
    }




}


