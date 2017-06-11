package com.taichuan.uilibrary.loadingdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.taichuan.uilibrary.R;


public class ProgressDlgUtils {
    private static Dialog loadingDialog = null;


    public static void showLoadingDialog(Context ctx) {
        showLoadingDialog(ctx, "");
    }

    public static void showLoadingDialog(Context ctx, String msg) {
        if (ctx == null) {
            return;
        }
        loadingDialog = new Dialog(ctx, R.style.Dialog_No_Border);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCanceledOnTouchOutside(false);
        if (msg == null)
            msg = "";
        TextView tv_tip = (TextView) loadingDialog.findViewById(R.id.tv_tip);
        tv_tip.setText(msg);
        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
            return;
        }
        loadingDialog.show();
    }

    public static void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
            loadingDialog=null;
        }
    }

    public static void setDialogCancelble(boolean b) {
        if (loadingDialog != null)
            loadingDialog.setCancelable(b);
    }

}
