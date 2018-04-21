package at.aau.pokerfox.partypoker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.media.AudioManager;
import android.media.MediaPlayer;


import at.aau.pokerfox.partypoker.R;

public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_PLAYER_NAME = "BUNDLE_PLAYER_NAME";

    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        final Button btnHost = findViewById(R.id.btn_host);
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("HostGameActivity");
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PLAYER_NAME, playerName);
                intent.putExtras(bundle);
                startActivity(intent);

                MediaPlayer click = MediaPlayer.create(MainActivity.this,R.raw.click);
                click.start();
            }


        });
        final Button btnJoin = findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("TablechoiceActivity");
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PLAYER_NAME, playerName);
                intent.putExtras(bundle);
                startActivity(intent);

                MediaPlayer click1 = MediaPlayer.create(MainActivity.this,R.raw.click);
                click1.start();
            }
        });

        EditText txtName = findViewById(R.id.txt_name);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    btnJoin.setEnabled(true);
                    btnHost.setEnabled(true);
                }
                else{
                    btnJoin.setEnabled(false);
                    btnHost.setEnabled(false);
                }
                playerName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnJoin.setEnabled(false);
        btnHost.setEnabled(false);
    }


}
