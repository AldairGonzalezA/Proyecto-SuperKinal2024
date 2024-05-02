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
public class CategoriaProducto {
    private int categoriaProductoId;
    private String nombreCategoria;
    private String descrippcionCategoria;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int categoriaProductoId, String nombreCategoria, String descrippcionCategoria) {
        this.categoriaProductoId = categoriaProductoId;
        this.nombreCategoria = nombreCategoria;
        this.descrippcionCategoria = descrippcionCategoria;
    }

    public int getCategoriaProductoId() {
        return categoriaProductoId;
    }

    public void setCategoriaProductoId(int categoriaProductoId) {
        this.categoriaProductoId = categoriaProductoId;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescrippcionCategoria() {
        return descrippcionCategoria;
    }

    public void setDescrippcionCategoria(String descrippcionCategoria) {
        this.descrippcionCategoria = descrippcionCategoria;
    }

    @Override
    public String toString() {
        return "CategoriaProducto{" + "categoriaProductoId=" + categoriaProductoId + ", nombreCategoria=" + nombreCategoria + ", descripcionCaategoria=" + descrippcionCategoria + '}';
    }
    
    
}
