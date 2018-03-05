package com.liuxiaozhu.hashmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * HashMap自己实现
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyHashMap<Integer, Integer> hashMap = new MyHashMap<>(20);
        for (int i = 0; i < 20; i++) {
            hashMap.put(i, i);
        }
        Log.e("TAG", "第一次打印" + hashMap.toString());
        for (int i = 0; i < hashMap.size; i++) {
            Log.e("TAG", "key" + i + "val" + hashMap.get(i));
        }
    }
}
