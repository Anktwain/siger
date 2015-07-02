/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author antonio
 */
public class MD5 {
    
    private static final char[] CONSTANTES_HEX = { '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    public static String encriptar(String palabra) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(palabra.getBytes());
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for( int i = 0; i < bytes.length; i++) {
                int bajo = (int)(bytes[i] & 0x0f);
                int alto = (int)((bytes[i] & 0xf0) >> 4);
                sb.append(CONSTANTES_HEX[alto]);
                sb.append(CONSTANTES_HEX[bajo]);
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }
}
