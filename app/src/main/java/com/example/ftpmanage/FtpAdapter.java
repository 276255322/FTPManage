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
import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.UiUtil;

import java.util.List;

public class FtpAdapter extends RecyclerView.Adapter<FtpAdapter.ViewHolder> {

    private int userIcon = 0;

    public Context mContext;

    public int averageLength = 0;

    //数据源
    public List<FtpConfig> mList;

    public GridLayoutManager glm;

    public FtpAdapter(Context context, GridLayoutManager gm, List<FtpConfig> list) {
        userIcon = AppUtil.getRandomInt(0, 5);
        mContext = context;
        mList = list;
        glm = gm;
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
    public void onBindViewHolder(@NonNull final FtpAdapter.ViewHolder holder, final int position) {
        final FtpConfig fe = mList.get(position);
        String fname = fe.getFtpName();
        holder.mView.setText(fname);
        ViewGroup.LayoutParams linearParm = holder.linear.getLayoutParams();
        linearParm.width = averageLength;
        linearParm.height = averageLength;
        if (userIcon == 0) {
            Glide.with(mContext).load(R.drawable.sys_ftp_server1).into(holder.mImage);
            userIcon++;
        } else if (userIcon == 1) {
            Glide.with(mContext).load(R.drawable.sys_ftp_server2).into(holder.mImage);
            userIcon++;
        } else if (userIcon == 2) {
            Glide.with(mContext).load(R.drawable.sys_ftp_server3).into(holder.mImage);
            userIcon++;
        } else if (userIcon == 3) {
            Glide.with(mContext).load(R.drawable.sys_ftp_server4).into(holder.mImage);
            userIcon++;
        } else if (userIcon > 3) {
            Glide.with(mContext).load(R.drawable.sys_ftp_server5).into(holder.mImage);
            userIcon = 0;
        }
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position, fe);
                }
            }
        });
        holder.mImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, position, fe);
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
            int jg = UiUtil.dp2px(mContext, 5) * 5 + UiUtil.dp2px(mContext, 20);
            averageLength = (glm.getWidth() - jg) / glm.getSpanCount();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, FtpConfig fe);

        void onItemLongClick(View v, int position, FtpConfig fe);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }
}
