package com.paucampos.tareasapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tareas.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_TAREA = "tarea";

    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_FECHA = "fecha";
    public static final String COL_ESTADO = "estado";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Este método se ejecuta la primera vez que se crea la base de datos.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCreateTable = "CREATE TABLE " + TABLE_TAREA + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT NOT NULL, " +
                COL_DESCRIPCION + " TEXT, " +
                COL_FECHA + " TEXT NOT NULL, " +
                COL_ESTADO + " TEXT NOT NULL)";

        db.execSQL(sqlCreateTable);
    }

    // Este método permite actualizar la estructura de la base de datos si cambia la versión.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREA);
        onCreate(db);
    }
}
