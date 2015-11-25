/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.constantes;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface Gestiones {
  public static final List<String> TIPO_GESTION = Arrays.asList("VISITA DOMICILIARIA", "TELEFONIA", "CORPORATIVO");
  
  public static final List<String> DONDE_VISITA = Arrays.asList("CASA", "OFICINA", "DOMICILIO AVAL", "DOMICILIO REPRESENTANTE LEGAL", "DOMICILIO REFERENCIA");
          
  public static final List<String> DONDE_TELEFONIA = Arrays.asList("CASA", "OFICINA", "CELULAR", "AVAL", "REPRESENTANTE LEGAL", "REFERENCIA");
  
  public static final List<String> DONDE_CORPORATIVO = Arrays.asList("OFICINAS CENTRALES D.F.");
  
  public static final List<String> ASUNTO = Arrays.asList("RECADO DE COBRO", "FALLECIDO", "ILOCALIZABLE", "PRISION", "CONVENIO CON INSTITUCION", "ACLARACIONES", "CONVENIOS", "POSITIVOS", "NEGATIVOS", "RECORDATORIO DE PAGOS", "COMPROBANTES");
  
  public static final List<String> ASUNTO_CORPORATIVO = Arrays.asList("COMPROBANTES", "DIVERSOS IBR", "ESTATUS", "CORREO ELECTRONICO", "MENSAJE SMS", "LOCALIZACION");
  
  public static final List<String> TIPO_SUJETOS = Arrays.asList("TITULAR", "DIRECTOS", "LATERALES", "LEGALES", "AMISTADES", "LABORALES", "REFERENCIAS");
  
  public static final List<String> SUJETOS_DIRECTOS = Arrays.asList("PADRE", "MADRE", "HIJO(A)", "ESPOSO(A)", "HERMANO(A)", "ABUELO(A)");
  
  public static final List<String> SUJETOS_LATERALES = Arrays.asList("TIO(A)", "PRIMO(A)", "FAMILIAR");
  
  public static final List<String> SUJETOS_LEGALES = Arrays.asList("SUEGRO(A)", "CUÑADO(A)", "CONCUÑO(A)", "PADRASTRO", "MADRASTRA", "HERMANASTRO(A)");
  
  public static final List<String> SUJETOS_AMISTADES = Arrays.asList("COMPADRE", "COMADRE", "NOVIO(A)", "AMIGO(A)", "VECINO(A)", "SIRVIENTA");
  
  public static final List<String> SUJETOS_LABORALES = Arrays.asList("CONTADOR(A)", "GERENTE", "JEFE INMEDIATO", "TESORERO(A)", "COMPAÑERO(A)", "ASISTENTE", "EMPLEADO(A)", "SECRETARIA", "RECEPCIONISTA", "VIGILANTE");
  
  public static final List<String> SUJETOS_REFERENCIAS = Arrays.asList("AVAL", "REFERENCIA", "REPRESENTANTE LEGAL");
  
  public static final List<String> ESTATUS_INFORMATIVO = Arrays.asList("CONTACTO CON TITULAR", "CONVENIO DE PAGO", "FUERA DE SERVICIO", "INCUMPLIMIENTO DE PAGO", "ILOCALIZABLE", "MENSAJE CON TERCEROS", "MENSAJE EN CONTESTADORA", "RENUENTE AL PAGO", "VISITA", "ENVIO SEPOMEX", "CLIENTE HACE PAGO", "TELEFONO NO EXISTE", "ENVIO MAIL", "DETENER GESTION", "ENVIO SMS", "BUSQUEDA Y LOCALIZACION", "NO CONTESTA", "ACLARACION");
  
}
