package ru.samsung.itschool.mdev.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.samsung.itschool.mdev.myapplication.api.YandexAPI;
import ru.samsung.itschool.mdev.myapplication.model.Answer;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://predictor.yandex.net/";
    public static final String KEY = "pdct.1.1.20220412T145449Z.ed53b660ddacdc8e.353ee4c0c5adc174b6be636450d97faa6e34a072";
    private TextView tv;
    private EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        ed = findViewById(R.id.editText);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) {
                    tv.setText("");
                } else
                doRequest();
            }
        });

    }

    public void doRequest() {
        // retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YandexAPI api = retrofit.create(YandexAPI.class);
        api.req(KEY,ed.getText().toString(),"en",5).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                try {
                    if(response.code() == 200) {
                        List<String> answerList = response.body().getText();
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String s: answerList)
                            stringBuilder.append(s+"\n");
                        tv.setText(stringBuilder.toString());
                    }
                } catch (Exception e) {
                    Log.d("RRR",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.d("RRR",t.getMessage());
            }
        });
    }
}