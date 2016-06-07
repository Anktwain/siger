package carga;

import dto.Fila;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author brionvega
 */
public class EscritorRemesaLimpia {

  public static void escribirArchivo(List<Fila> filas, String archivo) {
    try {
      Workbook libro = Workbook.getWorkbook(new File(archivo));

      WritableWorkbook libroSalida = Workbook.createWorkbook(new File(archivo + "_salida.xls"));
      WritableSheet hoja = libroSalida.createSheet("info", 0);

      hoja.addCell(new Label(0, 0, "Credito"));
      hoja.addCell(new Label(1, 0, "Nombre"));
      hoja.addCell(new Label(2, 0, "RefCobro"));
      hoja.addCell(new Label(3, 0, "Linea"));
      hoja.addCell(new Label(4, 0, "TipoCredito"));
      hoja.addCell(new Label(5, 0, "Estatus"));
      hoja.addCell(new Label(6, 0, "MesesVen"));
      hoja.addCell(new Label(7, 0, "Despacho"));
      hoja.addCell(new Label(8, 0, "FechaInicio"));
      hoja.addCell(new Label(9, 0, "FechaVencimiento"));
      hoja.addCell(new Label(10, 0, "Disposicion"));
      hoja.addCell(new Label(11, 0, "Mensualidad"));
      hoja.addCell(new Label(12, 0, "SaldoIns"));
      hoja.addCell(new Label(13, 0, "SaldoVen"));
      hoja.addCell(new Label(14, 0, "Tasa"));
      hoja.addCell(new Label(15, 0, "Cuenta"));
      hoja.addCell(new Label(16, 0, "FechaUP"));
      hoja.addCell(new Label(17, 0, "FechaUVP"));
      hoja.addCell(new Label(18, 0, "numCliente"));
      hoja.addCell(new Label(19, 0, "RFC"));
      hoja.addCell(new Label(20, 0, "Calle"));
      hoja.addCell(new Label(21, 0, "Colonia"));
      hoja.addCell(new Label(22, 0, "Estado"));
      hoja.addCell(new Label(23, 0, "Municipio"));
      hoja.addCell(new Label(24, 0, "CP"));
      hoja.addCell(new Label(25, 0, "Año"));
      hoja.addCell(new Label(26, 0, "Mes"));
      hoja.addCell(new Label(27, 0, "FacMes"));
      hoja.addCell(new Label(28, 0, "Monto"));
      hoja.addCell(new Label(29, 0, "Año"));
      hoja.addCell(new Label(30, 0, "Mes"));
      hoja.addCell(new Label(31, 0, "FacMes"));
      hoja.addCell(new Label(32, 0, "Monto"));
      hoja.addCell(new Label(33, 0, "Año"));
      hoja.addCell(new Label(34, 0, "Mes"));
      hoja.addCell(new Label(35, 0, "FacMes"));
      hoja.addCell(new Label(36, 0, "Monto"));
      hoja.addCell(new Label(37, 0, "Referencia1"));
      hoja.addCell(new Label(38, 0, "Referencia2"));
      hoja.addCell(new Label(39, 0, "Referencia3"));
      hoja.addCell(new Label(40, 0, "Correo"));
      hoja.addCell(new Label(41, 0, "TelAdicional1"));
      hoja.addCell(new Label(42, 0, "TelAdicional2"));
      hoja.addCell(new Label(43, 0, "Direccion"));
      hoja.addCell(new Label(44, 0, "Vacia"));
      hoja.addCell(new Label(45, 0, "Marcaje"));
      hoja.addCell(new Label(46, 0, "FechaQuebranto"));

      for (int f = 1; f <= filas.size(); f++) {
        hoja.addCell(new Label(0, f, filas.get(f - 1).getCredito()));
        hoja.addCell(new Label(1, f, filas.get(f - 1).getNombre()));
        hoja.addCell(new Label(2, f, filas.get(f - 1).getRefCobro()));
        hoja.addCell(new Label(3, f, filas.get(f - 1).getLinea()));
        hoja.addCell(new Label(4, f, filas.get(f - 1).getTipoCredito()));
        hoja.addCell(new Label(5, f, filas.get(f - 1).getEstatus()));
        hoja.addCell(new Label(6, f, filas.get(f - 1).getMesesVencidos()));
        hoja.addCell(new Label(7, f, filas.get(f - 1).getDespacho()));
        hoja.addCell(new Label(8, f, filas.get(f - 1).getFechaInicioCredito()));
        hoja.addCell(new Label(9, f, filas.get(f - 1).getFechaVencimientoCred()));
        hoja.addCell(new Label(10, f, filas.get(f - 1).getDisposicion()));
        hoja.addCell(new Label(11, f, filas.get(f - 1).getMensualidad()));
        hoja.addCell(new Label(12, f, filas.get(f - 1).getSaldoInsoluto()));
        hoja.addCell(new Label(13, f, filas.get(f - 1).getSaldoVencido()));
        hoja.addCell(new Label(14, f, filas.get(f - 1).getTasa()));
        hoja.addCell(new Label(15, f, filas.get(f - 1).getCuenta()));
        hoja.addCell(new Label(16, f, filas.get(f - 1).getFechaUltimoPago()));
        hoja.addCell(new Label(17, f, filas.get(f - 1).getFechaUltimoVencimientoPagado()));
        hoja.addCell(new Label(18, f, filas.get(f - 1).getIdCliente()));
        hoja.addCell(new Label(19, f, filas.get(f - 1).getRfc()));
        hoja.addCell(new Label(20, f, filas.get(f - 1).getCalle()));
        hoja.addCell(new Label(21, f, filas.get(f - 1).getColonia()));
        hoja.addCell(new Label(22, f, filas.get(f - 1).getEstado()));
        hoja.addCell(new Label(23, f, filas.get(f - 1).getMunicipio()));
        hoja.addCell(new Label(24, f, filas.get(f - 1).getCp()));
        hoja.addCell(new Number(25, f, filas.get(f - 1).getFacs().get(0).getAnio()));
        hoja.addCell(new Label(26, f, escribirMes(filas.get(f - 1).getFacs().get(0).getMes())));
        hoja.addCell(new Label(27, f, filas.get(f - 1).getFacs().get(0).getFacMes()));
        hoja.addCell(new Label(28, f, filas.get(f - 1).getFacs().get(0).getFacPor()));
        hoja.addCell(new Number(29, f, filas.get(f - 1).getFacs().get(1).getAnio()));
        hoja.addCell(new Label(30, f, escribirMes(filas.get(f - 1).getFacs().get(1).getMes())));
        hoja.addCell(new Label(31, f, filas.get(f - 1).getFacs().get(1).getFacMes()));
        hoja.addCell(new Label(32, f, filas.get(f - 1).getFacs().get(1).getFacPor()));
        hoja.addCell(new Number(33, f, filas.get(f - 1).getFacs().get(2).getAnio()));
        hoja.addCell(new Label(34, f, escribirMes(filas.get(f - 1).getFacs().get(2).getMes())));
        hoja.addCell(new Label(35, f, filas.get(f - 1).getFacs().get(2).getFacMes()));
        hoja.addCell(new Label(36, f, filas.get(f - 1).getFacs().get(2).getFacPor()));
        hoja.addCell(new Label(37, f, "")); // Referencia1
        hoja.addCell(new Label(38, f, "")); // Referencia2
        hoja.addCell(new Label(39, f, "")); // Referencia3
        //hoja.addCell(new Label(40, f, filas.get(f - 1).getCorreo()));
        hoja.addCell(new Label(41, f, "")); // TelAdicional1
        hoja.addCell(new Label(42, f, "")); // TelAdicional2
        hoja.addCell(new Label(43, f, "")); // Direccion
        hoja.addCell(new Label(44, f, ""));
        hoja.addCell(new Label(45, f, "13"));
        hoja.addCell(new Label(46, f, ""));
      }

      libroSalida.write();
      libroSalida.close();

    } catch (IOException ex) {
      Logger.getLogger(EscritorRemesaLimpia.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BiffException ex) {
      Logger.getLogger(EscritorRemesaLimpia.class.getName()).log(Level.SEVERE, null, ex);
    } catch (WriteException ex) {

    }
  }

  private static String escribirMes(int mes) {
    switch (mes) {
      case 1:
        return "ENERO";
      case 2:
        return "FEBRERO";
      case 3:
        return "MARZO";
      case 4:
        return "ABRIL";
      case 5:
        return "MAYO";
      case 6:
        return "JUNIO";
      case 7:
        return "JULIO";
      case 8:
        return "AGOSTO";
      case 9:
        return "SEPTIEMBRE";
      case 10:
        return "OCTUBRE";
      case 11:
        return "NOVIEMBRE";
      case 12:
        return "DICIEMBRE";
    }
    return null;
  }
}
