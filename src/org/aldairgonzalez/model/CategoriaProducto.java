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
    private String descripcionCaategoria;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int categoriaProductoId, String nombreCategoria, String descripcionCaategoria) {
        this.categoriaProductoId = categoriaProductoId;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCaategoria = descripcionCaategoria;
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

    public String getDescripcionCaategoria() {
        return descripcionCaategoria;
    }

    public void setDescripcionCaategoria(String descripcionCaategoria) {
        this.descripcionCaategoria = descripcionCaategoria;
    }

    @Override
    public String toString() {
        return "CategoriaProducto{" + "categoriaProductoId=" + categoriaProductoId + ", nombreCategoria=" + nombreCategoria + ", descripcionCaategoria=" + descripcionCaategoria + '}';
    }
    
    
}
