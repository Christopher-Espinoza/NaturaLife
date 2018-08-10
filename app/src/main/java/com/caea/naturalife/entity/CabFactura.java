package com.caea.naturalife.entity;

public class CabFactura {
    String cliente;
    String emision;
    String vencimiento;

    public CabFactura() {
    }

    public CabFactura(String cliente, String emision, String vencimiento) {
        this.cliente = cliente;
        this.emision = emision;
        this.vencimiento = vencimiento;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }
}
