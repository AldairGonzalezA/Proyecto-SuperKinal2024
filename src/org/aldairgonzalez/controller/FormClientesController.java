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
import javafx.scene.control.TextField;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.dto.ClienteDTO;
import org.aldairgonzalez.model.Cliente;
import org.aldairgonzalez.system.Main;
import org.aldairgonzalez.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class FormClientesController implements Initializable {
    
    private Main stage;
    private int op;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resulSet = null;
    
    
    @FXML
    TextField tfNombre,tfApellido,tfTelefono, tfDireccion,tfNit, tfClienteId;
    @FXML
    Button btnAgregar,btnRegresar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(ClienteDTO.getClienteDTO().getCliente() != null){
            cargarDatos(ClienteDTO.getClienteDTO().getCliente());
        }
    }
    
    public void cargarDatos(Cliente cliente){
        tfClienteId.setText(Integer.toString(cliente.getClienteId()));
        tfNombre.setText(cliente.getNombre());
        tfApellido.setText(cliente.getApellido());
        tfTelefono.setText(cliente.getTelefono());
        tfDireccion.setText(cliente.getDireccion());
        tfNit.setText(cliente.getNit());
    }
    
    public void agregarClientes(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_AgregarClientes(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tfNombre.getText());
            statement.setString(2,tfApellido.getText());
            statement.setString(3,tfTelefono.getText());
            statement.setString(4, tfDireccion.getText());
            statement.setString(5,tfNit.getText());
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
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
    
    public void editarCliente(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EditarClientes(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(tfClienteId.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, tfApellido.getText());
            statement.setString(4, tfTelefono.getText());
            statement.setString(5, tfDireccion.getText());
            statement.setString(6, tfNit.getText());
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

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnAgregar) {
            if (op == 1) { 
                if(!tfNombre.getText().equals("") && !tfApellido.getText().equals("") && !tfDireccion.getText().equals("")){
                    agregarClientes();
                    stage.menuClientesView();
                    SuperKinalAlert.getInstance().mostrarAlertasInfo(401);
                } else {
                    SuperKinalAlert.getInstance().mostrarAlertasInfo(400);
                    tfNombre.requestFocus();
                    return;
                }
            } else if (op == 2) {
              if(!tfNombre.getText().equals("") && !tfApellido.getText().equals("") && !tfDireccion.getText().equals("")){
                  if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(500).get() == ButtonType.OK){
                      editarCliente();
                      ClienteDTO.getClienteDTO().setCliente(null);
                      stage.menuClientesView();
                  }
              }
            }
        } else if (event.getSource() == btnRegresar) {
            stage.menuClientesView();
            ClienteDTO.getClienteDTO().setCliente(null);
        }
    }
    
    public Main getStage(){
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
    
    
}
