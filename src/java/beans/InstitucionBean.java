package beans;

import dao.InstitucionDAO;
import dto.Institucion;
import dto.Sujeto;
import impl.InstitucionIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Eduardo
 */

@ManagedBean(name = "institucionBean")
@ViewScoped
public class InstitucionBean implements Serializable {

  // objetos necesarios
  private Institucion nuevaInstitucion;
  private Institucion institucionEditada;
  private String nombreCorto;
  private InstitucionDAO institucionDao;

  // Constructor
  public InstitucionBean(){
    nuevaInstitucion = new Institucion();
    institucionDao = new InstitucionIMPL();
  }
  
  // funcion pàra buscar Instituciones
  public List<Sujeto> buscarInstituciones() {
    return institucionDao.buscarInstituciones();
  }
  
  // funcion para crear a la Institucion segun los datos primarios brindados
  // falta incluir los datos obtenidos de las vistas de telefonos, correos, direcciones y contactos
  public int crearInstitucion(Sujeto nuevoSujeto) {
    // setteamos los atributos a la Institucion
    nuevaInstitucion.setNombreCorto(nombreCorto);
    nuevaInstitucion.setSujeto(nuevoSujeto);
    // creamos la nueva Institucion
    boolean okNuevaInstitucion = institucionDao.insertar(nuevaInstitucion);
    // si se creo la Institucion
    if(okNuevaInstitucion){
      return 1;
    }
    // si no se creo la Institucion
    else{
      return 0;
    }
  }
  
  // funcion para editar Institucion
  // recibe como parametro el sujeto
  public boolean editarInstitucion(Sujeto sujetoSeleccionado){
    // obtenemos a la Institucion que se pretende editar
    int id = sujetoSeleccionado.getIdSujeto();
    institucionEditada = institucionDao.buscarInstitucionPorSujeto(id);
    // primero editamos a la Institucion
    institucionEditada.setNombreCorto(nombreCorto);
    // llamamos al metodo de editar Institucion
    boolean ok = institucionDao.editar(institucionEditada);
    if(ok){
      return true;
    }
    else{
      return false;
    }
  }
  
  // setters y getters
  
  public InstitucionDAO getInstitucionDao() {
    return institucionDao;
  }

  public void setInstitucionDao(InstitucionDAO institucionDao) {
    this.institucionDao = institucionDao;
  }

  public Institucion getNuevaInstitucion() {
    return nuevaInstitucion;
  }

  public void setNuevaInstitucion(Institucion nuevaInstitucion) {
    this.nuevaInstitucion = nuevaInstitucion;
  }

  public String getNombreCorto() {
    return nombreCorto;
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

  public Institucion getInstitucionEditada() {
    return institucionEditada;
  }

  public void setInstitucionEditada(Institucion institucionEditada) {
    this.institucionEditada = institucionEditada;
  }

}
