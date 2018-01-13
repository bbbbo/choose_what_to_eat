package lecture.mobile.final_project.ma02_20150969.cook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import lecture.mobile.final_project.ma02_20150969.R;

/**
 * Created by bkbk0 on 2017-12-29.
 */

public class CookAdapter extends BaseAdapter {
    public static final String TAG = "CookAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<Cook> list;
    private ViewHolder viewHodler = null;

    private String imageSavedPath;

    public CookAdapter(Context context, int resource, ArrayList<Cook> list) {
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
    public Cook getItem(int position) {
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
            viewHodler.tvName = (TextView)view.findViewById(R.id.reName);
            viewHodler.tvSummary = (TextView)view.findViewById(R.id.tvSummary);
            viewHodler.tvTime = (TextView)view.findViewById(R.id.tvTime);
            viewHodler.ivImage = (ImageView)view.findViewById(R.id.RE_FOOD);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder)view.getTag();
        }

        Cook dto = list.get(position);

        viewHodler.tvName.setText(dto.getName());
        viewHodler.tvSummary.setText(dto.getSummary());
        viewHodler.tvTime.setText(dto.getTime());

        String imageFileName = dto.getImage();

        if (imageFileName.equals("")) {
            viewHodler.ivImage.setImageResource(R.drawable.choice);
        } else {
            GetImageAsyncTask task = new GetImageAsyncTask();
            try {
//                A. 비트맵을 네트워크에서 다 받을 때까지 대기한 후 진행하고자 할 경우 get() 메소드 사용
                //화면에 네트워크에서 이미지를 가져오고 나서 화면 세팅됨
                Bitmap bitmap = task.execute(dto.getImage(), imageFileName).get();
                viewHodler.ivImage.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return view;
    }


    public void setList(ArrayList<Cook> list) {
        this.list = list;
    }

    public void clear() {
        this.list = new ArrayList<Cook>();
    }



    static class ViewHolder {
        public TextView tvName = null;
        public TextView tvSummary = null;
        public TextView tvTime = null;
        public ImageView ivImage = null;
    }



    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        String imageFileName;

        @Override
        protected Bitmap doInBackground(String... params) {     //주소와 파일이름만 변경해서 사용

            Bitmap bitmap = null;
            String imageAddress = params[0];
            imageFileName = params[1];

            Log.i(TAG, imageAddress);

            try {
                URL Url = new URL(imageAddress);
                URLConnection imageConn = Url.openConnection();
                imageConn.connect();

//                이미지 크기 확인
                int imageLength = imageConn.getContentLength();
//                InputStream 을 가져와 BufferedInputStream 으로 변환
                BufferedInputStream bis = new BufferedInputStream(imageConn.getInputStream(), imageLength);
//                Stream 을 Bitmap 으로 변환
                bitmap = BitmapFactory.decodeStream(bis);

                bis.close();
            } catch (FileNotFoundException e) {
//                서버에 이미지 파일이 없을 경우 리소스에 있는 기본 이미지 사용
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

        }
    }


}
