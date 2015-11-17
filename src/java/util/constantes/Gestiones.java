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
  public static final List<String> TIPO_GESTION = Arrays.asList("VISITA DOMICILIARIA", "TELEFONIA", "CORPORATIVO", "AUTOMATICA");
  
  public static final List<String> DONDE_VISITA = Arrays.asList("CASA", "OFICINA", "DOMICILIO AVAL", "DOMICILIO REPRESENTANTE LEGAL", "DOMICILIO REFERENCIA");
          
  public static final List<String> DONDE_TELEFONIA = Arrays.asList("CASA", "OFICINA", "CELULAR", "AVAL", "REPRESENTANTE LEGAL", "REFERENCIA");
  
  public static final List<String> ASUNTO = Arrays.asList("RECADO DE COBRO", "FALLECIDO", "ILOCALIZABLE", "PRISION", "CONVENIO CON INSTITUCION", "ACLARACIONES", "CONVENIOS", "POSITIVOS", "NEGATIVOS", "RECORDATORIO DE PAGOS", "COMPROBANTES");
  
}
