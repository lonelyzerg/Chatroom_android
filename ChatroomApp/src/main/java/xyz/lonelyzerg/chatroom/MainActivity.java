package xyz.lonelyzerg.chatroom;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    TextView err_msg;
    private static final String TAG = "Register";
    private Handler register_handler;
    private String host;
    private String username;
    private Registration r;
    private long back_pressed = 0;

    private int exit_interval = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        err_msg = findViewById(R.id.err_msg);
    }
    public void connect (View view) {
        Log.i(TAG, "Button pressed");
        host = "";
        username = "";
        register_handler  = new RegisterHandler();
        host = ((EditText)findViewById(R.id.host_input)).getText().toString();
        username = ((EditText)findViewById(R.id.username_input)).getText().toString();
        if(host.length() == 0 || username.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.invalid_host_username, Toast.LENGTH_SHORT).show();
        }
        else {
            err_msg.setText("");
            r = new Registration(host, username, register_handler);
            r.start();
        }
    }

    public class RegisterHandler extends Handler{

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    err_msg.setVisibility(View.INVISIBLE);
                    ChatApp chatApp = (ChatApp)getApplication();
                    chatApp.setSocket(r.getSocket());
                    chatApp.setInputStream(r.getInputStream());
                    chatApp.setOutputStream(r.getOutputStream());
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("host", host);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    break;
                case 1:
                    Log.d(TAG, "Register failed");
                    err_msg.setText(R.string.err_username);
                    err_msg.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Log.d(TAG, "Host not responding");
                    err_msg.setText(R.string.err_host);
                    err_msg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void onBackPressed() {
        if(back_pressed == 0 || back_pressed + exit_interval < System.currentTimeMillis()){
            back_pressed = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), R.string.press_back_again, Toast.LENGTH_SHORT).show();
            return;
        }
        this.finish();
    }


}
