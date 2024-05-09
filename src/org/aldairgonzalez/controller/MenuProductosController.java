/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.CategoriaProducto;
import org.aldairgonzalez.model.Distribuidor;
import org.aldairgonzalez.model.Producto;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuProductosController implements Initializable {
    
    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    private List<File> files = null;
    
    @FXML
    TextField tfProductoId,tfNombre,tfCantidad,tfPrecioUnitario,tfPrecioMayor,tfPrecioCompra,tfBuscarProducto;
    @FXML
    TextArea taDescripcion;
    @FXML
    ComboBox cmbDistribuidor, cmbCategoria;
    @FXML
    TableView tblProductos;
    @FXML
    TableColumn colProductoId, colNombre,colDescripcion,colCantidad,colPrecioUnitario,colPrecioMayor,colPrecioCompra,colDistribuidor,colCategoria;
    @FXML
    ImageView  imgMostar,imgCargar;
    @FXML
    Button btnRegresar, btnGuardar, btnVaciar, btnBuscar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosProductos();
        cmbDistribuidor.setItems(listarDistribuidores());
        cmbCategoria.setItems(listarCategorias());
    }    

    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }else if(event.getSource() == btnBuscar){
            tblProductos.getItems().clear();
            if(tfBuscarProducto.getText().equals("")){
                cargarDatosProductos();
            }else{
                try{
                    tblProductos.getItems().add(buscarProducto());
                    colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
                    colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
                    colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
                    colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
                    colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
                    colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
                    colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
                    colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String>("distribuidor"));
                    colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoriaProducto"));
                
                    Producto producto = buscarImagen();
                    if(producto != null){
                        InputStream file = producto.getImagenProducto().getBinaryStream();
                        Image imagen = new Image(file);
                        imgMostar.setImage(imagen);
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } else if(event.getSource() == btnGuardar){
            if(tfProductoId.getText().isEmpty()){
                agregarProducto();
                cargarDatosProductos();
            }else {
                editarProductos();
                cargarDatosProductos();
            }
        }
    }
    
    public void vaciarCampos(){
        tfProductoId.clear();
        tfNombre.clear();
        tfCantidad.clear();
        tfPrecioUnitario.clear();
        tfPrecioMayor.clear();
        tfPrecioCompra.clear();
        tfBuscarProducto.clear();
        taDescripcion.clear();
        cmbDistribuidor.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
        imgMostar.setImage(null);
        
    }
    
    public void handleOnDrop(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    
    public void handleOnDrag(DragEvent event){
        try{
            files = event.getDragboard().getFiles();
            FileInputStream file = new FileInputStream(files.get(0));
            Image image = new Image(file);
            imgCargar.setImage(image);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatosEditar(){
        Producto p = (Producto)tblProductos.getSelectionModel().getSelectedItem();
        if(p != null){
            tfProductoId.setText(Integer.toString(p.getProductoId()));
            tfNombre.setText(p.getNombreProducto());
            tfCantidad.setText(Integer.toString(p.getCantidadStock()));
            tfPrecioUnitario.setText(Double.toString(p.getPrecioVentaUnitario()));
            tfPrecioMayor.setText(Double.toString(p.getPrecioVentaMayor()));
            tfPrecioCompra.setText(Double.toString(p.getPrecioCompra()));
            taDescripcion.setText(p.getDescripcionProducto());
            cmbDistribuidor.getSelectionModel().select(obtenerIndexDistribuidor());
            cmbCategoria.getSelectionModel().select(obtenerIndexCategoria());
        }
    }
    
    public void cargarDatosProductos(){
        tblProductos.setItems(listarProductos());
        colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String>("distribuidor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoriaProducto"));
    }
    
    public int obtenerIndexDistribuidor(){
        int index = 0;
        for(int i = 0 ; i<= cmbDistribuidor.getItems().size() ; i++){
            String distribuidorCmb = cmbDistribuidor.getItems().get(i).toString();
            String distribuidorTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDistribuidor();
            if(distribuidorCmb.equals(distribuidorTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexCategoria(){
        int index = 0;
        for(int i = 0 ; i <= cmbCategoria.getItems().size() ; i++){
         String categoriaCmb = cmbCategoria.getItems().get(i).toString();
         String categoriaTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCategoriaProducto();
         if(categoriaCmb.equals(categoriaTbl)){
             index = i;
             break;
         }
        }
        
        return index;
    }
    
    public ObservableList<Producto>listarProductos(){
        ArrayList<Producto> productos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarProducto()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombre = resultSet.getString("nombreProducto");
                String descripcion = resultSet.getString("descripcionProducto");
                int cantidad = resultSet.getInt("cantidadStock");
                double unitario = resultSet.getDouble("precioVentaUnitario");
                double mayor = resultSet.getDouble("precioVentaMayor");
                double precio = resultSet.getDouble("precioCompra");
                Blob imagen = resultSet.getBlob("imagenProducto");
                String distribuidor = resultSet.getString("distribuidor");
                String categoria = resultSet.getString("categoriaProducto");
                
                productos.add(new Producto(productoId,nombre,descripcion,cantidad,unitario,mayor,precio,imagen,distribuidor,categoria));
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
        
        return FXCollections.observableList(productos);
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
    
    public ObservableList<Distribuidor> listarDistribuidores(){
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombre = resultSet.getString("nombreDistribuidor");
                String direccion = resultSet.getString("direccionDistribuidor");
                String nit = resultSet.getString("nitDistribuidor");
                String telefono = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                
                distribuidores.add(new Distribuidor(distribuidorId,nombre,direccion,nit,telefono,web));
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
        return FXCollections.observableList(distribuidores);
    } 
    
    public void agregarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProducto(?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tfNombre.getText());
            statement.setString(2,taDescripcion.getText());
            statement.setInt(3, Integer.parseInt(tfCantidad.getText()));
            statement.setDouble(4, Double.parseDouble(tfPrecioUnitario.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioMayor.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioCompra.getText()));
            InputStream img = new FileInputStream(files.get(0));
            statement.setBinaryStream(7, img);
            statement.setInt(8, ((Distribuidor)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9, ((CategoriaProducto)cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
            statement.execute();
            
        }catch(Exception e){
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
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarProducto(?,?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1,tfNombre.getText());
            statement.setString(2,taDescripcion.getText());
            statement.setInt(3, Integer.parseInt(tfCantidad.getText()));
            statement.setDouble(4, Double.parseDouble(tfPrecioUnitario.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioMayor.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioCompra.getText()));
            InputStream img = new FileInputStream(files.get(0));
            statement.setBinaryStream(7, img);
            statement.setInt(8, ((Producto)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9, ((Producto)cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
            statement.execute();
        }catch(Exception e){
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
                System.out.println(e.getMessage());
            }
        }
    }
    
    public Producto buscarProducto(){
        Producto producto = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarProducto(?)";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombre = resultSet.getString("nombreProducto");
                String descripcion = resultSet.getString("descripcionProducto");
                int cantidad = resultSet.getInt("cantidadStock");
                double unitario = resultSet.getDouble("precioVentaUnitario");
                double mayor = resultSet.getDouble("precioVentaMayor");
                double precio = resultSet.getDouble("precioCompra");
                String distribuidor = resultSet.getString("distribuidor");
                String categoria = resultSet.getString("categoriaProducto");
                
                producto = (new Producto(productoId,nombre,descripcion,cantidad,unitario,mayor,precio,distribuidor,categoria));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
              System.out.println(e.getMessage());
            }
        }
        
        return producto;
    }
    
    public Producto buscarImagen(){
        Producto producto = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarImagen(?)";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                Blob imagen = resultSet.getBlob("imagenProducto");
                
                producto = (new Producto(imagen));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
              System.out.println(e.getMessage());
            }
        }
        
        return producto;
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
