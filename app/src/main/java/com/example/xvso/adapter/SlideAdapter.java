package com.example.xvso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xvso.R;
import com.example.xvso.object.SliderItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

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

        SliderItem sliderItem1 = sliderItems.get(0);
        SliderItem sliderItem2 = sliderItems.get(1);
        SliderItem sliderItem3 = sliderItems.get(2);
        SliderItem sliderItem4 = sliderItems.get(3);
        SliderItem sliderItem5 = sliderItems.get(4);
        SliderItem sliderItem6 = sliderItems.get(5);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alienListener.onAlienClick(sliderItem);
            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String alien1Text = "Name: Tasp"
                        + System.getProperty("line.separator")
                        + System.getProperty("line.separator")
                        + "Characteristics:"
                        + System.getProperty("line.separator")
                        + System.getProperty("line.separator")
                        + "Name on earth: Humans call it Monoculus"
                        + System.getProperty("line.separator")
                        + "Race: Amphibio"
                        + System.getProperty("line.separator")
                        + "Height: 60 - 80cm (for adult of its species)"
                        + System.getProperty("line.separator")
                        + "Adulthood: under 14 years"
                        + System.getProperty("line.separator")
                        + "Planet: Pegasus 3"
                        + System.getProperty("line.separator")
                        + "Taps' short story:"
                        + System.getProperty("line.separator")
                        + "On their home planet, Pegasus 3, the climate is very stormy most of the part of the year, but their amphibian nature helped them to find abundant food during the whole year. Their planet is home to usually smaller life forms, Tasp being the largest."
                        + System.getProperty("line.separator");

                String alien2Text = "Name: Glu"
                        + System.getProperty("line.separator")
                        + System.getProperty("line.separator")
                        + "Characteristics:"
                        + System.getProperty("line.separator")
                        + System.getProperty("line.separator")
                        + "Name on earth: Humans call it Eyed-Jelly"
                        + System.getProperty("line.separator")
                        + "Race: Alborian"
                        + System.getProperty("line.separator")
                        + "Height: 135 - 150cm (for adult of its species)"
                        + System.getProperty("line.separator")
                        + "Adulthood: under 7 years"
                        + System.getProperty("line.separator")
                        + "Planet: Alborus"
                        + System.getProperty("line.separator")
                        + "Glu's short story:"
                        + System.getProperty("line.separator")
                        + "Alborus is a planet with moderate climate. Alborians are forming a society with common support to each others. Everything is well designed and they live under strict social rules. They are friendly beings, but humans have to be careful not to break their rules as punishment can even to not return to Earth ever again."
                        + System.getProperty("line.separator");

                String alien3Text = "3";
                String alien4Text = "4";
                String alien5Text = "5";
                String alien6Text = "6";

                if (sliderItem == sliderItem1) {
                    Toast.makeText(holder.imageView.getContext(), alien1Text, Toast.LENGTH_LONG).show();
                } else if (sliderItem == sliderItem2) {
                    Toast.makeText(holder.imageView.getContext(), alien2Text, Toast.LENGTH_LONG).show();
                } else if (sliderItem == sliderItem3) {
                    Toast.makeText(holder.imageView.getContext(), alien3Text, Toast.LENGTH_LONG).show();
                } else if (sliderItem == sliderItem4) {
                    Toast.makeText(holder.imageView.getContext(), alien4Text, Toast.LENGTH_LONG).show();
                } else if (sliderItem == sliderItem5) {
                    Toast.makeText(holder.imageView.getContext(), alien5Text, Toast.LENGTH_LONG).show();
                } else if (sliderItem == sliderItem6) {
                    Toast.makeText(holder.imageView.getContext(), alien6Text, Toast.LENGTH_LONG).show();
                }
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
