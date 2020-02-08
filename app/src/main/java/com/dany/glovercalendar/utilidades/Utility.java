package com.dany.glovercalendar.utilidades;

import java.io.Serializable;
import java.util.Calendar;

public class Utility implements Serializable {

    public static final String EVENT_COLOR_NORMAL = "#FD3434";
    public static final String EVENT_COLOR_ESPECIAL = "#FDDC34";
    public static final String DATA_BASE_NAME = "dataBase";
    public static final int VERSION = 1;

    //Constantes de la tabla altaDemanda

    public static final String TABLA_ALTADEMANDA = "altaDemanda";
    public static final String CAMPO_ID_ALTADEMANDA = "id_alta_demanda";
    public static final String CAMPO_DIA_SEMANA_AD = "dia_semana";
    public static final String CAMPO_DIA_MES_AD = "dia_mes";
    public static final String CAMPO_MES_AD = "mes";
    public static final String CAMPO_ANIO_AD = "anio";
    public static final String CAMPO_PEDIDOS = "pedidos";

    public static final String CREAR_TABLA_ALTADEMANDA = "CREATE TABLE " + TABLA_ALTADEMANDA +
            " (" + CAMPO_ID_ALTADEMANDA + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_DIA_SEMANA_AD + " TEXT, " + CAMPO_DIA_MES_AD + " TEXT, " + CAMPO_MES_AD + " TEXT, " +
            CAMPO_ANIO_AD + " TEXT, " + CAMPO_PEDIDOS + " TEXT)";

    // Constantes de la tabla efectivo

    public static final String TABLA_EFECTIVO = "efectivo";
    public static final String CAMPO_ID_EFECTIVO = "id_efectivo";
    public static final String CAMPO_DIA_SEMANA_EFECTIVO = "dia_semana";
    public static final String CAMPO_DIA_MES_EFECTIVO = "dia_mes";
    public static final String CAMPO_MES_EFECTIVO = "mes";
    public static final String CAMPO_ANIO_EFECTIVO = "anio";
    public static final String CAMPO_EFECTIVO = "efectivo";

    public static final String CREAR_TABLA_EFECTIVO = "CREATE TABLE " + TABLA_EFECTIVO +
            " (" + CAMPO_ID_EFECTIVO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_DIA_SEMANA_EFECTIVO + " TEXT, " + CAMPO_DIA_MES_EFECTIVO + " TEXT, " +
            CAMPO_MES_EFECTIVO + " TEXT, " + CAMPO_ANIO_EFECTIVO + " TEXT, " + CAMPO_EFECTIVO + " TEXT)";


    // Funciones utiles

    public static String printFecha (String dia_Semana, String dia_Mes, String mes, String anio) {
        String fecha = "";
        if (dia_Semana.equals("1"))
            fecha+= "Dom. ";
        if (dia_Semana.equals("2"))
            fecha+= "Lun. ";
        if (dia_Semana.equals("3"))
            fecha+= "Mar. ";
        if (dia_Semana.equals("4"))
            fecha+= "Mie. ";
        if (dia_Semana.equals("5"))
            fecha+= "Jue. ";
        if (dia_Semana.equals("6"))
            fecha+= "Vie. ";
        if (dia_Semana.equals("7"))
            fecha+= "Sab. ";

        fecha+= dia_Mes + "/" + mes + "/" + anio;

        return fecha;
    }

    public static boolean fechaValida (String dia_Semana, String dia_Mes, String mes, String anio) {

        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR, 0);
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);

        Calendar fechaIngresada = Calendar.getInstance();
        fechaIngresada.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1, Integer.parseInt(dia_Mes));
        fechaIngresada.set(Calendar.HOUR, 0);
        fechaIngresada.set(Calendar.HOUR_OF_DAY, 0);
        fechaIngresada.set(Calendar.MINUTE, 0);
        fechaIngresada.set(Calendar.SECOND, 0);

        long hoyMS = hoy.getTimeInMillis();
        long vo_dMS = (hoy.getTimeInMillis() - 2419200000L); // Restarle 28 dias a "hoy"
        long fechaIngresadaMS = fechaIngresada.getTimeInMillis();

        if (fechaIngresadaMS == hoyMS){return true;}

        if (fechaIngresadaMS < vo_dMS || fechaIngresadaMS > hoyMS)
            return false;
        else
            return true;
    }

    }

