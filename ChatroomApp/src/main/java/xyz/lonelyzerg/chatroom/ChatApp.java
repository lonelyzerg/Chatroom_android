package xyz.lonelyzerg.chatroom;

import android.app.Application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ChatApp extends Application {
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataOutputStream getOutputStream() {
        return out;
    }

    public void setOutputStream(DataOutputStream out) {
        this.out = out;
    }

    public BufferedReader getInputStream() {
        return in;
    }

    public void setInputStream(BufferedReader in) {
        this.in = in;
    }
}
