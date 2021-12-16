package com.example.planteinvasives.vue.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.planteinvasives.modele.MaFiche;
import com.example.planteinvasives.R;
import com.example.planteinvasives.vue.activity.PhotoActivity;

import java.util.ArrayList;
/**Classe qui gere l'affichage d'une liste
 *dont les données à afficher dans chaque listItem d'une Listview
 * @author etienne baillif
 * @version 1.0
 */

public class MyArrayAdapter extends ArrayAdapter<MaFiche> {

    private final Context context;
    private Activity activity;

    public MyArrayAdapter(Context context, ArrayList<MaFiche> lesfiches, Activity activity) {
        super(context, R.layout.activity_cellule, lesfiches);
        this.context = context;
        this.activity = activity;
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
        //TextView textView = (TextView)cellView.findViewById(R.id.nomPlante);
        TextView nomPlante =(TextView)cellView.findViewById(R.id.nomPlante);
        TextView date = (TextView) cellView.findViewById(R.id.textView_date);

        // id et path Textview hidden
        TextView id = (TextView) cellView.findViewById(R.id.textView_id);
        TextView path = (TextView) cellView.findViewById(R.id.textView_path);
        ImageView imageView = (ImageView)cellView.findViewById(R.id.imageviewphoto);


        //recupere la fiche de l'arraylist  a la pos actuelle
        MaFiche f = getItem(position);
        nomPlante.setText(f.getNomPlante());
        date.setText(f.getDate());

        //chargement de la photo
        //PhotoActivity photoActivity = new PhotoActivity();
        //Bitmap bitmap = photoActivity.loadImageFromStorage(f.getPhotoPath(),activity);
        Glide.with(getContext())
                .load(f.getPhotoPath())
                .centerCrop()
                .into(imageView);




        //imageView.setImageBitmap(bitmap);
        //imageView.setRotation(photoActivity.getPhotoOrientation(f.getPhotoPath()));

        //conversion de id int en string
        id.setText(String.valueOf(f.getId()));
        path.setText(f.getPhotoPath());

        return cellView;
    }
}