package com.example.datingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.example.datingapp.entity.Like;
import com.example.datingapp.entity.Message;
import com.example.datingapp.view.ChatPrivateFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context context;
    private List<Message> messageList;

    // Create a final private AdapterOnClickHandler called mClickHandler
    private final AdapterOnClickHandler mClickHandler;

    public MessagesAdapter(AdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public MessagesAdapter(Context context, List<Message> messageList, AdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.messageList = messageList;
        this.mClickHandler = mClickHandler;
    }

    // Add an interface called AdapterOnClickHandler
    // Within that interface, define a void method that handles the onClick event
    public interface AdapterOnClickHandler {
        void onClick(Message data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message item = messageList.get(position);
        holder.name.setText(item.getName());
        holder.content.setText(item.getContent());

        if(item.getCount() <= 0){
            holder.viewIndicator.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", "come in click in onBindViewHolder");
            }
        });

        StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                .child("images/" + item.getPicture() + ".jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_error_24)
                        .into(holder.thumbnail));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, content, count;
        public ImageView thumbnail;
        public RelativeLayout viewIndicator;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.text_name);
            content = view.findViewById(R.id.text_content);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewIndicator = view.findViewById(R.id.layout_dot_indicator);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("DEBUG", "come in to message onclick");
                }
            });

            // Call setOnClickListener on the view passed into the constructor
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("DEBUG", "click oke");
            mClickHandler.onClick(messageList.get(getAdapterPosition()));
        }
    }
}
