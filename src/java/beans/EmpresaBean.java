package beans;

import dao.EmpresaDAO;
import dto.Empresa;
import dto.Sujeto;
import impl.EmpresaIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Eduardo
 */

@ManagedBean(name = "empresaBean")
@ViewScoped
public class EmpresaBean implements Serializable {

  // objetos necesarios
  private Empresa nuevaEmpresa;
  private Empresa empresaEditada;
  private String nombreCorto;
  private EmpresaDAO empresaDao;

  // Constructor
  public EmpresaBean(){
    nuevaEmpresa = new Empresa();
    empresaDao = new EmpresaIMPL();
  }
  
  // funcion pàra buscar empresas
  public List<Sujeto> buscarEmpresas() {
    return empresaDao.buscarEmpresas();
  }
  
  // funcion para crear a la empresa segun los datos primarios brindados
  // falta incluir los datos obtenidos de las vistas de telefonos, correos, direcciones y contactos
  public int crearEmpresa(Sujeto nuevoSujeto) {
    // setteamos los atributos a la empresa
    nuevaEmpresa.setNombreCorto(nombreCorto);
    nuevaEmpresa.setSujeto(nuevoSujeto);
    // creamos la nueva empresa
    boolean okNuevaEmpresa = empresaDao.insertar(nuevaEmpresa);
    // si se creo la empresa
    if(okNuevaEmpresa){
      return 1;
    }
    // si no se creo la empresa
    else{
      return 0;
    }
  }
  
  // funcion para editar empresa
  // recibe como parametro el sujeto
  public boolean editarEmpresa(Sujeto sujetoSeleccionado){
    // obtenemos a la empresa que se pretende editar
    int id = sujetoSeleccionado.getIdSujeto();
    empresaEditada = empresaDao.buscarEmpresaPorSujeto(id);
    // primero editamos a la empresa
    empresaEditada.setNombreCorto(nombreCorto);
    // llamamos al metodo de editar empresa
    boolean ok = empresaDao.editar(empresaEditada);
    if(ok){
      return true;
    }
    else{
      return false;
    }
  }
  
  // setters y getters
  
  public EmpresaDAO getEmpresaDao() {
    return empresaDao;
  }

  public void setEmpresaDao(EmpresaDAO empresaDao) {
    this.empresaDao = empresaDao;
  }

  public Empresa getNuevaEmpresa() {
    return nuevaEmpresa;
  }

  public void setNuevaEmpresa(Empresa nuevaEmpresa) {
    this.nuevaEmpresa = nuevaEmpresa;
  }

  public String getNombreCorto() {
    return nombreCorto;
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

  public Empresa getEmpresaEditada() {
    return empresaEditada;
  }

  public void setEmpresaEditada(Empresa empresaEditada) {
    this.empresaEditada = empresaEditada;
  }

}
