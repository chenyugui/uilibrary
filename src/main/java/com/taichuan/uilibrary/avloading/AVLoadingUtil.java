package com.taichuan.uilibrary.avloading;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.taichuan.uilibrary.R;
import com.taichuan.utils.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by gui on 2017/7/18.
 * AVLoadingDialog工具 <br><br>
 * 使用showLoading显示dialog <br>
 * 使用stopLoading关闭dialog <br>
 * 使用setDefault可以配置默认样式等<br>
 */
public class AVLoadingUtil {
    private static final String TAG = "AVLoadingUtil";
    private static final ArrayList<Dialog> LOADERS = new ArrayList<>();// dialogList集合，方便stopLoading时关闭所有Dialog
    private static Enum<LoadingStyle> defaultStyle = LoadingStyle.BallBeatIndicator;// 默认样式
    public static boolean default_cancelable = true;//
    private static final int DEFAULT_SCALE = 7;// 屏幕宽高/dialog的宽高的倍数
    private static final Object lock = new Object();

    /**
     * 切换loadingDialog的默认样式
     */
    public static void setDefault(Enum<LoadingStyle> loadingStyle, boolean cancelable) {
        defaultStyle = loadingStyle;
        default_cancelable = cancelable;
    }

    public static void showLoading(Context context) {
        showLoading(context, defaultStyle, default_cancelable);
    }

    public static void showLoading(Context context, boolean cancelable) {
        showLoading(context, defaultStyle, cancelable);
    }

    @SuppressWarnings("unused")
    public static void showLoading(Context context, Enum<LoadingStyle> loaderStyleEnum) {
        showLoading(context, loaderStyleEnum.name(), default_cancelable);
    }

    public static void showLoading(Context context, Enum<LoadingStyle> loaderStyleEnum, boolean cancelable) {
        showLoading(context, loaderStyleEnum.name(), cancelable);
    }

    /**
     * @param context 尽量不要传ApplicationContext
     */
    public static void showLoading(Context context, String styleName, boolean cancelable) {
        AppCompatDialog dialog = new AppCompatDialog(context, R.style.AVLoader);
        AVLoadingIndicatorView avLoadingIndicatorView = AVLoadingViewCreator.create(context, styleName);
        dialog.setContentView(avLoadingIndicatorView);
        // 设置Dialog大小
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            int deviceWidth = DimenUtil.getScreenWidth(context);
            int deviceHeight = DimenUtil.getScreenHeight(context);
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / DEFAULT_SCALE;
            lp.height = deviceHeight / DEFAULT_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        synchronized (lock) {
            LOADERS.add(dialog);
        }
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public static void stopLoading() {
        synchronized (lock) {
            for (Dialog dialogs : LOADERS) {
                if (dialogs != null) {
                    if (dialogs.isShowing()) {
                        dialogs.cancel();
                    }
                }
            }
            LOADERS.clear();
        }
    }
}
