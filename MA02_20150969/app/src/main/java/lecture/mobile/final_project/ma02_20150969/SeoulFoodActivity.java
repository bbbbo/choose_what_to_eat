package lecture.mobile.final_project.ma02_20150969;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import lecture.mobile.final_project.ma02_20150969.Food.DetailActivity;
import lecture.mobile.final_project.ma02_20150969.Seoul.SimpleRes;

public class SeoulFoodActivity extends AppCompatActivity {
    private EditText etTarget;      //검색 키워드
    private ListView lvList;
    private ArrayAdapter<SimpleRes> simpleAdapter;
    private ArrayList<SimpleRes> simpleList;
    String category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seoul_food);

        etTarget = (EditText)findViewById(R.id.reName);
        lvList = (ListView)findViewById(R.id.lvList);

        simpleList = new ArrayList<SimpleRes>();
        simpleAdapter = new ArrayAdapter<SimpleRes>(this, android.R.layout.simple_list_item_1, simpleList);
        lvList.setAdapter(simpleAdapter);

        Intent intent = getIntent();
        int sigunguCode = intent.getIntExtra("sigunguCode", 1);
        category = intent.getStringExtra("category");

        if(sigunguCode == 0) {   //지역 알수없을 경우 서울 전체지역으로 보여주기

            String url = getResources().getString(R.string.food_url);
            String cat = "&cat3=";

            if (category.equals("null")) {
                new SeoulFoodActivity.AllAsyncTask().execute(url);
            } else {
                String kind = null;
                if (category.equals("한식")) {
                    kind = "A05020100";
                } else if (category.equals("일식")) {
                    kind = "A05020300";
                } else if (category.equals("중국식")) {
                    kind = "A05020400";
                } else if (category.equals("경양식")) {
                    kind = "A05020200";
                } else if (category.equals("기타")) {
                    kind = "A05020500";
                }

                if (kind.equals("null")) {
                    new SeoulFoodActivity.AllAsyncTask().execute(url);
                } else {
                    new SeoulFoodActivity.AllAsyncTask().execute(url + cat + kind);
                }
            }
        }


            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SimpleRes dto = simpleList.get(position);
                    Intent intent = new Intent(SeoulFoodActivity.this, DetailActivity.class);
                    intent.putExtra("title", dto.getTitle());
                    intent.putExtra("tel", dto.getTel());
                    intent.putExtra("address", dto.getAddr());
                    startActivity(intent);
                }
            });

        }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:
                String address = getResources().getString(R.string.server_url);
                String target = etTarget.getText().toString();
                if (target.equals("")) target = etTarget.getHint().toString();  // 입력값이 없을 경우 hint 속성의 값을 기본 값으로 설정
                new SeoulFoodActivity.NetworkAsyncTask().execute(address + target);
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        public final static String TAG = "NetworkAsyncTask";
        public final static int TIME_OUT = 10000;

        ProgressDialog progressDlg;

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            StringBuilder resultBuilder = new StringBuilder();

            try {
                URL url = new URL(address);
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
                Toast.makeText(SeoulFoodActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultBuilder.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SeoulFoodActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected void onPostExecute(String s) {
            MyXmlParser parser = new MyXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!simpleList.isEmpty()) simpleAdapter.clear();

//            parsing 수행
            simpleList = parser.parse(s);

            if(simpleList.isEmpty()){
                Toast.makeText(SeoulFoodActivity.this, "정보가 없습니다!", Toast.LENGTH_SHORT).show();
            }
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            simpleAdapter.addAll(simpleList);

            progressDlg.dismiss();
        }
    }

    class AllAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 10000;
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SeoulFoodActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            StringBuilder resultBuilder = new StringBuilder();

            try {
                URL url = new URL(address);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            MyXmlParser parser = new MyXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!simpleList.isEmpty()) simpleAdapter.clear();

//            parsing 수행
            simpleList = parser.parse(s);

            if(simpleList.isEmpty()){
                Toast.makeText(SeoulFoodActivity.this, "정보가 없습니다!", Toast.LENGTH_SHORT).show();
            }
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            simpleAdapter.addAll(simpleList);
            progressDlg.dismiss();
        }
    }

}
