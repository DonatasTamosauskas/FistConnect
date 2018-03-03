package com.example.android.fistconnect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Domantas on 2018-03-02.
 */

public class EnemyObjectAdapter extends ArrayAdapter<Enemy> {

    //Constructor with context, ArrayList of enemies
    public EnemyObjectAdapter(Context context, ArrayList<Enemy> enemies) {
        super(context, 0, enemies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Enemy enemy = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_enemyobjectadapter, parent, false);
        }

        TextView enemyUsername = (TextView) convertView.findViewById(R.id.enemy_username);
        TextView enemyLevel = (TextView) convertView.findViewById(R.id.enemy_level);
        ImageView enemyImage = (ImageView) convertView.findViewById(R.id.enemy_image);

        enemyUsername.setText(String.valueOf(enemy.username));
        enemyLevel.setText(String.valueOf(enemy.level));
        enemyImage.setImageResource(R.drawable.ic_launcher_background);

        //Change background of list item
        /*
        if(enemy.isChallenged) {
            LinearLayout layout =(LinearLayout) convertView.findViewById(R.id.enemylayout);
            layout.setBackgroundResource(R.drawable.ic_launcher_background); //change to background image
        } else {
            LinearLayout layout =(LinearLayout) convertView.findViewById(R.id.enemylayout);
            layout.setBackgroundResource(R.drawable.ic_launcher_background); //change to background image
        }
        */

        return convertView;
    }
}
