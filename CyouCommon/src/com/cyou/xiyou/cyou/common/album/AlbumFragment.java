package com.cyou.xiyou.cyou.common.album;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.cyou.xiyou.cyou.common.R;
import com.cyou.xiyou.cyou.common.album.AlbumAdapter.OnPhotoClickListener;
import com.cyou.xiyou.cyou.common.album.AlbumDirAdapter.OnDirClickListener;
import com.cyou.xiyou.cyou.common.ui.CommonFragment;
import com.cyou.xiyou.cyou.common.util.CommonUtil;
import com.cyou.xiyou.cyou.common.util.ResourceUtil;
import com.cyou.xiyou.cyou.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

/**
 * Created by 003 on 2017-05-31.
 */
public class AlbumFragment extends CommonFragment implements OnPhotoClickListener, OnDirClickListener, OnClickListener
{
    private static final String KEY_PATH = "Path";

    private RecyclerView dirView;

    private View overlayView;

    private AlbumAdapter albumAdapter;

    private AlbumDirAdapter dirAdapter;

    private List<AlbumItem> allPictures, pictures;

    private List<AlbumDir> dirs;

    private TreeMap<String, List<AlbumItem>> dirMap;

    @Override
    protected int getViewResId()
    {
        return R.layout.fragment_album;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState, @NonNull View view)
    {
        Context context = view.getContext();
        this.pictures = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dirMap = new TreeMap<>();
        RecyclerView photoView = view.findViewById(R.id.photoView);
        this.dirView = view.findViewById(R.id.dirView);
        this.albumAdapter = new AlbumAdapter(photoView, pictures, this);
        this.dirAdapter = new AlbumDirAdapter(dirs, this);
        this.overlayView = view.findViewById(R.id.overlayView);
        View btnSwitchDir = view.findViewById(R.id.btnSwitchDir);
        overlayView.setOnClickListener(this);
        btnSwitchDir.setOnClickListener(this);
        photoView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        View footerView = new View(context);
        int footerHeight = CommonUtil.dp2px(context, 45);
        footerView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, footerHeight));
        albumAdapter.addFooterView(footerView);
        photoView.setAdapter(albumAdapter);
        dirView.setLayoutManager(new LinearLayoutManager(context));
        dirView.setAdapter(dirAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ResourceUtil.getDrawable(context, R.drawable.album_dir_divider));
        dirView.addItemDecoration(decoration);
        AlbumPermissionsDispatcher.loadWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    void load()
    {
        this.allPictures = ResourceUtil.getAlbumPictures(getContext());
        String bucketDisplayName;
        List<AlbumItem> list;
        AlbumDir dir;

        for(AlbumItem item: allPictures)
        {
            bucketDisplayName = item.getBucketDisplayName();
            list = dirMap.get(bucketDisplayName);

            if(list == null)
            {
                list = new ArrayList<>();
                dirMap.put(bucketDisplayName, list);
            }

            list.add(item);
        }

        allPictures.clear();

        //这里再次遍历是为了排序，dirMap本身是按Key的自然顺序来存放数据的
        for(Entry<String, List<AlbumItem>> entry: dirMap.entrySet())
        {
            dir = new AlbumDir();
            list = entry.getValue();
            dir.setName(entry.getKey());
            dir.setPhotoCount(list.size());
            dir.setFirstPath(list.get(0).getPath());
            dirs.add(dir);
            allPictures.addAll(list);
        }

        if(!allPictures.isEmpty())
        {
            dir = new AlbumDir();
            dir.setPhotoCount(allPictures.size());
            dir.setFirstPath(allPictures.get(0).getPath());
            dir.setName(ResourceUtil.getString(getContext(), R.string.all_pictures));
            dir.setAll(true);
            dirs.add(0, dir);
            pictures.addAll(allPictures);
            albumAdapter.notifyDataSetChanged();
            dirAdapter.notifyDataSetChanged();
        }
    }

    private void showOrHideDirView(Boolean show)
    {
        Context context = this.getContext();
        int visibility = dirView.getVisibility();
        Animation dirAnimation = null;
        Animation overlayAnimation = null;
        boolean changed = false;

        if(show == null)
        {
            show = visibility != View.VISIBLE;
        }

        if(!show && visibility == View.VISIBLE)
        {
            overlayView.setVisibility(View.INVISIBLE);
            dirView.setVisibility(View.INVISIBLE);
            dirAnimation = AnimationUtils.loadAnimation(context, R.anim.out_topbottom);
            overlayAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            changed = true;
        }
        else if(show && visibility != View.VISIBLE)
        {
            overlayView.setVisibility(View.VISIBLE);
            dirView.setVisibility(View.VISIBLE);
            dirAnimation = AnimationUtils.loadAnimation(context, R.anim.in_bottomtop);
            overlayAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            changed = true;
        }

        if(changed)
        {
            dirAnimation.setDuration(200);
            overlayAnimation.setDuration(200);
            dirView.startAnimation(dirAnimation);
            overlayView.startAnimation(overlayAnimation);
        }
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForAlbum(PermissionRequest request)
    {
        ToastUtil.showCustomToast(getContext(), R.string.no_permissions_for_album, Toast.LENGTH_SHORT);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE})
    void onAlbumNeverAskAgain()
    {
        ToastUtil.showCustomToast(getContext(), R.string.album_permissions_denied_hint, Toast.LENGTH_SHORT);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE})
    void onAlbumDenied()
    {
        ToastUtil.showCustomToast(getContext(), R.string.album_permissions_denied_hint, Toast.LENGTH_SHORT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AlbumPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onPhotoClick(AlbumItem albumItem)
    {
        Activity activity = this.getActivity();

        if(activity != null)
        {
            Intent data = new Intent();
            data.putExtra(KEY_PATH, albumItem.getPath());
            activity.setResult(Activity.RESULT_OK, data);
            activity.finish();
        }
    }

    @Override
    public void onDirClick(AlbumDir dir)
    {
        if(allPictures != null)
        {
            showOrHideDirView(false);
            pictures.clear();
            pictures.addAll(dir.isAll()? allPictures: dirMap.get(dir.getName()));
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if(id == R.id.btnSwitchDir)
        {
            showOrHideDirView(null);
        }
        else if(id == R.id.overlayView)
        {
            showOrHideDirView(false);
        }
    }

    public static String getPicutrePath(Intent data)
    {
        return data.getStringExtra(KEY_PATH);
    }
}