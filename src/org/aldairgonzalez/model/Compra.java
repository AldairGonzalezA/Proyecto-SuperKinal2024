/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.model;

/**
 *
 * @author Compu Fire
 */
public class Compra {
    private int compraId;
    private String fechaCompra;
    private double totalCompra;

    public Compra() {
    }

    public Compra(int compraId, String fechaCompra,double totalCompra) {
        this.compraId = compraId;
        this.fechaCompra = fechaCompra;
        this.totalCompra = totalCompra;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    @Override
    public String toString() {
        return "Compra{" + "compraId=" + compraId + ", fechaCompra=" + fechaCompra + ", totalCompra=" + totalCompra + '}';
    }
    
    
}
