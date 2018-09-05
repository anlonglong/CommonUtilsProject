package com.cyou.xiyou.cyou.common.album;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyou.xiyou.cyou.common.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by 003 on 2016-12-13.
 */
public class AlbumAdapter extends BaseQuickAdapter<AlbumItem, BaseViewHolder>
{
    private RecyclerView recyclerView;

    private OnPhotoClickListener onPhotoClickListener;

    public AlbumAdapter(RecyclerView recyclerView, List<AlbumItem> data, OnPhotoClickListener onPhotoClickListener)
    {
        super(R.layout.album_item, data);
        this.recyclerView = recyclerView;
        this.onPhotoClickListener = onPhotoClickListener;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AlbumItem albumItem)
    {
        SimpleDraweeView draweeView = holder.getView(R.id.imageView);
        Uri uri = albumItem.getUri();
        int itemHeight = (recyclerView.getWidth() - recyclerView.getPaddingLeft() - recyclerView.getPaddingRight()) / 3;
        int imageSize = (int)(itemHeight * 0.75f);
        holder.itemView.getLayoutParams().height = itemHeight;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(imageSize, imageSize)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeView.getController()).setImageRequest(request).build();
        draweeView.setController(controller);

        if(onPhotoClickListener != null)
        {
            draweeView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    onPhotoClickListener.onPhotoClick(albumItem);
                }
            });
        }
    }

    public interface OnPhotoClickListener
    {
        void onPhotoClick(AlbumItem albumItem);
    }
}