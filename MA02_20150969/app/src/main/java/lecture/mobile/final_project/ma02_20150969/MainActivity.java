package lecture.mobile.final_project.ma02_20150969;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lecture.mobile.final_project.ma02_20150969.Food.DetailActivity;
import lecture.mobile.final_project.ma02_20150969.Food.FoodActivity;
import lecture.mobile.final_project.ma02_20150969.MY.MyListActivity;
import lecture.mobile.final_project.ma02_20150969.Seoul.SimpleRes;

public class MainActivity extends AppCompatActivity {
    private final static int PERMISSION_REQ_CODE = 100;
    private final static String TAG = "MainActivity";
    private LocationManager locManager;
    private Geocoder geocoder;

    private ListView lvList;

    private ArrayAdapter<SimpleRes> adapter;
    private ArrayList<SimpleRes> resultList;

    private String[] regionCode = { "전체" , "강남구", "강동구", "강북구", "강서구",
                "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구",
                "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구",
                "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구" };
    private String region = regionCode[0];
    private int sigunguCode = 0;

    private TextView tvPosition;
    private TextView tvBest;
    private Spinner spinner;
    private LinearLayout layout_random;
    private LinearLayout layout_food;
    private LinearLayout layout_korea;
    private LinearLayout layout_japan;
    private LinearLayout layout_china;
    private LinearLayout layout_western;
    private LinearLayout layout_world;
    private LinearLayout layout_cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvList = (ListView)findViewById(R.id.listView);
        tvPosition = (TextView)findViewById(R.id.tvPosition);
        tvBest = (TextView)findViewById(R.id.tvBest);
        spinner = (Spinner)findViewById(R.id.spinner);

        layout_random = (LinearLayout)findViewById(R.id.layout_random);
        layout_food = (LinearLayout)findViewById(R.id.layout_food);
        layout_korea = (LinearLayout)findViewById(R.id.layout_korea);
        layout_japan = (LinearLayout)findViewById(R.id.layout_japan);
        layout_china = (LinearLayout)findViewById(R.id.layout_china);
        layout_western = (LinearLayout)findViewById(R.id.layout_western);
        layout_world = (LinearLayout)findViewById(R.id.layout_world);
        layout_cook = (LinearLayout)findViewById(R.id.layout_cook);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_region, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "정보 로딩 중입니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setAdapter(spinnerAdapter);


        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        resultList = new ArrayList<SimpleRes>();
        adapter = new ArrayAdapter<SimpleRes>(this, android.R.layout.simple_list_item_1, resultList);
        lvList.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION },
                        PERMISSION_REQ_CODE);
            return;
        }
//                위치 정보 수신 시작  (테스트를 위해 passive provider로 진행)
        locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 10, locationListener);
        geocoder = new Geocoder(this);

        String address = getResources().getString(R.string.areaBased_url);

        if(sigunguCode == 0) {   //지역 못 찾았을 경우
            tvBest.setVisibility(View.VISIBLE);
        }

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleRes dto = resultList.get(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("title", dto.getTitle());
                intent.putExtra("tel", dto.getTel());
                intent.putExtra("address", dto.getAddr());
                startActivity(intent);
            }
        });


    }

    //layout onClick 실행
    public void onClick(View v){
        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
        Intent s_intent = new Intent(MainActivity.this, SeoulFoodActivity.class);
        String str_region = spinner.getSelectedItem().toString();
        switch (v.getId()){
            case R.id.layout_random:
                Intent randomIntent = new Intent(MainActivity.this, RandomActivity.class);
                intent = null;
                randomIntent.putExtra("sigunguCode", sigunguCode);
                startActivity(randomIntent);
                break;
            case R.id.layout_food:
                intent.putExtra("category", "null");
                s_intent.putExtra("category", "null");
                break;
            case R.id.layout_korea:
                intent.putExtra("category", "한식");
                s_intent.putExtra("category", "한식");
                break;
            case R.id.layout_japan:
                intent.putExtra("category", "일식");
                s_intent.putExtra("category", "일식");
                break;
            case R.id.layout_china:
                intent.putExtra("category", "중국식");
                s_intent.putExtra("category", "중국식");
                break;
            case R.id.layout_western:
                intent.putExtra("category", "경양식");
                s_intent.putExtra("category", "경양식");
                break;
            case R.id.layout_world:
                intent.putExtra("category", "기타");
                s_intent.putExtra("category", "기타");
                break;
            case R.id.layout_cook:
                intent = null;
                Intent newIntent = new Intent(MainActivity.this, MyListActivity.class);
                startActivity(newIntent);
                break;
        }

        if(intent != null){
            if(sigunguCode == 0){
                if(str_region.equals("지역 선택")) {
                    s_intent.putExtra("sigunguCode", sigunguCode);
                }else{
                    for(int i = 1; i < regionCode.length; i++){
                        if(regionCode[i].equals(str_region)){
                            sigunguCode = i;
                            break;
                        }else{
                            continue;
                        }
                    }
                    s_intent.putExtra("sigunguCode", sigunguCode);
                }
                startActivity(s_intent);
            }else{
                if(str_region.equals("지역 선택")) {
                    intent.putExtra("sigunguCode", sigunguCode);
                }else{
                    for(int i = 1; i < regionCode.length; i++){
                        if(regionCode[i].equals(str_region)){
                            sigunguCode = i;
                            break;
                        }else{
                            continue;
                        }
                    }
                    intent.putExtra("sigunguCode", sigunguCode);
                }
                startActivity(intent);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        위치 정보 수신 종료 - 위치 정보 수신 종료를 누르지 않았을 경우를 대비
        locManager.removeUpdates(locationListener);
    }

    /*위치 정보 수신 LocationListener*/
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Current Location : " + location.getLatitude() + ", " + location.getLongitude());

            List<Address> addressList = null;
            try{
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addressList != null && addressList.size() > 0){
                region = addressList.get(0).getSubLocality();    //getLocality() -> 서울특별시
                tvPosition.setText(region);
                Log.i(TAG, "지역: " + region);
            }

            for(int i = 1; i < regionCode.length; i++){
                if(regionCode[i].equals(region)){
                    sigunguCode = i;
                    break;
                }else{
                    continue;
                }
            }

            String address = getResources().getString(R.string.areaBased_url);

            if(sigunguCode != 0){
                tvBest.setVisibility(View.GONE);
                String p = "&sigunguCode=" + sigunguCode;
                new CurrentLocAsyncTask().execute(address + p);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };


    class CurrentLocAsyncTask extends AsyncTask<String, Integer, String> {
        public final static int TIME_OUT = 10000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            Log.i(TAG, "AsyncTask: " + address);
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
                Toast.makeText(MainActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            MyXmlParser parser = new MyXmlParser();

//            어댑터에 이전에 보여준 데이터가 있을 경우 클리어
            if (!resultList.isEmpty()) adapter.clear();

//            parsing 수행
            resultList = parser.parse(s);

            Log.v(TAG, "가게 " + resultList.get(0).getTitle());
//            리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
            adapter.addAll(resultList);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission was granted!", Toast.LENGTH_SHORT).show();
                } else {
//                   Toast.makeText(this, "Permission was denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
