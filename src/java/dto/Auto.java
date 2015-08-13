package dto;
// Generated 7/08/2015 09:51:09 AM by Hibernate Tools 4.3.1



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

  /**
   *
   */
  public Auto() {
    }

  /**
   *
   * @param idAuto
   * @param credito
   */
  public Auto(int idAuto, Credito credito) {
        this.idAuto = idAuto;
        this.credito = credito;
    }

  /**
   *
   * @param idAuto
   * @param credito
   * @param clave
   * @param marca
   * @param modelo
   * @param color
   * @param valor
   */
  public Auto(int idAuto, Credito credito, String clave, String marca, String modelo, String color, Float valor) {
       this.idAuto = idAuto;
       this.credito = credito;
       this.clave = clave;
       this.marca = marca;
       this.modelo = modelo;
       this.color = color;
       this.valor = valor;
    }
   
  /**
   *
   * @return
   */
  public int getIdAuto() {
        return this.idAuto;
    }
    
  /**
   *
   * @param idAuto
   */
  public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

  /**
   *
   * @return
   */
  public Credito getCredito() {
        return this.credito;
    }
    
  /**
   *
   * @param credito
   */
  public void setCredito(Credito credito) {
        this.credito = credito;
    }

  /**
   *
   * @return
   */
  public String getClave() {
        return this.clave;
    }
    
  /**
   *
   * @param clave
   */
  public void setClave(String clave) {
        this.clave = clave;
    }

  /**
   *
   * @return
   */
  public String getMarca() {
        return this.marca;
    }
    
  /**
   *
   * @param marca
   */
  public void setMarca(String marca) {
        this.marca = marca;
    }

  /**
   *
   * @return
   */
  public String getModelo() {
        return this.modelo;
    }
    
  /**
   *
   * @param modelo
   */
  public void setModelo(String modelo) {
        this.modelo = modelo;
    }

  /**
   *
   * @return
   */
  public String getColor() {
        return this.color;
    }
    
  /**
   *
   * @param color
   */
  public void setColor(String color) {
        this.color = color;
    }

  /**
   *
   * @return
   */
  public Float getValor() {
        return this.valor;
    }
    
  /**
   *
   * @param valor
   */
  public void setValor(Float valor) {
        this.valor = valor;
    }




}


