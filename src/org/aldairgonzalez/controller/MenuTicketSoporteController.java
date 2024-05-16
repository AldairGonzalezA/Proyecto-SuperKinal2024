/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
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
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Cliente;
import org.aldairgonzalez.model.DetalleFactura;
import org.aldairgonzalez.model.Factura;
import org.aldairgonzalez.model.TicketSoporte;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuTicketSoporteController implements Initializable {
    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    
    @FXML
    Button btnGuardar, btnRegresar, btnVaciar;
    @FXML
    ComboBox cmbEstatus, cmbClientes, cmbFacturas;
    @FXML
    TableView tblTickets;
    @FXML
    TableColumn colTicketId,colDescripcion,colEstatus,colCliente,colFactura;
    @FXML
    TextArea taDescripcion;
    @FXML
    TextField tfTicketId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCmbEstatus();
        cmbClientes.setItems(listarClientes());
        cmbFacturas.setItems(listarDetalleFacturas());
        cargarDatos();
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnGuardar){
            if(tfTicketId.getText().isEmpty() ){
                agregarTickets();
                cargarDatos();
            }else{
                editarTickets();
                cargarDatos();
            }
        }else if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    //Vacia todos los campos para agregar un nuevo Ticket
    public void vaciarCampos(){
        tfTicketId.clear();
        taDescripcion.clear();
        cmbEstatus.getSelectionModel().clearSelection();
        cmbClientes.getSelectionModel().clearSelection();
    }
    
    //Cargar datos a la tableView
    public void cargarDatos(){
        tblTickets.setItems(listarTickets());
        colTicketId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("ticketSoporteId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("descripcionTicket"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("estatus"));
        colCliente.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("cliente"));
        colFactura.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("facturaId"));
        tblTickets.getSortOrder().add(colTicketId);
    }
    
    //Carga Datos en los campos a Editar
    public void cargarDatosEditar(){
        TicketSoporte ts = (TicketSoporte)tblTickets.getSelectionModel().getSelectedItem();
        if(ts != null){
            tfTicketId.setText(Integer.toString(ts.getTicketSoporteId()));
            cmbEstatus.getSelectionModel().select(0);
            cmbClientes.getSelectionModel().select(obtenerIndexCliente());
            taDescripcion.setText(ts.getDescripcionTicket());
        }
    }
    
    // Cargar el comboxClientes
    public int obtenerIndexCliente(){
        int index = 0;
        for(int i = 0 ; i <= cmbClientes.getItems().size() ; i++){
            String clienteCmb = cmbClientes.getItems().get(i).toString();
            String clienteTbl = ((TicketSoporte)tblTickets.getSelectionModel().getSelectedItem()).getCliente();
            if(clienteCmb.equals(clienteTbl)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    // cargar combox de estatus
    public void cargarCmbEstatus(){
        cmbEstatus.getItems().add("En Proceso");
        cmbEstatus.getItems().add("Finalizado");
    }

    public ObservableList<TicketSoporte> listarTickets(){
        ArrayList<TicketSoporte> tickets = new ArrayList<>();
        
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarTicketSoporte()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int ticketSoporteId = resultSet.getInt("ticketSoporteId");
                String descripcion = resultSet.getString("descripcionTicket");
                String estatus = resultSet.getString("estatus");
                String cliente = resultSet.getString("cliente");
                int facturaId = resultSet.getInt("facturaId");
                
                tickets.add(new TicketSoporte(ticketSoporteId,descripcion,estatus,cliente,facturaId));
                
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
        
        return FXCollections.observableList(tickets);
    }
    
    // Consulta sp_ListarClientes a la base de datos(llenar el combox)
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarClientes()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                String nit = resultSet.getString("nit");
                
                clientes.add(new Cliente(clienteId,nombre,apellido,telefono,direccion,nit));
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
           
        return FXCollections.observableList(clientes);
    }
    
    public ObservableList<DetalleFactura> listarDetalleFacturas(){
        ArrayList<DetalleFactura> detalleFacturas = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDetalleFactura()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                double total = resultSet.getDouble("total");
                String cliente = resultSet.getString("cliente");
                String empleado = resultSet.getString("empleado");
                String producto = resultSet.getString("producto");
                
                detalleFacturas.add(new DetalleFactura(producto,facturaId,fecha,hora,total,cliente,empleado));
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
        return FXCollections.observableList(detalleFacturas);
    }
    
    public void agregarTickets(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_AgregarTicketSoporte(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, taDescripcion.getText());
            statement.setInt(2,((Cliente)cmbClientes.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(3, ((DetalleFactura)cmbFacturas.getSelectionModel().getSelectedItem()).getFacturaId());
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
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarTickets(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "sp_EditarTicketSoporte(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfTicketId.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setString(3, cmbEstatus.getSelectionModel().getSelectedItem().toString());
            statement.setString(4, ((Cliente)cmbClientes.getSelectionModel().getSelectedItem()).toString());
            statement.setString(5,((DetalleFactura)cmbFacturas.getSelectionModel().getSelectedItem()).toString());
            statement.execute();
            
        }catch(SQLException e ){
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
