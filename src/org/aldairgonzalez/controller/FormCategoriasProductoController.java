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
import org.aldairgonzalez.dto.CategoriaProductoDTO;
import org.aldairgonzalez.model.CategoriaProducto;
import org.aldairgonzalez.system.Main;
import org.aldairgonzalez.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class FormCategoriasProductoController implements Initializable {

    private Main stage;
    private int op;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfCategoriaId,  tfNombreCategoria;
    @FXML
    TextArea taDescripcion;
    @FXML
    Button btnRegresar, btnGuardar; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto() != null){
            cargarDatosCategoria(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto());
        }
    }    

    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.MenuCategoriaProductoView();
        } else if(event.getSource() == btnGuardar){
          if(op == 1){
              if(!tfNombreCategoria.getText().isEmpty() && !taDescripcion.getText().isEmpty()){
                  agregarCategoria();
                  stage.MenuCategoriaProductoView();
                  SuperKinalAlert.getInstance().mostrarAlertasInfo(401);
              } else {
                  SuperKinalAlert.getInstance().mostrarAlertasInfo(400);
                  tfNombreCategoria.requestFocus();
                  return;
              }
          }else if(op == 2){
              if(!tfNombreCategoria.getText().isEmpty() && !taDescripcion.getText().isEmpty()){
                  if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(500).get() == ButtonType.OK){
                      editarCategoria();
                      CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
              stage.MenuCategoriaProductoView();
                  }
              }
          }
        }
    }
    
    public void cargarDatosCategoria(CategoriaProducto categoria){
        tfCategoriaId.setText(Integer.toString(categoria.getCategoriaProductoId()));
        tfNombreCategoria.setText(categoria.getNombreCategoria());
        taDescripcion.setText(categoria.getDescrippcionCategoria());
    }
    
    public void agregarCategoria(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCategoriaProductos(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tfNombreCategoria.getText());
            statement.setString(2,taDescripcion.getText());
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
    
    public void editarCategoria(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCategoriaProductos(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(tfCategoriaId.getText()));
            statement.setString(2, tfNombreCategoria.getText());
            statement.setString(3, taDescripcion.getText());
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

    public void setOp(int op) {
        this.op = op;
    }
    
    public int getOp(){
        return op;
    }
    
}
