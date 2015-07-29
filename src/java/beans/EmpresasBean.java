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

/**
 *
 * @author Eduardo
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
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar la empresa " + corto);
    }
    
    public void eliminarEmpresa(){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere eliminar la empresa " + corto);
    }
    
    public Sujeto getSujeto() {
        return sujeto;
    }

    public void setSujeto(Sujeto sujeto) {
        this.sujeto = sujeto;
    }

    public SujetoDAO getSujetoDao() {
        return sujetoDao;
    }

    public void setSujetoDao(SujetoDAO sujetoDao) {
        this.sujetoDao = sujetoDao;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }
    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
    public List<Sujeto> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Sujeto> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }
    
    public Sujeto getEmpresa() {
        return empresa;
    }

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
