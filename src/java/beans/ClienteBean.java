package beans;

import dao.ClienteDAO;
import dto.Cliente;
import dto.Sujeto;
import impl.ClienteIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {

  // Objeto que gestiona este bean

  private Cliente cliente;

  // Atributos del objeto Cliente
  private String numeroCliente;
  private String curp;
  private String numeroSeguroSocial;
  
  private Sujeto sujeto;

  // DAO, para acceso a la BD
  private ClienteDAO clienteDao;

  // Otros beans
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;

  // Construyendo...
  public ClienteBean() {
    cliente = new Cliente();
    clienteDao = new ClienteIMPL();
  }

  // GESTIÓN DE CLIENTES
  public List<Cliente> listar() {
    return clienteDao.buscarTodo();
  }

  public Cliente insertar() {
    // Crea el Sujeto asociado al cliente
    sujeto = sujetoBean.insertar();

    // Verfica que el sujeto se haya creado correctamente
    if (sujeto == null) {
      return null;
    } else {
      // Crea el objeto Cliente
      cliente.setNumeroCliente(numeroCliente);
      cliente.setCurp(curp);
      cliente.setNumeroSeguroSocial(numeroSeguroSocial);
      cliente.setSujeto(sujeto);
      
      return clienteDao.insertar(cliente);
    }
  }


  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    numeroCliente = null;
    curp = null;
    numeroSeguroSocial = null;
    sujeto = new Sujeto();
  }


  // SETTERS & GETTERS
  public String getNumeroCliente() {
    return numeroCliente;
  }

  public void setNumeroCliente(String numeroCliente) {
    this.numeroCliente = numeroCliente;
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
