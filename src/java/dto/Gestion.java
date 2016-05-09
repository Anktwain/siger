package dto;
// Generated 27/04/2016 09:32:53 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Gestion generated by hbm2java
 */
public class Gestion  implements java.io.Serializable {


     private Integer idGestion;
     private AsuntoGestion asuntoGestion;
     private Credito credito;
     private DescripcionGestion descripcionGestion;
     private DondeGestion dondeGestion;
     private EstatusInformativo estatusInformativo;
     private QuienGestion quienGestion;
     private TipoGestion tipoGestion;
     private TipoQuienGestion tipoQuienGestion;
     private Usuario usuario;
     private Date fecha;
     private String gestion;

    public Gestion() {
    }

    public Gestion(AsuntoGestion asuntoGestion, Credito credito, DescripcionGestion descripcionGestion, DondeGestion dondeGestion, EstatusInformativo estatusInformativo, QuienGestion quienGestion, TipoGestion tipoGestion, TipoQuienGestion tipoQuienGestion, Usuario usuario, Date fecha, String gestion) {
       this.asuntoGestion = asuntoGestion;
       this.credito = credito;
       this.descripcionGestion = descripcionGestion;
       this.dondeGestion = dondeGestion;
       this.estatusInformativo = estatusInformativo;
       this.quienGestion = quienGestion;
       this.tipoGestion = tipoGestion;
       this.tipoQuienGestion = tipoQuienGestion;
       this.usuario = usuario;
       this.fecha = fecha;
       this.gestion = gestion;
    }
   
    public Integer getIdGestion() {
        return this.idGestion;
    }
    
    public void setIdGestion(Integer idGestion) {
        this.idGestion = idGestion;
    }
    public AsuntoGestion getAsuntoGestion() {
        return this.asuntoGestion;
    }
    
    public void setAsuntoGestion(AsuntoGestion asuntoGestion) {
        this.asuntoGestion = asuntoGestion;
    }
    public Credito getCredito() {
        return this.credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    public DescripcionGestion getDescripcionGestion() {
        return this.descripcionGestion;
    }
    
    public void setDescripcionGestion(DescripcionGestion descripcionGestion) {
        this.descripcionGestion = descripcionGestion;
    }
    public DondeGestion getDondeGestion() {
        return this.dondeGestion;
    }
    
    public void setDondeGestion(DondeGestion dondeGestion) {
        this.dondeGestion = dondeGestion;
    }
    public EstatusInformativo getEstatusInformativo() {
        return this.estatusInformativo;
    }
    
    public void setEstatusInformativo(EstatusInformativo estatusInformativo) {
        this.estatusInformativo = estatusInformativo;
    }
    public QuienGestion getQuienGestion() {
        return this.quienGestion;
    }
    
    public void setQuienGestion(QuienGestion quienGestion) {
        this.quienGestion = quienGestion;
    }
    public TipoGestion getTipoGestion() {
        return this.tipoGestion;
    }
    
    public void setTipoGestion(TipoGestion tipoGestion) {
        this.tipoGestion = tipoGestion;
    }
    public TipoQuienGestion getTipoQuienGestion() {
        return this.tipoQuienGestion;
    }
    
    public void setTipoQuienGestion(TipoQuienGestion tipoQuienGestion) {
        this.tipoQuienGestion = tipoQuienGestion;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getGestion() {
        return this.gestion;
    }
    
    public void setGestion(String gestion) {
        this.gestion = gestion;
    }




}


