/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ClienteDAO;
import dao.SujetoDAO;
import dto.Cliente;
import dto.Sujeto;
import impl.ClienteIMPL;
import impl.SujetoIMPL;
import util.constantes.Sujetos;

/**
 *
 * @author brionvega
 */
public class ClienteBean {
    // Cliente y Sujeto, porque un cliente es un sujeto:
    private Cliente cliente;
    private Sujeto sujeto;
    
    // Clases para acceso a datos
    private ClienteDAO clienteDao;
    private SujetoDAO sujetoDao;
    
    // Atributos de cliente:
    private String numeroCliente;
    private String curp;
    private String numeroSeguroSocial;
    
    // Atributos de sujeto:
    private String nombreRazonSocial;
    private String rfc;
    private int eliminado;
    
    // Construyendo...
    public ClienteBean() {
        cliente = new Cliente();
        clienteDao = new ClienteIMPL();
        sujeto = new Sujeto();
        sujetoDao = new SujetoIMPL();
        eliminado = Sujetos.ACTIVO;
    }
    
    private void agregar() {
        // Verificamos que los campos obligatorios estén llenos.
    }
    
    // Agregar un nuevo cliente
    private void agregarSujeto() {
        // primero agregará un sujeto...
        sujeto.setNombreRazonSocial(nombreRazonSocial);
        sujeto.setRfc(rfc);
        sujeto.setEliminado(eliminado);
        
        // luego un cliente...
        cliente.setNumeroCliente(numeroCliente);
        cliente.setCurp(curp);
        cliente.setNumeroSeguroSocial(numeroSeguroSocial);
        
        sujetoDao.insertar(sujeto);
    }
}
