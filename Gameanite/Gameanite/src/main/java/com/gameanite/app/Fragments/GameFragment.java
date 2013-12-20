package com.gameanite.app.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gameanite.app.Adapters.ActiveGamesAdapter;
import com.gameanite.app.Containers.GameRoomInfo;
import com.gameanite.app.Containers.UserInfo;
import com.gameanite.app.MainActivity;
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
public class GameFragment extends Fragment implements UserInfoDialogFragment.NoticeDialogListener{
    private final SocketIO socketIO;
    private ActiveGamesAdapter activeGamesAdapter;
    private ArrayList<GameRoomInfo> activeGames = new ArrayList<GameRoomInfo>();
    private GameFragment thisFragment;
    public UserInfo userInfo;

    private enum SocketTypes {
        connected,
        gameList,
        connectionClose;
    }

    public GameFragment(URL connectString,Activity activity){
        System.out.println("Connecting to: " + connectString.toString());
        thisFragment = this;
        userInfo = new UserInfo();
        final ProgressDialog connection = new ProgressDialog(activity);
        connection.setMessage("Connecting...");
        connection.show();
        //getActivity().setTitle("Game List");
        socketIO = new SocketIO(connectString,new IOCallback() {
            @Override
            public void onDisconnect() {

            }

            @Override
            public void onConnect() {
                connection.cancel();
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
                        try {
                            JSONObject gameList = new JSONObject(args[0].toString());
                            Iterator<String> iterator = gameList.keys();
                            activeGames.clear();
                            while (iterator.hasNext()){
                                String key = iterator.next();
                                GameRoomInfo info = new GameRoomInfo();
                                info.setRoom(key);
                                JSONObject game = (JSONObject) gameList.get(key);
                                String gameName = game.getString("roomName");
                                info.setRoomName(gameName);
                                activeGames.add(info);
                            }
                            thisFragment.UpdateActiveGameList();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("ListCount: " +activeGames.size());
                        break;
                    case connectionClose:
                        break;
                }

            }

            @Override
            public void onError(SocketIOException e) {
                System.out.println("Error Socket:");
                e.printStackTrace();
            }
        });
    }

    private void SendClientInfo(){
        JSONObject user = new JSONObject();
        try {
            user.put("clientName",userInfo.getUserName());
            user.put("clientColor",userInfo.getColor());
            user.put("clientRoom",userInfo.getRoom());
            user.put("clientRoomSocket",userInfo.getRoomSocket());

            socketIO.emit("clientAdd",user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void UpdateActiveGameList(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activeGamesAdapter = new ActiveGamesAdapter(getActivity(), activeGames);
                ListView activeGamesList = (ListView) getActivity().findViewById(R.id.ActiveGamesList);
                activeGamesList.setAdapter(activeGamesAdapter);
                activeGamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        userInfo.setRoom(activeGamesAdapter.getItem(position).getRoomName());
                        userInfo.setRoomSocket(activeGamesAdapter.getItem(position).getRoom());
                        DialogFragment newFragment = UserInfoDialogFragment.newInstance(thisFragment);
                        newFragment.show(getFragmentManager(),null);
                        parent.setVisibility(View.GONE);
                        Button rollDice = (Button) getActivity().findViewById(R.id.RollDiceBtn);
                        rollDice.setVisibility(View.VISIBLE);
                        rollDice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                JSONObject dice = new JSONObject();
                                try {
                                    dice.put("roll",1);
                                    dice.put("clientRoomSocket",userInfo.getRoomSocket());
                                    socketIO.emit("rollDice",dice);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.game_fragment, container, false);

        return rootView;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog thisDialog = dialog.getDialog();
        EditText userName = (EditText) thisDialog.findViewById(R.id.clientName);
        EditText userColor = (EditText) thisDialog.findViewById(R.id.clientColor);
        userInfo.setUserName(userName.getText().toString());
        userInfo.setColor(userColor.getText().toString());
        SendClientInfo();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
