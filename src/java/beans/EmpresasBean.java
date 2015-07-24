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

    public EmpresasBean() {
        sujetoDao = new SujetosIMPL();
        sujeto = new Sujetos();
        listaEmpresas = sujetoDao.buscarEmpresas();
        /*
        int i;
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Empresas registradas en el sistema:");
        for(i=0; i<(listaEmpresas.size()); i++)
        {
            System.out.println(listaEmpresas.get(i).getNombreRazonSocial());
        }
        */
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
}
