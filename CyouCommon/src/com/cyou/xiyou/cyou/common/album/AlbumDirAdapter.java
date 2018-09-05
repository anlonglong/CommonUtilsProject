package com.cyou.xiyou.cyou.common.album;

import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyou.xiyou.cyou.common.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by 003 on 2016-12-13.
 */
public class AlbumDirAdapter extends BaseQuickAdapter<AlbumDir, BaseViewHolder>
{
    private OnDirClickListener dirClickListener;

    private int selectedIndex;

    public AlbumDirAdapter(List<AlbumDir> data, OnDirClickListener dirClickListener)
    {
        super(R.layout.album_dir_item, data);
        this.dirClickListener = dirClickListener;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AlbumDir dir)
    {
        final int position = holder.getAdapterPosition();
        SimpleDraweeView imageView = holder.getView(R.id.imageView);
        Uri uri = dir.getFirstUri();
        imageView.setImageURI(uri);
        holder.setText(R.id.txtName, dir.getName());
        holder.setText(R.id.txtCount, String.valueOf(dir.getPhotoCount()));
        holder.setVisible(R.id.checkMark, selectedIndex == position);
        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectedIndex = position;
                notifyDataSetChanged();

                if(dirClickListener != null)
                {
                    dirClickListener.onDirClick(dir);
                }
            }
        });
    }

    public interface OnDirClickListener
    {
        void onDirClick(AlbumDir dir);
    }
}