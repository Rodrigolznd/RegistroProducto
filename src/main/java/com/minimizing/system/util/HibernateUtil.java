package com.minimizing.system.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Clase utilitaria para configurar y obtener la fábrica de sesiones de Hibernate
public class HibernateUtil {

    // Instancia estática de SessionFactory para gestionar las sesiones de Hibernate
    private static SessionFactory sessionFactory;

    // Bloque estático para inicializar la fábrica de sesiones al cargar la clase
    static {
        try {
            // Crear la fábrica de sesiones utilizando la configuración de Hibernate definida en el archivo hibernate.cfg.xml
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")  // Carga la configuración desde el archivo hibernate.cfg.xml
                    .addAnnotatedClass(com.minimizing.system.Producto.class)  // Registra la clase Producto como una entidad
                    .buildSessionFactory();  // Crea la fábrica de sesiones
        } catch (Exception e) {
            // Captura cualquier excepción durante la configuración e imprime el stack trace para diagnóstico
            e.printStackTrace();
        }
    }

    // Método estático para obtener la instancia de SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
