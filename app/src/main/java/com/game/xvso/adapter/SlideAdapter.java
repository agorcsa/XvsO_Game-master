package com.game.xvso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.game.xvso.R;
import com.game.xvso.object.SliderItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    private SliderViewHolder.AlienClick alienListener;

    private ShowDescription showDescription;

    public SlideAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2, SliderViewHolder.AlienClick alienListener, ShowDescription descriptionListener) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.alienListener = alienListener;
        this.showDescription = descriptionListener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderItem sliderItem = sliderItems.get(position);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alienListener.onAlienClick(sliderItem);
            }
        });

        holder.setImage(sliderItems.get(position));


        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDescription.onImageHover(sliderItem.getDescription());
                return true;
            }
        });
    }

    public interface ShowDescription {
        void onImageHover(String description);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;
        private TextView description;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slide);
        }

        void setImage(SliderItem sliderItem) {
            imageView.setImageResource(sliderItem.getImage());
        }


        public interface AlienClick {
            void onAlienClick(SliderItem item);
        }
    }
}
