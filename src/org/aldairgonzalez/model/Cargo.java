/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.model;

/**
 *
 * @author informatica
 */
public class Cargo {
    private int cargoId;
    private String nombreCargo;
    private String descripcion;

    public Cargo() {
    }

    public Cargo(int cargoId, String nombreCargo, String descripcion) {
        this.cargoId = cargoId;
        this.nombreCargo = nombreCargo;
        this.descripcion = descripcion;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
