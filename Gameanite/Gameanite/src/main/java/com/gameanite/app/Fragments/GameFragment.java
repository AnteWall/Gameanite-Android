package com.gameanite.app.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gameanite.app.Adapters.ActiveGamesAdapter;
import com.gameanite.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * Created by Klante on 2013-12-19.
 */
public class GameFragment extends Fragment {
    private final SocketIO socketIO;

    private ListView activeGamesList;
    private ActiveGamesAdapter activeGamesAdapter;
    private ArrayList<String> activeGames = new ArrayList<String>();

    private enum SocketTypes {
        connected,
        gameList,
        connectionClose;
    }

    public GameFragment(URL connectString){
//        final ProgressDialog connection = new ProgressDialog(getActivity());
  //      connection.setTitle("Connecting...");
    //    connection.show();
        //getActivity().setTitle("Game List");
        socketIO = new SocketIO(connectString,new IOCallback() {
            @Override
            public void onDisconnect() {

            }

            @Override
            public void onConnect() {
             //   connection.cancel();
            }

            @Override
            public void onMessage(String s, IOAcknowledge ioAcknowledge) {

            }

            @Override
            public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {

            }

            @Override
            public void on(String event, IOAcknowledge ioAcknowledge, Object... args) {
                System.out.println(args[0]);
                SocketTypes e = SocketTypes.valueOf(event);
                System.out.println(e);
                switch (e){
                    case connected:
                        JSONObject connected = new JSONObject();
                        try {
                            connected.put("status","OK");
                            socketIO.emit("client-connection",connected);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case gameList:
                        System.out.println("GAMELIST:");
                        try {

                            JSONObject gameList = new JSONObject(args[0].toString());
                            System.out.println(gameList.toString());
                            Iterator<String> iterator = gameList.keys();
                            while (iterator.hasNext()){
                                String key = iterator.next();
                                System.out.println(key);
                                JSONObject game = (JSONObject) gameList.get(key);
                                String gameName = game.getString("roomName");
                                System.out.println(gameName);
                                activeGamesAdapter.AddItem(gameName);
                                activeGamesAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        break;
                    case connectionClose:
                        break;
                }

            }

            @Override
            public void onError(SocketIOException e) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.game_fragment, container, false);



        activeGamesAdapter = new ActiveGamesAdapter(getActivity(),activeGames);
        activeGamesList = (ListView) rootView.findViewById(R.id.ActiveGamesList);
        activeGamesList.setAdapter(activeGamesAdapter);



        return rootView;
    }

}
