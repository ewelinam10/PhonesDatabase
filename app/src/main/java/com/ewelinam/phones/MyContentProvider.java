package com.ewelinam.phones;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

//DOSTĘP DO DOSTAWCY TREŚCI ODBYWA SIĘ ZA POŚREDNICTWEM IDENTYFIKATORA URI( który jednoznacznie określa dostawcę )

public class MyContentProvider extends ContentProvider {

    private MyOpenHelper database; //baza danych
    private static final String S_AUTHORITY = "com.ewelinam.phones.contentprovider";//pozwala na odroznienie dostawcy
    public static final Uri URI_CONTENT = Uri.parse("content://" + S_AUTHORITY +"/" + TablePhones.S_TABLE_NAME);//odwolywanie sie do dostawcy
    private static final int FULL_TABLE = 1; //dzieki tym stalym bedzie mozliwe rozrozninie dwoch rodzajow URI
    private static final int CHOSEN_ROW = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static { //dwa rozpoznawane uri przez dostawcę
        uriMatcher.addURI(S_AUTHORITY,TablePhones.S_TABLE_NAME,FULL_TABLE);
        uriMatcher.addURI(S_AUTHORITY,TablePhones.S_TABLE_NAME + "/#",CHOSEN_ROW);
    }



    @Override
    public boolean onCreate() {
        database = new MyOpenHelper(getContext());
        SQLiteDatabase db = database.getReadableDatabase();
        String path = db.getPath();
        Log.d("path",path);
        Log.d("bla","PROVIDER");
        return false;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //obiekt budujący zapytanie
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //ustawienie tabeli
        queryBuilder.setTables(TablePhones.S_TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case FULL_TABLE:
                break;
            case CHOSEN_ROW:
                queryBuilder.appendWhere(TablePhones.S_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType){
            case FULL_TABLE:
                id = sqlDB.insert(TablePhones.S_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(TablePhones.S_TABLE_NAME + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int typeUri = uriMatcher.match(uri);
        SQLiteDatabase base = database.getWritableDatabase();
        int rows_deleted = 0;
        switch (typeUri)
        {
            case FULL_TABLE: // przekazywanie parametrow z wywolania metody query dostawcy do metody query bazy
                rows_deleted = base.delete(TablePhones.S_TABLE_NAME,selection,selectionArgs);

                break;
            case CHOSEN_ROW: // do argumnetu selection dodawana jest wartosc klucza, ktory okresla zadany wiersz
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rows_deleted = base.delete(TablePhones.S_TABLE_NAME, TablePhones.S_ID + "=" +id, null);
                } else {
                    rows_deleted = base.delete(TablePhones.S_TABLE_NAME, TablePhones.S_ID + "=" +id + " and " + selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Indefident URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rows_deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int typeUri = uriMatcher.match(uri);
        SQLiteDatabase base = database.getWritableDatabase();
        int rows_updated = 0;
        switch (typeUri)
        {
            case FULL_TABLE: // przekazywanie parametrow z wywolania metody update dostawcy do metody update bazy
                rows_updated = base.update(TablePhones.S_TABLE_NAME,values,selection,selectionArgs);
                break;
            case CHOSEN_ROW:
                String id = uri.getLastPathSegment();
                // do argumnetu selection dodawana jest wartosc klucza, ktory okresla zadany wiersz
                if(TextUtils.isEmpty(selection)) {
                    rows_updated = base.update(TablePhones.S_TABLE_NAME,values,TablePhones.S_ID + "=" +id, null);
                } else {
                    rows_updated = base.update(TablePhones.S_TABLE_NAME,values,TablePhones.S_ID + "=" +id + " and " + selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Indefident URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null); // informujemy o zmianie danych dzieki temu obserwator bedzie mogl zaaragowc na ta zmiane danych
        return rows_updated;
    }
}
