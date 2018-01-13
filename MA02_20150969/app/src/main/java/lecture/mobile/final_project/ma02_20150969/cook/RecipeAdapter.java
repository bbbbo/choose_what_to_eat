package lecture.mobile.final_project.ma02_20150969.cook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lecture.mobile.final_project.ma02_20150969.R;

/**
 * Created by bkbk0 on 2017-12-29.
 */

class RecipeAdapter extends BaseAdapter {
    public static final String TAG = "CookAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<Recipe> list;
    private ViewHolder viewHodler = null;


    public RecipeAdapter(Context context, int resource, ArrayList<Recipe> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Recipe getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;

        if (view == null) {
            viewHodler = new ViewHolder();      //static 변수(한번만 찾아주면 됨) - 검색해보기
            view = inflater.inflate(layout, parent, false);
            viewHodler.tvNo = (TextView)view.findViewById(R.id.reName);
            viewHodler.tvComm = (TextView)view.findViewById(R.id.tvSummary);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder)view.getTag();
        }

        Recipe dto = list.get(position);

        viewHodler.tvNo.setText(dto.getNo());
        viewHodler.tvComm.setText(dto.getComment());


        return view;
    }


    public void setList(ArrayList<Recipe> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<Recipe>();
    }



    static class ViewHolder {
        public TextView tvNo = null;
        public TextView tvComm = null;
    }



}
