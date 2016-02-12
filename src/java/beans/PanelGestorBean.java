package beans;

import dto.Sujeto;
import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * La clase {@code PanelGestorBean} permite ... y es el bean correspondiente a
 * la vista {@code panelGestor.xhtml}
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
@ManagedBean
@SessionScoped
public class PanelGestorBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private String nombreUsuario;
  private String imagenDePerfil;
  private String nombre;
  private String correo;
  private String despacho;
  private Sujeto sujeto;

  /**
   *
   */
  public PanelGestorBean() {
    nombre = indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno();
    nombreUsuario = indexBean.getUsuario().getNombreLogin();
    imagenDePerfil = indexBean.getUsuario().getImagenPerfil();
    correo = indexBean.getUsuario().getCorreo();
    despacho = indexBean.getUsuario().getDespacho().getSujeto().getNombreRazonSocial();
  }

  /**
   *
   *
   * @return
   */
  public String getNombreUsuario() {
    return nombreUsuario;
  }

  /**
   *
   *
   * @param nombreUsuario
   */
  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  /**
   *
   *
   * @return
   */
  public String getImagenDePerfil() {
    return imagenDePerfil;
  }

  /**
   *
   *
   * @param imagenDePerfil
   */
  public void setImagenDePerfil(String imagenDePerfil) {
    this.imagenDePerfil = imagenDePerfil;
  }

  /**
   *
   *
   * @return
   */
  public String getNombre() {
    return nombre;
  }

  /**
   *
   *
   * @param nombre
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   *
   *
   * @return
   */
  public String getCorreo() {
    return correo;
  }

  /**
   *
   *
   * @param correo
   */
  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getDespacho() {
    return despacho;
  }

  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }

  public Sujeto getSujeto() {
    return sujeto;
  }

  public void setSujeto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

}
