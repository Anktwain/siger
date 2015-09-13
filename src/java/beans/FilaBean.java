package beans;

import dto.Fila;
import java.io.Serializable;

/**
 *
 * @author Pablo
 */
public class FilaBean implements Serializable {

  private Fila filaActual;

  public FilaBean() {

  }

  public Fila getFilaActual() {
    return filaActual;
  }

  public void setFilaActual(Fila filaActual) {
    this.filaActual = filaActual;
  }
  /**
   * 
   */
  public void validaNumCred(String numCred) throws Exception{
    if (!this.filaActual.getCredito().isEmpty()){
      if(this.filaActual.getCredito() > ){
      }
    }else{
      throw new Exception("El campo se encuentra vacío");
    }
  }
  
  /**
   * 
   */
  public void valida(String s){
    
  }

}
