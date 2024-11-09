package com.example.githubbrowser.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubbrowser.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder>{
    private List<Repo> repoList;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public RepoAdapter(List<Repo> repoList) {
        this.repoList = repoList;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_repo_item, parent, false);
        return new RepoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = repoList.get(position);

        holder.repoName.setText(repo.getRepoName());
        holder.usernameRepo.setText(repo.getUsername());

        // load the image
        Glide.with(holder.itemView.getContext())
                .load(repo.getOwnImgUrl())
                .into(holder.repoImg);
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }


    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView usernameRepo, repoName;
        ShapeableImageView repoImg;
        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameRepo = itemView.findViewById(R.id.usernameRepo);
            repoName = itemView.findViewById(R.id.repo_name);
            repoImg = itemView.findViewById(R.id.repo_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null)
                itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}