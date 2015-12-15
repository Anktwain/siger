package carga;

import dao.CreditoDAO;
import dao.CreditoRemesaDAO;
import dao.DeudorDAO;
import dao.RemesaDao;
import dto.Credito;
import dto.Deudor;
import dto.Fila;
import impl.CreditoIMPL;
import impl.CreditoRemesaIMPL;
import impl.DeudorIMPL;
import impl.RemesaIMPL;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Clasificador {
  private static final int EN_LA_FIESTA = 1;
  private static final int ESTABA_EN_LA_FIESTA = 2;
  private static final int NUEVO_CREDITO = 3;
  private static final int NUEVO_TOTAL = 4;
  
  //private CreditoDAO creditoDao;

  public Clasificador() {
    //creditoDao = new CreditoIMPL();
  }
  
  
  public static List<Fila> clasificar(List<Fila> filas) {
    String numeroCredito;
    Credito credito;
    CreditoDAO creditoDao = new CreditoIMPL();
    Deudor deudor;
    DeudorDAO deudorDao = new DeudorIMPL();
    RemesaDao remesaDao = new RemesaIMPL();
    CreditoRemesaDAO creditoRemesaDao = new CreditoRemesaIMPL();
    int remesaActual, remesaActualCredito;
    remesaActual = remesaDao.buscarRemesaActual();
    
    for(Fila f : filas) {
      credito = creditoDao.buscar(f.getCredito());
      if(credito != null) {
        remesaActualCredito = creditoRemesaDao.buscarRemesaActual(credito.getNumeroCredito());
        if(remesaActualCredito == remesaActual) {
          f.setClasificacion(EN_LA_FIESTA);
        } else {
          f.setClasificacion(ESTABA_EN_LA_FIESTA);
        }
      } else {
        deudor = deudorDao.buscar(f.getIdCliente());
        if(deudor != null) {
          f.setClasificacion(NUEVO_CREDITO); // No encuentra crédito ni cliente
        } else {
          f.setClasificacion(NUEVO_TOTAL); // No encuentra crédito, sí cliente
        }
      }
    }
    
    
    return filas;
  }

}