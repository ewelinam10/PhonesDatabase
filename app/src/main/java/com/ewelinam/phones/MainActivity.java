package com.ewelinam.phones;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_phones);
        fillData();

    }
    private void fillData(){
        String[] from = new String[] {TablePhones.S_PRODUCER,TablePhones.S_MODEL};
        int[] to = new int[] {R.id.textView_producer, R.id.textView_model};
        getLoaderManager().initLoader(0,null,this);
        adapter = new SimpleCursorAdapter(this,R.layout.row_of_list,null,from,to,0);
        setListAdapter(adapter);


    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TablePhones.S_ID, TablePhones.S_PRODUCER, TablePhones.S_MODEL};
        CursorLoader cursorLoader = new CursorLoader(this,MyContentProvider.URI_CONTENT,projection,null,null,null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}
