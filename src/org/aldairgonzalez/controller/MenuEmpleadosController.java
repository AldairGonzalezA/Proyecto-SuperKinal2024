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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Cargo;
import org.aldairgonzalez.model.Empleado;
import org.aldairgonzalez.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuEmpleadosController implements Initializable {

    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfEmpleadoId,tfNombreEmpleado,tfApellido,tfSueldo,tfHoraEntrada,tfHoraSalida; 
    @FXML
    ComboBox cmbCargo,cmbEncargado;
    @FXML
    TableView tblEmpleados;
    @FXML
    TableColumn colEmpleadoId,colNombre,colApellido,colSueldo,colEntrada,colSalida,colCargo,colEncargado;
    @FXML
    Button btnGuardar,btnRegresar,btnVaciar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbCargo.setItems(listarCargos());
        cmbEncargado.setItems(listarEmpleados());
        cargarDatos();
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnGuardar){
            if(tfEmpleadoId.getText().isEmpty()){
                agregarEmpleados();
                cargarDatos();
            }else{
                editarEmpleado();
                cargarDatos();
            }
        }else if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    public void vaciarCampos(){
        tfEmpleadoId.clear();
        tfNombreEmpleado.clear();
        tfApellido.clear();
        tfSueldo.clear();
        tfHoraEntrada.clear();
        tfHoraSalida.clear();
        cmbCargo.getSelectionModel().clearSelection();
        cmbEncargado.getSelectionModel().clearSelection();
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(listarEmpleados());
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("Cargo"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("encargado"));
    }
    
    public void cargarDatosEditar(){
        Empleado ep = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        if(ep != null){
            tfEmpleadoId.setText(Integer.toString(ep.getEmpleadoId()));
            tfNombreEmpleado.setText(ep.getNombreEmpleado());
            tfApellido.setText(ep.getApellidoEmpleado());
            tfSueldo.setText(Double.toString(ep.getSueldo()));
            tfHoraEntrada.setText(ep.getHoraEntrada().toString());
            tfHoraSalida.setText(ep.getHoraSalida().toString());
            cmbCargo.getSelectionModel().select(0);
            cmbEncargado.getSelectionModel().select(obtenerIndexEncargado());
        }
    }
    
    public int obtenerIndexEncargado(){
        int index = 0;
        for(int i = 0 ; i<= cmbEncargado.getItems().size() ; i++){
            String encargadoCmb = cmbEncargado.getItems().get(1).toString();
            String encargadoTbl = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEncargado();
            if(encargadoCmb.equals(encargadoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
        
    
    public ObservableList<Empleado> listarEmpleados(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarEmpleados()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombre = resultSet.getString("nombreEmpleado");
                String apellido = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaEntrada = resultSet.getTime("horaEntrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargo = resultSet.getString("cargo");
                String empleado = resultSet.getString("encargado");
                
                empleados.add(new Empleado(empleadoId,nombre,apellido,sueldo,horaEntrada,horaSalida,cargo,empleado));
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
        return FXCollections.observableList(empleados);
    }
    
    public void agregarEmpleados(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Time horaEntrada = null;
        Time horaSalida = null;
        try{
            horaEntrada = new Time(sdf.parse(tfHoraEntrada.getText()).getTime());
            horaSalida = new Time(sdf.parse(tfHoraSalida.getText()).getTime());
        }catch(ParseException e){
             e.printStackTrace();
        }
        
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarEmpleados(?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreEmpleado.getText());
            statement.setString(2, tfApellido.getText());
            statement.setDouble(3, Double.parseDouble(tfSueldo.getText()));
            statement.setTime(4, horaEntrada);
            statement.setTime(5, horaSalida);
            statement.setInt(6,((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCargoId());
            statement.setInt(7, ((Empleado)cmbEncargado.getSelectionModel().getSelectedItem()).getEncargadoId());
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

    public ObservableList<Cargo> listarCargos(){
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
        return FXCollections.observableList(cargos);
    }
    
    public void editarEmpleado(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Time horaEntrada = null;
        Time horaSalida = null;
        try{
            horaEntrada = new Time(sdf.parse(tfHoraEntrada.getText()).getTime());
            horaSalida = new Time(sdf.parse(tfHoraSalida.getText()).getTime());
        }catch(ParseException e){
             e.printStackTrace();
        }
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarEmpleados(?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfEmpleadoId.getText()));
            statement.setString(2, tfNombreEmpleado.getText());
            statement.setString(3, tfApellido.getText());
            statement.setDouble(4, Double.parseDouble(tfSueldo.getText()));
            statement.setTime(5, horaEntrada);
            statement.setTime(6, horaSalida);
            statement.setString(7, ((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).toString());
            statement.setString(8, (cmbEncargado.getSelectionModel().getSelectedItem()).toString());
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
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
