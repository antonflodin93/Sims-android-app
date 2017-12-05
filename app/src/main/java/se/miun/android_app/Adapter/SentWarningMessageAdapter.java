package se.miun.android_app.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.miun.android_app.Model.Message;
import se.miun.android_app.R;

public class SentWarningMessageAdapter extends RecyclerView.Adapter<SentWarningMessageAdapter.MyViewHolder>{

    // Stores the messages
    private ArrayList<Message> messages;

    private Context context;

    public SentWarningMessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }


    @Override
    public SentWarningMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_item, parent, false);
        return new SentWarningMessageAdapter.MyViewHolder(view, messages);
    }

    @Override
    public void onBindViewHolder(SentWarningMessageAdapter.MyViewHolder holder, int position) {

        holder.messageHeader.setText("SENT TO: " + messages.get(position).getBuildingName());
        holder.messageTextView.setText(messages.get(position).getMessageText());

    }



    @Override
    public int getItemCount() {
        return messages.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageHeader, messageTextView;
        ImageView messageSentIcon;
        List<Message> messages;

        public MyViewHolder(View itemView, List<Message> messages) {
            super(itemView);
            this.messages = messages;
            // Init the fields for each row
            messageHeader = (TextView) itemView.findViewById(R.id.messageHeader);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageSentIcon = (ImageView) itemView.findViewById(R.id.messageSentIcon);


        }
    }


}
