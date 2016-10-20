/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.QuincenaDAO;
import dto.Quincena;
import impl.QuincenaIMPL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Eduardo
 */
public class GeneradorQuincenas {

  // VARIABLES DE CLASE
  private final QuincenaDAO quincenaDao;
  
  // CONSTRUCTOR
  public GeneradorQuincenas() {
    quincenaDao = new QuincenaIMPL();
  }
  
  // METODO QUE GENERA LAS QUINCENAS DEL AÑO EN VIGOR
  public boolean generarQuincenasActuales(){
    Calendar c = Calendar.getInstance();
    Quincena q = new Quincena();
    DateFormat sdf = new SimpleDateFormat("yyyy");
    String anio = sdf.format(c.getTime()).substring(2, 3);
    String nombre;
    // 1ER QUINCENA DE ENERO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.JANUARY);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Ene " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE ENERO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Ene " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE FEBRERO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.FEBRUARY);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Feb " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE FEBRERO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    if((c.get(Calendar.YEAR) % 4 == 0) && (c.get(Calendar.YEAR) % 100 != 0) || (c.get(Calendar.YEAR) % 400 == 0)){
      c.set(Calendar.DAY_OF_MONTH, 29);
    }else{
      c.set(Calendar.DAY_OF_MONTH, 28);
    }
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Feb " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE MARZO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.MARCH);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Mar " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE MARZO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Mar " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE ABRIL
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.APRIL);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Abr " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE ABRIL
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 30);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Abr " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE MAYO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.MAY);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "May " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE MAYO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "May " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE JUNIO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.JUNE);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Jun " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE JUNIO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 30);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Jun " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE JULIO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.JULY);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Jul " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE JULIO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Jul " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE AGOSTO
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.AUGUST);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Ago " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE AGOSTO
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Ago " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE SEPTIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.SEPTEMBER);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Sep " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE SEPTIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 30);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Ago " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE OCTUBRE
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.OCTOBER);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Oct " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE OCTUBRE
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Oct " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE NOVIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.NOVEMBER);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Nov " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE NOVIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 30);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Nov " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 1A QUINCENA DE DICIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.MONTH, Calendar.DECEMBER);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Dic " + anio + "-1";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    // 2A QUINCENA DE DICIEMBRE
    c.set(Calendar.DAY_OF_MONTH, 16);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    q.setFechaInicio(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 31);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    q.setFechaFin(c.getTime());
    nombre = "Dic " + anio + "-2";
    q.setNombre(nombre);
    if(!quincenaDao.insertar(q)){
      return false;
    }
    return true;
  }
  
}
