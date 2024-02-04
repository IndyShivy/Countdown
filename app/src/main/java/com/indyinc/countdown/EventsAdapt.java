package com.indyinc.countdown;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapt extends RecyclerView.Adapter<EventsAdapt.ViewHolder> {
    private final ArrayList<DateItem> dateItems;
    private DateDatabase db;
    private final Context context;
    private OnItemClickListener listener;





    public EventsAdapt(ArrayList<DateItem> dateItems, Context context) {
        this.dateItems = dateItems;
        this.context = context;
        this.db = new DateDatabase(context);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_events_adapter_item, parent, false);
        db = new DateDatabase(view.getContext());
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        // Assuming DateItem has a unique id field
        return dateItems.get(position).getId();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateItem dateItem = dateItems.get(position);
        holder.bind(dateItem, listener);
        holder.eventTitleTextView.setText(dateItem.getTitle());
        String date = dateItem.getDate();
        //date will be : 24/12/2024, remove the 20 from 2024
        date = date.substring(0, 6) + date.substring(8);
        holder.dateTextView.setText(date);

        // Set the image based on the format
        String format = dateItem.getFormat();
        switch (format) {
            case "Day":
                holder.formatImage.setImageResource(R.drawable.act_events_icon_day);
                break;
            case "Week":
                holder.formatImage.setImageResource(R.drawable.act_events_icon_week);
                break;
            case "Fortnight":
                holder.formatImage.setImageResource(R.drawable.act_events_icon_fortnight);
                break;
            case "Month":
                holder.formatImage.setImageResource(R.drawable.act_events_icon_month);
                break;
        }
        holder.removeButton.setOnClickListener(v -> setupRemoveDialog(dateItem, position));
    }

    private void setupRemoveDialog(DateItem dateItem, int position) {
        String eventTitle = dateItem.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        LayoutInflater backupInflate = LayoutInflater.from(context);
        View dialogView = backupInflate.inflate(R.layout.act_events_alert_dialog, null);
        builder.setView(dialogView);
        TextView title = dialogView.findViewById(R.id.dialog_title);
        String titleSetter = "Remove Event:\n";
        title.setText(titleSetter);
        title.setTextColor((ContextCompat.getColor(context, R.color.black)));
        TextView message = dialogView.findViewById(R.id.dialog_message);
        message.setTextColor((ContextCompat.getColor(context, R.color.black)));
        String textSetter = "\"" + eventTitle + "\"";
        message.setText(textSetter);
        builder.setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteEvent(dateItem);
                    dateItems.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return dateItems.size();
    }

    // Create the view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitleTextView;
        TextView dateTextView;
        ImageButton removeButton;
        ImageView formatImage;

        // Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.eventTitle);
            dateTextView = itemView.findViewById(R.id.eventDate);
            removeButton = itemView.findViewById(R.id.removeButton);
            formatImage = itemView.findViewById(R.id.formatImage); //

        }
        public void bind(DateItem dateItem, OnItemClickListener listener){
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(dateItem);
                }
            });
        }





    }


}
