package dto;
// Generated 25/01/2016 12:05:14 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Municipio generated by hbm2java
 */
public class Municipio  implements java.io.Serializable {


     private Integer idMunicipio;
     private EstadoRepublica estadoRepublica;
     private String nombre;
     private Set direccions = new HashSet(0);
     private Set colonias = new HashSet(0);
     private Set regions = new HashSet(0);

    public Municipio() {
    }

	
    public Municipio(EstadoRepublica estadoRepublica, String nombre) {
        this.estadoRepublica = estadoRepublica;
        this.nombre = nombre;
    }
    public Municipio(EstadoRepublica estadoRepublica, String nombre, Set direccions, Set colonias, Set regions) {
       this.estadoRepublica = estadoRepublica;
       this.nombre = nombre;
       this.direccions = direccions;
       this.colonias = colonias;
       this.regions = regions;
    }
   
    public Integer getIdMunicipio() {
        return this.idMunicipio;
    }
    
    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
    public EstadoRepublica getEstadoRepublica() {
        return this.estadoRepublica;
    }
    
    public void setEstadoRepublica(EstadoRepublica estadoRepublica) {
        this.estadoRepublica = estadoRepublica;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set getDireccions() {
        return this.direccions;
    }
    
    public void setDireccions(Set direccions) {
        this.direccions = direccions;
    }
    public Set getColonias() {
        return this.colonias;
    }
    
    public void setColonias(Set colonias) {
        this.colonias = colonias;
    }
    public Set getRegions() {
        return this.regions;
    }
    
    public void setRegions(Set regions) {
        this.regions = regions;
    }




}


