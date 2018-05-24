package at.aau.pokerfox.partypoker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;


import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.R;

public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_PLAYER_NAME = "BUNDLE_PLAYER_NAME";

    private String playerName = "";
    private Salut network;

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
                PartyPokerApplication.setIsHost(true);

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
                PartyPokerApplication.setIsHost(false);

//                Intent intent = new Intent("TablechoiceActivity");
//                Bundle bundle = new Bundle();
//                bundle.putString(BUNDLE_PLAYER_NAME, playerName);
//                intent.putExtras(bundle);
//                startActivity(intent);

//                MediaPlayer click1 = MediaPlayer.create(MainActivity.this,R.raw.click);
//                click1.start();
                MediaPlayer click = MediaPlayer.create(MainActivity.this,R.raw.click);
                click.start();
                discoverAndJoinService();
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

    private void setupNetwork() {
        SalutDataReceiver dataReceiver = new SalutDataReceiver(MainActivity.this, PartyPokerApplication
                .getMessageHandler());
        SalutServiceData serviceData = new SalutServiceData(PartyPokerApplication.SALUT_SERVICE_NAME,
                PartyPokerApplication.SALUT_PORT, playerName);
        final Salut network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Toast.makeText(MainActivity.this, "Sorry, this device is not supported", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        PartyPokerApplication.setNetwork(network);
        PartyPokerApplication.setSalutDataReceiver(dataReceiver);
        PartyPokerApplication.setSalutServiceData(serviceData);

        this.network = network;
    }

    private void discoverAndJoinService() {
        PartyPokerApplication.setIsHost(false);

        if (this.network == null)
            setupNetwork();

        try {
            this.network.stopServiceDiscovery(true);
        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }

        if (!this.network.isRunningAsHost && !this.network.isDiscovering) {

            network.discoverNetworkServices(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice device) {
                    Toast.makeText(MainActivity.this, "A device has been found with the name " + device.deviceName,
                            Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = buildDialog(network.foundDevices);
                    builder.show();
                }
            }, false);
        } else {
            this.network.stopServiceDiscovery(true);
        }
    }

    private AlertDialog.Builder buildDialog(ArrayList<SalutDevice> devices) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select table to join");

        final ArrayAdapter<SalutDevice> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout
                .select_dialog_singlechoice);
        arrayAdapter.addAll(devices);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final SalutDevice device = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                builderInner.setMessage(device.instanceName);
                builderInner.setTitle("You will join");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                        connectToHost(device);
                    }
                });
                builderInner.show();
            }
        });

        return builderSingle;
    }

    private void connectToHost(SalutDevice host) {
        try {
            network.registerWithHost(host, new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(MainActivity.this, "You registered successfully!",
                            Toast.LENGTH_LONG).show();
                Intent intent = new Intent("GameActivity");
                startActivity(intent);
                }
            }, new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(MainActivity.this, "You couldn't be registered...",
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(network != null) {
            if(network.isRunningAsHost) {
                try {
                    network.stopNetworkService(true);

                } catch (Exception e) {

                }
            } else {
                try {
                    network.unregisterClient(true);
                } catch (Exception e) {

                }
            }
        }
    }
}
