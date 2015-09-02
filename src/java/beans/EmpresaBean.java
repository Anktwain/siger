package beans;

import dao.EmpresaDAO;
import dto.Empresa;
import dto.Sujeto;
import impl.EmpresaIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Eduardo
 */

@ManagedBean(name = "empresaBean")
@ViewScoped
public class EmpresaBean implements Serializable {

  // objeto empresa
  private Empresa nuevaEmpresa;
  
  // atributo del objeto empresa
  private String nombreCorto;
  
  // objeto sujeto
  private Sujeto nuevoSujeto;
  
  // objeto dao
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

  public Sujeto getNuevoSujeto() {
    return nuevoSujeto;
  }

  public void setNuevoSujeto(Sujeto nuevoSujeto) {
    this.nuevoSujeto = nuevoSujeto;
  }

  public String getNombreCorto() {
    return nombreCorto;
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

}
