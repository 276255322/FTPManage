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

import com.bumptech.glide.Glide;
import com.example.ftpmanage.entity.FileExt;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;

import java.util.List;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {

    public Context mContext;

    public int averageLength = 0;

    //数据源
    public List<FileExt> mList;

    public GridLayoutManager glm;

    public String localDir = "";

    public LocalAdapter(Context context, GridLayoutManager gm, String localdir, List<FileExt> list) {
        mContext = context;
        mList = list;
        glm = gm;
        localDir = localdir;
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
    public void onBindViewHolder(@NonNull final LocalAdapter.ViewHolder holder, final int position) {
        final FileExt ffile = mList.get(position);
        String fname = ffile.getName();
        holder.mView.setText(fname);
        if (!ffile.isDirectory()) {
            if (AppUtil.isExt(fname, ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
                ViewGroup.LayoutParams imgParm = holder.mImage.getLayoutParams();
                imgParm.width = averageLength;
                imgParm.height = averageLength;
                Glide.with(mContext).load(ffile).into(holder.mImage);
            } else {
                Glide.with(mContext).load(FtpUtils.getFileIcon(true, true, 0, fname)).into(holder.mImage);
            }
        } else {
            if (ffile.getChildCount() < 0) {
                ffile.setChildCount(ffile.getFileExtList().size());
            }
            Glide.with(mContext).load(FtpUtils.getFileIcon(false, true, ffile.getChildCount(), fname)).into(holder.mImage);
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mView;
        public ImageView mImage;
        public FrameLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.text_view);
            mImage = itemView.findViewById(R.id.img_list);
            linear = itemView.findViewById(R.id.img_linear);
            averageLength = glm.getWidth() / glm.getSpanCount();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, FileExt file);

        void onItemLongClick(View v, int position, FileExt file);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }
}
