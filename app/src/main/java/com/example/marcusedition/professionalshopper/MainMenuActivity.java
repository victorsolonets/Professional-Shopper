package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by victor on 01.10.15.
 */
public class MainMenuActivity extends Activity {

    private Dialog dialog;
    private Intent intent;
    private static int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
    }

    public void onClickButton(View view){
        if(view.getId() == R.id.button_view) {
            intent = new Intent(getApplicationContext(),ViewActivity.class);
        } else if (view.getId() == R.id.button_record) {
            intent = new Intent(getApplicationContext(), RecordActivity.class);
        } else if (view.getId() == R.id.button_delete) {
            showDialog(1);
            return;
        }
        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Видалити");
            adb.setMessage("Ви впевнені що хочете видалити всі дані?");
            adb.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), "Дані видалені", duration);
                    toast.show();
                }
            });
            adb.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog = adb.create();
            return dialog;
        }
        return super.onCreateDialog(id);
    }

}
