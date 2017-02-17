package com.doandstevensen.lifecollage.ui.collage_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;

/**
 * Created by Sheena on 2/17/17.
 */

public class UpdateCollageDialogFragment extends DialogFragment {
    private int mCollageId;
    private String mCollageName;
    private EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_update_collage, null);
        mEditText = (EditText) view.findViewById(R.id.collageName);
        mEditText.setText(mCollageName);
        builder.setView(view)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogView = (Dialog) dialog;
                        String name = mEditText.getText().toString();
                        mListener.onUpdateDialogPositiveClick(UpdateCollageDialogFragment.this, name, mCollageId);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(UpdateCollageDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public void setCollage(int collageId, String collageName) {
        mCollageId = collageId;
        mCollageName = collageName;
    }

    public interface UpdateCollageDialogListener {
        void onUpdateDialogPositiveClick(DialogFragment dialog, String collageName, int collageId);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    UpdateCollageDialogFragment.UpdateCollageDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UpdateCollageDialogFragment.UpdateCollageDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
