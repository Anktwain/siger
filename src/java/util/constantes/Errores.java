
package util.constantes;

/**
 *
 * @author Pablo
 */
public interface Errores {
  public static final String CAMPO_VACIO = "El campo no debe encontrarse vacío.";
  public static final String CAMPO_DESBORDADO = "El campo excede el límite de caracteres permitidos.";
  public static final String CAMPO_ENTERO_REQUERIDO = "El campo no contiene un número entero o rebasa los límites del tipo int(4 bytes).";
  public static final String CAMPO_DDMMAAA_REQUERIDO = "El campo no contiene una fecha en el formato DD/MM/AAAA";
  public static final String CAMPO_FECHA_INVALIDO = "Algun(os) valor(es) en el campo fecha excede(n) los límites de una fecha válida.";
  public static final String CAMPO_FLOAT_REQUERIDO = "El campo no contiene un valor Float o rebasa los límites del tipo Float(4 bytes).";
  public static final String ANIO_FUERA_DE_RANGO_EXCEL = "El año está fuera del rango permitido por Excel.";
  public static final String FILA_CON_CELDAS_VACIAS = "Se esperaba un valor, dado que alguna(s) otra(s) celda(s) del fac lo contiene(n).";
  public static final String FORMATO_EMAIL_INVALIDO = "El formato de correo electrónico no es válido.";
}
