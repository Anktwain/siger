package carga;

import dao.DeudorDAO;
import dao.SujetoDAO;
import dto.Deudor;
import dto.Fila;
import dto.Sujeto;
import impl.DeudorIMPL;
import impl.SujetoIMPL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import util.BautistaDeArchivos;
import util.constantes.Directorios;

/**
 *
 * @author brionvega
 */
public class CreadorSQL {

  /**
   * Crea tantos objetos Sujeto como créditos clasificados como "nuevos totales"
   * se hayan encontrado en la remesa. Estos objetos se asociarán con los nuevos
   * deudores.
   *
   * @param filas La lista de objetos Fila que contiene a todos los créditos
   * clasificados como "nuevos totales".
   */
  public static void crearSujetos(List<Fila> filas) {
    Sujeto sujeto;
    SujetoDAO sujetoDao = new SujetoIMPL();
    Deudor deudor;
    DeudorDAO deudorDao = new DeudorIMPL();

    for (Fila f : filas) {
      // ESTIMADO TIO:
      // ME HE TOMADO EL ATREVIMIENTO DE UTILIZAR EL CONSTRUCTOR POR DEFECTO
      // Y DESPUES ESTABLECERLE LOS ATRIBUTOS
      // ESTO CON LA FINALIDAD DE QUE EN LOS NUEVOS POJOS NO MARQUE ERROR
      // DISCULPA MI OSADIA
      Sujeto s = new Sujeto();
      s.setNombreRazonSocial(f.getNombre());
      s.setRfc(f.getRfc());
      s.setEliminado(0);
      sujeto = sujetoDao.insertar(s);
      if (sujeto != null) {
        f.setIdSujeto(sujeto.getIdSujeto());
        deudor = deudorDao.insertar(new Deudor(null, sujeto));
        if (deudor != null) {
          deudor.setNumeroDeudor(f.getIdCliente());
          f.setIdDeudor(deudor.getIdDeudor());
        }

      }
    }
  } // fin del método crearSujetos

  /**
   * Crea tantas sentencias sql como objetos Fila se obtuvo del archivo de remesa.
   * Estas sentencias son guardadas en un script que más tarde se ejecuta para
   * consumar la carga.
   *
   * @param filas La lista de objetos Fila para las cuales se generarán las sentencias.
   * @return 
   */
  public static String crearSQL(List<Fila> filas) {
    String query = "";
    String linea = null;
    String archivoSql = null;
    for (Fila f : filas) {
      int s = f.getIdSujeto();
      // Sea el nuevo deudor:
      query += "INSERT INTO `sigerbd`.`deudor` (`numero_deudor`, `curp`, `numero_seguro_social`, `id_sujeto`) "
              + "VALUES ('" + f.getIdCliente() + "', '', '', '" + s + "');\n";

      // Sean los datos de contacto para ese nuevo deudor:
      if (!f.getCorreos().isEmpty()) {
        for (String email : f.getCorreos()) {
          query += "INSERT INTO `sigerbd`.`email` (`direccion`, `id_sujeto`) VALUES ('" + email + "', '" + s + "');\n";
        }
      }
      if (!f.getTelsAdicionales().isEmpty()) {
        for (String tel : f.getTelsAdicionales()) {
          query += "INSERT INTO `sigerbd`.`telefono` (`numero`, `id_sujeto`) VALUES ('" + tel + "', '" + s + "');\n";
        }
      }
      // ESTIMADO TIO:
      // ME HE TOMADO EL ATREVIMIENTO DE AGREGAR A ESTA CONSULTA LOS CAMPOS QUE FALTAN
      // DISCULPA MI OSADIA
      // Sea la dirección del deudor:
      if (f.getIdColonia() != 0) {
        query += "INSERT INTO `sigerbd`.`direccion` (`calle`, `id_sujeto`, `id_municipio`, `id_estado`, `id_colonia`, `exterior` , `latitud`, `longitud`) "
                + "VALUES ('" + f.getCalle() + "', '" + s + "', '" + f.getIdMunicipio() + "', '" + f.getIdEstado() + "', '" + f.getIdColonia() + "', 'S/N', '0.000000', '0.000000');\n";
      } else {
        linea = s + ";" + f.getCredito() + ";" + f.getCalle() + ";" + f.getColonia()
                + ";" + f.getMunicipio() + ";" + f.getEstado() + ";" + f.getCp() + "\n";
      }

      // Sea el crédito:
      query += "INSERT INTO `sigerbd`.`credito` (`numero_credito`, `fecha_inicio`, `fecha_fin`, `fecha_quebranto`, "
              + "`monto`, `mensualidad`, `tasa_interes`, `dias_mora`, `numero_cuenta`, `tipo_credito`, `id_institucion`,"
              + " `id_producto`, `id_subproducto`, `id_deudor`, `id_gestor`, `id_despacho`) VALUES ('" + f.getCredito() + "',"
              + " '" + fechador(f.getFechaInicioCredito()) + "', '" + fechador(f.getFechaVencimientoCred()) + "', '" + fechador(f.getFechaQuebranto()) + "',"
              + " '" + f.getDisposicion() + "', '" + f.getMensualidad() + "', '" + numerador(f.getTasa()) + "', '" + numerador(null) + "', "
              + "'" + f.getCuenta() + "', '1', '1', '" + f.getIdProducto() + "', '" + f.getIdSubproducto() + "', '" + f.getIdDeudor() + "', "
              + "'" + f.getIdGestor() + "', '" + f.getIdDespacho() + "');\n";
      
      // Este archivo fungirá como script
      archivoSql = Directorios.RUTA_REMESAS + BautistaDeArchivos.bautizar("script", BautistaDeArchivos.PREFIJO, "sql");
      
      // Llegado a este punto, se deberá guardar la query en un archivo...
      guardarQueryEnArchivo(query, archivoSql);
      query = "";
      if(linea != null)
        System.out.println(linea);
    }
    return archivoSql;
  } // fin del método crearSQL

  /**
   * Prepara una fecha para que se aceptada por mysql. Toma una fecha en formato
   * dd/mm/aaaa y la regresa en formato aaaa-mm-dd.
   *
   * @param fecha Un String que representa la fecha en formato dd/mm/aaaa
   * @return Un String que representa la fecha en formato aaaa-mm-dd
   */
  private static String fechador(String fecha) {
    // La fecha originalmente viene en formato: dd/mm/aaaa, se regresara como: aaaa-mm-dd
    if (fecha == null || fecha.isEmpty()) {
      return "9999-12-31";
    } else {
      String[] f = fecha.split("/");
      return f[2] + "-" + f[1] + "-" + f[0];
    }
  }

  /**
   * Coloca el valor "0" en caso de que el número dado sea null o una cadena vacía.
   * Esto evita errores de punteros nulos.
   *
   * @param numero Un String que representa un número
   * @return Un String que el mismo número o u 0 en caso de que el valor de entrada
   * sea null o cadena vacía.
   */
  private static String numerador(String numero) {
    if (numero == null || numero.isEmpty()) {
      return "0";
    } else {
      return numero;
    }
  }
  
  /**
   * Guarda la sentencia en un archivo sql.
   *
   * @param query Un String que contiene la sentencia sql.
   * @param nombreArchivoSql Un String que contiene el nombre y ruta del archivo
   * en que se guardará la sentencia.
   */
  private static void guardarQueryEnArchivo(String query, String nombreArchivoSql) {
    // SE CREA UN ARCHIVO "VIRTUAL" PARA COMPROBAR SU EXISTENCIA
    File fichero = new File(nombreArchivoSql);
    // VERIFICAMOS SI EL ARCHIVO YA EXISTE
    if (fichero.exists()) {
      // SE ESCRIBE AL FINAL DEL ARCHIVO
      try {
        // SE CREA UN ARCHIVO DEL TIPO FILEWRITER CON LA OPCION TRUE PARA PODER AGREGAR DATOS AL FINAL DEL ARCHIVO Y NO SOBREESCRIBIRLO
        FileWriter fileWriter = new FileWriter(nombreArchivoSql, true);
        // SE ENVIA EL TEXTO PARA AGREGAR
        fileWriter.append("\n" + query);
        // CIERRE DEL ARCHIVO
        fileWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    } else {
      // SE CREA EL ARCHIVO
      try {
        // SE CREA EL ARCHIVO DE TEXTO
        fichero.createNewFile();
        // SE CREAN VARIABLES (ESCRITOR Y BUFFER) PARA PODER ESCRIBIR EN EL NUEVO FICHERO
        FileWriter fileWriter = new FileWriter(fichero);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        // SE ESCRIBE EN EL ARCHIVO
        printWriter.write(query);
        // CIERRE DEL ARCHIVO
        printWriter.close();
        bufferedWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}