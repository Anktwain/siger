/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ImpresionDAO;
import dto.EstadoRepublica;
import dto.Impresion;
import dto.Municipio;
import impl.EstadoRepublicaIMPL;
import impl.ImpresionIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.QuickSort;
import util.constantes.GoogleMaps;
import util.constantes.Impresiones;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "listaVisitasBean")
@SessionScoped
public class ListaVisitasBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private List<Impresion> listaVisitas;
  private List<Impresion> listaCorreo;
  private List<VisitasEstado> listaVisitasEstado;
  private List<DireccionesResultado> listaDireccionesEncontradas;
  DireccionesResultado direccionSeleccionada;
  private Impresion visitaSeleccionada;
  private final ImpresionDAO impresionDao;
  private JSONObject obj;
  private String latitud;
  private String longitud;

  // CONSTRUCTOR
  public ListaVisitasBean() {
    impresionDao = new ImpresionIMPL();
    direccionSeleccionada = new DireccionesResultado();
    obtenerListas();
  }

  // METODO QUE CARGA LAS LISTAS INICIALES
  private void obtenerListas() {
    listaVisitas = ordenarVisitas(impresionDao.buscarPorTipoImpresion(Impresiones.VISITA_DOMICILIARIA, indexBean.getUsuario().getDespacho().getIdDespacho()));
    listaCorreo = impresionDao.buscarPorTipoImpresion(Impresiones.CORREO_ORDINARIO, indexBean.getUsuario().getDespacho().getIdDespacho());
    listaVisitasEstado = obtenerVisitasEstado();
    listaDireccionesEncontradas = new ArrayList();
  }

  // METODO QUE ORDENARA LAS VISITAS
  public List<Impresion> ordenarVisitas(List<Impresion> listaSinOrdenar) {
    List<Impresion> cdmx = new ArrayList();
    List<Impresion> zmvm = new ArrayList();
    List<Impresion> interior = new ArrayList();
    for (int i = 0; i < (listaSinOrdenar.size()); i++) {
      int idMun = listaSinOrdenar.get(i).getDireccion().getMunicipio().getIdMunicipio();
      if (listaSinOrdenar.get(i).getDireccion().getEstadoRepublica().getIdEstado() == 9) {
        cdmx.add(listaSinOrdenar.get(i));
      } else if ((idMun == 669) || (idMun == 676) || (idMun == 680) || (idMun == 777) || (idMun == 681) || (idMun == 685) || (idMun == 687) || (idMun == 689) || (idMun == 693) || (idMun == 695) || (idMun == 713) || (idMun == 714) || (idMun == 716) || (idMun == 726) || (idMun == 737) || (idMun == 760) || (idMun == 765) || (idMun == 778)) {
        zmvm.add(listaSinOrdenar.get(i));
      } else {
        interior.add(listaSinOrdenar.get(i));
      }
    }
    List<Impresion> ordenada = new ArrayList();
    QuickSort qs = new QuickSort();
    ordenada.addAll(qs.quickSortCp(cdmx));
    ordenada.addAll(qs.quickSortCp(zmvm));
    ordenada.addAll(qs.quickSortCp(interior));
    return ordenada;
  }

  // METODO QUE REGRESA EL VALOR DE COLOR PARA ESA FILA
  public String obtenerEstilo(Municipio m) {
    if (m.getEstadoRepublica().getIdEstado() == 9) {
      return "background: #65CEA7;";
    } else if ((m.getIdMunicipio() == 669) || (m.getIdMunicipio() == 676) || (m.getIdMunicipio() == 680) || (m.getIdMunicipio() == 777) || (m.getIdMunicipio() == 681) || (m.getIdMunicipio() == 685) || (m.getIdMunicipio() == 687) || (m.getIdMunicipio() == 689) || (m.getIdMunicipio() == 693) || (m.getIdMunicipio() == 695) || (m.getIdMunicipio() == 713) || (m.getIdMunicipio() == 714) || (m.getIdMunicipio() == 716) || (m.getIdMunicipio() == 726) || (m.getIdMunicipio() == 737) || (m.getIdMunicipio() == 760) || (m.getIdMunicipio() == 765) || (m.getIdMunicipio() == 778)) {
      return "background: #F3CE85;";
    } else {
      return "background: #6BAFBD;";
    }
  }

  // METODO QUE OBTIENE LA CANTIDAD DE VISITAS POR ESTADO
  public List<VisitasEstado> obtenerVisitasEstado() {
    List<VisitasEstado> visitas = new ArrayList();
    List<EstadoRepublica> estados = new EstadoRepublicaIMPL().buscarTodo();
    for (int i = 0; i < (estados.size()); i++) {
      VisitasEstado ve = new VisitasEstado();
      ve.setEstado(estados.get(i).getNombre());
      ve.setVisitas(impresionDao.calcularVisitasPorEstado(indexBean.getUsuario().getDespacho().getIdDespacho(), estados.get(i).getIdEstado()).intValue());
      ve.setCorreo(impresionDao.calcularCorreoPorEstado(indexBean.getUsuario().getDespacho().getIdDespacho(), estados.get(i).getIdEstado()).intValue());
      visitas.add(ve);
    }
    return visitas;
  }

  //METODO QUE PREPARA LA DIRECCION Y ABRE LA VISTA DE VISITAS
  public void prepararDireccion() {
    if ((visitaSeleccionada.getDireccion().getLatitud() != BigDecimal.ZERO) && (visitaSeleccionada.getDireccion().getLongitud() != BigDecimal.ZERO)) {
      latitud = visitaSeleccionada.getDireccion().getLatitud().toString();
      longitud = visitaSeleccionada.getDireccion().getLongitud().toString();
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("cercanias.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista cercanias.");
        Logs.log.error(ioe);
      }
    } else {
      String direccion = visitaSeleccionada.getDireccion().getCalle().toLowerCase() + ",";
      direccion = direccion + visitaSeleccionada.getDireccion().getColonia().getNombre() + ",";
      direccion = direccion + visitaSeleccionada.getDireccion().getMunicipio().getNombre() + ",";
      direccion = direccion + visitaSeleccionada.getDireccion().getEstadoRepublica().getNombre() + ",";
      direccion = direccion + visitaSeleccionada.getDireccion().getColonia().getCodigoPostal();
      direccion = direccion.replace("á", "a");
      direccion = direccion.replace("é", "e");
      direccion = direccion.replace("í", "i");
      direccion = direccion.replace("ó", "o");
      direccion = direccion.replace("ú", "u");
      direccion = direccion.replace("Á", "A");
      direccion = direccion.replace("É", "E");
      direccion = direccion.replace("Í", "I");
      direccion = direccion.replace("Ó", "O");
      direccion = direccion.replace("Ú", "U");
      direccion = direccion.replace(" ", "+");
      String cadena = GoogleMaps.RUTA_GEOCODING + direccion + GoogleMaps.CLAVE_API;
      try {
        URL url = new URL(cadena);
        String respuesta;
        try (Scanner scan = new Scanner(url.openStream())) {
          respuesta = new String();
          while (scan.hasNext()) {
            respuesta += scan.nextLine();
          }
        }
        obj = new JSONObject(respuesta);
        JSONArray resultados = obj.getJSONArray("results");
        if (resultados.length() == 1) {
          latitud = resultados.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
          longitud = resultados.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
          try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("cercanias.xhtml");
          } catch (IOException ioe) {
            Logs.log.error("No se pudo redirigir a la vista cercanias.");
            Logs.log.error(ioe);
          }
        } else if (resultados.length() > 1) {
          listaDireccionesEncontradas = new ArrayList();
          for (int i = 0; i < (resultados.length()); i++) {
            DireccionesResultado d = new DireccionesResultado();
            d.setIdResultado(i + 1);
            String formateada = resultados.getJSONObject(i).get("formatted_address").toString();
            formateada = formateada.replace("Ã¡", "á");
            formateada = formateada.replace("Ã©", "é");
            // falta la í
            formateada = formateada.replace("Ã³", "ó");
            // falta la ú
            d.setDireccionEncontrada(formateada);
            d.setLat(resultados.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lat").toString());
            d.setLon(resultados.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lng").toString());
            d.setObjetoDevuelto(resultados.getJSONObject(i));
            listaDireccionesEncontradas.add(d);
          }
          abrirCercanias();
        } else {
          System.out.println("NO SE ENCONTRO NINGUNA DIRECCION PARA ESTA BUSQUEDA");
          System.out.println(obj.getJSONObject("status").toString().replace("_", " "));
        }
      } catch (MalformedURLException mue) {
        Logs.log.error("No se pudo formar la url: " + cadena);
        Logs.log.error(mue);
      } catch (IOException ioe) {
        Logs.log.error("No existe la url: " + cadena);
        Logs.log.error(ioe);
      } catch (JSONException je) {
        Logs.log.error("No se pudo crear el objeto JSON de la url: " + cadena);
        Logs.log.error(je);
      }
    }
  }

  // METODO QUE ABRE LA VISTA DE CERCANIAS Y PREPARA LA DIRECCION SELECCIONADA
  public void abrirCercanias() {
    latitud = listaDireccionesEncontradas.get(0).getLat();
    longitud = listaDireccionesEncontradas.get(0).getLon();
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("cercanias.xhtml");
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir a la vista cercanias.");
      Logs.log.error(ioe);
    }
  }

  // GETTERS & SETTERS
  public List<Impresion> getListaVisitas() {
    return listaVisitas;
  }

  public void setListaVisitas(List<Impresion> listaVisitas) {
    this.listaVisitas = listaVisitas;
  }

  public List<Impresion> getListaCorreo() {
    return listaCorreo;
  }

  public void setListaCorreo(List<Impresion> listaCorreo) {
    this.listaCorreo = listaCorreo;
  }

  public Impresion getVisitaSeleccionada() {
    return visitaSeleccionada;
  }

  public void setVisitaSeleccionada(Impresion visitaSeleccionada) {
    this.visitaSeleccionada = visitaSeleccionada;
  }

  public List<VisitasEstado> getListaVisitasEstado() {
    return listaVisitasEstado;
  }

  public void setListaVisitasEstado(List<VisitasEstado> listaVisitasEstado) {
    this.listaVisitasEstado = listaVisitasEstado;
  }

  public JSONObject getObj() {
    return obj;
  }

  public void setObj(JSONObject obj) {
    this.obj = obj;
  }

  public String getLatitud() {
    return latitud;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public String getLongitud() {
    return longitud;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public List<DireccionesResultado> getListaDireccionesEncontradas() {
    return listaDireccionesEncontradas;
  }

  public void setListaDireccionesEncontradas(List<DireccionesResultado> listaDireccionesEncontradas) {
    this.listaDireccionesEncontradas = listaDireccionesEncontradas;
  }

  public DireccionesResultado getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(DireccionesResultado direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  // CLASE MIEMBRO PARA VISUALIZAR LA CANTIDAD DE VISITAS POR ESTADO
  public static class VisitasEstado {

    private String estado;
    private int visitas;
    private int correo;

    public VisitasEstado() {
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public int getVisitas() {
      return visitas;
    }

    public void setVisitas(int visitas) {
      this.visitas = visitas;
    }

    public int getCorreo() {
      return correo;
    }

    public void setCorreo(int correo) {
      this.correo = correo;
    }

  }

  // CLASE MIEMBRO PARA ALOJAR LAS DIRECCIONES ENCONTRADAS POR GOOGLE MAPS
  public static class DireccionesResultado {

    private int idResultado;
    private JSONObject objetoDevuelto;
    private String direccionEncontrada;
    private String lat;
    private String lon;

    public DireccionesResultado() {
    }

    public int getIdResultado() {
      return idResultado;
    }

    public void setIdResultado(int idResultado) {
      this.idResultado = idResultado;
    }

    public JSONObject getObjetoDevuelto() {
      return objetoDevuelto;
    }

    public void setObjetoDevuelto(JSONObject objetoDevuelto) {
      this.objetoDevuelto = objetoDevuelto;
    }

    public String getDireccionEncontrada() {
      return direccionEncontrada;
    }

    public void setDireccionEncontrada(String direccionEncontrada) {
      this.direccionEncontrada = direccionEncontrada;
    }

    public String getLat() {
      return lat;
    }

    public void setLat(String lat) {
      this.lat = lat;
    }

    public String getLon() {
      return lon;
    }

    public void setLon(String lon) {
      this.lon = lon;
    }

  }

}
