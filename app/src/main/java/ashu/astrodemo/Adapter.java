package ashu.astrodemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import javax.sql.DataSource;

import ashu.astrodemo.model.ItemDTO;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by apple on 28/04/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Context context;
    private List<ItemDTO> itemDTOs;

    private ItemClickListener itemClickListener;
    private int lastPosition = -1;


    public Adapter(Context context, List<ItemDTO> itemDTOs, ItemClickListener itemClickListener){
        this.context = context;
        this.itemDTOs = itemDTOs;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtLoading.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(itemDTOs.get(position).getMedia().getM())
                .crossFade()
                .centerCrop()
                .override(300,300)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.txtLoading.setText("Some Error With Network");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.txtLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imgLoad);


        holder.txtTitle.setText(itemDTOs.get(position).getTitle());

        setAnimation(holder.itemView, position);
            holder.imgLoad.setOnClickListener(holder);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return itemDTOs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imgLoad)
        ImageView imgLoad;

        @BindView(R.id.txtLoading)
        TextView txtLoading;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            imgLoad.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int pos);
    }
}
