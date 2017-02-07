package com.doandstevensen.lifecollage.ui.collage_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;

/**
 * Created by Sheena on 2/7/17.
 */

public class NewCollageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_new_collage, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText collageName = (EditText) dialogView.findViewById(R.id.collageName);
                        String name = collageName.getText().toString();
                        mListener.onDialogPositiveClick(NewCollageDialogFragment.this, name);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(NewCollageDialogFragment.this);
                    }
                });
        return builder.create();
    }


    public interface NewCollageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String collageName);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NewCollageDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NewCollageDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
