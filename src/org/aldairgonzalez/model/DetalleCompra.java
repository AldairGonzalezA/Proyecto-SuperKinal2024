/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aldairgonzalez.model;

import java.sql.Date;

/**
 *
 * @author Compu Fire
 */
public class DetalleCompra extends Compra {
    private int detalleCompraId;
    private int cantidadCompra;
    private int productoId;
    private String producto;
    private int compraid;
    private String compra;

    public DetalleCompra() {
    }

    public DetalleCompra(int detalleCompraId, int cantidadCompra, int productoId, int compraid, int compraId,  Date fechaCompra, double totalCompra) {
        super(compraId, fechaCompra, totalCompra);
        this.detalleCompraId = detalleCompraId;
        this.cantidadCompra = cantidadCompra;
        this.productoId = productoId;
        this.compraid = compraid;
    }

    public DetalleCompra(int detalleCompraId, int cantidadCompra, String producto, String compra, int compraId, Date fechaCompra, double totalCompra) {
        super(compraId, fechaCompra, totalCompra);
        this.detalleCompraId = detalleCompraId;
        this.cantidadCompra = cantidadCompra;
        this.producto = producto;
        this.compra = compra;
    }

    public DetalleCompra(int cantidadCompra, String producto, int compraId, Date fechaCompra, double totalCompra) {
        super(compraId, fechaCompra, totalCompra);
        this.cantidadCompra = cantidadCompra;
        this.producto = producto;
    }
    

    public int getDetalleCompraId() {
        return detalleCompraId;
    }

    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
    }

    public int getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(int cantidadCompra) {
        this.cantidadCompra = cantidadCompra;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCompraid() {
        return compraid;
    }

    public void setCompraid(int compraid) {
        this.compraid = compraid;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" + "detalleCompraId=" + detalleCompraId + ", cantidadCompra=" + cantidadCompra + ", productoId=" + productoId + ", producto=" + producto + ", compraid=" + compraid + ", compra=" + compra + '}';
    }
    
    
}
