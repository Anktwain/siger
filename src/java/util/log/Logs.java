/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.log;

import org.apache.log4j.Logger;

/**
 *
 * @author brionvega
 */
public class Logs {
    /* Los loggers son los componentes más esenciales de un proceso de logging.
     Son los responsables de capturar la información de logging.
     */
    /* Se instancia un Logger estático global para la clase, basado en el nombre
     de la clase.
     */

    public static final Logger log = Logger.getLogger(Logs.class);

    public static void main(String[] args) {
        if (log.isTraceEnabled()) {
            log.trace("mensaje de TRACE");
        }

        if (log.isDebugEnabled()) {
            log.debug("mensaje de DEBUG");
        }

        if (log.isInfoEnabled()) {
            log.info("mensaje de INFO");
        }

        
        log.warn("mensaje de WARN");
        log.error("mensaje de ERROR");
        log.fatal("mensaje de FATAL");
    }
}

// Referencias:
// http://www.javatutoriales.com/2011/04/log4j-para-creacion-de-eventos-de-log.html
