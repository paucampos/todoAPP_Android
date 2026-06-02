package com.paucampos.tareasapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paucampos.tareasapp.model.Tarea;

import java.util.ArrayList;
import java.util.List;

public class TareaRepository {
    private final DBHelper dbHelper;

    public TareaRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    // INSERT: guarda una nueva tarea en la base de datos SQLite.
    public long insertarTarea(Tarea tarea) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_TITULO, tarea.getTitulo());
        values.put(DBHelper.COL_DESCRIPCION, tarea.getDescripcion());
        values.put(DBHelper.COL_FECHA, tarea.getFecha());
        values.put(DBHelper.COL_ESTADO, tarea.getEstado());

        long resultado = db.insert(DBHelper.TABLE_TAREA, null, values);

        db.close();

        return resultado;
    }

    // SELECT: recupera todas las tareas almacenadas en SQLite.
    public List<Tarea> obtenerTareas() {

        List<Tarea> listaTareas = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + DBHelper.TABLE_TAREA +
                " ORDER BY " + DBHelper.COL_ID + " DESC";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(DBHelper.COL_ID)
                );

                String titulo = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBHelper.COL_TITULO)
                );

                String descripcion = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBHelper.COL_DESCRIPCION)
                );

                String fecha = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBHelper.COL_FECHA)
                );

                String estado = cursor.getString(
                        cursor.getColumnIndexOrThrow(DBHelper.COL_ESTADO)
                );

                Tarea tarea = new Tarea(id, titulo, descripcion, fecha, estado);
                listaTareas.add(tarea);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaTareas;
    }

    // DELETE: elimina una tarea específica según su id.
    public int eliminarTarea(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int filasEliminadas = db.delete(
                DBHelper.TABLE_TAREA,
                DBHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return filasEliminadas;
    }

    // UPDATE: marca una tarea como completada según su id.
    public int completarTarea(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ESTADO, "Completada");

        int filasActualizadas = db.update(
                DBHelper.TABLE_TAREA,
                values,
                DBHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return filasActualizadas;
    }
}
