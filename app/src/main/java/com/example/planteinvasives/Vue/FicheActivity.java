package com.example.planteinvasives.Vue;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;

public class FicheActivity extends AppCompatActivity {
    private ListView mListView;
    private MyArrayAdapter mAdapter;
    private String taches[] = {"Douche", "Sieste"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        mListView = (ListView) findViewById(R.id.listeFiche);
        mAdapter = new MyArrayAdapter(this, taches);
        mListView.setAdapter(mAdapter);

    }



}