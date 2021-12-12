package com.example.planteinvasives.vue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.planteinvasives.R;
import com.example.planteinvasives.vue.activity.InfoActivity;
import com.example.planteinvasives.vue.activity.MenuActivity;

public class InfoFragment extends Fragment {

    ImageView image;
    Button btn;
    TextView apercu, description;
    //numero du fragment
    private int curpos = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info,container,false);
        image = view.findViewById(R.id.imginfo);
        btn = view.findViewById(R.id.btnNextfrag);
        apercu = view.findViewById(R.id.quickdesc);
        description = view.findViewById(R.id.textinfo);

        switch (curpos){
            case 1:
                image.setImageResource(R.drawable.background);
                btn.setText("Suivant");
                apercu.setText("Identifier et repérer!");
                description.setText("Utilisez la caméra de l'application pour prendre en photo les espèces invasives.");
                image.setImageResource(R.drawable.takephoto);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {
                        InfoActivity.next_fragment(getView());
                    }
                });
                break;
            case 2:
                image.setImageResource(R.drawable.background2);
                btn.setText("Suivant");
                apercu.setText("Complétez le formulaire!");
                description.setText("Un formulaire sera disponible pour renseigner des informations sur l'espece prise en photo");
                image.setImageResource(R.drawable.form);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InfoActivity.next_fragment(getView());
                    }
                });
                break;
            case 3:
                image.setImageResource(R.drawable.background);
                btn.setText("J'ai compris!");
                apercu.setText("Configuration des noms d'espèces");
                description.setText("Pour utiliser l'application, une liste de 5 plantes doit être paramètrée");
                image.setImageResource(R.drawable.param);
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
