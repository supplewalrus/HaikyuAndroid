package processor.haikyuapp;

/**
 * Created by brand on 12/10/2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.List;

/**
 * <p>Image adapter.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;

    private List<AlbumFile> mAlbumFiles;

    public ImageAdapter(Context context, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<AlbumFile> imagePathList) {
        this.mAlbumFiles = imagePathList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        AlbumFile albumFile = mAlbumFiles.get(position);
        if (albumFile.getMediaType() == AlbumFile.TYPE_IMAGE) {
            return AlbumFile.TYPE_IMAGE;
        } else {
            return AlbumFile.TYPE_VIDEO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(mInflater.inflate(R.layout.item_content_image, parent, false), mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ImageViewHolder) holder).setData(mAlbumFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;
        private ImageView mIvImage;

        ImageViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_album_content_image);
            itemView.setOnClickListener(this);
        }

        public void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
