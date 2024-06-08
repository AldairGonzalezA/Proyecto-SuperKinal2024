/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.report;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.aldairgonzalez.dao.Conexion;
import win.zqxu.jrviewer.JRViewerFX;

/**
 *
 * @author informatica
 */
public class GenerarReporte {
    private static GenerarReporte instance;
    
    private static Connection conexion = null;
    
    private GenerarReporte(){
        
    }

    public static GenerarReporte getInstance() {
        if(instance == null){
            instance = new GenerarReporte();
        }
        return instance;
    }
    
    public void generarFactura(int factId){
        try{
            //paso 1: Abrir la conexion a la base de datos
            conexion = Conexion.getInstance().obtenerConexion();
            //Paso 2: (no siempre solo en el caso que el reporte solicite parametros) Definir parametros del reporte
            Map<String, Object>parametros = new HashMap<>();
            parametros.put("factId", factId);
            //Paso 3: Crear el Reporte
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/aldairgonzalez/report/Factura.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath,parametros,conexion);
            //Paso 4: Crear la ventana de javaFX para mostrar el reporte
            Stage reportStage = new Stage();
            //Paso 5: llenar el stage con el reporte
            JRViewerFX reportViewer = new JRViewerFX(reporte);
            Pane pane = new Pane();
            pane.getChildren().add(reportViewer);
            reportViewer.setPrefSize(1000,800);
            Scene scene = new Scene(pane);
            reportStage.setScene(scene);
            reportStage.setTitle("Factura");
            reportStage.show();
            //GenerarReporte.getInstance().generarFactura(Integet.parseInt(tfFacturaId.getText()));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void generarClientes(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/aldairgonzalez/report/Waves_Landscape.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath, null,conexion);
            Stage reportStage = new Stage();
            JRViewerFX reporteViewer = new JRViewerFX(reporte);
            Pane root = new Pane();
            root.getChildren().add(reporteViewer);
            reporteViewer.setPrefSize(1000, 800);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Clientes");
            reportStage.show();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void generarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/aldairgonzalez/report/Productos.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath,null,conexion);
            Stage reportStage = new Stage();
            JRViewerFX reporteViewer = new JRViewerFX(reporte);
            Pane root = new Pane();
            root.getChildren().add(reporteViewer);
            reporteViewer.setPrefSize(842,595);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Productos");
            reportStage.show();
        }catch(Exception e){
           System.out.println(e.getMessage()); 
        }
    }
}
