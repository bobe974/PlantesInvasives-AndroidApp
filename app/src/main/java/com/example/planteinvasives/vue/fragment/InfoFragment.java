package com.example.planteinvasives.vue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.planteinvasives.R;
import com.example.planteinvasives.vue.activity.MenuActivity;

public class InfoFragment extends Fragment {

    ImageView image;
    Button btn;
    //numero du fragment
    private int curpos = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info,container,false);
        image = view.findViewById(R.id.imginfo);
        btn = view.findViewById(R.id.btnNextfrag);

        switch (curpos){
            case 1:
                image.setImageResource(R.drawable.background);
                btn.setText("Suivant");
                break;
            case 2:
                image.setImageResource(R.drawable.background2);
                btn.setText("suivant2");
                break;
            case 3:
                image.setImageResource(R.drawable.background);
                btn.setText("c'est parti!");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().startActivity(new Intent(getContext(), MenuActivity.class));
                    }
                });
                break;
        }

        return view;
    }

    public void setCurpos(int pos){
        this.curpos = pos;
    }
}
