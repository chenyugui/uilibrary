package com.taichuan.uilibrary.loadmoreview.base;

/**
 * Created by gui on 2017/3/18.
 */

public interface LoadMoreBaseView {
    boolean isShowFooter();

    int getItemCount();

    boolean isLoading();

    void setIsLoading(boolean isLoading);
}
