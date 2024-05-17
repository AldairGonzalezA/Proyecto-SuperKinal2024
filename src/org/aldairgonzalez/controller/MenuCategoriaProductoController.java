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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.aldairgonzalez.system.Main;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.dto.CategoriaProductoDTO;
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
    Button btnAgregar, btnEditar, btnBuscar, btnEliminar, btnRegresar;
    @FXML
    TextField tfCategoriaId;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cargarListaCategorias();
    }    

    public void cargarListaCategorias(){
        tblCategorias.setItems(listarCategorias());
        colCategoriaId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer>("categoriaProductoId"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("nombreCategoria"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<CategoriaProducto,String>("descrippcionCategoria"));
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        } else if(event.getSource() == btnEliminar){
            int cateId = ((CategoriaProducto)tblCategorias.getSelectionModel().getSelectedItem()).getCategoriaProductoId();
            eliminarCategoria(cateId);
            
        }else if(event.getSource() == btnBuscar){
            tblCategorias.getItems().clear();
            if(tfCategoriaId.getText().equals("")){
                cargarListaCategorias();
            } else {
                tblCategorias.getItems().add(buscarCategoria());
                colCategoriaId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer>("categoriaProductoId"));
                colNombreCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("nombreCategoria"));
                colDescripcion.setCellValueFactory(new PropertyValueFactory<CategoriaProducto,String>("descrippcionCategoria"));
            }
        } else if(event.getSource() == btnAgregar){
            stage.formCategoriasProductoView(1);
            
        } else if(event.getSource() == btnEditar){
            CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto((CategoriaProducto)tblCategorias.getSelectionModel().getSelectedItem());
            stage.formCategoriasProductoView(2);
        }
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
                String descripcion = resultSet.getString("descrippcionCategoria");
                
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
    
    public void eliminarCategoria(int cateId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCategoriaProductos(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, cateId);
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
            }catch(SQLException e ){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public CategoriaProducto buscarCategoria(){
        CategoriaProducto categorias = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCategoriaProductos(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCategoriaId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int categoriaId = resultSet.getInt("categoriaProductoId");
                String nombre = resultSet.getString("nombreCategoria");
                String descripcion = resultSet.getString("descrippcionCategoria");
                
                categorias = (new CategoriaProducto(categoriaId,nombre,descripcion));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            
        }
        return categorias;
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
