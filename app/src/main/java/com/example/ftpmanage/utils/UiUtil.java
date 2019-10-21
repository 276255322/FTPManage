package com.example.ftpmanage.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.ftpmanage.entity.MessagePopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;

public class UiUtil {

    public static BasePopupView positionView;

    public static void showToast(Context con, String mess) {
        if (con != null) {
            Toast toast = Toast.makeText(con, null, Toast.LENGTH_LONG);
            toast.setText(mess);
            toast.show();
        }
    }

    public static void showPosition(Context con, String mess) {
        showPosition(con, mess, 3000);
    }

    public static void showPosition(Context con, String mess, int delay) {
        if (positionView != null) {
            positionView.dismiss();
        }
        positionView = new XPopup.Builder(con)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .isCenterHorizontal(true)
                .asCustom(new MessagePopup(con, mess))
                .show();
        if (delay > -1) {
            positionView.delayDismiss(delay);
        }
    }

    public static ProgressDialog getSProgressDialog(Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public static ProgressDialog getHProgressDialog(Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public static void showProgressDialog(ProgressDialog pd, String title, String message) {
        pd.setTitle(title);
        pd.setMessage(message);
        pd.show();
    }

    public static void showFileProgressDialog(ProgressDialog pd, String title, int maxCount) {
        showSeedProgressDialog(pd, title, maxCount, "%1d Mb /%2d Mb");
    }

    public static void showFileProgressDialog(ProgressDialog pd, String title, long maxCount) {
        showSeedProgressDialog(pd, title, (int) maxCount / 1024 / 1024, "%1d Mb /%2d Mb");
    }

    public static void showSeedProgressDialog(ProgressDialog pd, String title, int maxCount, String numberFormat) {
        pd.setTitle(title);
        pd.setMax(maxCount);
        pd.setProgress(0);
        pd.setProgressNumberFormat(numberFormat);
        pd.show();
    }

    public static void setSeedProgressDialog(ProgressDialog pd, String title) {
        pd.setTitle(title);
    }

    public static void setSeedProgressDialog(ProgressDialog pd, String title, int size) {
        pd.setTitle(title);
        pd.setProgress(size);
    }

    public static String getProgressText(int mode, int type) {
        if (mode == 0) {
            if (type == 0) {
                return "准备下载";
            } else if (type == 1) {
                return "下载中";
            } else if (type == 2) {
                return "下载完成";
            } else if (type == 3) {
                return "下载已被取消";
            } else if (type == 4) {
                return "下载失败";
            }
        } else if (mode == 1) {
            if (type == 0) {
                return "准备上传";
            } else if (type == 1) {
                return "上传中";
            } else if (type == 2) {
                return "上传完成";
            } else if (type == 3) {
                return "上传已被取消";
            } else if (type == 4) {
                return "上传失败";
            }
        }
        return "";
    }

    public static boolean wakeLockAcquire(PowerManager.WakeLock m_wklk) {
        try {
            m_wklk.acquire();
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean wakeLockRelease(PowerManager.WakeLock m_wklk) {
        try {
            m_wklk.release();
        } catch (Exception e) {
        }
        return false;
    }

    public static void setMenuItemVisible(Toolbar toolbar, int itemId, boolean visible){
        Menu menu = toolbar.getMenu();
        MenuItem item = menu.findItem(itemId);
        item.setVisible(visible);
    }

    public static void sleep(int t){
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
