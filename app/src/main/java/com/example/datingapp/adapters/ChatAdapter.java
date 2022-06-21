package com.example.datingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingapp.R;
import com.example.datingapp.entity.Chat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<Chat> chatList;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(chatList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (chatList.get(position).getSender().equals(currentUser)) {
            return MSG_TYPE_RIGHT;
        }
        return MSG_TYPE_LEFT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessage, textClockTime;
        public ImageView messageImage;
        public LinearLayout textLayout, imageLayout;

        public ViewHolder(View view) {
            super(view);
            textMessage = itemView.findViewById(R.id.tv_message);
            textClockTime = itemView.findViewById(R.id.tc_time);
//            likeName = itemView.findViewById(R.id.text_like_name);
//            likeImage = itemView.findViewById(R.id.circle_image_like);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        public void bind(Chat chat) {
            textMessage.setText(chat.getMessage());
            Log.d("debug", chat.getCreateAt());
            textClockTime.setText(chat.getCreateAt());
        }
    }
}
