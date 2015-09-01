package beans;

import dao.EmpresaDAO;
import dto.Empresa;
import dto.Sujeto;
import impl.EmpresaIMPL;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import util.constantes.Patrones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "empresaBean")
@ViewScoped
public class EmpresaBean implements Serializable {

  // objeto empresa
  private Empresa empresa;
  
  // atributos del objeto empresa
  private String nombreCorto;
  private String idEmpresa;
  private Sujeto sujeto;
  
  // objeto dao
  private EmpresaDAO empresaDao;
  
  // llamada a otros beans
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;
  
  // Constructor
  public EmpresaBean(){
    empresa = new Empresa();
    empresaDao = new EmpresaIMPL();
  }
  
  public List<Sujeto> buscarEmpresas() {
    return empresaDao.buscarEmpresas();
  }
  
  public boolean validarRfc(String rfc) {
    Pattern patron = Pattern.compile(Patrones.PATRON_RFC_MORAL);
    Matcher matcher = patron.matcher(rfc);
    return matcher.matches();
  }
  
  public Empresa getEmpresa() {
    return empresa;
  }

  public void setEmpresa(Empresa empresa) {
    this.empresa = empresa;
  }

  public String getNombreCorto() {
    return nombreCorto;
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

  public String getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(String idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Sujeto getSujeto() {
    return sujeto;
  }

  public void setSujeto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

  public EmpresaDAO getEmpresaDao() {
    return empresaDao;
  }

  public void setEmpresaDao(EmpresaDAO empresaDao) {
    this.empresaDao = empresaDao;
  }

  public SujetoBean getSujetoBean() {
    return sujetoBean;
  }

  public void setSujetoBean(SujetoBean sujetoBean) {
    this.sujetoBean = sujetoBean;
  }
  
}
