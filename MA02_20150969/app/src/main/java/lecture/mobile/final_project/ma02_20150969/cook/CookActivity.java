package lecture.mobile.final_project.ma02_20150969.cook;

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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import lecture.mobile.final_project.ma02_20150969.MainActivity;
import lecture.mobile.final_project.ma02_20150969.R;
import lecture.mobile.final_project.ma02_20150969.RandomActivity;

public class CookActivity extends AppCompatActivity {
    private final static String TAG = "CookActivity";
    private EditText etCook;
    private String address;
    private ListView lvCook;
    private Spinner spinner;

    private CookAdapter c_adapter;
    private ArrayList<Cook> cookList;

    private String id = "0";
    String word = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

//        etCook = (EditText)findViewById(R.id.etCook);
        lvCook = (ListView)findViewById(R.id.cookList);
        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_recipe, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(spinnerAdapter);

        address = getResources().getString(R.string.cook_url);
        Log.d(TAG, "CookAsyncTask: " + address);
        cookList = new ArrayList<Cook>();
        c_adapter = new CookAdapter(this, R.layout.search_recipe_layout, cookList);

        lvCook.setAdapter(c_adapter);


    }

    public void menu(){
        String recipe_menu = spinner.getSelectedItem().toString();
        if(!recipe_menu.equals("음식 선택")) {
            new cookSearchAsyncTask().execute(address + recipe_menu);
        }
    }


    public void findRecipe(){
        lvCook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CookActivity.this, RecipeActivity.class);
                intent.putExtra("id", cookList.get(0).getRecipe_id());
                intent.putExtra("summary", cookList.get(0).getSummary());
                intent.putExtra("name", cookList.get(0).getName());
                intent.putExtra("image", cookList.get(0).getImage());
                startActivity(intent);
            }
        });
    }

    class cookSearchAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 40000;
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(CookActivity.this, "Wait", "검색 결과 찾는중...");
        }

        @Override
        protected String doInBackground(String... params) {
            String addr = params[0];
            Log.v(TAG, "CookAsyncTask: " + addr);
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
                Toast.makeText(CookActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            CookXmlParser parser = new CookXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!cookList.isEmpty()) c_adapter.clear();

//            parsing 수행
            cookList = parser.parse(s);

            if(cookList.isEmpty()){
                Toast.makeText(CookActivity.this, "정보가 없습니다!", Toast.LENGTH_SHORT).show();
            }
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            c_adapter.setList(cookList);
            c_adapter.notifyDataSetChanged();
//            c_adapter.addAll(cookList);
            id = cookList.get(0).getRecipe_id();
            findRecipe();
            progressDlg.dismiss();
        }
    }

}
