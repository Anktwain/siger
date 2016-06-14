package dto;
// Generated 14/06/2016 11:17:20 AM by Hibernate Tools 4.3.1



/**
 * Telefono generated by hbm2java
 */
public class Telefono  implements java.io.Serializable {


     private Integer idTelefono;
     private Sujeto sujeto;
     private String numero;
     private String tipo;
     private String extension;
     private String lada;
     private String horario;
     private Integer principal;

    public Telefono() {
    }

	
    public Telefono(Sujeto sujeto, String numero) {
        this.sujeto = sujeto;
        this.numero = numero;
    }
    public Telefono(Sujeto sujeto, String numero, String tipo, String extension, String lada, String horario, Integer principal) {
       this.sujeto = sujeto;
       this.numero = numero;
       this.tipo = tipo;
       this.extension = extension;
       this.lada = lada;
       this.horario = horario;
       this.principal = principal;
    }
   
    public Integer getIdTelefono() {
        return this.idTelefono;
    }
    
    public void setIdTelefono(Integer idTelefono) {
        this.idTelefono = idTelefono;
    }
    public Sujeto getSujeto() {
        return this.sujeto;
    }
    
    public void setSujeto(Sujeto sujeto) {
        this.sujeto = sujeto;
    }
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getLada() {
        return this.lada;
    }
    
    public void setLada(String lada) {
        this.lada = lada;
    }
    public String getHorario() {
        return this.horario;
    }
    
    public void setHorario(String horario) {
        this.horario = horario;
    }
    public Integer getPrincipal() {
        return this.principal;
    }
    
    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }




}


