package dto;
// Generated 15/06/2015 12:32:14 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Remesas generated by hbm2java
 */
@Entity
@Table(name="remesas"
    ,catalog="sigerbd"
)
public class Remesas  implements java.io.Serializable {


     private Integer idRemesa;
     private Integer mesesVencidos;
     private Float saldoVencido;
     private String estatus;
     private Date fechaUltimoPago;
     private Set facses = new HashSet(0);
     private Set creditosRemesases = new HashSet(0);

    public Remesas() {
    }

    public Remesas(Integer mesesVencidos, Float saldoVencido, String estatus, Date fechaUltimoPago, Set facses, Set creditosRemesases) {
       this.mesesVencidos = mesesVencidos;
       this.saldoVencido = saldoVencido;
       this.estatus = estatus;
       this.fechaUltimoPago = fechaUltimoPago;
       this.facses = facses;
       this.creditosRemesases = creditosRemesases;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id_remesa", unique=true, nullable=false)
    public Integer getIdRemesa() {
        return this.idRemesa;
    }
    
    public void setIdRemesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }

    
    @Column(name="meses_vencidos")
    public Integer getMesesVencidos() {
        return this.mesesVencidos;
    }
    
    public void setMesesVencidos(Integer mesesVencidos) {
        this.mesesVencidos = mesesVencidos;
    }

    
    @Column(name="saldo_vencido", precision=12, scale=0)
    public Float getSaldoVencido() {
        return this.saldoVencido;
    }
    
    public void setSaldoVencido(Float saldoVencido) {
        this.saldoVencido = saldoVencido;
    }

    
    @Column(name="estatus", length=10)
    public String getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="fecha_ultimo_pago", length=10)
    public Date getFechaUltimoPago() {
        return this.fechaUltimoPago;
    }
    
    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="remesas")
    public Set getFacses() {
        return this.facses;
    }
    
    public void setFacses(Set facses) {
        this.facses = facses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="remesas")
    public Set getCreditosRemesases() {
        return this.creditosRemesases;
    }
    
    public void setCreditosRemesases(Set creditosRemesases) {
        this.creditosRemesases = creditosRemesases;
    }




}


