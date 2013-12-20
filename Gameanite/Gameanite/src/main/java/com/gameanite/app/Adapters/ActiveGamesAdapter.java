package com.gameanite.app.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gameanite.app.Containers.GameRoomInfo;
import com.gameanite.app.R;

import java.util.ArrayList;

/**
 * Created by Klante on 2013-12-18.
 */
public class ActiveGamesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<GameRoomInfo> games = new ArrayList<GameRoomInfo>();
    private Context context;

    public ActiveGamesAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);

    }

    public ActiveGamesAdapter(Context context, ArrayList<GameRoomInfo> games) {
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        if(games.size() == 0){
            AddItem("No games available");
        }else{
            this.games = games;
        }
    }

    public void AddItem(String name){
        System.out.println("ADDING: " + name);
        games.add(new GameRoomInfo(name));
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public GameRoomInfo getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.game_list_item,null);
            holder.textView = (TextView)convertView.findViewById(R.id.GameListItemText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(games.get(position).getRoomName());
        return convertView;
    }

    public void AddNewList(ArrayList<GameRoomInfo> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    public static class ViewHolder{
        public TextView textView;
    }
}
