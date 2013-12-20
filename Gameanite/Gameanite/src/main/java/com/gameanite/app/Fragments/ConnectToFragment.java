package com.gameanite.app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gameanite.app.MainActivity;
import com.gameanite.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
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
public class ConnectToFragment extends Fragment implements View.OnClickListener{

    private Button connectToBtn;
    private EditText connectInput;

    public ConnectToFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_to_fragment, container, false);

        connectInput = (EditText) rootView.findViewById(R.id.ConnectToServerInput);
        connectToBtn = (Button) rootView.findViewById(R.id.ConnectToServerBtn);
        connectToBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ConnectToServerBtn:
                URL url = null;
                try {
                    url = new URL(connectInput.getText().toString());

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    GameFragment fragment = new GameFragment(url,getActivity());
                    fragmentTransaction.add(R.id.container,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } catch (MalformedURLException e) {
                    Toast msg = Toast.makeText(getActivity(),"Error Parsing URL",Toast.LENGTH_LONG);
                    msg.show();
                    e.printStackTrace();
                }
                break;
        }
    }
}
