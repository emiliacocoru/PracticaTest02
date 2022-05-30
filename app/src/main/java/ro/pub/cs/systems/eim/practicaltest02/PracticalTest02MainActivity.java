package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText portEditTextServer;
    private Button startServerButton;
    private ServerThread serverThread;
    private EditText addressEditText;
    private EditText portEditTextClient;
    private EditText moneyEditText;
    private Button getValue;
    private TextView responseTextView;
    private ClientThread clientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        portEditTextServer = findViewById(R.id.PortEditText);
        startServerButton = findViewById(R.id.ConnectButton);
        addressEditText = findViewById(R.id.address);
        portEditTextClient = findViewById(R.id.portClient);
        moneyEditText = findViewById(R.id.money_type);
        getValue = findViewById(R.id.get_value);
        responseTextView = findViewById(R.id.response);
        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int port = Integer.parseInt(portEditTextServer.getText().toString());
                serverThread = new ServerThread(port);
                serverThread.start();
            }
        });

        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Practic", "Sunt aici");
                String address = addressEditText.getText().toString();
                int port = Integer.parseInt(portEditTextClient.getText().toString());
                String moneyType = moneyEditText.getText().toString();
                clientThread = new ClientThread(port, address, moneyType, responseTextView);
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i("Destroy", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}