package carga;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author brionvega
 */
public class Confirmador {

  public static boolean ejecutarScriptSql(String archivoSql) throws IOException, SQLException {
    EjecutarScript ejecutarScript = new EjecutarScript();
    ejecutarScript.setFile(archivoSql);
    ejecutarScript.ejecutar();
    return true;
  }
}
