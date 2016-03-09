package beans;

import dao.DeudorDAO;
import dto.Deudor;
import dto.Sujeto;
import impl.DeudorIMPL;
import java.io.Serializable;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class DeudorBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // Objeto que gestiona este bean
  private Deudor deudor;

  // Atributos del objeto Deudor
  private String numeroDeudor;
  private String curp;
  private String numeroSeguroSocial;

  private Sujeto sujeto;

  // DAO, para acceso a la BD
  private final DeudorDAO deudorDao;

  // Otros beans
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;

  // Construyendo...
  public DeudorBean() {
    deudor = new Deudor();
    deudorDao = new DeudorIMPL();
  }

  // GESTIÓN DE CLIENTES
  public List<Deudor> listar() {
    return deudorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  public Deudor insertar() {
    // Crea el Sujeto asociado al deudor
    sujeto = sujetoBean.insertar();

    // Verfica que el sujeto se haya creado correctamente
    if (sujeto == null) {
      return null;
    } else {
      // Crea el objeto Deudor
      deudor.setNumeroDeudor(numeroDeudor);
      deudor.setCurp(curp);
      deudor.setNumeroSeguroSocial(numeroSeguroSocial);
      deudor.setSujeto(sujeto);

      return deudorDao.insertar(deudor);
    }
  }

  public boolean eliminar(Deudor c) {
    return sujetoBean.eliminar(c.getSujeto());
  }

  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    numeroDeudor = null;
    curp = null;
    numeroSeguroSocial = null;
    sujeto = new Sujeto();
  }

  // SETTERS & GETTERS
  public String getNumeroDeudor() {
    return numeroDeudor;
  }

  public void setNumeroDeudor(String numeroDeudor) {
    this.numeroDeudor = numeroDeudor;
  }

  public String getCurp() {
    return curp;
  }

  public void setCurp(String curp) {
    this.curp = curp;
  }

  public String getNumeroSeguroSocial() {
    return numeroSeguroSocial;
  }

  public void setNumeroSeguroSocial(String numeroSeguroSocial) {
    this.numeroSeguroSocial = numeroSeguroSocial;
  }

  public SujetoBean getSujetoBean() {
    return sujetoBean;
  }

  public void setSujetoBean(SujetoBean sujetoBean) {
    this.sujetoBean = sujetoBean;
  }

}
