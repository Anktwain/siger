package beans;

import dao.EmpresasDAO;
import dao.SujetosDAO;
import dto.Empresas;
import dto.Sujetos;
import impl.EmpresasIMPL;
import impl.SujetosIMPL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "empresasBean")
@ViewScoped
public class EmpresasBean {
    private Empresas empresa;
    private Sujetos sujeto;
}
