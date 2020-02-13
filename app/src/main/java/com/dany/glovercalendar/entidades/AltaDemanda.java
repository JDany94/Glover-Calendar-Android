package com.dany.glovercalendar.entidades;

import java.io.Serializable;
import java.util.Date;

public class AltaDemanda implements Serializable {

    private String id;
    private Date fecha;
    private String pedidos;

    public AltaDemanda() {
    }

    public AltaDemanda(String id, Date fecha, String pedidos) {
        this.id = id;
        this.fecha = fecha;
        this.pedidos = pedidos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPedidos() {
        return pedidos;
    }

    public void setPedidos(String pedidos) {
        this.pedidos = pedidos;
    }
}