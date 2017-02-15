package com.doandstevensen.lifecollage.ui.collage_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.doandstevensen.lifecollage.R;

/**
 * Created by Sheena on 2/13/17.
 */

public class DeleteCollageDialogFragment extends DialogFragment {
    private int mCollageId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_delete_collage, null))
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDeleteDialogPositiveClick(DeleteCollageDialogFragment.this, mCollageId);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(DeleteCollageDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public void setCollageId(int collageId) {
        mCollageId = collageId;
    }

    public interface DeleteCollageDialogListener {
        public void onDeleteDialogPositiveClick(DialogFragment dialog, int collageId);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DeleteCollageDialogFragment.DeleteCollageDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeleteCollageDialogFragment.DeleteCollageDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
