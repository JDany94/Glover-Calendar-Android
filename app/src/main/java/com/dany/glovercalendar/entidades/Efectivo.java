package com.dany.glovercalendar.entidades;

import java.io.Serializable;

public class Efectivo implements Serializable {

    private String id;
    private String dia_semana;
    private String dia_mes;
    private String mes;
    private String anio;
    private String efectivo;

    public Efectivo() {
    }

    public Efectivo(String id, String dia_semana, String dia_mes, String mes, String anio, String efectivo) {
        this.id = id;
        this.dia_semana = dia_semana;
        this.dia_mes = dia_mes;
        this.mes = mes;
        this.anio = anio;
        this.efectivo = efectivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getDia_mes() {
        return dia_mes;
    }

    public void setDia_mes(String dia_mes) {
        this.dia_mes = dia_mes;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
    }
}
