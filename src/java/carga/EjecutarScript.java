package carga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.ScriptRunner;

/**
 *
 * @author brionvega
 */
public class EjecutarScript {
  
  private Connection mConnection;
  private String file;

  public void ejecutar() throws IOException, SQLException {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      mConnection = DriverManager.getConnection("jdbc:mysql://10.0.0.26:3306/sigerbd?user=cofradia&password=cofradiaDB");
    } catch (ClassNotFoundException e) {
      System.err.println("Unable to get mysql driver: " + e);
    } catch (SQLException e) {
      System.err.println("Unable to connect to server: " + e);
    }
    ScriptRunner runner = new ScriptRunner(mConnection, false, false);
    runner.runScript(new BufferedReader(new FileReader(file)));
  }

  public void setFile(String file) {
    this.file = file;
  }

}
