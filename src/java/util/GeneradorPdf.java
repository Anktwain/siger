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
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class GeneradorPdf {

  // VARIABLES DE CLASE
  private final Font tituloN1 = FontFactory.getFont("arial", 16, Font.BOLD, BaseColor.BLACK);
  private final Font tituloN2 = FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK);
  private final Font tituloN3 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);
  private final Font cuerpo = FontFactory.getFont("arial", 11, Font.NORMAL, BaseColor.BLACK);
  private final Font cuerpo2 = FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK);
  private final Font cuerpoNegritas = FontFactory.getFont("arial", 11, Font.BOLD, BaseColor.BLACK);
  private final Font cuerpoMini = FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK);
  private final Locale locale = new Locale("es", "MX");
  private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
  private Image foto;

  // CONSTRUCTOR
  public GeneradorPdf() {
  }

  // METODO QUE CREA UN ARCHIVO PDF
  public String crearPdf(String nombreArchivo, Credito credito, Direccion direccion, float saldoVencido) {
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
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      // TABULADOR
      String tabulador = "               ";
      // DATOS DEL DESTINATARIO
      p = new Paragraph(tabulador + credito.getDeudor().getSujeto().getNombreRazonSocial().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      if (direccion.getInterior() == null) {
        p = new Paragraph(tabulador + direccion.getCalle().toUpperCase() + " " + direccion.getExterior().toUpperCase(), cuerpo2);
        p.setAlignment(Chunk.ALIGN_LEFT);
        documento.add(p);
      } else {
        p = new Paragraph(tabulador + direccion.getCalle().toUpperCase() + " " + direccion.getExterior().toUpperCase() + " " + direccion.getInterior().toUpperCase(), cuerpo2);
        p.setAlignment(Chunk.ALIGN_LEFT);
        documento.add(p);
      }
      p = new Paragraph(tabulador + direccion.getColonia().getTipo().toUpperCase() + " " + direccion.getColonia().getNombre().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(tabulador + direccion.getMunicipio().getNombre().toUpperCase() + ", " + direccion.getEstadoRepublica().getAbreviatura().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(tabulador + "C.P. " + direccion.getColonia().getCodigoPostal(), cuerpo2);
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
      if (credito.getProducto().getFamilia().contains("CT EXPRESS")) {
        producto = "Crédito CT Express";
        texto1 = TextoPdfs.TEXTO_EXPRESS_1;
        texto2 = TextoPdfs.TEXTO_EXPRESS_2;
        if ((credito.getSubproducto().getNombre().contains("EXPRESS CT")) || (credito.getSubproducto().getNombre().contains("EXPRESS TPV"))) {
          texto4 = "Numero de cuenta asociado a su credito (" + credito.getNumeroCuenta() + ")";
        } else {
          texto4 = TextoPdfs.TEXTO_EXPRESS_4;
        }
      } else {
        producto = "Crédito TELMEX (SOFOM)";
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
      p = new Paragraph("A T E N T A M E N T E", cuerpo);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      p = new Paragraph("BUFETE DEL RIO, S.C.", cuerpo);
      p.setAlignment(Chunk.ALIGN_CENTER);
      documento.add(p);
      documento.add(new Paragraph(Chunk.NEWLINE));
      // AVISO DE PRIVACIDAD
      p = new Paragraph(TextoPdfs.AVISO_PRIVACIDAD, cuerpoMini);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      documento.close();
      archivo.close();
      return rutaArchivo;
    } catch (IOException | DocumentException e) {
      Logs.log.error("No se creo el documento PDF");
      Logs.log.error(e.getMessage());
      return "";
    }
  }

  // METODO QUE CREA UN ARCHIVO PDF PARA LOS CREDITOS DE QUEBRANTO
  public String crearPdfQuebranto(String nombreArchivo, Credito credito, Direccion direccion, float capital, float intereses, float saldoVencido, float total) {
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
      documento.add(new Paragraph(Chunk.NEWLINE));
      documento.add(new Paragraph(Chunk.NEWLINE));
      // TABULADOR
      String tabulador = "               ";
      // DATOS DEL DESTINATARIO
      p = new Paragraph(tabulador + credito.getDeudor().getSujeto().getNombreRazonSocial().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      if (direccion.getInterior() == null) {
        p = new Paragraph(tabulador + direccion.getCalle().toUpperCase() + " " + direccion.getExterior().toUpperCase(), cuerpo2);
        p.setAlignment(Chunk.ALIGN_LEFT);
        documento.add(p);
      } else {
        p = new Paragraph(tabulador + direccion.getCalle().toUpperCase() + " " + direccion.getExterior().toUpperCase() + " " + direccion.getInterior().toUpperCase(), cuerpo2);
        p.setAlignment(Chunk.ALIGN_LEFT);
        documento.add(p);
      }
      p = new Paragraph(tabulador + direccion.getColonia().getTipo().toUpperCase() + " " + direccion.getColonia().getNombre().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(tabulador + direccion.getMunicipio().getNombre().toUpperCase() + ", " + direccion.getEstadoRepublica().getAbreviatura().toUpperCase(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      p = new Paragraph(tabulador + "C.P. " + direccion.getColonia().getCodigoPostal(), cuerpo2);
      p.setAlignment(Chunk.ALIGN_LEFT);
      documento.add(p);
      // ZONA DE COMPARACION
      String producto, texto1, texto2, texto3, texto4, texto5;
      texto3 = TextoPdfs.TEXTO_GENERAL_3;
      texto5 = TextoPdfs.TEXTO_GENERAL_5;
      if (credito.getProducto().getFamilia().contains("CT EXPRESS")) {
        producto = "Crédito CT Express";
        texto1 = TextoPdfs.TEXTO_EXPRESS_1;
        texto2 = TextoPdfs.TEXTO_EXPRESS_2;
        if ((credito.getSubproducto().getNombre().contains("EXPRESS CT")) || (credito.getSubproducto().getNombre().contains("EXPRESS TPV"))) {
          texto4 = "Numero de cuenta asociado a su credito (" + credito.getNumeroCuenta() + ")";
        } else {
          texto4 = TextoPdfs.TEXTO_EXPRESS_4;
        }
      } else {
        producto = "Crédito TELMEX (SOFOM)";
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
      // TO FIX:
      // AQUI ES DONDE IRIA UNA IMPRESION ESPECIAL DE LAS VISITAS DE QUEBRANTO
      // ESTO REQUIERE UNA PREAPROBACION DEL BANCO EN CUESTION
      // ES IMPRESCINDIBLE QUE SE HAGA UNA REESTRUCTURACION ORDENADA
      // PUESTO QUE NO SABEMOS BIEN QUE OCURRA
      return rutaArchivo;
    } catch (IOException | DocumentException e) {
      Logs.log.error("No se creo el documento PDF");
      Logs.log.error(e.getMessage());
      return "";
    }
  }
}
