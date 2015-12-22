package carga;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author brionvega
 */
public class Confirmador {

  /**
   * Ejecuta un script almacenado en el servidor.
   *
   * @param archivoSql Nombre y rura del script a ejecutar.
   * @return valor booleano que indica si la operación fue exitosa (true) o no
   * (false).
   */
  public static boolean ejecutarScriptSql(String archivoSql) throws IOException, SQLException {
    EjecutarScript ejecutarScript = new EjecutarScript();
    ejecutarScript.setFile(archivoSql);
    ejecutarScript.ejecutar();
    return true;
  }
}
