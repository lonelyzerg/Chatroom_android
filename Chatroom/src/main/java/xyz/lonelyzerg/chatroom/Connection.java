package xyz.lonelyzerg.chatroom;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;

import exceptions.NameException;

public class Connection extends Thread{
    public ArrayList<data.Message> message_list;

    private Handler chat_handler;
    private Socket connection;
    private DataOutputStream out;
    private BufferedReader in;
    private boolean stop = false;

    private static final String exit_code = "[EXIT]";
    private static final String chat_prefix = "[CHAT]";
    private static final String suffix = "\n";
    private static final String charset_utf_8 = "UTF-8";
    public static final String TAG = "ChatService";
    private static final int port = 999;



    public Connection(Socket connection, BufferedReader in, DataOutputStream out, ChatActivity.ChatHandler chat_handler){
        this.in = in;
        this.out = out;
        this.chat_handler = chat_handler;
        this.connection = connection;
    }

    @Override
    public void run(){
        message_list = new ArrayList<data.Message>();
        ReceivingThread r = new ReceivingThread();
        r.start();

    }


    public void onClose() {

        try {
            stop = true;
            send(exit_code + suffix);
            out.flush();
            TimeUnit.MILLISECONDS.sleep(500);
            in.close();
            out.close();
            connection.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void send(String message){
        new Send(message).start();
    }

    public class Send extends Thread{
        String message;
        Send(String message){
            this.message = message;
        }
        public void run(){
            try{
                Log.d(TAG,message);
                out.write((chat_prefix + message + suffix).getBytes(charset_utf_8));
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public class ReceivingThread extends Thread {
        public void run() {
            try {
                this.getId();
                String[] raw_message;
                while (!stop) {
                    Log.i(TAG," Reading...");
                    String msg = in.readLine();
                    if (msg!=null) {
                        if(msg.equals(exit_code)){
                            break;
                        }
                        raw_message = parse(msg);
                        data.Message message = new data.Message(raw_message[1], raw_message[2]);
                        Log.i(TAG,"received a message from server");
                        Message m = Message.obtain();
                        m.obj = message;
                        chat_handler.sendMessage(m);
                    }
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    public String[] parse(String message) {
        String[] result = new String[3];
        int i = 5;
        int j = message.indexOf(']', i + 1);
        result[0] = message.substring(1, i);
        result[1] = message.substring(i + 2, j);
        result[2] = message.substring(j + 1);
        return result;
    }

}
