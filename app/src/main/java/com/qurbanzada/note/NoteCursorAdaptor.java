package com.qurbanzada.note;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aqn3130 on 05/07/2017.
 */

public class NoteCursorAdaptor extends CursorAdapter {
    public NoteCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String noteTxt= cursor.getString(cursor.getColumnIndex(DBOpen.NOTE_TEXT));
        int pos = noteTxt.indexOf(10);
        if(pos != -1){
            noteTxt = noteTxt.substring(0,pos)+"...";

        }
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(noteTxt);
    }
}
