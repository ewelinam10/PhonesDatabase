package com.ewelinam.phones;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//KLASA ODZWIERCIEDLAJĄCA TABELĘ W BAZIE DANYCH APLIKACJI ,
//ZAWIERA PUBLICZNE STATYCZNE POLA OPISUJĄCE TABELĘ ORAZ METODĘ TWORZĄCĄ I AKTUALIZUJĄCĄ BAZĘ :)

public class TablePhones {
    public static String S_DATABASE_NAME = "DBphones"; //nazwa bazy
    public static int S_VERSION = 1;    //wersja
    public static String S_TABLE_NAME = "phones"; //nazwa tabeli
    public static String S_ID = "_id";  //nazwa 1 kolumny
    public static String S_PRODUCER = "producer";   //nazwa 2 kolumny
    public static String S_MODEL = "model";     //nazwa 3 kolumny
    public static String S_ANDROID = "android"; //nazwa 4 kolumny
    public static String S_WWW = "www";     //nazwa 5 kolumny


    public static final String S_CREATE_DATABASE = "CREATE TABLE "
            + S_TABLE_NAME +
            "("
            + S_ID
            + " integer primary key autoincrement, "
            + S_PRODUCER
            + " text not null, "
            + S_MODEL
            + " text not null,"
            + S_ANDROID
            + " text not null, "
            + S_WWW
            + " text);";
    private static final String S_DELETE_DATABASE = "DROP TABLE IF EXISTS " + S_DATABASE_NAME;


    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(S_CREATE_DATABASE);
        Log.d("create_db","BAZA ZOSTAŁA STWORZONA!");

    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(S_DELETE_DATABASE);
        onCreate(db);
    }
}
