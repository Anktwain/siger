package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * La clase {@code MD5} permite ...
 *
 * @author
 * @author
 * @author antonio
 * @since SigerWeb2.0
 */
public class MD5 {

    private static final char[] CONSTANTES_HEX = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     *
     *
   * @param palabra
     * @return
     */
    public static String encriptar(String palabra) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(palabra.getBytes());
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int bajo = (int) (bytes[i] & 0x0f);
                int alto = (int) ((bytes[i] & 0xf0) >> 4);
                sb.append(CONSTANTES_HEX[alto]);
                sb.append(CONSTANTES_HEX[bajo]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }
}
