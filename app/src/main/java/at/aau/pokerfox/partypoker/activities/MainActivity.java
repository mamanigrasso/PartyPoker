package at.aau.pokerfox.partypoker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.aau.pokerfox.partypoker.R;

public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_PLAYER_NAME = "BUNDLE_PLAYER_NAME";

    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnSettings = findViewById(R.id.btn_host);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostGameActivity.class.getSimpleName());
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PLAYER_NAME, playerName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        final Button btnJoin = findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    btnSettings.setEnabled(true);
                }
                else{
                    btnJoin.setEnabled(false);
                    btnSettings.setEnabled(false);
                }
                playerName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
