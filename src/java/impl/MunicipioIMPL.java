/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MunicipioDAO;
import dto.Municipio;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Eduardo
 */
public class MunicipioIMPL implements MunicipioDAO{
    @Override
    public List<Municipio>buscarMunicipiosPorEstado(int idEstado){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Municipio> municipios;
        try { 
            municipios = sesion.createSQLQuery("select * from municipio where id_estado_estados = " + idEstado + " order by nombre asc;").addEntity(Municipio.class).list();
        } catch(HibernateException he) {
            municipios = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return municipios;
    }
    
    @Override
    public List<Municipio>buscarTodo(){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Municipio> municipios;
        try { 
            // LA CONSULTA NO ES ASI, ESTA ES UNA CONSULTA DE PRUEBA
            municipios = sesion.createSQLQuery("select * from municipio where id_estado_estados = 9 order by nombre asc;").addEntity(Municipio.class).list();
            // CONSULTA ORIGINAL
            // municipios = sesion.createSQLQuery("select * from municipio order by nombre asc;").addEntity(Municipio.class).list();
        } catch(HibernateException he) {
            municipios = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return municipios;
    }
    
    @Override
    public Municipio buscar(int idMunicipio){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Municipio municipio;
        try { 
            municipio = (Municipio) sesion.createSQLQuery("select * from municipio where id_municipio = " + idMunicipio + " ;").uniqueResult();
        } catch(HibernateException he) {
            municipio = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return municipio;
    }
    
    private void cerrar(Session sesion) {
        if (sesion.isOpen()) {
            sesion.close();
        }
    }
}
