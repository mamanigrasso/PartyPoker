package at.aau.pokerfox.partypoker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import at.aau.pokerfox.partypoker.R;

public class HostGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostgame);

        EditText txt_tablename = findViewById(R.id.txt_table);
        CheckBox cbx_cheaton = findViewById(R.id.box_cheatOn);
        EditText txt_bigblind = findViewById(R.id.txt_bigblind);
        EditText txt_playerpot= findViewById(R.id.txt_playerpot);
        Button btn_create = findViewById(R.id.btn_create);






    }
}
