package com.example.planteinvasives.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planteinvasives.R;

public class MyArrayAdapter extends ArrayAdapter<String> {

    private final Context context;

    public MyArrayAdapter(Context context, String[] values) {
        super(context, R.layout.activity_cellule, values);
        this.context = context;
    }

    // Méthode appelée automatiquement lorsqu’Android
    // doit afficher une cellule:
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gestion optimisée de la mémoire (réutilisation de cellules):
        View cellView = convertView;
        if (cellView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.activity_cellule, parent, false);
        }
        //Remplissage de la cellule:
        TextView textView = (TextView)cellView.findViewById(R.id.nomPlante);
        TextView nomPlante =(TextView)cellView.findViewById(R.id.nomUtilisateur);
        ImageView imageView = (ImageView)cellView.findViewById(R.id.photo);

        String s = getItem(position);
        textView.setText(s);
        nomPlante.setText("user :");
        imageView.setImageResource(R.drawable.map);
        //imageView.setImageResource(R.drawable.camera);
        return cellView;
    }
    }
