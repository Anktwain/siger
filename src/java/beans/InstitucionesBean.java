/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.InstitucionDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import dao.SujetoDAO;
import dto.Institucion;
import dto.Producto;
import dto.Subproducto;
import dto.Sujeto;
import impl.InstitucionIMPL;
import impl.ProductoIMPL;
import impl.SubproductoIMPL;
import impl.SujetoIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Patrones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "institucionesBean")
@ViewScoped
public class InstitucionesBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private Sujeto nuevoSujeto;
  private Institucion nuevaInstitucion;
  private Institucion seleccionada;
  private Producto seleccionado;
  private Subproducto subSeleccionado;
  private String nombreRazonSocial;
  private String rfc;
  private String nombrecorto;
  private String nombreProducto;
  private String descripcionProducto;
  private String nombreSubproducto;
  private String descripcionSubproducto;
  private SujetoDAO sujetoDao;
  private InstitucionDAO institucionDao;
  private final ProductoDAO productoDao;
  private final SubproductoDAO subproductoDao;
  private List<Institucion> listaInstituciones;
  private List<Institucion> institucionSeleccionada;
  private List<Producto> listaProductos;
  private List<Producto> productoSeleccionado;
  private List<Subproducto> listaSubproductos;
  private List<Subproducto> subproductoSeleccionado;

  // CONSTRUCTOR
  public InstitucionesBean() {
    nuevoSujeto = new Sujeto();
    nuevaInstitucion = new Institucion();
    seleccionada = new Institucion();
    seleccionado = new Producto();
    subSeleccionado = new Subproducto();
    sujetoDao = new SujetoIMPL();
    institucionDao = new InstitucionIMPL();
    subproductoDao = new SubproductoIMPL();
    productoDao = new ProductoIMPL();
    listaInstituciones = new ArrayList();
    institucionSeleccionada = new ArrayList();
    listaProductos = new ArrayList();
    productoSeleccionado = new ArrayList();
    listaSubproductos = new ArrayList();
    subproductoSeleccionado = new ArrayList();
    cargarListas();
  }

  // METODO QUE CARGA LAS LISTAS PARA PODER ACTUALIZARLAS
  public final void cargarListas() {
    listaInstituciones = institucionDao.buscarTodo();
  }

  // METODO QUE CREA A LA NUEVA INSTITUCION 
  public void crearInstitucion() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    nuevoSujeto.setEliminado(1);
    nuevoSujeto.setNombreRazonSocial(nombreRazonSocial);
    rfc = rfc.toUpperCase();
    if (validarRfc(rfc)) {
      nuevoSujeto.setRfc(rfc);
      nuevoSujeto = sujetoDao.insertar(nuevoSujeto);
      if (nuevoSujeto.getIdSujeto() != null) {
        nuevaInstitucion.setSujeto(nuevoSujeto);
        nuevaInstitucion.setNombreCorto(nombrecorto);
        boolean ok = institucionDao.insertar(nuevaInstitucion);
        if (ok) {
          cargarListas();
          nombreRazonSocial = "";
          nombrecorto = "";
          RequestContext.getCurrentInstance().update("formNuevaEmpresa");
          RequestContext.getCurrentInstance().update("editarEmpresasForm");
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo la institucion: " + nombrecorto));
          RequestContext.getCurrentInstance().execute("PF('dlgNuevaEmpresa').hide();");
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo la institucion. Contacte al equipo de sistemas."));
        }
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El RFC proporcionado no es valido."));
      sujetoDao.eliminarEnSerio(nuevoSujeto);
    }
  }

  // METODO QUE EDITA LA INSTITUCION
  public void editarInstitucion() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    ok = validarRfc(seleccionada.getSujeto().getRfc().toUpperCase());
    if (ok) {
      ok = institucionDao.editar(seleccionada);
      if (ok) {
        cargarListas();
        RequestContext.getCurrentInstance().update("editarEmpresasForm");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se modifico la institucion."));
        RequestContext.getCurrentInstance().execute("PF('dlgDatosPrimariosEmpresa').hide();");
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se modifico la institucion. Contacte al equipo de sistemas."));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El RFC proporcionado no es valido."));
    }
  }

  // METODO QUE CREA UN NUEVO PRODUCTO
  public void crearProducto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    Producto p = new Producto();
    p.setNombre(nombreProducto);
    p.setDescripcion(descripcionProducto);
    p.setInstitucion(seleccionada);
    ok = productoDao.insertar(p);
    if (ok) {
      nombreProducto = "";
      descripcionProducto = "";
      RequestContext.getCurrentInstance().update("nuevoProductoForm");
      RequestContext.getCurrentInstance().update("editarProductosEmpresaForm");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el producto."));
      RequestContext.getCurrentInstance().execute("PF('dlgNuevoProducto').hide();");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el producto. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE EDITA UN PRODUCTO SELECCIONADO
  public void editarProducto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    ok = productoDao.editar(seleccionado);
    if (ok) {
      listaProductos = productoDao.buscarProductosPorInstitucion(seleccionada.getIdInstitucion());
      RequestContext.getCurrentInstance().update("editarProductosEmpresaForm");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se modifico el producto."));
      RequestContext.getCurrentInstance().execute("PF('dlgEditarProducto').hide();");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se modifico el producto. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE CREA UN NUEVO SUBPRODUCTO
  public void crearSubproducto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    Subproducto s = new Subproducto();
    s.setNombre(nombreSubproducto);
    s.setDescripcion(descripcionSubproducto);
    s.setProducto(seleccionado);
    ok = subproductoDao.insertar(s);
    if (ok) {
      nombreSubproducto = "";
      descripcionSubproducto = "";
      RequestContext.getCurrentInstance().update("nuevoSubproductoForm");
      RequestContext.getCurrentInstance().update("editarSubproductosProductosForm");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el subproducto."));
      RequestContext.getCurrentInstance().execute("PF('dlgNuevoSubproducto').hide();");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el subproducto. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE EDITA UN SUBPRODUCTO
  public void editarSubproducto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    ok = subproductoDao.editar(subSeleccionado);
    if (ok) {
      listaSubproductos = subproductoDao.buscarSubproductosPorProducto(seleccionado.getIdProducto());
      RequestContext.getCurrentInstance().update("editarSubproductosProductoForm");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se modifico el subproducto."));
      RequestContext.getCurrentInstance().execute("PF('dlgEditarProducto').hide();");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se modifico el subproducto. Contacte al equipo de sistemas."));
    }
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosDatosPrimarios() {
    seleccionada = institucionSeleccionada.get(0);
    RequestContext.getCurrentInstance().execute("PF('dlgEditarEmpresa').hide();");
    RequestContext.getCurrentInstance().update("editarEmpresaForm");
    RequestContext.getCurrentInstance().execute("PF('dlgDatosPrimariosEmpresa').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosProductos() {
    seleccionada = institucionSeleccionada.get(0);
    listaProductos = productoDao.buscarProductosPorInstitucion(seleccionada.getIdInstitucion());
    RequestContext.getCurrentInstance().update("editarProductosForm");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarEmpresa').hide();");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarProductosEmpresa').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosEditarProducto() {
    seleccionado = productoSeleccionado.get(0);
    RequestContext.getCurrentInstance().update("editarProductoForm");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarProductosEmpresa').hide();");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarProducto').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosNuevoProducto() {
    RequestContext.getCurrentInstance().execute("PF('dlgEditarProductosEmpresa').hide();");
    RequestContext.getCurrentInstance().execute("PF('dlgNuevoProducto').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosSubproductos() {
    seleccionado = productoSeleccionado.get(0);
    listaSubproductos = subproductoDao.buscarSubproductosPorProducto(seleccionado.getIdProducto());
    RequestContext.getCurrentInstance().execute("PF('dlgEditarProductosEmpresa').hide();");
    RequestContext.getCurrentInstance().update("editarSubproductosForm");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarSubproductosProducto').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosEditarSubproducto() {
    subSeleccionado = subproductoSeleccionado.get(0);
    RequestContext.getCurrentInstance().update("editarSubproductoForm");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarSubproductosProducto').hide();");
    RequestContext.getCurrentInstance().execute("PF('dlgEditarSubproducto').show();");
  }

  // METODO DE CONTROL DE DIALOGOS
  public void controlDialogosNuevoSubproducto() {
    RequestContext.getCurrentInstance().execute("PF('dlgEditarSubproductosProducto').hide();");
    RequestContext.getCurrentInstance().execute("PF('dlgNuevoSubproducto').show();");
  }

  // METODO QUE VALIDA EL RFC
  boolean validarRfc(String rfc) {
    boolean ok = false;
    Pattern patron = Pattern.compile(Patrones.PATRON_RFC_MORAL);
    Matcher mat = patron.matcher(rfc);
    if (mat.matches()) {
      ok = true;
    }
    return ok;
  }

  // GETTERS & SETTERS
  public Sujeto getNuevoSujeto() {
    return nuevoSujeto;
  }

  public void setNuevoSujeto(Sujeto nuevoSujeto) {
    this.nuevoSujeto = nuevoSujeto;
  }

  public Institucion getNuevaInstitucion() {
    return nuevaInstitucion;
  }

  public void setNuevaInstitucion(Institucion nuevaInstitucion) {
    this.nuevaInstitucion = nuevaInstitucion;
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

  public String getNombrecorto() {
    return nombrecorto;
  }

  public void setNombrecorto(String nombrecorto) {
    this.nombrecorto = nombrecorto;
  }

  public SujetoDAO getSujetoDao() {
    return sujetoDao;
  }

  public void setSujetoDao(SujetoDAO sujetoDao) {
    this.sujetoDao = sujetoDao;
  }

  public InstitucionDAO getInstitucionDao() {
    return institucionDao;
  }

  public void setInstitucionDao(InstitucionDAO institucionDao) {
    this.institucionDao = institucionDao;
  }

  public List<Institucion> getListaInstituciones() {
    return listaInstituciones;
  }

  public void setListaInstituciones(List<Institucion> listaInstituciones) {
    this.listaInstituciones = listaInstituciones;
  }

  public List<Institucion> getInstitucionSeleccionada() {
    return institucionSeleccionada;
  }

  public void setInstitucionSeleccionada(List<Institucion> institucionSeleccionada) {
    this.institucionSeleccionada = institucionSeleccionada;
  }

  public Institucion getSeleccionada() {
    return seleccionada;
  }

  public void setSeleccionada(Institucion seleccionada) {
    this.seleccionada = seleccionada;
  }

  public List<Producto> getListaProductos() {
    return listaProductos;
  }

  public void setListaProductos(List<Producto> listaProductos) {
    this.listaProductos = listaProductos;
  }

  public List<Producto> getProductoSeleccionado() {
    return productoSeleccionado;
  }

  public void setProductoSeleccionado(List<Producto> productoSeleccionado) {
    this.productoSeleccionado = productoSeleccionado;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public String getDescripcionProducto() {
    return descripcionProducto;
  }

  public void setDescripcionProducto(String descripcionProducto) {
    this.descripcionProducto = descripcionProducto;
  }

  public Producto getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(Producto seleccionado) {
    this.seleccionado = seleccionado;
  }

  public Subproducto getSubSeleccionado() {
    return subSeleccionado;
  }

  public void setSubSeleccionado(Subproducto subSeleccionado) {
    this.subSeleccionado = subSeleccionado;
  }

  public List<Subproducto> getListaSubproductos() {
    return listaSubproductos;
  }

  public void setListaSubproductos(List<Subproducto> listaSubproductos) {
    this.listaSubproductos = listaSubproductos;
  }

  public List<Subproducto> getSubproductoSeleccionado() {
    return subproductoSeleccionado;
  }

  public void setSubproductoSeleccionado(List<Subproducto> subproductoSeleccionado) {
    this.subproductoSeleccionado = subproductoSeleccionado;
  }

  public String getNombreSubproducto() {
    return nombreSubproducto;
  }

  public void setNombreSubproducto(String nombreSubproducto) {
    this.nombreSubproducto = nombreSubproducto;
  }

  public String getDescripcionSubproducto() {
    return descripcionSubproducto;
  }

  public void setDescripcionSubproducto(String descripcionSubproducto) {
    this.descripcionSubproducto = descripcionSubproducto;
  }

}
