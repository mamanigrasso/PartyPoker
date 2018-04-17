package at.aau.pokerfox.partypoker.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import at.aau.pokerfox.partypoker.R;

public class HostGameActivity extends AppCompatActivity {
    public static final String TAG = ".activities.HostGameActivity";

    private String tableName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostgame);

        final EditText txt_tablename = findViewById(R.id.txt_table);
        CheckBox cbx_cheaton = findViewById(R.id.box_cheatOn);
        EditText txt_bigblind = findViewById(R.id.txt_bigblind);
        EditText txt_playerpot= findViewById(R.id.txt_playerpot);
        Button btn_create = findViewById(R.id.btn_create);



        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer click = MediaPlayer.create(HostGameActivity.this,R.raw.click);
                click.start();

                if (isNullOrEmpty(tableName)) {
                   Toast.makeText(HostGameActivity.this, "Tablename cannot be empty", Toast.LENGTH_SHORT).show();
               }
                Intent intent = new Intent("GameActivity");
                startActivity(intent);
            }

        });


    }

    private static boolean isNullOrEmpty(String s) {
        boolean isNullOrEmpty = false;

        if (s == null || s.equals(""))
            isNullOrEmpty = true;

        return isNullOrEmpty;
    }
}
