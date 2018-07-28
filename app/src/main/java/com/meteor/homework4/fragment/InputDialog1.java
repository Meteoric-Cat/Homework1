package com.meteor.homework4.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.meteor.homework4.activity.R;

public class InputDialog1 extends DialogFragment {

    private ClickHandler clickHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickHandler = (ClickHandler) getActivity();
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dg_title)
                .setView(R.layout.dialog_content);
        builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (clickHandler != null) {
                    String name = ((EditText) getDialog().findViewById(R.id.et_name)).getText().toString().trim();
                    String contactInfo = ((EditText) getDialog().findViewById(R.id.et_contactInfo)).getText().toString().trim();

                    if (name.equals("") || contactInfo.equals("")) {
                        String error = "Invalid Input";
                        Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    } else {
                        clickHandler.handleSave1(name, contactInfo);
                    }
                }
            }
        })
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        return builder.create();
    }

    public interface ClickHandler {
        public void handleSave1(String name, String contactInfo);
    }

}
