package lecture.mobile.final_project.ma02_20150969.MY;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import lecture.mobile.final_project.ma02_20150969.Food.DetailActivity;
import lecture.mobile.final_project.ma02_20150969.R;

public class MyListActivity extends AppCompatActivity {
    ListView lvContacts = null;
    MyDBHelper helper;
    Cursor cursor;
    MyCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        lvContacts = (ListView)findViewById(R.id.MyList);
        helper = new MyDBHelper(this);
        adapter = new MyCursorAdapter(this, null);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyListActivity.this, DetailActivity.class);
                SQLiteDatabase db = helper.getReadableDatabase();
                String selection = "_id=?";
                String[] selectionArgs = new String[] { Long.valueOf(id).toString() };

                Cursor c = db.query(MyDBHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
                c.moveToNext();
                intent.putExtra("title", c.getString(1));
                intent.putExtra("tel", c.getString(2));
                intent.putExtra("address", c.getString(3));
                c.close();
                helper.close();
                startActivity(intent);
            }
        });

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long ID = id;

                AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity.this);
                builder.setTitle("삭제 확인");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String whereClause = MyDBHelper.COL_ID + "=?";
                        String[] whereArgs = { Long.valueOf(ID).toString() };

                        db.delete(MyDBHelper.TABLE_NAME, whereClause, whereArgs);
                        onResume();
                        helper.close();
                    }
                });
                builder.setNegativeButton("취소", null);

                Dialog dig = builder.create();
                dig.show();
                return true;
            }
        });
    }

    protected void onResume(){
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + MyDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null){
            cursor.close();
        }
    }
}
