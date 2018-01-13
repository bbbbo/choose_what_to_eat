package lecture.mobile.final_project.ma02_20150969.Food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import lecture.mobile.final_project.ma02_20150969.R;
import lecture.mobile.final_project.ma02_20150969.Seoul.SearchXmlParser;

public class FoodActivity extends AppCompatActivity {
    private final static String TAG = "FoodActivity";

    private EditText etTarget; //검색 키워드
    private String menu;
    private ListView lvList;

    private ArrayAdapter<Restaurant> adapter;
    private ArrayList<Restaurant> resultList;

    String category = null;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        etTarget = (EditText)findViewById(R.id.etMenu);
        lvList = (ListView)findViewById(R.id.listView);

        resultList = new ArrayList<Restaurant>();
        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, resultList);
        lvList.setAdapter(adapter);

        Intent intent = getIntent();
        int sigunguCode = intent.getIntExtra("sigunguCode", 1);
        category = intent.getStringExtra("category");
        Log.d(TAG, "code: " + sigunguCode);
        Log.d(TAG, "category: " + category);


        findRegion(sigunguCode);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Restaurant dto = resultList.get(position);
                    Intent intent = new Intent(FoodActivity.this, DetailActivity.class);
                    intent.putExtra("title", dto.getTitle());
                    intent.putExtra("menu", dto.getMenu());
                    intent.putExtra("tel", dto.getTel());
                    intent.putExtra("address", dto.getAddr());
                    startActivity(intent);
             }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSearch:
                menu = etTarget.getText().toString();
                if (menu.equals("")) menu = etTarget.getHint().toString();
                new SearchFoodAsyncTask().execute(address);
                break;
        }
    }

    public void findRegion(int sigunguCode){
        address = getResources().getString(R.string.s1_url);
        switch (sigunguCode) {
            case 1:
                address = getResources().getString(R.string.s1_url);
                break;
            case 2:
                address = getResources().getString(R.string.s2_url);
                break;
            case 3:
                address = getResources().getString(R.string.s3_url);
                break;
            case 4:
                address = getResources().getString(R.string.s4_url);
                break;
            case 5:
                address = getResources().getString(R.string.s5_url);
                break;
            case 6:
                address = getResources().getString(R.string.s6_url);
                break;
            case 7:
                address = getResources().getString(R.string.s7_url);
                break;
            case 8:
                address = getResources().getString(R.string.s8_url);
                break;
            case 9:
                address = getResources().getString(R.string.s9_url);
                break;
            case 10:
                address = getResources().getString(R.string.s10_url);
                break;
            case 11:
                address = getResources().getString(R.string.s11_url);
                break;
            case 12:
                address = getResources().getString(R.string.s12_url);
                break;
            case 13:
                address = getResources().getString(R.string.s13_url);
                break;
            case 14:
                address = getResources().getString(R.string.s14_url);
                break;
            case 15:
                address = getResources().getString(R.string.s15_url);
                break;
            case 16:
                address = getResources().getString(R.string.s16_url);
                break;
            case 17:
                address = getResources().getString(R.string.s17_url);
                break;
            case 18:
                address = getResources().getString(R.string.s18_url);
                break;
            case 19:
                address = getResources().getString(R.string.s19_url);
                break;
            case 20:
                address = getResources().getString(R.string.s20_url);
                break;
            case 21:
                address = getResources().getString(R.string.s21_url);
                break;
            case 22:
                address = getResources().getString(R.string.s22_url);
                break;
            case 23:
                address = getResources().getString(R.string.s23_url);
                break;
            case 24:
                address = getResources().getString(R.string.s24_url);
                break;
            case 25:
                address = getResources().getString(R.string.s25_url);
                break;
        }
        new FoodAsyncTask().execute(address);
    }

    class FoodAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 50000;
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(FoodActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... params) {
            String addr = params[0];
            Log.i(TAG, "AsyncTask: " + addr);
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
                Toast.makeText(FoodActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            FoodXmlParser parser = new FoodXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!resultList.isEmpty()) adapter.clear();

//            parsing 수행
            resultList = parser.parse(s, category);

            if(resultList.isEmpty()){
                Toast.makeText(FoodActivity.this, "정보가 없습니다!", Toast.LENGTH_SHORT).show();
            }
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            adapter.addAll(resultList);
            progressDlg.dismiss();
        }
    }

    class SearchFoodAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 50000;
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(FoodActivity.this, "Wait", "검색 결과 찾는중...");
        }

        @Override
        protected String doInBackground(String... params) {
            String addr = params[0];
            Log.i(TAG, "AsyncTask: " + addr);
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
                Toast.makeText(FoodActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            SearchXmlParser parser = new SearchXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!resultList.isEmpty()) adapter.clear();

//            parsing 수행
            resultList = parser.parse(s, menu);

            if(resultList.isEmpty()){
                Toast.makeText(FoodActivity.this, "정보가 없습니다!", Toast.LENGTH_SHORT).show();
            }
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            adapter.addAll(resultList);
            progressDlg.dismiss();
        }
    }
}
