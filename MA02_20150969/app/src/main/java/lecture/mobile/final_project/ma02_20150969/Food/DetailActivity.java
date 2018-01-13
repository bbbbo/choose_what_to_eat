package lecture.mobile.final_project.ma02_20150969.Food;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import lecture.mobile.final_project.ma02_20150969.MY.MyDBHelper;
import lecture.mobile.final_project.ma02_20150969.MY.MyListActivity;
import lecture.mobile.final_project.ma02_20150969.R;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvTel;
    private TextView tvAddr;
    private TextView tvRating;
    private RatingBar rb;

    private GoogleMap mGoogleMap;
    private Geocoder geocoder;
    private MarkerOptions markerOptions;
    private Marker marker;
    private String address = "서울";
    private String title;
    private String tel;

    MyDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTel = (TextView) findViewById(R.id.tvTel);
        tvAddr = (TextView) findViewById(R.id.tvAddr);

        helper = new MyDBHelper(this);
        Intent resultIntent = getIntent();

        title = resultIntent.getStringExtra("title");
        tel = resultIntent.getStringExtra("tel");
        address = resultIntent.getStringExtra("address");

        tvTitle.setText(title);
        if (tel.length() == 0) {
            tvTel.setText("정보없음");
        } else {
            tvTel.setText(tel);
        }
        tvAddr.setText(address);

        tvRating = (TextView)findViewById(R.id.tvRating);
        rb = (RatingBar)findViewById(R.id.ratingBar);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRating.setText(String.valueOf(rating));
            }
        });

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker));

        geocoder = new Geocoder(this);
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            List<Address> addressList = null;
            try{
                addressList = geocoder.getFromLocationName(address, 1);
            }catch (IOException e){
                e.printStackTrace();
            }

            if(addressList != null && addressList.size() > 0){
                LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                markerOptions.title(title);
                markerOptions.snippet(tel);
                markerOptions.position(latLng);

                marker = mGoogleMap.addMarker(markerOptions);
                marker.showInfoWindow();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.store:
                SQLiteDatabase db = helper.getWritableDatabase();

                String name = tvTitle.getText().toString();
                String phone = tvTel.getText().toString();
                String address = tvAddr.getText().toString();

                ContentValues row = new ContentValues();
                row.put(MyDBHelper.COL_TITLE, name);
                row.put(MyDBHelper.COL_TEL, phone);
                row.put(MyDBHelper.COL_ADDRESS, address);

                db.insert(MyDBHelper.TABLE_NAME, null, row);
                helper.close();
                Toast.makeText(DetailActivity.this, "저장완료", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_SUBJECT, "Go");
                msg.putExtra(Intent.EXTRA_TEXT, "가게이름 " + tvTitle.getText().toString() + "\n주소 " + tvAddr.getText().toString());
                msg.putExtra(Intent.EXTRA_TITLE, "제목");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유"));
                break;
            case R.id.list:
                startActivity(new Intent(DetailActivity.this, MyListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
