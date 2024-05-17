/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.dto.CargoDTO;
import org.aldairgonzalez.model.Cargo;
import org.aldairgonzalez.system.Main;
import org.aldairgonzalez.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class FormCargosController implements Initializable {

    
    private Main stage;
    private int op;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resulSet = null;
    
    @FXML
    TextField tfNombreCargo, tfCargoId;
    @FXML
    TextArea taDescripcionCargo;
    @FXML
    Button btnRegresar, btnGuardar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(CargoDTO.getCargoDTO().getCargo() != null){
            cargarDatosCargo(CargoDTO.getCargoDTO().getCargo());
        }
    }    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuCargosView();
        } else if(event.getSource() == btnGuardar){
            if(op == 1){
                if(!tfNombreCargo.getText().equals("") && !taDescripcionCargo.getText().equals("")){
                    agregarCargos();
                    stage.menuCargosView();
                    SuperKinalAlert.getInstance().mostrarAlertasInfo(401);
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertasInfo(400);
                    tfNombreCargo.requestFocus();
                    return;
                }
            } else if(op == 2){
               if(!tfNombreCargo.getText().equals("") && !taDescripcionCargo.getText().equals("")){
                   if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(500).get() == ButtonType.OK){
                        editarCargos();
                        CargoDTO.getCargoDTO().setCargo(null);
                        stage.menuCargosView();
                   }
               }
            }
        } else if(event.getSource() == btnRegresar){
            stage.menuCargosView();
            CargoDTO.getCargoDTO().setCargo(null);
        }
    }
    
    public void cargarDatosCargo(Cargo cargo){
        tfCargoId.setText(Integer.toString(cargo.getCargoId()));;
        tfNombreCargo.setText(cargo.getNombreCargo());
        taDescripcionCargo.setText(cargo.getDescripcion());
    }
    
    public void agregarCargos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCargo(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tfNombreCargo.getText());
            statement.setString(2,taDescripcionCargo.getText());
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public void editarCargos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(tfCargoId.getText()));
            statement.setString(2, tfNombreCargo.getText());
            statement.setString(3, taDescripcionCargo.getText());
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
    
}
