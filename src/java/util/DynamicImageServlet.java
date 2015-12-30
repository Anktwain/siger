/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Eduardo
 */
public class DynamicImageServlet extends HttpServlet {

  // VARIABLES DE CLASE
  private final String ruta = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 8.0.3\\bin\\Comprobantes\\";

  // METODO QUE REALIZA LA PETICION HTTP
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      // OBTENER IMAGEN
      String archivo = request.getParameter("archivo");
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(ruta + archivo));
      // Get image contents.
      byte[] bytes = new byte[in.available()];
      in.read(bytes);
      in.close();
      // ENVIAR LA IMAGEN OBTENIDA
      response.getOutputStream().write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
