package com.dany.glovercalendar.entidades;

import java.io.Serializable;
import java.util.Date;

public class Efectivo implements Serializable {

    private String id;
    private Date fecha;
    private String efectivo;

    public Efectivo() {
    }

    public Efectivo(String id, Date fecha, String efectivo) {
        this.id = id;
        this.fecha = fecha;
        this.efectivo = efectivo;
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

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
    }
}
