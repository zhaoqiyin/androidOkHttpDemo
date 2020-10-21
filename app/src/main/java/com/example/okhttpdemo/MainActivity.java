package com.example.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private static final String TAG = "MainActivity---";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
    }

    public void getSyncRequest(View view) {
        // 1. 拿到OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 2. 构造Request对象
        final Request request = new Request.Builder()
                .get()
                .url("https:www.baidu.com")
                .build();
        // 3. 将Request封装为Call
        final Call call = client.newCall(request);
        // 4. 根据需要调用同步请求方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 同步调用,返回Response，会抛出IO异常
                    Response response = call.execute();
                    Log.d(TAG, "response.code() = " + response.code() +
                            ", response.message() = " + response.message() +
                            ", response.body() = " + response.body().string() +
                            "");
                } catch (IOException e) {
                    Log.d(TAG, "" + e +
                            "");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getAsyncRequest(View view) {
        // 1. 拿到OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 2. 构造Request对象
        final Request request = new Request.Builder()
                .get()
                .url("https:www.baidu.com")
                .build();
        // 3. 将Request封装为Call
        final Call call = client.newCall(request);
        // 4. 根据需要调用异步请求方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "get failed" +
                        "");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(res);
                    }
                });
            }
        });
    }

    public void postFormRequest(View view) {
        // 1. 拿到OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 2. 构建FormBody，传入参数
        FormBody formBody = new FormBody.Builder()
                .add("username", "admin")
                .add("username", "admin")
                .build();
        // 3. 构建Request，将FormBody作为Post方法的参数传入
        final Request request = new Request.Builder()
                .url("http://www.jianshu.com")
                .post(formBody)
                .build();
        // 4. 将Request封装为Call
        Call call = client.newCall(request);
        // 5. 调用请求，重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "get failed" +
                        ", e.getMessage() = " + e.getMessage() +
                        ", e = " + e +
                        "");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(res);
                    }
                });
            }
        });

    }
}
