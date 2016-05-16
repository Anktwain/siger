package dto;
// Generated 11/05/2016 10:38:30 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * EstadoRepublica generated by hbm2java
 */
public class EstadoRepublica  implements java.io.Serializable {


     private Integer idEstado;
     private String nombre;
     private String abreviatura;
     private Set municipios = new HashSet(0);
     private Set regions = new HashSet(0);
     private Set direccions = new HashSet(0);

    public EstadoRepublica() {
    }

	
    public EstadoRepublica(String nombre) {
        this.nombre = nombre;
    }
    public EstadoRepublica(String nombre, String abreviatura, Set municipios, Set regions, Set direccions) {
       this.nombre = nombre;
       this.abreviatura = abreviatura;
       this.municipios = municipios;
       this.regions = regions;
       this.direccions = direccions;
    }
   
    public Integer getIdEstado() {
        return this.idEstado;
    }
    
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getAbreviatura() {
        return this.abreviatura;
    }
    
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    public Set getMunicipios() {
        return this.municipios;
    }
    
    public void setMunicipios(Set municipios) {
        this.municipios = municipios;
    }
    public Set getRegions() {
        return this.regions;
    }
    
    public void setRegions(Set regions) {
        this.regions = regions;
    }
    public Set getDireccions() {
        return this.direccions;
    }
    
    public void setDireccions(Set direccions) {
        this.direccions = direccions;
    }




}


