package beans;

import dao.EmpresaDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import dao.SujetoDAO;
import dto.Empresa;
import dto.Producto;
import dto.Subproducto;
import dto.Sujeto;
import impl.EmpresaIMPL;
import impl.ProductoIMPL;
import impl.SubproductoIMPL;
import impl.SujetoIMPL;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Patrones;
import util.constantes.Sujetos;

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
@SessionScoped
public class EmpresasBean implements Serializable {

    private Sujeto sujeto;
    private SujetoDAO sujetoDao;
    private ProductoDAO productoDao;
    private String nombreRazonSocial;
    private String rfc;
    private List<Sujeto> listaEmpresas;
    private List<Producto> listaProductos;
    private List<Subproducto> listaSubproductos;
    private Sujeto empresa;
    private Producto producto;
    private Subproducto subproduct;
    private Producto productoSeleccionado;
    private Empresa empresaSeleccionada;
    private Subproducto subproductoSeleccionado;
    private String prod;
    private String desc;
    private String subprod;
    private String subdesc;
    private Sujeto sujetoSeleccionado;
    private EmpresaDAO empresaDao;
    private String razonSocial;
    private String corto;
    private String auxRfc;
    private int id;
    private SubproductoDAO subproductoDao;
    private Sujeto nuevoSujeto;
    private String nuevaRazonSocial;
    private String nuevoRfc;
    private int idNuevoSujeto;
    private Empresa nuevaEmpresa;
    private String nuevoCorto;
    private boolean okNuevaEmpresa;
    private boolean okEditarEmpresa;
    private boolean okEditarSujeto;
    private boolean okBorrarEmpresa;
    private Producto nuevoProducto;
    private String nuevoProd;
    private String nuevaDesc;
    private boolean okNuevoProducto;
    private boolean okEditarProducto;
    private Subproducto nuevoSubproducto;
    private String nuevoSubprod;
    private String nuevaSubdesc;
    private Producto seleccionadoCombobox;
    private boolean okNuevoSubproducto;
    private boolean okEditarSubproducto;
    
    /**
     *
     *
     *
     */
    public EmpresasBean() {
        sujetoDao = new SujetoIMPL();
        sujeto = new Sujeto();
        listaEmpresas = sujetoDao.buscarEmpresas();
        empresaDao = new EmpresaIMPL();
        productoDao = new ProductoIMPL();
        subproductoDao = new SubproductoIMPL();
        nuevoSujeto = new Sujeto();
        nuevaEmpresa = new Empresa();
        nuevoProducto = new Producto();
        nuevoSubproducto = new Subproducto();
    }

    public boolean validarRfc(String nuevo){
        boolean validacion = false;
        Pattern patron = Pattern.compile(Patrones.PATRON_RFC);
        nuevo = nuevoRfc;
        Matcher coincidencia = patron.matcher(nuevoRfc);
        validacion = coincidencia.matches();
            if(!validacion){
                FacesContext actual = FacesContext.getCurrentInstance();
                actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error", "El RFC especificado no es valido, intente nuevamente"));
            }
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println(validacion);
        return validacion;
    }
    
    public void guardarEmpresa() {
        empresaSeleccionada = empresaDao.buscarEmpresaPorSujeto(empresa.getIdSujeto());
        sujetoSeleccionado = sujetoDao.buscar(empresa.getIdSujeto());
        razonSocial = sujetoSeleccionado.getNombreRazonSocial();
        corto = empresaSeleccionada.getNombreCorto();
        auxRfc = sujetoSeleccionado.getRfc();
        id = empresaSeleccionada.getIdEmpresa();
        listaProductos = productoDao.buscarProductosPorEmpresa(id);
        listaSubproductos = subproductoDao.buscarSubproductosPorEmpresa(id);
    }

    public void guardarProducto() {
        productoSeleccionado = productoDao.buscar(producto.getIdProducto());
        prod = productoSeleccionado.getNombre();
        desc = productoSeleccionado.getDescripcion();
    }

    public void guardarSubproducto() {
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println(subproduct.getIdSubproducto());
        subproductoSeleccionado = subproductoDao.buscar(subproduct.getIdSubproducto());
        subprod = subproductoSeleccionado.getNombre();
        subdesc = subproductoSeleccionado.getDescripcion();
    }
    
    public void crearEmpresa(){
        nuevoRfc = nuevoRfc.toUpperCase();
        nuevoSujeto.setNombreRazonSocial(nuevaRazonSocial);
        nuevoSujeto.setRfc(nuevoRfc);
        nuevoSujeto.setEliminado(Sujetos.ACTIVO);
        idNuevoSujeto = sujetoDao.insertar(nuevoSujeto);
        nuevaEmpresa.setNombreCorto(nuevoCorto);
        nuevaEmpresa.setSujeto(nuevoSujeto);
        okNuevaEmpresa = empresaDao.insertar(nuevaEmpresa);
        FacesContext actual = FacesContext.getCurrentInstance();
        if(okNuevaEmpresa){
            actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO,"Insercion exitosa", "Se registro a la empresa " + nuevaRazonSocial + " en el sistema"));
            RequestContext.getCurrentInstance().update("formNuevaEmpresa");
            RequestContext.getCurrentInstance().update("formEditarEmpresa");
        }
        else{
            actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error", "No se registro a la empresa " + nuevaRazonSocial + " en el sistema"));
        }
    }

    public void editarEmpresa() {
        System.out.println("Se edito a la empresa " + razonSocial);
        /*
        sujetoSeleccionado.setNombreRazonSocial(razonSocial);
        System.out.println(razonSocial);
        sujetoSeleccionado.setRfc(auxRfc);
        System.out.println(auxRfc);
        okEditarSujeto = sujetoDao.editar(sujetoSeleccionado);
        empresaSeleccionada.setNombreCorto(corto);
        System.out.println(corto);
        okEditarEmpresa = empresaDao.editar(empresaSeleccionada);
        FacesContext actual = FacesContext.getCurrentInstance();
        if(okEditarEmpresa && okEditarSujeto){
            actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualizacion exitosa", "Se edito a la empresa " + corto));
        }
        else{
            actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error", "No se guardaron los cambios de la empresa " + corto));
        }
        */
    }

    public void eliminarEmpresa() {
        empresa.setEliminado(Sujetos.ELIMINADO);
        okBorrarEmpresa = sujetoDao.eliminar(empresa);
        if(okBorrarEmpresa){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se elimino a la empresa " + corto);
        RequestContext.getCurrentInstance().update("editarEmpresas");
        }
        else{
            System.out.println("Error fatal. No se pudo borrar a la empresa " + empresa.getNombreRazonSocial());
        }
    }
    
    public void crearProducto() {
        nuevoProducto.setNombre(nuevoProd);
        nuevoProducto.setDescripcion(nuevaDesc);
        okNuevoProducto = productoDao.insertar(nuevoProducto);
        if(okNuevoProducto){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se registro el producto " + nuevoProd + " exitosamente");
        /*
        RequestContext.getCurrentInstance().update("formNuevaEmpresa");
        RequestContext.getCurrentInstance().update("formEditarEmpresa");
        */
        }
        else{
            System.out.println("Error fatal. No se registro el producto " + nuevoProd + " en el sistema");
        }
    }

    public void editarProducto() {
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar el producto " + prod);
        RequestContext.getCurrentInstance().update("editarProductos");
    }
    
    public void crearSubproducto() {
        nuevoSubproducto.setNombre(nuevoSubprod);
        nuevoSubproducto.setDescripcion(nuevaSubdesc);
        nuevoSubproducto.setProducto(seleccionadoCombobox);
        okNuevoSubproducto = subproductoDao.insertar(nuevoSubproducto);
        if(okNuevoSubproducto){
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se registro el subproducto " + nuevoSubprod + " exitosamente");
        /*
        RequestContext.getCurrentInstance().update("formNuevaEmpresa");
        RequestContext.getCurrentInstance().update("formEditarEmpresa");
        */
        }
        else{
            System.out.println("Error fatal. No se registro el producto " + nuevoSubprod + " en el sistema");
        }
    }

    public void editarSubproducto() {
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Se quiere editar el subproducto " + subprod);
        RequestContext.getCurrentInstance().update("editarSubproductos");
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
     * @param sujeto
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

    public Empresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Sujeto getSujetoSeleccionado() {
        return sujetoSeleccionado;
    }

    public void setSujetoSeleccionado(Sujeto sujetoSeleccionado) {
        this.sujetoSeleccionado = sujetoSeleccionado;
    }

    public EmpresaDAO getEmpresaDao() {
        return empresaDao;
    }

    public void setEmpresaDao(EmpresaDAO empresaDao) {
        this.empresaDao = empresaDao;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCorto() {
        return corto;
    }

    public void setCorto(String corto) {
        this.corto = corto;
    }

    public String getAuxRfc() {
        return auxRfc;
    }

    public void setAuxRfc(String auxRfc) {
        this.auxRfc = auxRfc;
    }

    public List<Subproducto> getListaSubproductos() {
        return listaSubproductos;
    }

    public void setListaSubproductos(List<Subproducto> listaSubproductos) {
        this.listaSubproductos = listaSubproductos;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SubproductoDAO getSubproductoDao() {
        return subproductoDao;
    }

    public void setSubproductoDao(SubproductoDAO subproductoDao) {
        this.subproductoDao = subproductoDao;
    }

    public String getNuevaRazonSocial() {
        return nuevaRazonSocial;
    }

    public void setNuevaRazonSocial(String nuevaRazonSocial) {
        this.nuevaRazonSocial = nuevaRazonSocial;
    }

    public String getNuevoCorto() {
        return nuevoCorto;
    }

    public void setNuevoCorto(String nuevoCorto) {
        this.nuevoCorto = nuevoCorto;
    }

    public String getNuevoRfc() {
        return nuevoRfc;
    }

    public void setNuevoRfc(String nuevoRfc) {
        this.nuevoRfc = nuevoRfc;
    }
    
    public Sujeto getNuevoSujeto() {
        return nuevoSujeto;
    }

    public void setNuevoSujeto(Sujeto nuevoSujeto) {
        this.nuevoSujeto = nuevoSujeto;
    }

    public Empresa getNuevaEmpresa() {
        return nuevaEmpresa;
    }

    public void setNuevaEmpresa(Empresa nuevaEmpresa) {
        this.nuevaEmpresa = nuevaEmpresa;
    }

    public int getIdNuevoSujeto() {
        return idNuevoSujeto;
    }

    public void setIdNuevoSujeto(int idNuevoSujeto) {
        this.idNuevoSujeto = idNuevoSujeto;
    }

    public boolean isOkNuevaEmpresa() {
        return okNuevaEmpresa;
    }

    public void setOkNuevaEmpresa(boolean okNuevaEmpresa) {
        this.okNuevaEmpresa = okNuevaEmpresa;
    }
    
    public boolean isOkEditarEmpresa() {
        return okEditarEmpresa;
    }

    public void setOkEditarEmpresa(boolean okEditarEmpresa) {
        this.okEditarEmpresa = okEditarEmpresa;
    }

    public boolean isOkEditarSujeto() {
        return okEditarSujeto;
    }

    public void setOkEditarSujeto(boolean okEditarSujeto) {
        this.okEditarSujeto = okEditarSujeto;
    }

    public boolean isOkBorrarEmpresa() {
        return okBorrarEmpresa;
    }

    public void setOkBorrarEmpresa(boolean okBorrarEmpresa) {
        this.okBorrarEmpresa = okBorrarEmpresa;
    }

    public Producto getNuevoProducto() {
        return nuevoProducto;
    }

    public void setNuevoProducto(Producto nuevoProducto) {
        this.nuevoProducto = nuevoProducto;
    }

    public String getNuevoProd() {
        return nuevoProd;
    }

    public void setNuevoProd(String nuevoProd) {
        this.nuevoProd = nuevoProd;
    }

    public String getNuevaDesc() {
        return nuevaDesc;
    }

    public void setNuevaDesc(String nuevaDesc) {
        this.nuevaDesc = nuevaDesc;
    }

    public boolean isOkNuevoProducto() {
        return okNuevoProducto;
    }

    public void setOkNuevoProducto(boolean okNuevoProducto) {
        this.okNuevoProducto = okNuevoProducto;
    }

    public boolean isOkEditarProducto() {
        return okEditarProducto;
    }

    public void setOkEditarProducto(boolean okEditarProducto) {
        this.okEditarProducto = okEditarProducto;
    }

    public Subproducto getNuevoSubproducto() {
        return nuevoSubproducto;
    }

    public void setNuevoSubproducto(Subproducto nuevoSubproducto) {
        this.nuevoSubproducto = nuevoSubproducto;
    }

    public String getNuevoSubprod() {
        return nuevoSubprod;
    }

    public void setNuevoSubprod(String nuevoSubprod) {
        this.nuevoSubprod = nuevoSubprod;
    }

    public String getNuevaSubdesc() {
        return nuevaSubdesc;
    }

    public void setNuevaSubdesc(String nuevaSubdesc) {
        this.nuevaSubdesc = nuevaSubdesc;
    }

    public Producto getSeleccionadoCombobox() {
        return seleccionadoCombobox;
    }

    public void setSeleccionadoCombobox(Producto seleccionadoCombobox) {
        this.seleccionadoCombobox = seleccionadoCombobox;
    }

    public boolean isOkNuevoSubproducto() {
        return okNuevoSubproducto;
    }

    public void setOkNuevoSubproducto(boolean okNuevoSubproducto) {
        this.okNuevoSubproducto = okNuevoSubproducto;
    }

    public boolean isOkEditarSubproducto() {
        return okEditarSubproducto;
    }

    public void setOkEditarSubproducto(boolean okEditarSubproducto) {
        this.okEditarSubproducto = okEditarSubproducto;
    }

    public Subproducto getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(Subproducto subproduct) {
        this.subproduct = subproduct;
    }

    public Subproducto getSubproductoSeleccionado() {
        return subproductoSeleccionado;
    }

    public void setSubproductoSeleccionado(Subproducto subproductoSeleccionado) {
        this.subproductoSeleccionado = subproductoSeleccionado;
    }

    public String getSubprod() {
        return subprod;
    }

    public void setSubprod(String subprod) {
        this.subprod = subprod;
    }

    public String getSubdesc() {
        return subdesc;
    }

    public void setSubdesc(String subdesc) {
        this.subdesc = subdesc;
    }

    public ProductoDAO getProductoDao() {
        return productoDao;
    }

    public void setProductoDao(ProductoDAO productoDao) {
        this.productoDao = productoDao;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
