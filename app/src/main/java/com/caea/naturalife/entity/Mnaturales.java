package com.caea.naturalife.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Mnaturales {
    private String fotourl;
    private String nombre;
    private String descripcion;
    private String formula;
    private String recomendacion;
    private String sabor;
    private Double precio;
    private String grasa;
    private String azucar;
    private String sal;

    public Mnaturales() {}

    public Mnaturales(String fotourl, String nombre, String descripcion, String formula, String recomendacion, String sabor, Double precio) {
        this.fotourl = fotourl;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.formula = formula;
        this.recomendacion = recomendacion;
        this.sabor = sabor;
        this.precio = precio;
    }

    public Mnaturales(String descripcion, String formula, String sabor, Double precio) {
        this.descripcion = descripcion;
        this.formula = formula;
        this.sabor = sabor;
        this.precio = precio;
    }

    public Mnaturales(String grasa, String azucar, String sal) {
        this.grasa = grasa;
        this.azucar = azucar;
        this.sal = sal;
    }

    public String getFotourl() {
        return fotourl;
    }

    public void setFotourl(String fotourl) {
        this.fotourl = fotourl;
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

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getGrasa() {
        return grasa;
    }

    public void setGrasa(String grasa) {
        this.grasa = grasa;
    }

    public String getAzucar() {
        return azucar;
    }

    public void setAzucar(String azucar) {
        this.azucar = azucar;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }
}
