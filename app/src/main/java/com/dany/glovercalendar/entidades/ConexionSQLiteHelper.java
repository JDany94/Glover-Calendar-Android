package com.dany.glovercalendar.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dany.glovercalendar.utilidades.Utility;

import java.util.ArrayList;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context) {
        super(context, Utility.DATA_BASE_NAME, null, Utility.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utility.CREAR_TABLA_ALTADEMANDA);
        db.execSQL(Utility.CREAR_TABLA_EFECTIVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utility.TABLA_ALTADEMANDA);
        db.execSQL("DROP TABLE IF EXISTS " + Utility.TABLA_EFECTIVO);
        onCreate(db);
    }

    // Funciones para consultar la tabla altaDemanda
    public void insertarAD (String dia_Semana, String dia_Mes, String mes, String anio, String contador) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put(Utility.CAMPO_DIA_SEMANA_AD, dia_Semana);
        registro.put(Utility.CAMPO_DIA_MES_AD, dia_Mes);
        registro.put(Utility.CAMPO_MES_AD, mes);
        registro.put(Utility.CAMPO_ANIO_AD, anio);
        registro.put(Utility.CAMPO_PEDIDOS, contador);

        Long idResultante = db.insert(Utility.TABLA_ALTADEMANDA, "null", registro);
        db.close();
    }

    public void updatePorID_AD (String dia_Semana, String dia_Mes, String mes, String anio, String contador, String id[]) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues newRegistro = new ContentValues();
        newRegistro.put(Utility.CAMPO_DIA_SEMANA_AD, dia_Semana);
        newRegistro.put(Utility.CAMPO_DIA_MES_AD, dia_Mes);
        newRegistro.put(Utility.CAMPO_MES_AD, mes);
        newRegistro.put(Utility.CAMPO_ANIO_AD, anio);
        newRegistro.put(Utility.CAMPO_PEDIDOS, contador);

        db.update(Utility.TABLA_ALTADEMANDA, newRegistro, Utility.CAMPO_ID_ALTADEMANDA + "=?", id);
        db.close();
    }

    public void eliminarPorID_AD (String id[]) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Utility.TABLA_ALTADEMANDA, Utility.CAMPO_ID_ALTADEMANDA + "=?", id);
        db.close();
    }

    public ArrayList<AltaDemanda> selectAllFromAD () {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<AltaDemanda> listaAltaDemanda;
        listaAltaDemanda = new ArrayList<>();
        AltaDemanda registro = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utility.TABLA_ALTADEMANDA,null);
        while (cursor.moveToNext()) {
            registro = new AltaDemanda();
            registro.setId(cursor.getString(0));
            registro.setDia_semana(cursor.getString(1));
            registro.setDia_mes(cursor.getString(2));
            registro.setMes(cursor.getString(3));
            registro.setAnio(cursor.getString(4));
            registro.setPedidos(cursor.getString(5));
            listaAltaDemanda.add(registro);
        }
        db.close();
        return listaAltaDemanda;
    }

    public AltaDemanda buscarRegistro (String dia_Mes, String mes, String anio) {
        SQLiteDatabase db = getWritableDatabase();

        AltaDemanda registro = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utility.TABLA_ALTADEMANDA + " WHERE " + Utility.CAMPO_DIA_MES_AD + " = " + dia_Mes + " AND " + Utility.CAMPO_MES_AD + " = " + mes + " AND " + Utility.CAMPO_ANIO_AD + " = " + anio,null);
        while (cursor.moveToNext()) {
            registro = new AltaDemanda();
            registro.setId(cursor.getString(0));
            registro.setDia_semana(cursor.getString(1));
            registro.setDia_mes(cursor.getString(2));
            registro.setMes(cursor.getString(3));
            registro.setAnio(cursor.getString(4));
            registro.setPedidos(cursor.getString(5));
        }
        db.close();
        return registro;
    }

    // Funciones para consultar la tabla efectivo

}
