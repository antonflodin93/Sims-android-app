package se.miun.android_app.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.miun.android_app.R;
import se.miun.android_app.Model.Message;

public class WarningMessageAdapter extends RecyclerView.Adapter<WarningMessageAdapter.MyViewHolder> {

    // Stores the messages
    private List<Message> messages;

    public WarningMessageAdapter(List<Message> messages) {

        this.messages = messages;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warning_message_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.messageLabelTextView.setText(messages.get(position).getMessageLabel());
        holder.messageTextTextView.setText(messages.get(position).getMessageText());
        holder.dateWarningTextView.setText(messages.get(position).getDate());
        holder.timeWarningTextView.setText(messages.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageLabelTextView, messageTextTextView, dateWarningTextView, timeWarningTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            // Init the fields for each row
            messageLabelTextView = (TextView) itemView.findViewById(R.id.messageLabelTextView);
            messageTextTextView = (TextView) itemView.findViewById(R.id.messageTextTextView);
            dateWarningTextView = (TextView) itemView.findViewById(R.id.dateWarningTextView);
            timeWarningTextView = (TextView) itemView.findViewById(R.id.timeWarningTextView);



        }
    }

}
