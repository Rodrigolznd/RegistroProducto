package com.minimizing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registro de Producto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false); // Para evitar que la ventana sea redimensionable

        // Logo en la parte superior izquierda
        ImageIcon logoIcon = new ImageIcon("logo.png"); 
        JLabel logoLabel = new JLabel(logoIcon);
        frame.add(logoLabel, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));

        // Componentes
        JLabel labelNombre = new JLabel("Nombre del producto:");
        labelNombre.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField textFieldNombre = new JTextField();
        textFieldNombre.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel labelPrecio = new JLabel("Precio:");
        labelPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField textFieldPrecio = new JTextField();
        textFieldPrecio.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField textFieldCantidad = new JTextField();
        textFieldCantidad.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton buttonRegistrar = new JButton("Registrar");
        buttonRegistrar.setFont(new Font("Arial", Font.BOLD, 14));

        // Layout con GridBagLayout
        frame.add(labelNombre, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0));
        frame.add(textFieldNombre, new GridBagConstraints(1, 1, 1, 1, 0.9, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
        frame.add(labelPrecio, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
        frame.add(textFieldPrecio, new GridBagConstraints(1, 2, 1, 1, 0.9, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        frame.add(labelCantidad, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
        frame.add(textFieldCantidad, new GridBagConstraints(1, 3, 1, 1, 0.9, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        frame.add(buttonRegistrar, new GridBagConstraints(0, 4, 2, 1, 1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));

        frame.setVisible(true);

        buttonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Captura de datos de la interfaz gráfica
                String nombre = textFieldNombre.getText();
                double precio = 0;
                int cantidad = 0;

                // Validaciones básicas
                try {
                    precio = Double.parseDouble(textFieldPrecio.getText());
                    cantidad = Integer.parseInt(textFieldCantidad.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor ingrese valores válidos para el precio y la cantidad.");
                    return;
                }

                if (nombre.length() > 50) { // Validación de longitud del nombre
                    JOptionPane.showMessageDialog(frame, "El nombre del producto no puede tener más de 50 caracteres.");
                    return;
                }

                // Creación de objeto Producto
                Producto producto = null;
                try {
                    producto = new Producto(nombre, precio, cantidad);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    return;
                }

                // Conexión a la base de datos
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory_db", "root", "");
                    String query = "INSERT INTO Producto (nombre, precio, cantidad) VALUES (?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                        statement.setString(1, producto.getNombre());
                        statement.setDouble(2, producto.getPrecio());
                        statement.setInt(3, producto.getCantidad());
                        int affectedRows = statement.executeUpdate();

                        if (affectedRows > 0) {
                            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    producto.setId(generatedKeys.getInt(1));
                                    JOptionPane.showMessageDialog(frame, "Producto registrado con éxito: " + producto.toString());
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error al insertar el producto: " + ex.getMessage());
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al conectar con la base de datos: " + ex.getMessage());
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error al cerrar la conexión: " + ex.getMessage());
                    }
                }
            }
        });
    }
}

class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;

    // Constructor
    public Producto(String nombre, double precio, int cantidad) throws IllegalArgumentException {
        setNombre(nombre);
        setPrecio(precio);
        setCantidad(cantidad);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) throws IllegalArgumentException {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) throws IllegalArgumentException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + "]";
    }
}
