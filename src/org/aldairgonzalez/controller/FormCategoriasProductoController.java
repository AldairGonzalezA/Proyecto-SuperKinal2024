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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.aldairgonzalez.model.CategoriaProducto;
import org.aldairgonzalez.system.Main;

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
    TextField tfCategoriaId,  tfNombreCategoria, tfDescripcionCategoria;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void cargarDatosCategoria(CategoriaProducto categoria){
        tfCategoriaId.setText(Integer.toString(categoria.getCategoriaProductoId()));
        tfNombreCategoria.setText(categoria.getNombreCategoria());
        tfDescripcionCategoria.setText(categoria.getDescrippcionCategoria());
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
