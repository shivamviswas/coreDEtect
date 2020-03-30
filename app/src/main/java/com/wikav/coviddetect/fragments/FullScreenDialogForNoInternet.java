package com.wikav.coviddetect.fragments;

import android.app.Dialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.wikav.coviddetect.R;


public class FullScreenDialogForNoInternet extends DialogFragment {
    private ConnectivityManager connectivityManager;
    private android.net.ConnectivityManager.NetworkCallback mCallback;
        boolean back=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_no_intenet, container, false);

        return view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
               if(back)
               {
                   getActivity().finish();
                   dismiss();
               }
               else
               {
                   back=true;
                   Toast.makeText(getActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();

               }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }



}
