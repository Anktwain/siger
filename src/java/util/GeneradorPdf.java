/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dto.Credito;
import dto.Direccion;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import util.constantes.Directorios;
import util.constantes.TextoPdfs;

/**
 *
 * @author Eduardo
 */
public class GeneradorPdf {

  // VARIABLES DE CLASE
  private static final Font tituloN1 = FontFactory.getFont("arial", 16, Font.BOLD, BaseColor.BLACK);
  private static final Font tituloN2 = FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK);
  private static final Font tituloN3 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);
  private static final Font cuerpo = FontFactory.getFont("arial", 11, Font.NORMAL, BaseColor.BLACK);
  private static final Font cuerpo2 = FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK);
  private static final Font cuerpoNegritas = FontFactory.getFont("arial", 11, Font.BOLD, BaseColor.BLACK);
  private static final Font cuerpoMini = FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK);
  private static final Locale locale = new Locale("es", "MX");
  private static final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
  private static Image foto;
  private static Image foto2;

  // CONSTRUCTOR
  public GeneradorPdf() {
  }

  // METODO QUE ESCRIBE EN UN PDF YA EXISTENTE
  public static String crearPdf(String nombreArchivo, Credito credito, Direccion direccion, float saldoVencido) {
    try {
      Document documento = new Document(PageSize.LETTER, 30, 30, 20, 20);
      String rutaArchivo = Directorios.RUTA_WINDOWS_CARGA_VISITAS + nombreArchivo;
      FileOutputStream archivo;
      archivo = new FileOutputStream(rutaArchivo);
      PdfWriter writer = PdfWriter.getInstance(documento, archivo);
      writer.setInitialLeading(20);
      documento.open();
      documento.newPage();
      // ENCABEZADO
      foto = Image.getInstance(Directorios.RUTA_IMAGENES + "firma.jpg");
      foto.scaleToFit(100, 100);
      foto.setAbsolutePosition(60, 715);
      documento.add(foto);
      Paragraph p = new Paragraph("BUFETE DEL RIO, S.C.", tituloN1);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      p = new Paragraph("La Firma Marca la Diferencia ®", tituloN2);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      p = new Paragraph("Balboa 1111, Portales Sur, 03300, Ciudad de México", tituloN3);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      p = new Paragraph("(55) 5539 0104 con 10 Líneas         contacto@corporativodelrio.com", tituloN3);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      // ESPACIADO
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      // DATOS DEL DESTINATARIO
      p = new Paragraph(credito.getDeudor().getSujeto().getNombreRazonSocial(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(direccion.getCalle(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(direccion.getColonia().getTipo() + " " + direccion.getColonia().getNombre(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(direccion.getMunicipio().getNombre() + ", " + direccion.getEstadoRepublica().getAbreviatura(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph("C.P. " + direccion.getColonia().getCodigoPostal(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      // DATOS DEL CREDITO
      p = new Paragraph("Credito: " + credito.getNumeroCredito(), cuerpo);
      p.setAlignment(Chunk.ALIGN_RIGHT);
      documento.add(p);
      p = new Paragraph("Saldo vencido: " + nf.format(saldoVencido), cuerpo);
      p.setAlignment(Chunk.ALIGN_RIGHT);
      documento.add(p);
      // ZONA DE COMPARACION
      String producto, texto1, texto2, texto3, texto4, texto5;
      texto3 = TextoPdfs.TEXTO_GENERAL_3;
      texto5 = TextoPdfs.TEXTO_GENERAL_5;
      if (credito.getProducto().getNombre().contains("CREDITO EXPRESS CT")) {
        // CREDITOS SOFOM
        producto = "Crédito TELMEX (SOFOM)";
        texto1 = TextoPdfs.TEXTO_TELMEX_1;
        texto2 = TextoPdfs.TEXTO_TELMEX_2;
        texto4 = "Numero de cuenta asociado a su credito (" + credito.getNumeroCuenta() + ")";
      } else if (credito.getProducto().getNombre().contains("EXPRESS")) {
        // CREDITOS CT EXPRESS
        producto = "Credito CT Express";
        texto1 = TextoPdfs.TEXTO_EXPRESS_1;
        texto2 = TextoPdfs.TEXTO_EXPRESS_2;
        texto4 = TextoPdfs.TEXTO_EXPRESS_4;
      } else if (credito.getProducto().getNombre().contains("TELMEX")) {
        // CREDITOS LINEA TELMEX
        producto = "Crédito TELMEX";
        texto1 = TextoPdfs.TEXTO_TELMEX_1;
        texto2 = TextoPdfs.TEXTO_TELMEX_2;
        texto4 = TextoPdfs.TEXTO_TELMEX_4;
      } else {
        producto = credito.getProducto().getNombre();
        texto1 = TextoPdfs.TEXTO_TELMEX_1;
        texto2 = TextoPdfs.TEXTO_TELMEX_2;
        texto4 = TextoPdfs.TEXTO_TELMEX_4;
      }
      p = new Paragraph(producto, tituloN2);
      p.setAlignment(Chunk.ALIGN_RIGHT);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // CUERPO DEL DOCUMENTO
      // PARRAFO 1
      p = new Paragraph(texto1, cuerpo);
      p.setAlignment(Chunk.ALIGN_JUSTIFIED);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // PARRAFO 2
      p = new Paragraph(texto2, cuerpo);
      p.setAlignment(Chunk.ALIGN_JUSTIFIED);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // PARRAFO 3
      p = new Paragraph(texto3, cuerpo);
      p.setAlignment(Chunk.ALIGN_JUSTIFIED);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // PARRAFO 4
      p = new Paragraph(texto4, cuerpo);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // PARRAFO 5
      p = new Paragraph(texto5, cuerpo);
      p.setAlignment(Chunk.ALIGN_JUSTIFIED);
      documento.add(p);
      // ESPACIADO
      documento.add(new Paragraph(Chunk.NEWLINE));
      // PARRAFO 6
      p = new Paragraph(TextoPdfs.TEXTO_GENERAL_6, cuerpoNegritas);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      p = new Paragraph("A T E N T A M E N T E", cuerpo);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      p = new Paragraph("BUFETE DEL RIO, S.C.", cuerpo);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      // AVISO DE PRIVACIDAD
      p = new Paragraph(TextoPdfs.AVISO_PRIVACIDAD, cuerpoMini);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      documento.close();
      archivo.close();
      return rutaArchivo;
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
      return "";
    }
  }

}
