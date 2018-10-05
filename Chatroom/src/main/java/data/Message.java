package data;

public class Message {
    public static final int SEND = 1;
    public static final int RECEIVE = 0;
    private int type;
    private String username;
    private String message;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
        type = RECEIVE;
    }

    public Message(String username, String message, int type) {
        this.username = username;
        this.message = message;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
}
