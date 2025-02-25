package com.minimizing.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

// Controlador para gestionar las interacciones de la interfaz de usuario en el Dashboard
public class DashboardController {

    // Anotación @FXML para vincular este botón con el correspondiente en el archivo FXML
    @FXML
    private Button myButton;

    // Método que se ejecuta cuando el botón es presionado
    // La anotación @FXML permite que el método sea invocado desde el archivo FXML
    @FXML
    public void onButtonClick() {
        // Imprime un mensaje en la consola cada vez que el botón es presionado
        System.out.println("Button clicked!");
    }
}
