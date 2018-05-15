package com.example.android.fistconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.models.Enemy;

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

        switch (enemy.avatarID) {
            case 0:
                enemyImage.setImageResource(R.drawable.playericon);
                break;

            case 1:
                enemyImage.setImageResource(R.drawable.playericon1);
                break;
        }

        return convertView;
    }
}
