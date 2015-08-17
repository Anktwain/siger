/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.constantes;

/**
 *
 * @author brionvega
 */
public interface Patrones {
    public static final String PATRON_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    public static final String PATRON_RFC = "^[A-Z]{3,4}([0-9]{6})(([A-Z]|[0-9]){3})?$";
}
