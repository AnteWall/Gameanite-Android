package com.gameanite.app.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.gameanite.app.R;

/**
 * Created by Klante on 2013-12-20.
 */
public class UserInfoDialogFragment extends DialogFragment {

    private GameFragment gameFragment;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public UserInfoDialogFragment(GameFragment thisFragment){
        gameFragment = thisFragment;
    }

    NoticeDialogListener mListener;

    static UserInfoDialogFragment newInstance(GameFragment thisFragment) {
        UserInfoDialogFragment f = new UserInfoDialogFragment(thisFragment);

        // Supply num input as an argument.
        Bundle args = new Bundle();
        //args.putInt("num", num);
        //f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) this.gameFragment;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText username = (EditText)getActivity().findViewById(R.id.clientName);
        final EditText userColor = (EditText)getActivity().findViewById(R.id.clientColor);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.choose_name_dialog,null))

                .setPositiveButton(R.string.CreateUserPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(UserInfoDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.Canel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
