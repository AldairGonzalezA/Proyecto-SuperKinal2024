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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.aldairgonzalez.dao.Conexion;
import org.aldairgonzalez.model.Usuario;
import org.aldairgonzalez.system.Main;
import org.aldairgonzalez.utils.PasswordUtils;
import org.aldairgonzalez.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class LoginController implements Initializable {

    private Main stage;
    private int op;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfUser;
    @FXML
    PasswordField pfPassword;
    @FXML
    Button btnRegistrar, btnIniciar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       btnRegistrar.setDisable(true);
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnIniciar){
            Usuario usuario = buscarUsuario();
            if(op == 0){
                if(usuario != null){
                    if(PasswordUtils.getInstance().checkPassword(pfPassword.getText(), usuario.getContrasenia())){
                        if(usuario.getNivelAccesoId() == 1){
                            btnRegistrar.setDisable(false);
                            btnIniciar.setText("Ir al menu");
                            op = 1;
                        }else if(usuario.getNivelAccesoId() == 2){
                            stage.menuPrincipalView();
                        }
                    }else{
                        SuperKinalAlert.getInstance().mostrarAlertasInfo(605);
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertasInfo(602);
                }
            } else {
                stage.menuPrincipalView();
            }
        } else if(event.getSource() == btnRegistrar){
            stage.formularioUsuarioView();
        }
    }
    
    public Usuario buscarUsuario(){
        Usuario usuario = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarUsuario(?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfUser.getText());
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int usuarioId = resultSet.getInt("usuarioId");
                String usuario_ = resultSet.getString("usuario");
                String contra = resultSet.getString("contrasenia");
                int nivel = resultSet.getInt("nivelAccesoId");
                int empleadoId = resultSet.getInt("empleadoId");
                
                usuario = (new Usuario(usuarioId,usuario_,contra,nivel,empleadoId)); 
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
        return usuario;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
