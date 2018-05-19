package com.example.android.fistconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.models.User;

import java.util.ArrayList;

public class EnemyObjectAdapter extends ArrayAdapter<User> {

    //Constructor with context, ArrayList of enemies
    public EnemyObjectAdapter(Context context, ArrayList<User> enemies) {
        super(context, 0, enemies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        User enemy = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_enemyobjectadapter, parent, false);
        }

        TextView enemyUsername = (TextView) convertView.findViewById(R.id.enemy_username);
        TextView enemyLevel = (TextView) convertView.findViewById(R.id.enemy_level);
        ImageView enemyImage = (ImageView) convertView.findViewById(R.id.enemy_image);

        enemyUsername.setText(String.valueOf(enemy.getUsername()));
        enemyLevel.setText(String.valueOf(enemy.getLevel()));

        switch (enemy.getAvatarID()) {
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
