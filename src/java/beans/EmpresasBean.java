package beans;

import dao.EmpresaDAO;
import dao.SujetoDAO;
import dto.Empresa;
import dto.Sujeto;
import impl.EmpresaIMPL;
import impl.SujetoIMPL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import util.log.Logs;

/**
 * La clase {@code EmpresasBean} permite ... y es el bean correspondiente a la
 * vista {@code empresas.xhtml}
 *
 * @author
 * @author
 * @author Eduardo
 * @since SigerWeb2.0
 */
@ManagedBean(name = "empresasBean")
@ViewScoped
@SessionScoped
public class EmpresasBean implements Serializable{

    private Sujeto sujeto;
    private SujetoDAO sujetoDao;
    private String nombreRazonSocial;
    private String rfc;
    private List<Sujeto> listaEmpresas;
    private Sujeto empresa;
    private Empresa empresaSeleccionada;
    private Sujeto sujetoSeleccionado;
    private EmpresaDAO empresaDao;
    private String razonSocial;
    private String corto;
    private String auxRfc;

    /**
     *
     *
     *
     */
    public EmpresasBean() {
        sujetoDao = new SujetoIMPL();
        sujeto = new Sujeto();
        listaEmpresas = sujetoDao.buscarEmpresas();
        empresaDao = new EmpresaIMPL();
    }
    
    public void guardarEmpresa(){
        empresaSeleccionada = empresaDao.buscarEmpresaPorSujeto(empresa.getIdSujeto());
        sujetoSeleccionado = sujetoDao.buscar(empresa.getIdSujeto());
        razonSocial = sujetoSeleccionado.getNombreRazonSocial();
        corto = empresaSeleccionada.getNombreCorto();
        auxRfc = sujetoSeleccionado.getRfc();
    }
    
    public void editarEmpresa(){
        Logs.log.debug("************ CONSOLA SIGERWEB ****************");
        Logs.log.debug("Se quiere editar la empresa " + corto);
    }
    
    public void eliminarEmpresa(){
        Logs.log.debug("************ CONSOLA SIGERWEB ****************");
        Logs.log.debug("Se quiere eliminar la empresa " + corto);
    }

    /**
     *
     *
     * @return
     */
    public Sujeto getSujeto() {
        return sujeto;
    }

    /**
     *
     *
     * @param
     */
    public void setSujeto(Sujeto sujeto) {
        this.sujeto = sujeto;
    }

    /**
     *
     *
     * @return
     */
    public SujetoDAO getSujetoDao() {
        return sujetoDao;
    }

    /**
     *
     *
     * @param
     */
    public void setSujetoDao(SujetoDAO sujetoDao) {
        this.sujetoDao = sujetoDao;
    }

    /**
     *
     *
     * @return
     */
    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    /**
     *
     *
     * @param
     */
    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    /**
     *
     *
     * @return
     */
    public String getRfc() {
        return rfc;
    }

    /**
     *
     *
     * @return
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     *
     *
     * @return
     */
    public List<Sujeto> getListaEmpresas() {
        return listaEmpresas;
    }

    /**
     *
     *
     * @param
     */
    public void setListaEmpresas(List<Sujeto> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    /**
     *
     *
     * @return
     */
    public Sujeto getEmpresa() {
        return empresa;
    }

    /**
     *
     *
     * @param
     */
    public void setEmpresa(Sujeto empresa) {
        this.empresa = empresa;
    }
    
    public Empresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Sujeto getSujetoSeleccionado() {
        return sujetoSeleccionado;
    }

    public void setSujetoSeleccionado(Sujeto sujetoSeleccionado) {
        this.sujetoSeleccionado = sujetoSeleccionado;
    }

    public EmpresaDAO getEmpresaDao() {
        return empresaDao;
    }

    public void setEmpresaDao(EmpresaDAO empresaDao) {
        this.empresaDao = empresaDao;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCorto() {
        return corto;
    }

    public void setCorto(String corto) {
        this.corto = corto;
    }

    public String getAuxRfc() {
        return auxRfc;
    }

    public void setAuxRfc(String auxRfc) {
        this.auxRfc = auxRfc;
    }

}
