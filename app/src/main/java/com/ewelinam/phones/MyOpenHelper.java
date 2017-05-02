package com.ewelinam.phones;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//KLASA ZAWIERA WYWOŁANIA STATYCZNYCH METOD KLASY TABLEPHONES

public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        super(context,TablePhones.S_DATABASE_NAME,null,TablePhones.S_VERSION);
    }
//metoda wywoływana podczas tworzenia bazy
    @Override
    public void onCreate(SQLiteDatabase db) {
        TablePhones.onCreate(db);
        Log.d("bla","BALSBDKJDASDKSHHSKSHDHSJDHKHDJA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TablePhones.onUpgrade(db,oldVersion,newVersion);
    }
}
