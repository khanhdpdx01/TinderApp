package com.example.datingapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.example.datingapp.entity.Message;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context context;
    private List<Message> messageList;

    public MessagesAdapter() {
    }

    public MessagesAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message item = messageList.get(position);
        holder.name.setText(item.getName());
        holder.content.setText(item.getContent());

        if(item.getCount() <= 0){
            holder.viewIndicator.setVisibility(View.INVISIBLE);
        }

        StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                .child("images/" + item.getPicture() + ".jpg");

        Glide.with(context)
                .load(storageReference)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
//                    DogBreed dog = dogBreeds.get(getAdapterPosition());
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("dogBreed", dog);
//                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
                }
            });
        }
    }
}
