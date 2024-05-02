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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.aldairgonzalez.system.Main;
import javafx.scene.control.Button;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.CategoriaProducto;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class MenuCategoriaProductoController implements Initializable {
    
    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TableView tblCategorias;
    @FXML
    TableColumn colCategoriaId, colNombreCategoria, colDescripcion;
    @FXML
    Button btnAgregar, btnEditar, btnBuscar, btnElimnar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void cargarListaCategorias(){
        tblCategorias.setItems(listarCategorias());
    }
    
    public ObservableList<CategoriaProducto> listarCategorias(){
        ArrayList<CategoriaProducto> categorias = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCategoriaProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int categoriaId = resultSet.getInt("categoriaProductoId");
                String nombre = resultSet.getString("nombreCategoria");
                String descripcion = resultSet.getString("descripcionCategoria");
                
                categorias.add(new CategoriaProducto(categoriaId,nombre,descripcion));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        
        return FXCollections.observableList(categorias);
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
