package xyz.lonelyzerg.chatroom;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import data.Message;
import adapters.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView ChatRecyclerView;
    private RecyclerView.Adapter ChatAdapter;
    private RecyclerView.LayoutManager ChatLayoutManager;
    private ArrayList<Message> message_list;
    private ChatHandler chat_handler;
    private EditText message_input;
    private String host;
    private String username;
    private Connection conn;
    private ChatApp chatApp;
    private int back_pressed = 0;
    private static final int RCV_MESSAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back_pressed = 0;
        setContentView(R.layout.activity_chat);
        username = getIntent().getStringExtra("username");
        message_input = findViewById(R.id.message_input);

        message_list = new ArrayList<>();

        ChatRecyclerView = findViewById(R.id.rv);
        ChatRecyclerView.setHasFixedSize(true);
        ChatLayoutManager = new LinearLayoutManager(this);
        ChatRecyclerView.setLayoutManager(ChatLayoutManager);
        ChatAdapter = new ChatAdapter(message_list);
        ChatRecyclerView.setAdapter(ChatAdapter);

        chat_handler = new ChatHandler();
        chatApp = (ChatApp)getApplication();
        conn = new Connection(chatApp.getSocket(), chatApp.getInputStream(), chatApp.getOutputStream(), chat_handler);
        conn.start();

    }
    public void sendMessage(View view) {
        String message_text = message_input.getText().toString();
        if(message_text.length() == 0){
            message_input.setHint(R.string.message_prompt);
            return;
        }
        Message m = new Message(username, message_text, Message.SEND);
        addMessage(m);
        message_input.setHint("");
        message_input.setText("");
        conn.send(message_text);
    }

    public void addMessage(Message message){
        message_list.add(message);
        ChatAdapter.notifyItemInserted(message_list.size()-1);
        ChatRecyclerView.scrollToPosition(message_list.size() - 1);
    }


    public class ChatHandler extends Handler{

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 0:
                   addMessage((Message) message.obj);

            }
        }
    }

    @Override
    public void onBackPressed() {
        if(back_pressed == 0){
            Toast.makeText(getApplicationContext(), R.string.press_back_again, Toast.LENGTH_SHORT).show();
            back_pressed++;
            return;
        }
        conn.onClose();
        chatApp.setSocket(null);
        chatApp.setInputStream(null);
        chatApp.setOutputStream(null);

        Toast.makeText(getApplicationContext(), R.string.disconnect_from_server, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
