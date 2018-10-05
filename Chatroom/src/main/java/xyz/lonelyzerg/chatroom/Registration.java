package xyz.lonelyzerg.chatroom;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import exceptions.NameException;

public class Registration extends Thread{

    private String host;
    private String username;
    private Handler register_handler;
    private DataOutputStream out;
    private BufferedReader in;
    private Socket connection;

    private static final String register_success = "[REGI]Success";
    private static final String register_failure = "[REGI]Failure";
    private static final String register_prefix = "[REGI]";
    public static final String TAG = "RegisterService";
    private static final String charset_utf_8 = "UTF-8";
    private static final String suffix = "\n";
    private static final int port = 999;
    private static final int REGISTER_SUCCESS = 0;
    private static final int REGISTER_FAILED = 1;
    private static final int HOST_FAILED = 2;



    public Registration(String host, String username, Handler register_handler){
        this.host = host;
        this.username = username;
        this.register_handler = register_handler;
    }

    public BufferedReader getInputStream(){
        return in;
    }

    public DataOutputStream getOutputStream(){
        return out;
    }

    public Socket getSocket(){
        return connection;
    }

    @Override
    public void run(){
        try {
            Log.i(TAG, "Connecting to server " + host + ":" + port);
            connection = new Socket();
            connection.connect(new InetSocketAddress(host, port), 5000);

            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset_utf_8));
            Log.i(TAG, "Registering with username " + username);
            if (register(username) ==  -1){
                throw new IOException("failed to register.");
            }
            Message m = Message.obtain();
            m.what = REGISTER_SUCCESS;
            register_handler.sendMessage(m);
            sleep(5000);
        }catch (NameException e){
            Message m = Message.obtain();
            m.arg1 = 0;
            m.what = REGISTER_FAILED;
            register_handler.sendMessage(m);
        }catch (IOException e){
            e.printStackTrace();
            Message m = Message.obtain();
            m.arg1 = 0;
            m.what = HOST_FAILED;
            register_handler.sendMessage(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int register(String username) throws NameException{
        try {
            out.write((register_prefix + username + suffix).getBytes(charset_utf_8));
            out.flush();
            // TimeUnit.SECONDS.sleep(1);
            String response = in.readLine();

            if (response.equals(register_failure)) {
                throw new NameException("register fails");
            } else if (response.equals(register_success)) {
                Log.i(TAG,"Registered!");
                return 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
}
