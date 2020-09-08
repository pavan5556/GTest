package com.pavan.gtest.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pavan.gtest.R;
import com.pavan.gtest.data.Constants;
import com.pavan.gtest.data.YTdataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private Context context;
    private List<YTdataModel> maindataList;
    private MainViewModel viewModel;
    List<YTdataModel> searchList;
    private List<YTdataModel> tempdatalist;

    public VideosAdapter(Context context, MainViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        maindataList = new ArrayList<>();
        tempdatalist = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.title.setText(tempdatalist.get(position).getTitle());
        holder.description.setText(tempdatalist.get(position).getDescription());

        String url = "http://img.youtube.com/vi/" + tempdatalist.get(position).getVideoId() + "/default.jpg";
        Picasso.get().load(url).into(holder.thumbnailView);
    }

    @Override
    public int getItemCount() {
        return tempdatalist.size();
    }

    public void updateFilter(List<YTdataModel> temp) {
        tempdatalist = temp;
        notifyDataSetChanged();
    }

    public void update(List<YTdataModel> list) {
        maindataList.clear();
        maindataList.addAll(list);
        tempdatalist = maindataList;
        notifyDataSetChanged();
    }

    public void search(String query) {
        searchList = new ArrayList<>();
        if (query != null) {
            for (YTdataModel model : maindataList) {
                if (model.getTitle().toLowerCase().contains(query.toLowerCase()) || model.getDescription().toLowerCase().contains(query.toLowerCase()))
                    searchList.add(model);
            }
            updateFilter(searchList);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailView;
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv_vh);
            description = itemView.findViewById(R.id.description_tv_vh);
            thumbnailView = itemView.findViewById(R.id.thumbnailview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setSelectedVideoLiveData(maindataList.get(getLayoutPosition()));
                }
            });
        }
    }
}
