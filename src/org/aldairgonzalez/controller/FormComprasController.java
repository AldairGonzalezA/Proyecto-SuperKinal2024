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
import javafx.scene.control.TextField;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.dto.CompraDTO;
import org.aldairgonzalez.model.Compra;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class FormComprasController implements Initializable {

    private Main stage;
    private int op ;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfCompraId, tfFechaCompra, tfTotalCompra;
    @FXML
    Button btnRegresar, btnGuardar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(CompraDTO.getCompraDTO().getCompra() != null){
            cargarDatosCompra(CompraDTO.getCompraDTO().getCompra());
        }
    }    

    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.MenuComprasView();
        } else if(event.getSource() == btnGuardar){
            if(op == 1){
                agregarCompra();
                stage.MenuComprasView();
            } else if(op == 2){
                editarCompra();
                CompraDTO.getCompraDTO().setCompra(null);
                stage.MenuComprasView();
            }
        }
    }
    
    public void cargarDatosCompra(Compra compra){
        tfCompraId.setText(Integer.toString(compra.getCompraId()));
        tfFechaCompra.setText(compra.getFechaCompra());
        tfTotalCompra.setText(Double.toString(compra.getTotalCompra()));
    }
    
    public void agregarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfFechaCompra.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
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
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCompra(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(tfCompraId.getText()));
            statement.setString(2, tfFechaCompra.getText());
            statement.setString(3, tfTotalCompra.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
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

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
    
}
