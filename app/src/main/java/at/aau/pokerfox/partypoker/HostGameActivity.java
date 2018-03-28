package at.aau.pokerfox.partypoker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HostGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle bundle = getIntent().getExtras();
        String playername = bundle.getString(MainActivity.BUNDLE_PLAYER_NAME);
    }
}
