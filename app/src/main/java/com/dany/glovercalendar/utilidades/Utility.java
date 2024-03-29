package com.dany.glovercalendar.utilidades;

import java.io.Serializable;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utility implements Serializable {

    // Firebase

    //Cloud Firestore
    public static final String USERS = "users";

    public static final String FNAME = "fName";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String CREATION_DATE = "creationDate";

    public static final String AD = "alta demanda";
    public static final String EF = "efectivo";

    //Realtime Database
    public static final String UPDATE = "update";
    public static final String VERSION = "version";
    public static final String LINK = "link";

    //Storage
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String PICTURE = "picture";

    //Entidades
    public static final String ID = "id";
    public static final String FECHA = "fecha";
    public static final String PEDIDOS = "pedidos";
    public static final String EFECTIVO = "efectivo";

    public static final String BUNDLE = "bundle";

    //Utilidad para calendario
    public static final String EVENT_COLOR_NORMAL = "#FD3434";
    public static final String EVENT_COLOR_ESPECIAL = "#FDDC34";

    public static final String SELECCIONE_FECHA = "Seleccione la fecha..";

    // Funciones utiles
    public static String printFecha (Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E dd/MM/yyyy");
        return sdf.format(date);
    }

    public static boolean fechaValida (Date date) {

        Date today = new Date();
        Date hoy = new Date(today.getYear(), today.getMonth(), today.getDate());
        Date fechaIngresada = new Date(date.getYear(), date.getMonth(), date.getDate());
        long voD = (hoy.getTime() - 2419200000L); // Restarle 28 dias a "hoy"

        if (fechaIngresada.getTime() == hoy.getTime()){return true;}

        if (fechaIngresada.getTime() < voD || fechaIngresada.getTime() > hoy.getTime())
            return false;
        else
            return true;
    }

}

