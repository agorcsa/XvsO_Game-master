package com.example.xvso.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xvso.R;
import com.example.xvso.object.SliderItem;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    private SliderViewHolder.AlienClick alienListener;

    public SlideAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2, SliderViewHolder.AlienClick alienListener) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.alienListener = alienListener;
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

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View view) {

                //Toast.makeText(view.getContext(), sliderItem.getDescription(), Toast.LENGTH_LONG).show();
                view.setTooltipText(sliderItem.getDescription());

                return true;
            }
        });

        holder.setImage(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

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
