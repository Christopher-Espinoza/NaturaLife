package com.caea.naturalife.entity;

import java.util.Date;

public class Carrito {
    private String nombre;
    private String descripcion;
    private String formula;
    private String imageurl;
    private String sabor;
    private Integer cantidad;
    private String fecha;
    private Integer ncarrito;
    private Double precio;
    private Double total;

    public Carrito() {
    }

    public Carrito(String nombre, String descripcion, String formula, String imageurl, String sabor, Integer cantidad, String fecha, Integer ncarrito, Double precio, Double total) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.formula = formula;
        this.imageurl = imageurl;
        this.sabor = sabor;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.ncarrito = ncarrito;
        this.precio = precio;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getNcarrito() {
        return ncarrito;
    }

    public void setNcarrito(Integer ncarrito) {
        this.ncarrito = ncarrito;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}