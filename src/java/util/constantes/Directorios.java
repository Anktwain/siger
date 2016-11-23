package util.constantes;

/**
 *
 * @author brionvega
 */
public interface Directorios {

  /**
   *
   */
  public static final String RUTA_IMAGENES_DE_PERFIL = "/usuarios/";
  public static final String RUTA_REMESAS = "Remesas/";
  public static final String RUTA_COLONIAS = "CSV/coloniasCP";
  public static final String RUTA_ASIGNACION = "Asignacion/";
  public static final String RUTA_PROGRESO_ADMIN = "Progreso/Admin/";
  public static final String RUTA_PROGRESO_GESTOR = "Progreso/Gestor/";
  public static final String RUTA_COLUMNAS_UTILES = "Remesas/columnas.txt";

  // RUTAS WINDOWS
  public static final String RUTA_IMAGENES = "D:\\img\\";
  public static final String RUTA_WINDOWS_CARGA_COMPROBANTES = "D:\\ApacheSoftwareFoundation\\Tomcat9.0\\webapps\\comprobantes\\";
  public static final String RUTA_WINDOWS_CARGA_VISITAS = "D:\\ApacheSoftwareFoundation\\Tomcat9.0\\webapps\\pdfs\\";
  public static final String RUTA_WINDOWS_PERIODO_IMPRESIONES = "C:\\Periodo_Impresiones\\";
  public static final String RUTA_CORREOS = "CorreosInbursa/";
  public static final String RUTA_WINDOWS_REPORTES_GESTIONES = "D:\\reportesGestiones\\";
  // BUG:
  // ruta provisional, debera sustituirse para produccion en el servidor por la unidad D:
  public static final String RUTA_WINDOWS_CARGA_REMESA = "C:\\cargasDePruebaNuevoSiger\\";
  
  // RUTAS BINAH
  public static final String RUTA_SERVIDOR_WEB_COMPROBANTES = "http://binah:8090/comprobantes/";
  public static final String RUTA_SERVIDOR_WEB_VISITAS = "http://binah:8090/pdfs/";
  
}
