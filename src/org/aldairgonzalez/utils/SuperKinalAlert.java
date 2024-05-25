/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author informatica
 */
public class SuperKinalAlert {
    private static SuperKinalAlert instance;
    
    private SuperKinalAlert(){
        
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    public void mostrarAlertasInfo(int code) {
        if (code == 400) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Campos Pendientes");
            alert.setContentText("Algunos campos necesarios para el registro estan pendientes");
            alert.showAndWait();
 
        } else if (code == 401) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmacion de registro");
            alert.setHeaderText("Confirmacion de registro");
            alert.setContentText("El registro se ha creado con exito!!!!!");
            alert.showAndWait();
        } else if(code == 602){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usuario Incorrecto");
            alert.setHeaderText("Usuario Incorrecto");
            alert.setContentText("Verifique el Usuario");
            alert.showAndWait();
        } else if(code == 605){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contraseña Incorrecta");
            alert.setHeaderText("Contraseña Incorrecta");
            alert.setContentText("Verifique la contraseña");
            alert.showAndWait();
        }
    }
 
    public Optional<ButtonType> mostrarAlertaConfirmacion(int code) {
        Optional<ButtonType> action = null;
        if (code == 405) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminacion de registro");
            alert.setHeaderText("Eliminacion de registro");
            alert.setContentText("¿Desea confirmar la eliminacion de registro?");
            action = alert.showAndWait();
        } else if(code == 500){
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edicion de Registro");
            alert.setHeaderText("Edicion de Registro");
            alert.setContentText("¿Desea confirmar la edicion del registro?");
            action = alert.showAndWait();
        }
        return action;
    }
}
