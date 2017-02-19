package com.doandstevensen.lifecollage.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;

/**
 * Created by Sheena on 2/19/17.
 */

public class DialogBuilder {
    private static EditText mEditText;

    public static Dialog DeleteCollageDialogFragment(Context context, final DialogBuilder.DialogClickListener listener, final int collageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.alert_dialog_delete_collage)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDeleteCollagePositiveClick(collageId);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    public static Dialog NewCollageDialogFragment(Context context, final DialogBuilder.DialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.alert_dialog_new_collage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText collageTitle = (EditText) dialogView.findViewById(R.id.collageName);
                        String title = collageTitle.getText().toString();
                        listener.onNewCollagePositiveClick(title);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    public static Dialog UpdateCollageDialogFragment(Context context, final DialogBuilder.DialogClickListener listener, final String title, final int collageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_dialog_update_collage, null);
        mEditText = (EditText) view.findViewById(R.id.collageName);
        mEditText.setText(title);
        builder.setView(view)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String title = mEditText.getText().toString();
                        listener.onUpdateCollagePositiveClick(title, collageId);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    public static Dialog DeleteAccountDialogFragment(Context context, final DialogBuilder.AccountDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.alert_dialog_delete_account)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    public interface DialogClickListener {
        void onDeleteCollagePositiveClick(int collageId);
        void onNewCollagePositiveClick(String title);
        void onUpdateCollagePositiveClick(String title, int collageId);
        void onDialogNegativeClick();
    }

    public interface AccountDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

}
