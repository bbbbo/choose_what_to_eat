package lecture.mobile.final_project.ma02_20150969.MY;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import lecture.mobile.final_project.ma02_20150969.R;

/**
 * Created by bkbk0 on 2017-12-27.
 */

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    Cursor cursor;

    public MyCursorAdapter(Context context, Cursor c) {
       super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(R.layout.list_layout, parent, false);
        return  listItemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.imgView);
        TextView textTitle = (TextView)view.findViewById(R.id.textTitle);
        TextView textTel = (TextView)view.findViewById(R.id.textTel);
        TextView textAddr = (TextView)view.findViewById(R.id.textAddr);

        textTitle.setText(cursor.getString(1));
        textTel.setText(cursor.getString(2));
        textAddr.setText(cursor.getString(3));

        Random dice = new Random();
        int result = dice.nextInt(7) + 1;
        if(result == 1){
            imageView.setImageResource(R.drawable.i1);
        }else if(result == 2) {
            imageView.setImageResource(R.drawable.i2);
        } else if(result == 3) {
            imageView.setImageResource(R.drawable.i3);
        }else if(result == 4) {
            imageView.setImageResource(R.drawable.i4);
        }else if(result == 5) {
            imageView.setImageResource(R.drawable.i5);
        }else if(result == 6) {
            imageView.setImageResource(R.drawable.i6);
        }else if(result == 7) {
            imageView.setImageResource(R.drawable.i7);
        }

    }
}
