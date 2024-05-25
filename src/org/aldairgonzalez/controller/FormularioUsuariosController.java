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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Empleado;
import org.aldairgonzalez.model.NivelAcceso;
import org.aldairgonzalez.system.Main;
import org.aldairgonzalez.utils.PasswordUtils;

/**
 * FXML Controller class
 *
 * @author Compu Fire
 */
public class FormularioUsuariosController implements Initializable {

    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfUsuario, tfPassword; 
    @FXML
    ComboBox cmbEmpleados,cmbNivelAcceso;
    @FXML
    Button btnRegistrar, btnEmpleados;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      cmbNivelAcceso.setItems(listarNiveles());
      cmbEmpleados.setItems(listarEmpleados());
      
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegistrar){
            agregarUsuario();
            stage.loginView();
        } else if (event.getSource() == btnEmpleados){
            stage.menuEmpleadosView(3);
            
        }
    }
    
    public void agregarUsuario(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarUsuario(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfUsuario.getText());
            statement.setString(2, PasswordUtils.getInstance().encryptedPassword(tfPassword.getText()));
            statement.setInt(3, ((NivelAcceso)cmbNivelAcceso.getSelectionModel().getSelectedItem()).getNivelAccesoId());
            statement.setInt(4, ((Empleado)cmbEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
        
    public ObservableList<NivelAcceso>listarNiveles(){
        ArrayList<NivelAcceso> niveles = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarUsuario()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int nivelId = resultSet.getInt("nivelAccesoId");
                String nivel = resultSet.getString("nivelAcceso");
                
                niveles.add(new NivelAcceso(nivelId,nivel));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            
        }
        
        return FXCollections.observableList(niveles);
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
