package beans;

import dao.SujetoDAO;
import dto.Sujeto;
import impl.SujetoIMPL;
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

    private Sujeto sujeto;
    private SujetoDAO sujetoDao;
    private String nombreRazonSocial;
    private String rfc;
    private List<Sujeto> listaEmpresas;
    private Sujeto empresa;

    public EmpresasBean() {
        sujetoDao = new SujetoIMPL();
        sujeto = new Sujeto();
        listaEmpresas = sujetoDao.buscarEmpresas();
    }
    
    public void editarEmpresa(Sujeto empresa){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar la empresa " + empresa.getNombreRazonSocial());
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
}
