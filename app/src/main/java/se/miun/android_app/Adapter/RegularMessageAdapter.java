package se.miun.android_app.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.miun.android_app.R;
import se.miun.android_app.model.Message;

public class RegularMessageAdapter extends RecyclerView.Adapter<RegularMessageAdapter.MyViewHolder> {

    // Stores the messages
    private List<Message> messages;

    public RegularMessageAdapter(List<Message> messages) {

        this.messages = messages;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_message_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.messageLabelTextView.setText(messages.get(position).getMessageId());
        holder.messageTextTextView.setText(messages.get(position).getMessageText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageLabelTextView, messageTextTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            // Init the fields for each row
            messageLabelTextView = (TextView) itemView.findViewById(R.id.messageLabelTextView);
            messageTextTextView = (TextView) itemView.findViewById(R.id.messageTextTextView);


        }
    }

}
