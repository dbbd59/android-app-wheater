package com.bolzoni.davide.miometeo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Davide on 28/06/2016.
 */
public class DataBase {
    private static final String DATABASE_NAME = "cittaDB";
    private static final int DATABASE_VERSION = 1;
    private DbHelper ourHelper;
    private static Context ourContext;
    private SQLiteDatabase ourDatabase;

    public static enum TipoQuery {
        Selezione,
        Comando
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE tabella1 (" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " nome TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public DataBase(Context c) {
        this.ourContext = c;
    }

    public DataBase open() throws SQLException {
        this.ourHelper = new DbHelper(ourContext);
        this.ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.ourHelper.close();
    }

    public Cursor Execute(String Query, TipoQuery tipoCmd) {
        Cursor c = null;

        try {
            switch (tipoCmd) {
                case Comando:
                    ourDatabase.execSQL(Query);
                    break;
                case Selezione:
                    c = ourDatabase.rawQuery(Query, null);
                    break;
            }
        } catch (Exception e) {
        }

        return c;
    }

}

