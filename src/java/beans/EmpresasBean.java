package beans;

import dao.EmpresasDAO;
import dto.Empresas;
import impl.EmpresasIMPL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "empresasBean")
@ViewScoped
public class EmpresasBean implements Serializable{

    private Empresas empresa;
    private EmpresasDAO empresaDao;
    private String nombreCorto;
    private String rfc;
    private List<Empresas> listaEmpresas;

    public EmpresasBean() {
        empresaDao = new EmpresasIMPL();
        empresa = new Empresas();
        listaEmpresas = empresaDao.buscarTodo();
        int i;
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Empresas registradas en el sistema:");
        for(i=0; i<(listaEmpresas.size()); i++)
        {
            System.out.println(listaEmpresas.get(i).getNombreCorto());
        }
    }
    
    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public EmpresasDAO getEmpresaDao() {
        return empresaDao;
    }

    public void setEmpresaDao(EmpresasDAO empresaDao) {
        this.empresaDao = empresaDao;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public List<Empresas> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

}
