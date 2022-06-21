package com.example.datingapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.example.datingapp.entity.Like;
import com.example.datingapp.view.ChatPrivateFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ContactViewHolder> {
    private Context context;
    private List<Like> likeList;

    public LikesAdapter(Context context, List<Like> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_item, parent, false);
        return new LikesAdapter.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final Like item = likeList.get(position);
        holder.likeName.setText(item.getName());

        StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                .child("images/" + item.getPicture() + ".jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_error_24)
                        .into(holder.likeImage));
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView likeName;
        public ImageView likeImage;
        public LinearLayout likeLayout;

        public ContactViewHolder(View view) {
            super(view);
            likeLayout = itemView.findViewById(R.id.layout_like);
            likeName = itemView.findViewById(R.id.text_like_name);
            likeImage = itemView.findViewById(R.id.circle_image_like);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Like like = likeList.get(getAdapterPosition());

//                    Log.d("debug", "Id container " + ((ViewGroup)view.getParent()).getId());
//                    Log.d("debug", "Id container " + R.id.fragmentContainerView);

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("user", like);
//                    Navigation.findNavController(viewActivity)
//                            .navigate(R.id.action_activityFragment_to_chatPrivateFragment, bundle);

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_container_home, new ChatPrivateFragment(like))
                        .addToBackStack(null)
                        .commit();
//                    NavHostFragment.findNavController(new ActivityFragment)
                }
            });
        }
    }
}
