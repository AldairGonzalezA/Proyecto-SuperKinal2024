/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.controller;

import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Producto;
import org.aldairgonzalez.model.Promocion;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class MenuPromocionesController implements Initializable {

    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfPrecioPromocion, tfPromocionId;
    @FXML
    TextArea taDescripcion;
    @FXML
    DatePicker dpFechaInicio, dpFechaFinalizacion;
    @FXML
    ComboBox cmbProductos;
    @FXML
    Button btnGuardar, btnRegresar, btnVaciar;
    @FXML
    TableView tblPromociones;
    @FXML
    TableColumn colPromocionId, colPrecio, colDescripcion, colFechaInicio,colFechaFinalizacion, colProducto;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbProductos.setItems(listarProductos());
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfPromocionId.getText().isEmpty()){
                agregarPromocion();
                cargarDatos();
            }else{
                editarPromocion();
                cargarDatos();
            }
        } else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    public void vaciarCampos(){
       tfPromocionId.clear();
       tfPrecioPromocion.clear();
       taDescripcion.clear();
       dpFechaInicio.setValue(null);
       dpFechaFinalizacion.setValue(null);
       cmbProductos.getSelectionModel().clearSelection();
       
    }
    
    public void cargarDatos(){
        tblPromociones.setItems(listarPromociones());
        colPromocionId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaInicio"));
        colFechaFinalizacion.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaFinalizacion"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, String>("producto"));
    } 
    
    public void cargarDatosEditar(){
        Promocion promo = (Promocion)tblPromociones.getSelectionModel().getSelectedItem();
        if(promo != null){
            tfPromocionId.setText(Integer.toString(promo.getPromocionId()));
            tfPrecioPromocion.setText(Double.toString(promo.getPrecioPromocion()));
            taDescripcion.setText(promo.getDescripcionPromocion());
            dpFechaInicio.setValue(promo.getFechaInicio().toLocalDate());
            dpFechaFinalizacion.setValue(promo.getFechaFinalizacion().toLocalDate());
            cmbProductos.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0 ; i<= cmbProductos.getItems().size() ; i++){
            String productoCmb = cmbProductos.getItems().get(i).toString();
            String productoTbl = ((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getProducto();
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public ObservableList<Promocion> listarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarPromociones()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precio = resultSet.getDouble("precioPromocion");
                String descripcion = resultSet.getString("descripcionPromocion");
                Date inicio = resultSet.getDate("fechaInicio");
                Date finalizacion = resultSet.getDate("fechaFinalizacion");
                String producto = resultSet.getString("producto");
                
                promociones.add(new Promocion(promocionId,precio,descripcion,inicio,finalizacion,producto));
            }
        }catch(SQLException e ){
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
        
        return FXCollections.observableList(promociones);
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
    
    public void agregarPromocion(){
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarPromocion(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(tfPrecioPromocion.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setDate(3, Date.valueOf(dpFechaInicio.getValue()));
            statement.setDate(4, Date.valueOf(dpFechaFinalizacion.getValue()));
            statement.setInt(5, ((Producto)cmbProductos.getSelectionModel().getSelectedItem()).getProductoId());
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
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarPromocion(){
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarPromocion(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocionId.getText()));
            statement.setDouble(2, Double.parseDouble(tfPrecioPromocion.getText()));
            statement.setString(3, taDescripcion.getText());
            statement.setDate(4, Date.valueOf(dpFechaInicio.getValue()));
            statement.setDate(5, Date.valueOf(dpFechaFinalizacion.getValue()));
            statement.setInt(6, ((Producto)cmbProductos.getSelectionModel().getSelectedItem()).getProductoId());
            
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
                System.out.println(e.getMessage());
            }
        }
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
