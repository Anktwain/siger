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
public class EmpresasBean implements Serializable {

    private Sujeto sujeto;
    private SujetoDAO sujetoDao;
    private String nombreRazonSocial;
    private String rfc;
    private List<Sujeto> listaEmpresas;
    private Sujeto empresa;

    /**
     *
     *
     *
     */
    public EmpresasBean() {
        sujetoDao = new SujetoIMPL();
        sujeto = new Sujeto();
        listaEmpresas = sujetoDao.buscarEmpresas();
    }

    /**
     *
     *
     * @param
     */
    public void editarEmpresa(Sujeto empresa) {
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar la empresa " + empresa.getNombreRazonSocial());
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
}
