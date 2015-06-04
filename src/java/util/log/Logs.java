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
    private static Logger log = Logger.getLogger(Logs.class);
    
    public static void main(String[] args) {
        if(log.isTraceEnabled()) {
            log.trace("mensaje de trace");
        }
        
        if(log.isDebugEnabled()) {
            log.debug("mensaje de debug");
        }
        
        if(log.isInfoEnabled()) {
            log.info("mensaje de info");
        }
        
        log.warn("mensaje de warn");
        log.error("mensaje de error");
        log.fatal("mensaje de fatal");
    }
}


// Referencias:
// http://www.javatutoriales.com/2011/04/log4j-para-creacion-de-eventos-de-log.html