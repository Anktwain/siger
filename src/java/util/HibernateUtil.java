/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author brionvega
 */
public class HibernateUtil {

  private static SessionFactory sessionFactory;

  static {
    try {
      // Create the SessionFactory from standard (hibernate.cfg.xml) 
      // config file.
      Configuration configuration = new Configuration().configure();
      ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
      sessionFactory = new Configuration().configure().buildSessionFactory(serviceRegistry);
    } catch (Throwable ex) {
      // Log the exception. 
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  /**
   *
   * @return
   */
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
