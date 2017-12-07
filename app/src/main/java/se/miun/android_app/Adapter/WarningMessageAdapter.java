package se.miun.android_app.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.miun.android_app.R;
import se.miun.android_app.Model.Message;

public class WarningMessageAdapter extends RecyclerView.Adapter<WarningMessageAdapter.MyViewHolder> {

    // Stores the messages
    private List<Message> messages;
    private int employeeId;
    private Context context;

    public WarningMessageAdapter(List<Message> messages, int employeeId, Context context) {
        this.messages = messages;
        this.employeeId = employeeId;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warning_message_row_item, parent, false);
        return new MyViewHolder(view, messages);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageLabelTextView, messageTextTextView, dateWarningTextView, timeWarningTextView;
        List<Message> messages;

        public MyViewHolder(View itemView, List<Message> messages) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.messages = messages;
            // Init the fields for each row
            messageLabelTextView = (TextView) itemView.findViewById(R.id.messageLabelTextView);
            messageTextTextView = (TextView) itemView.findViewById(R.id.messageTextTextView);
            dateWarningTextView = (TextView) itemView.findViewById(R.id.dateWarningTextView);
            timeWarningTextView = (TextView) itemView.findViewById(R.id.timeWarningTextView);




        }

        @Override
        public void onClick(View v) {
            //int position = getAdapterPosition();
        }
    }

}
