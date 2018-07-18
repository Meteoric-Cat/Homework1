package com.meteor.homework4.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.meteor.homework4.activity.R;
import com.meteor.homework4.other.DataHolder;

public class InputDialog2 extends DialogFragment {

    public static interface ClickHandler {
        public void handleSave2(String name, String contactInfo);
        public void handleDelete2();
}

    private ClickHandler clickHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            clickHandler=(ClickHandler) getActivity();
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.dg_title))
                .setView(R.layout.dialog_content2);
        builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (clickHandler != null) {
                            String name = ((EditText) getDialog().findViewById(R.id.et_otherName)).getText().toString().trim();
                            String contactInfo = ((EditText) getDialog().findViewById(R.id.et_otherContactInfo)).getText().toString().trim();

                            if (name.equals("") || contactInfo.equals("")) {
                                String error="Invalid Input";
                                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            } else {
                                clickHandler.handleSave2(name, contactInfo);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (clickHandler != null) {
                            clickHandler.handleDelete2();
                        }
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ((EditText) getDialog().findViewById(R.id.et_otherName)).setText(DataHolder.getInstance().getName());
                ((EditText) getDialog().findViewById(R.id.et_otherContactInfo)).setText(DataHolder.getInstance().getContactInfo());
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
