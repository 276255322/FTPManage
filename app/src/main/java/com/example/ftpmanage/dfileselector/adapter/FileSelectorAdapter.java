package com.example.ftpmanage.dfileselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ftpmanage.FtpUtils;
import com.example.ftpmanage.R;
import com.example.ftpmanage.dfileselector.bean.FileItem;
import com.example.ftpmanage.dfileselector.helper.ItemParameter;
import com.example.ftpmanage.dfileselector.provide.dateFormat.FileDateProvide;
import com.example.ftpmanage.dfileselector.provide.dateFormat.FileDateProvideDefault;
import com.example.ftpmanage.dfileselector.provide.size.FileSizeProvide;
import com.example.ftpmanage.dfileselector.provide.size.FileSizeProvideDefault;
import com.example.ftpmanage.dfileselector.util.FileSelectorUtils;
import com.example.ftpmanage.entity.ImageLoader;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author duke
 * @dateTime 2018-09-08 10:23
 * @description 文件列表适配器
 */
public class FileSelectorAdapter extends RecyclerView.Adapter<FileSelectorAdapter.FileViewHolder> {
    public Context context;
    private List<FileItem> list = new ArrayList<>();

    private ItemParameter itemParameter;

    private OnFileItemClickListener onFileItemClickListener;

    private FileDateProvideDefault fileDateProvideDefault = new FileDateProvideDefault();
    private FileDateProvide fileDateProvide = fileDateProvideDefault;

    private FileSizeProvideDefault fileSizeProvideDefault = new FileSizeProvideDefault();
    private FileSizeProvide fileSizeProvide = fileSizeProvideDefault;
    private FileFilter fileFilter;

    private boolean isMultiSelectionModel;
    private int multiModelMaxSize;
    private int currentSize;

    private boolean isNeedToastIfOutOfMaxSize;
    private String toastStringIfOutOfMaxSize;

    public void setOnFileItemClickListener(OnFileItemClickListener l) {
        this.onFileItemClickListener = l;
    }

    public void setMultiModelToast(boolean isNeedToastIfOutOfMaxSize, @NonNull String toastStringIfOutOfMaxSize) {
        this.isNeedToastIfOutOfMaxSize = isNeedToastIfOutOfMaxSize;
        this.toastStringIfOutOfMaxSize = toastStringIfOutOfMaxSize;
    }

    public void setMultiModelMaxSize(int multiModelMaxSize) {
        this.multiModelMaxSize = multiModelMaxSize;
    }

    public void setMultiSelectionModel(boolean multiSelectionModel) {
        if (this.isMultiSelectionModel != multiSelectionModel) {
            this.isMultiSelectionModel = multiSelectionModel;
        }
    }

    public void setFileFilter(FileFilter fileFilter) {
        if (this.fileFilter != fileFilter) {
            this.fileFilter = fileFilter;
        }
    }

    public void setFileSizeProvide(FileSizeProvide fileSizeProvide) {
        if (fileSizeProvide == null) {
            //不能为null
            return;
        }
        this.fileSizeProvide = fileSizeProvide;
    }

    public void setFileDateProvide(FileDateProvide fileDateProvide) {
        if (fileDateProvide == null) {
            //不能为null
            return;
        }
        this.fileDateProvide = fileDateProvide;
    }

    public void setItemParameter(ItemParameter itemParameter) {
        this.itemParameter = itemParameter;
    }

    public FileSelectorAdapter(Context context) {
        this.context = context;
    }

    public void setRefreshData(List<FileItem> list) {
        setData(list);
        notifyDataSetChanged();
    }

    public void setData(List<FileItem> list) {
        this.list.clear();
        if (!FileSelectorUtils.isEmpty(list)) {
            this.list.addAll(list);
        }
    }

    public List<FileItem> getList() {
        return this.list;
    }

    @Override
    public int getItemCount() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dfileselector_list_item, viewGroup, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder viewHolder, int position) {
        if (FileSelectorUtils.isEmpty(list)) {
            return;
        }
        final FileItem fileItem = list.get(position);
        if (fileItem == null || fileItem.file == null) {
            return;
        }

        viewHolder.fileItemName.setText(fileItem.file.getName());
        viewHolder.fileItemModify.setText(fileDateProvide.formatDate(fileItem.file.lastModified()));
        viewHolder.fileItemSizeOrCount.setText(fileSizeProvide.getFileSize(this.context, fileItem.file, fileFilter));
        //设置文件类型图标
        if (fileItem.file.isFile() && AppUtil.isExt(fileItem.file.getName(), ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
            Glide.with(this.context).load(fileItem.file).into(viewHolder.fileItemImage);
        } else {
            Glide.with(this.context).load(FtpUtils.getFileIcon(fileItem.file.isFile(), true, fileItem.file.getChildCount(), fileItem.file.getPath())).into(viewHolder.fileItemImage);
        }

        setViewProperties(viewHolder);

        //防止数据错乱
        //1.根据数据集合item的状态，设置checkbox。注意是setChecked()方法，不是setSelected()方法
        viewHolder.fileItemCheckBox.setChecked(!fileItem.file.isDirectory() && fileItem.isChecked);
        viewHolder.fileItemCheckBox.setVisibility(fileItem.file.isFile() && isMultiSelectionModel ? View.VISIBLE : View.GONE);
        //2.监控checkbox点击事件。注意是setOnClickListener事件，setOnCheckedChangeListener事件不行的
        viewHolder.fileItemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof CheckBox)) {
                    return;
                }
                //3.及时获取checkbox状态，保存到数据集中。注意是方法isChecked()获取状态
                //注意此处checkbox的选中状态是checkbox自己触发的，故获取到的是改变后的状态
                setCheckStatusAndCount(fileItem, viewHolder.fileItemCheckBox, !viewHolder.fileItemCheckBox.isChecked());
            }
        });

        viewHolder.fileItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileItem.file.isFile() && AppUtil.isExt(fileItem.file.getName(), ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
                    new XPopup.Builder(context).asImageViewer(viewHolder.fileItemImage, fileItem.file.getPath(),
                            false, -1, -1, -1, false,
                            new ImageLoader()).show();
                }
            }
        });

        viewHolder.fileItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultiSelectionModel) {
                    if (fileItem.file.isFile()) {
                        //注意此处checkbox的选中状态是认为触发的，故获取到的是以前的状态
                        setCheckStatusAndCount(fileItem, viewHolder.fileItemCheckBox, viewHolder.fileItemCheckBox.isChecked());
                    } else {
                        if (onFileItemClickListener != null && fileItem.file.isDirectory()) {
                            onFileItemClickListener.onFolderClick(fileItem.file);
                        }
                    }
                } else {
                    if (onFileItemClickListener != null) {
                        if (fileItem.file.isDirectory()) {
                            onFileItemClickListener.onFolderClick(fileItem.file);
                        } else {
                            onFileItemClickListener.onFileSelected(fileItem.file);
                        }
                    }
                }
            }
        });
    }

    private void setCheckStatusAndCount(@NonNull FileItem fileItem, @NonNull CheckBox checkBox, boolean oldCheckedStatus) {
        if (!oldCheckedStatus) {
            //选中操作
            if (multiModelMaxSize != 0 && currentSize >= multiModelMaxSize) {
                //禁止
                if (isNeedToastIfOutOfMaxSize) {
                    Toast.makeText(context, toastStringIfOutOfMaxSize, Toast.LENGTH_SHORT).show();
                }
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
                fileItem.isChecked = true;
                currentSize++;
            }
        } else {
            //取消操作
            checkBox.setChecked(false);
            fileItem.isChecked = false;
            currentSize--;
            if (currentSize < 0) {
                currentSize = 0;
            }
        }
    }

    private void setViewProperties(FileViewHolder viewHolder) {
        if (itemParameter == null) {
            return;
        }
        if (itemParameter.isSetLayoutPadding()) {
            viewHolder.fileItemRoot.setPadding((int) itemParameter.getLayoutPaddingLeft(),
                    (int) itemParameter.getLayoutPaddingTop(),
                    (int) itemParameter.getLayoutPaddingRight(),
                    (int) itemParameter.getLayoutPaddingBottom());
        }

        if (itemParameter.isSetLayoutBackgroundResourceId()) {
            viewHolder.fileItemRoot.setBackgroundResource(itemParameter.getLayoutBackgroundResourceId());
        }

        if (itemParameter.isSetLayoutBackgroundColor()) {
            viewHolder.fileItemRoot.setBackgroundColor(itemParameter.getLayoutBackgroundColor());
        }

        if (itemParameter.isSetImageWidth()
                || itemParameter.isSetImageHeight()
                || itemParameter.isSetImageMarginRight()) {
            ViewGroup.LayoutParams layoutParams = viewHolder.fileItemImage.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            if (itemParameter.isSetImageWidth()) {
                layoutParams.width = (int) itemParameter.getImageWidth();
            }
            if (itemParameter.isSetImageHeight()) {
                layoutParams.height = (int) itemParameter.getImageHeight();
            }
            if (itemParameter.isSetImageMarginRight() && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.rightMargin = (int) itemParameter.getImageMarginRight();
            }
            viewHolder.fileItemImage.setLayoutParams(layoutParams);
        }

        if (itemParameter.isSetTitleTextSize()) {
            viewHolder.fileItemName.getPaint().setTextSize(itemParameter.getTitleTextSize());
        }

        if (itemParameter.isSetTitleTextColor()) {
            viewHolder.fileItemName.setTextColor(itemParameter.getTitleTextColor());
        }

        viewHolder.fileItemName.setTypeface(FileSelectorUtils.getTypeface(itemParameter.isTitleTextBold()));

        if (itemParameter.isSetCountTextSize()) {
            viewHolder.fileItemSizeOrCount.getPaint().setTextSize(itemParameter.getCountTextSize());
        }

        if (itemParameter.isSetCountTextColor()) {
            viewHolder.fileItemSizeOrCount.setTextColor(itemParameter.getCountTextColor());
        }

        viewHolder.fileItemSizeOrCount.setTypeface(FileSelectorUtils.getTypeface(itemParameter.isCountTextBold()));

        if (itemParameter.isSetDateTextSize()) {
            viewHolder.fileItemModify.getPaint().setTextSize(itemParameter.getDateTextSize());
        }

        if (itemParameter.isSetDateTextColor()) {
            viewHolder.fileItemModify.setTextColor(itemParameter.getDateTextColor());
        }

        viewHolder.fileItemModify.setTypeface(FileSelectorUtils.getTypeface(itemParameter.isDateTextBold()));

        if (itemParameter.isSetSplitLineColor()) {
            viewHolder.splitView.setBackgroundColor(itemParameter.getSplitLineColor());
        }

        if (itemParameter.isSetSplitLineWidth()
                || itemParameter.isSetSplitLineHeight()
                || itemParameter.isSetSplitLineMarginLeft()
                || itemParameter.isSetSplitLineMarginRight()
        ) {
            ViewGroup.LayoutParams layoutParams = viewHolder.splitView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            if (itemParameter.isSetSplitLineWidth()) {
                layoutParams.width = (int) itemParameter.getSplitLineWidth();
            }
            if (itemParameter.isSetSplitLineHeight()) {
                layoutParams.height = (int) itemParameter.getSplitLineHeight();
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (itemParameter.isSetSplitLineMarginLeft()) {
                    marginLayoutParams.leftMargin = (int) itemParameter.getSplitLineMarginLeft();
                }
                if (itemParameter.isSetSplitLineMarginRight()) {
                    marginLayoutParams.rightMargin = (int) itemParameter.getSplitLineMarginRight();
                }
            }
            viewHolder.splitView.setLayoutParams(layoutParams);
        }
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout fileItemRoot;
        private ImageView fileItemImage;
        private RelativeLayout fileItemLayout;
        private TextView fileItemName;
        private TextView fileItemSizeOrCount;
        private TextView fileItemModify;
        private CheckBox fileItemCheckBox;
        private View splitView;

        FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileItemRoot = itemView.findViewById(R.id.file_item_root);
            fileItemImage = itemView.findViewById(R.id.file_item_image);
            fileItemLayout = itemView.findViewById(R.id.file_item_layout);
            fileItemName = itemView.findViewById(R.id.file_item_name);
            fileItemSizeOrCount = itemView.findViewById(R.id.file_item_size_or_count);
            fileItemModify = itemView.findViewById(R.id.file_item_modify);
            fileItemCheckBox = itemView.findViewById(R.id.file_item_checkbox);
            splitView = itemView.findViewById(R.id.file_item_split);
        }
    }

    public interface OnFileItemClickListener {
        void onFolderClick(File file);

        void onFileSelected(File file);
    }
}
