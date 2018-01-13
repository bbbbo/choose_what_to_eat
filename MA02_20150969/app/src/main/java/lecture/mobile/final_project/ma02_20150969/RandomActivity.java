package lecture.mobile.final_project.ma02_20150969;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import lecture.mobile.final_project.ma02_20150969.Food.FoodActivity;
import lecture.mobile.final_project.ma02_20150969.cook.CookActivity;

public class RandomActivity extends AppCompatActivity {
    private static final String TAG = "RandomActivity";
    private TextView tvResult;
    int sigunguCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        tvResult = (TextView)findViewById(R.id.tvResult);

        Intent resultIntent = getIntent();
        sigunguCode = resultIntent.getIntExtra("sigunguCode", 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_random, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        String category = "한식";
        int id = item.getItemId();

        if (sigunguCode == 0) {
            intent = new Intent(RandomActivity.this, SeoulFoodActivity.class);
        } else {
            intent = new Intent(RandomActivity.this, FoodActivity.class);
        }

        switch (id){
            case R.id.korea:
                category = "한식";
                intent.putExtra("sigunguCode", sigunguCode);
                intent.putExtra("category", category);
                startActivity(intent);
                Toast.makeText(RandomActivity.this, "한식", Toast.LENGTH_SHORT).show();
                break;
            case R.id.japan:
                category = "일식";
                intent.putExtra("sigunguCode", sigunguCode);
                intent.putExtra("category", category);
                startActivity(intent);
                Toast.makeText(RandomActivity.this, "일식", Toast.LENGTH_SHORT).show();
                break;
            case R.id.china:
                category = "중국식";
                intent.putExtra("sigunguCode", sigunguCode);
                intent.putExtra("category", category);
                startActivity(intent);
                Toast.makeText(RandomActivity.this, "중식", Toast.LENGTH_SHORT).show();
                break;
            case R.id.western:
                category = "경양식";
                intent.putExtra("sigunguCode", sigunguCode);
                intent.putExtra("category", category);
                startActivity(intent);
                Toast.makeText(RandomActivity.this, "경양식", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cook:
                Intent cookIntent = new Intent(RandomActivity.this, CookActivity.class);
                startActivity(cookIntent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnRoll:
                String menu;
                Random dice = new Random();
                int result = dice.nextInt(20) + 1;
                ArrayList<String> food = new ArrayList<String>();
                food.add("삼겹살");
                food.add("짜장면");
                food.add("돈까스");
                food.add("떡볶이");
                food.add("냉면");
                food.add("보쌈");
                food.add("부대찌개");
                food.add("초밥");
                food.add("회");
                food.add("파스타");
                food.add("스테이크");
                food.add("족발");
                food.add("보쌈");
                food.add("치킨");
                food.add("피자");
                food.add("햄버거");
                food.add("짬뽕");
                food.add("설렁탕");
                food.add("찜닭");
                food.add("빵");
                tvResult.setText(food.get(result));
                break;
        }
    }
}
