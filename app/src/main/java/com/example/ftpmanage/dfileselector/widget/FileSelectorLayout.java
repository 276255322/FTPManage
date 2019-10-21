package com.example.ftpmanage.dfileselector.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ftpmanage.CustomPopup;
import com.example.ftpmanage.R;
import com.example.ftpmanage.dfileselector.adapter.FileSelectorAdapter;
import com.example.ftpmanage.dfileselector.bean.FileItem;
import com.example.ftpmanage.dfileselector.helper.ClickHelper;
import com.example.ftpmanage.dfileselector.helper.FileSelector;
import com.example.ftpmanage.dfileselector.helper.ItemParameter;
import com.example.ftpmanage.dfileselector.provide.dateFormat.FileDateProvide;
import com.example.ftpmanage.dfileselector.provide.order.FileOrderProvide;
import com.example.ftpmanage.dfileselector.provide.size.FileSizeProvide;
import com.example.ftpmanage.dfileselector.util.FileSelectorUtils;
import com.example.ftpmanage.dfileselector.util.FileUtils;
import com.example.ftpmanage.dfileselector.util.SizeUtils;
import com.example.ftpmanage.entity.FileExt;
import com.example.ftpmanage.entity.FolderEntity;
import com.example.ftpmanage.utils.FileProvider;
import com.example.ftpmanage.utils.UiUtil;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author duke
 * @dateTime 2018-09-08 13:30
 * @description
 */
public class FileSelectorLayout extends RelativeLayout implements View.OnClickListener {

    private LinearLayout headRoot;
    private TextView headPathTextView;
    private TextView headBackTextView;

    private View headTopLine;
    private View headBottomLine;

    private FileRecyclerView recyclerView;
    private TextView submitTextView;
    private TextView emptyTextView;

    private FileSelector.OnFileSelectListener onFileSelectListener;

    //item布局控件属性
    private ItemParameter itemViewHelper;

    //是否是多选模式
    private boolean isMultiSelectionModel;
    private int multiModelMaxSize;
    private boolean isNeedToastIfOutOfMaxSize;
    private String toastStringIfOutOfMaxSize;

    private FileSizeProvide fileSizeProvide;
    private FileDateProvide fileDateProvide;
    private FileFilter fileFilter;
    private FileOrderProvide fileListOrder = new FileOrderProvide();

    private FileSelectorAdapter adapter;
    private ClickHelper clickHelper;

    public void setOnFileSelectListener(FileSelector.OnFileSelectListener l) {
        this.onFileSelectListener = l;
    }

    public void setItemViewHelper(ItemParameter itemViewHelper) {
        this.itemViewHelper = itemViewHelper;
    }

    public void setMultiSelectionModel(boolean isMultiSelectionModel) {
        this.isMultiSelectionModel = isMultiSelectionModel;
    }

    public void setMultiModelMaxSize(int multiModelMaxSize) {
        this.multiModelMaxSize = multiModelMaxSize;
    }

    public void setMultiModelToast(boolean isNeedToastIfOutOfMaxSize, @NonNull String toastStringIfOutOfMaxSize) {
        this.isNeedToastIfOutOfMaxSize = isNeedToastIfOutOfMaxSize;
        this.toastStringIfOutOfMaxSize = toastStringIfOutOfMaxSize;
    }

    public void setFileSizeProvide(FileSizeProvide fileSizeProvide) {
        this.fileSizeProvide = fileSizeProvide;
    }

    public void setFileDateProvide(FileDateProvide fileDateProvide) {
        this.fileDateProvide = fileDateProvide;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public void setFileOrderProvide(FileOrderProvide fileListOrder) {
        if (fileListOrder == null) {
            return;
        }
        this.fileListOrder = fileListOrder;
    }

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public View getHeadTopLine() {
        return headTopLine;
    }

    public View getHeadBottomLine() {
        return headBottomLine;
    }

    public LinearLayout getHeadRoot() {
        return headRoot;
    }

    public TextView getHeadPathTextView() {
        return headPathTextView;
    }

    public TextView getHeadBackTextView() {
        return headBackTextView;
    }

    public FileRecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TextView getSubmitTextView() {
        return submitTextView;
    }

    public TextView getEmptyTextView() {
        return emptyTextView;
    }

    public FileSelectorLayout(Context context) {
        this(context, null, 0);
    }

    public FileSelectorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileSelectorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dfileselector_layout, this, false);
        addView(view);
        headRoot = view.findViewById(R.id.head_root);
        headPathTextView = view.findViewById(R.id.head_path_text_view);
        headBackTextView = view.findViewById(R.id.head_back_text_view);
        recyclerView = view.findViewById(R.id.file_recycler_view);
        submitTextView = view.findViewById(R.id.file_submit_text_view);
        emptyTextView = view.findViewById(R.id.file_empty_text_view);
        headTopLine = view.findViewById(R.id.file_head_top_line);
        headBottomLine = view.findViewById(R.id.file_head_bottom_line);
        submitTextView.setOnClickListener(this);
        headBackTextView.setOnClickListener(this);
        //先设置默认的分割线
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(LinearLayoutManager.VERTICAL, SizeUtils.getDimenResToPx(getContext(), R.dimen.dp_0_1), getContext().getResources().getColor(R.color.color_CCCCCC));
        recycleViewDivider.setMargin(SizeUtils.getDimenResToPx(getContext(), R.dimen.dfileselector_common_margin));
        recyclerView.addItemDecoration(recycleViewDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(emptyTextView);
        adapter = new FileSelectorAdapter(context);
    }

    /**
     * 设置所有属性
     */
    public void setup() {
        //注意helper创建位置，否则onFileSelectListener = null
        clickHelper = new ClickHelper(this, onFileSelectListener);
        adapter.setFileDateProvide(fileDateProvide);
        adapter.setFileFilter(fileFilter);
        adapter.setFileSizeProvide(fileSizeProvide);
        adapter.setOnFileItemClickListener(clickHelper);
        adapter.setMultiSelectionModel(isMultiSelectionModel);
        adapter.setMultiModelMaxSize(multiModelMaxSize);
        adapter.setMultiModelToast(isNeedToastIfOutOfMaxSize, toastStringIfOutOfMaxSize);
        adapter.setItemParameter(itemViewHelper);
        recyclerView.setAdapter(adapter);
        submitTextView.setVisibility(isMultiSelectionModel ? View.VISIBLE : View.GONE);
        initData();
    }

    public void refreshUI(String parentPath, List<FileItem> list) {
        headPathTextView.setText(parentPath);
        if (fileListOrder != null) {
            fileListOrder.order(list);
        }
        adapter.setRefreshData(list);
    }

    private void initData() {
        File rootFile = FileUtils.getRootFile();
        if (rootFile == null) {
            refreshUI("unknown", null);
            return;
        }
        refreshUI(rootFile.getAbsolutePath(), FileSelectorUtils.parseToFileItemList(rootFile.listFiles(getFileFilter())));
    }

    @Override
    public void onClick(View v) {
        if (clickHelper == null) {
            return;
        }
        if (v.getId() == R.id.file_submit_text_view) {
            List<FileItem> list = adapter.getList();
            ArrayList<String> slist = FileSelectorUtils.getSelectedFileList(list);
            if (slist == null || slist.size() == 0) {
                UiUtil.showPosition(this.getContext(), "至少选择一个文件！");
                return;
            }
            clickHelper.onSubmitClick(adapter.getList());
        } else if (v.getId() == R.id.head_back_text_view) {
            clickHelper.onBackLevelClick(headPathTextView.getText().toString());
        }
    }

    public interface OnButtonClickListener {
        void onBackLevelClick(String currentFolder);

        void onSubmitClick(List<FileItem> list);
    }
}
