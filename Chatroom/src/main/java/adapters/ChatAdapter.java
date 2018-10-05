package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import data.Message;
import xyz.lonelyzerg.chatroom.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private ArrayList<Message>  message_list;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LinearLayout left_layout;
        LinearLayout right_layout;
        TextView left_username;
        TextView left_message;
        TextView right_username;
        TextView right_message;

        public ChatViewHolder(View view) {
            super(view);
            this.left_username = view.findViewById(R.id.left_username);
            this.left_message = view.findViewById(R.id.left_message);
            this.right_username = view.findViewById(R.id.right_username);
            this.right_message = view.findViewById(R.id.right_message);
            this.left_layout = view.findViewById(R.id.left_layout);
            this.right_layout = view.findViewById(R.id.right_layout);
        }
    }

    public ChatAdapter(ArrayList<Message> message_list) {
        this.message_list = message_list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Message message = message_list.get(position);
        if(message.getType() == Message.RECEIVE){
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.right_layout.setVisibility(View.GONE);
            holder.left_username.setText(message.getUsername() + ":");
            holder.left_message.setText(message.getMessage());
        }
        else{
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.left_layout.setVisibility(View.GONE);
            holder.right_username.setText(message.getUsername() + ":");
            holder.right_message.setText(message.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return message_list.size();
    }
}