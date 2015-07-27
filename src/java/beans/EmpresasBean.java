package beans;

import dao.SujetosDAO;
import dto.Sujetos;
import impl.SujetosIMPL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "empresasBean")
@ViewScoped
public class EmpresasBean implements Serializable{

    private Sujetos sujeto;
    private SujetosDAO sujetoDao;
    private String nombreRazonSocial;
    private String rfc;
    private List<Sujetos> listaEmpresas;
    private Sujetos empresa;

    public EmpresasBean() {
        sujetoDao = new SujetosIMPL();
        sujeto = new Sujetos();
        listaEmpresas = sujetoDao.buscarEmpresas();
    }
    
    public void editarEmpresa(Sujetos empresa){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar la empresa " + empresa.getNombreRazonSocial());
    }
    
    public Sujetos getSujeto() {
        return sujeto;
    }

    public void setSujeto(Sujetos sujeto) {
        this.sujeto = sujeto;
    }

    public SujetosDAO getSujetoDao() {
        return sujetoDao;
    }

    public void setSujetoDao(SujetosDAO sujetoDao) {
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
    
    public List<Sujetos> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Sujetos> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }
    
    public Sujetos getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Sujetos empresa) {
        this.empresa = empresa;
    }
}
