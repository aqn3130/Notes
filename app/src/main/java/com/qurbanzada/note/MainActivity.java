package com.qurbanzada.note;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cursorAdapter = new NoteCursorAdaptor(this,null,0);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri uri = Uri.parse(NoteProvider.CONTENT_URI+"/"+id);
                intent.putExtra(NoteProvider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent,EDITOR_REQUEST_CODE);

            }

        });
        getLoaderManager().initLoader(0,null,this);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
            case R.id.action_export:
                exportNotes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Export notes
    private void exportNotes() {
        DialogInterface.OnClickListener dl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {
                if(button == DialogInterface.BUTTON_POSITIVE){

                    Toast.makeText(MainActivity.this,getString(R.string.notes_export),Toast.LENGTH_SHORT).show();


                }
            }
        };
        dialogBuilder(dl);
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
//                            getContentResolver().delete(NoteProvider.CONTENT_URI,null,null);
//                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        dialogBuilder(dialogClickListener);
    }

    private void dialogBuilder(DialogInterface.OnClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertData() {
        insertNote("Sample note");
        insertNote("Multi-line\nnotes");
        insertNote("Very long notes with lots of text that exceeds the width of the screen");

        restartLoader();

    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }


    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpen.NOTE_TEXT,noteText);
        Uri noteUri = getContentResolver().insert(NoteProvider.CONTENT_URI,values);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,NoteProvider.CONTENT_URI,null,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }

    public void openEditorForNewNote(View view) {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivityForResult(intent,  EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
