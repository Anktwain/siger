/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.Producto;
import dao.ProductoDAO;
import impl.ProductoIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ProductoBean implements Serializable{
  
  // objeto para gestionar
  private Producto producto;
  
  // atributos del probuscto
  private String nombre;
  private String descripcion;
  
  // listas a utilizar
  private List listaProductos;
  
  // objeto dao para acceder a la base de datos
  private ProductoDAO productoDao;
  
  // constructor
  public ProductoBean(){
  producto = new Producto();
  productoDao = new ProductoIMPL();
  }
  
  // funciones de gestion
  public boolean listarProductos(int idEmpresa){
    boolean ok = false;
    listaProductos = productoDao.buscarProductosPorEmpresa(idEmpresa);
    if(listaProductos != null){
      ok = true;
    }
    return ok;
  }
}
