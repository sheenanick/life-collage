package com.doandstevensen.lifecollage.ui.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.doandstevensen.lifecollage.R;

/**
 * Created by Sheena on 2/15/17.
 */

public class DeleteAccountDialogFragment extends android.support.v4.app.DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_delete_account, null))
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(DeleteAccountDialogFragment.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(DeleteAccountDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public interface DeleteAccountDialogListener {
        void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog);
        void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog);
    }

    DeleteAccountDialogFragment.DeleteAccountDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeleteAccountDialogFragment.DeleteAccountDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
