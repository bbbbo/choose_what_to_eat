package lecture.mobile.final_project.ma02_20150969.cook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import lecture.mobile.final_project.ma02_20150969.R;

public class RecipeActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvExplain;
    ImageView imageView;

    private ListView lvRecipe;
    private ArrayList<Recipe> recipeList;
    private ArrayAdapter<Recipe> adapter;

    String address;
    String id;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        tvName = (TextView)findViewById(R.id.reName);
        tvExplain = (TextView)findViewById(R.id.tvExplain);
        imageView = (ImageView)findViewById(R.id.RE_FOOD);
        lvRecipe = (ListView)findViewById(R.id.lvRecipe);
        address = getResources().getString(R.string.recipe_url);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String summary = intent.getStringExtra("summary");
        img = intent.getStringExtra("image");

        tvName.setText(name);
        tvExplain.setText(summary);
        String imageFileName = img;

        if (imageFileName.equals("")) {
            imageView.setImageResource(R.drawable.choice);
        } else {
            GetImageAsyncTask task = new GetImageAsyncTask();
            try {
                Bitmap bitmap = task.execute(img, imageFileName).get();
                imageView.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        recipeList = new ArrayList<Recipe>();
        adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, recipeList);

        lvRecipe.setAdapter(adapter);

        findList();

    }

    public void findList(){
        new RecipeAsyncTask().execute(address + id);
    }

    class RecipeAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 40000;
        ProgressDialog progressDlg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(RecipeActivity.this, "Wait", "레시피 검색중...");
        }

        @Override
        protected String doInBackground(String... params) {
            String addr = params[0];
            StringBuilder resultBuilder = new StringBuilder();

            try {
                URL url = new URL(addr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn != null) {
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setUseCaches(false);
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            for (String line = br.readLine(); line != null; line = br.readLine()) {
                                resultBuilder.append(line + '\n');
                            }

                            br.close();
                        }
                        conn.disconnect();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(RecipeActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            RecipeXmlParser parser = new RecipeXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!recipeList.isEmpty()) adapter.clear();

//            parsing 수행
            recipeList = parser.parse(s);

//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            adapter.addAll(recipeList);
            progressDlg.dismiss();
        }
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        String imageFileName;

        @Override
        protected Bitmap doInBackground(String... params) {     //주소와 파일이름만 변경해서 사용
            Bitmap bitmap = null;
            String imageAddress = params[0];
            imageFileName = params[1];

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
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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
