/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EmailDAO;
import dto.Email;
import dto.Sujeto;
import impl.EmailIMPL;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Patrones;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class EmailBean implements Serializable {

    // Objeto Email, sus propiedades y acceso a la BD
    private Email email;

    private String direccion;
    private String tipo;

    private EmailDAO emailDao;

    // Construyendo...
    public EmailBean() {
        email = new Email();
        emailDao = new EmailIMPL();
    }

    public void agregar(Sujeto sujeto) {
        email.setDireccion(direccion);
        email.setTipo(tipo);
        email.setSujeto(sujeto);

        if (!validarCorreo()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo agregar el usuario", "El formato de correo electrónico no es válido."));
        } else if (emailDao.insertar(email)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Operación Exitosa",
                    "Se agregó un nuevo E-mail: " + direccion + " para: "
                    + " " + sujeto.getNombreRazonSocial()));
            limpiarEntradas();
            Logs.log.info("Se agregó objeto: Email");
        } else {
            Logs.log.error("No se pudo agregar objeto: Email.");
        }
    }

    public void limpiarEntradas() {
        direccion = null;
        tipo = null;
    }

    private boolean validarCorreo() {
        // Compila la cadena PATRON_EMAIL como un patrón
        Pattern patron = Pattern.compile(Patrones.PATRON_EMAIL);
        // Compara el correo con el patrón dado
        Matcher matcher = patron.matcher(direccion);
        return matcher.matches();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
