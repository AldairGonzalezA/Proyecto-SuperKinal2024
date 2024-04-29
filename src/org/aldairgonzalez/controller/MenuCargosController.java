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
import javafx.scene.control.cell.PropertyValueFactory;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Cargo;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuCargosController implements Initializable {

    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TableView tblCargos;
    @FXML
    TableColumn colCargoId, colCargo, colDescripcion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarListaCargos();
    }    

    
    
    public void cargarListaCargos(){
        tblCargos.setItems(listarCargos());
        colCargoId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("cargoId"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcion"));
    }
    
    public ObservableList<Cargo>listarCargos(){
        ArrayList<Cargo> cargos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCargos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombre = resultSet.getString("nombreCargo");
                String descripcion = resultSet.getString("descripcionCargo");
                
                cargos.add(new Cargo(cargoId,nombre,descripcion));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally {
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
        return FXCollections.observableList(cargos);
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
