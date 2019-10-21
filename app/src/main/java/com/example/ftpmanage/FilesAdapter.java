package com.example.ftpmanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.ftpmanage.entity.FTPFileExt;
import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.example.ftpmanage.utils.FileProvider;
import com.example.ftpmanage.utils.ImgUtil;

import java.io.File;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    public Context mContext;

    public FtpConfig ftpConfig;

    public int averageLength = 0;

    //数据源
    public List<FTPFileExt> mList;

    public GridLayoutManager glm;

    public String ftpDir = "";

    public String localDir = "";

    public VectorDrawableCompat vectorDrawableCompat;

    public FilesAdapter(Context context, GridLayoutManager gm, VectorDrawableCompat vdc, FtpConfig fConfig, String ftpdir, String localDir, List<FTPFileExt> list) {
        mContext = context;
        mList = list;
        glm = gm;
        ftpConfig = fConfig;
        ftpDir = ftpdir;
        vectorDrawableCompat = vdc;
        this.localDir = localDir;
        FileProvider.createrDirectory(localDir);
    }

    //返回item个数
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //创建ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    //填充视图
    @Override
    public void onBindViewHolder(@NonNull final FilesAdapter.ViewHolder holder, final int position) {
        final FTPFileExt ffile = mList.get(position);
        String fname = ffile.getName();
        holder.mView.setText(fname);
        setImageView(holder, ffile);
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position, ffile);
                }
            }
        });
        holder.mImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, position, ffile);
                }
                return false;
            }
        });
    }

    //填充局部视图
    @Override
    public void onBindViewHolder(@NonNull final FilesAdapter.ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }
        final FTPFileExt ffile = mList.get(position);
        String fname = ffile.getName();
        holder.mView.setText(fname);
        setImageView(holder, ffile);
    }

    public void setImageView(@NonNull final FilesAdapter.ViewHolder holder, final FTPFileExt ffext) {
        String fname = ffext.getName();
        holder.img.setVisibility(View.INVISIBLE);
        boolean isDown = false;
        if (ffext.getType() == 0) {
            boolean isLoadImage = false;
            if (AppUtil.isExt(fname, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
                isDown = true;
                String savePath = ImgUtil.getLocalPath(localDir + fname);
                if (AppUtil.isNotEmpty(savePath)) {
                    ViewGroup.LayoutParams imgParm = holder.mImage.getLayoutParams();
                    imgParm.width = averageLength;
                    imgParm.height = averageLength;
                    holder.img.setImageDrawable(vectorDrawableCompat);
                    holder.img.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(savePath).into(holder.mImage);
                    isLoadImage = true;
                    ffext.setDown(true);
                    ffext.setLocalPath(savePath);
                }
            }
            if (!isDown) {
                String savePath = localDir + fname;
                File sfile = new File(savePath);
                if (sfile.exists()) {
                    holder.img.setImageDrawable(vectorDrawableCompat);
                    holder.img.setVisibility(View.VISIBLE);
                    ffext.setDown(true);
                    ffext.setLocalPath(savePath);
                }
            }
            if (!isLoadImage) {
                Glide.with(mContext).load(FtpUtils.getFileIcon(true, false, 0, fname)).into(holder.mImage);
            }
        } else {
            Glide.with(mContext).load(FtpUtils.getFileIcon(false, false, 0, fname)).into(holder.mImage);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mView;
        public ImageView mImage;
        public FrameLayout linear;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.text_view);
            mImage = itemView.findViewById(R.id.img_list);
            linear = itemView.findViewById(R.id.img_linear);
            img = itemView.findViewById(R.id.img_finished);
            averageLength = glm.getWidth() / glm.getSpanCount();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, FTPFileExt ffext);
        void onItemLongClick(View v, int position, FTPFileExt ffext);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }
}
