package com.minimizing.system;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.minimizing.system.util.HibernateUtil;

// Clase encargada de manejar las operaciones de acceso a datos para la entidad Producto
public class InventarioDAO {

    // Método para guardar un producto en la base de datos
    public void saveProducto(Producto producto) {
        // Obtiene una nueva sesión de Hibernate a través de HibernateUtil
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Inicia una nueva transacción
            transaction = session.beginTransaction();
            // Guarda el producto en la base de datos
            session.save(producto);
            // Si no hubo errores, se confirma la transacción
            transaction.commit();
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción para evitar inconsistencias
            if (transaction != null) {
                transaction.rollback();
            }
            // Imprime el stack trace del error para diagnóstico
            e.printStackTrace();
        } finally {
            // Asegura el cierre de la sesión después de que se complete la operación
            session.close();
        }
    }
}
