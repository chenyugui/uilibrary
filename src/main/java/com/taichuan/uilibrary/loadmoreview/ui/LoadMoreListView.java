package com.taichuan.uilibrary.loadmoreview.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.taichuan.uilibrary.loadmoreview.base.LoadMoreBaseView;
import com.taichuan.uilibrary.loadmoreview.listener.ListScrollListener;
import com.taichuan.uilibrary.loadmoreview.listener.LoadMoreListener;

import java.lang.ref.WeakReference;


/**
 * 包含"加载更多"的ListView控件。
 * 只能使用LoadMoreBaseAdapter或其子类
 */
public class LoadMoreListView extends ListView implements LoadMoreBaseView {
    private static final String TAG = "LoadMoreListView";
    public ListScrollListener listScrollListener;
    private LoadMoreBaseAdapter loadMoreBaseAdapter;
    private boolean isLoading;
    private static MyHandler handler;

    private static class MyHandler extends Handler {
        private WeakReference<LoadMoreListView> weak;

        private MyHandler(LoadMoreListView loadMoreListView) {
            weak = new WeakReference<>(loadMoreListView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weak.get().setIsCanLoadMore(true);
        }
    }

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化和设置OnScrollListener
     */
    private void initScrollListener() {
        if (listScrollListener == null) {
            listScrollListener = new ListScrollListener(this);
            setOnScrollListener(listScrollListener);
        }
    }

    /**
     * 如果需要"加载更多"的功能，请使用{@link #setOnScrollListener(ListScrollListener)} ，而不是此方法。
     */
    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListener instanceof ListScrollListener) {
            listScrollListener = (ListScrollListener) onScrollListener;
            setOnScrollListener(listScrollListener);
        } else {
            super.setOnScrollListener(onScrollListener);
        }
    }

    public void setOnScrollListener(ListScrollListener listScrollListener) {
        super.setOnScrollListener(listScrollListener);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        initScrollListener();
        listScrollListener.setLoadMoreListener(loadMoreListener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof LoadMoreBaseAdapter) {
            setAdapter(loadMoreBaseAdapter);
        } else {
            try {
                throw new Exception("please use LoadMoreBaseAdapter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapter(LoadMoreBaseAdapter adapter) {
        this.loadMoreBaseAdapter = adapter;
        super.setAdapter(loadMoreBaseAdapter);
    }

    @Override
    public boolean isShowFooter() {
        return loadMoreBaseAdapter.isCanLoadMore();
    }

    /**
     * 设置能不能加载更多
     */
    public void setIsCanLoadMore(boolean isCanLoadMore) {
        if (loadMoreBaseAdapter != null) {
            loadMoreBaseAdapter.setIsShowFooter(isCanLoadMore);
        } else {
            try {
                throw new Exception("loadMoreBaseAdapter is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载结束
     */
    public void loadMoreFinish(boolean isCanLoadMore) {
        loadMoreBaseAdapter.setIsShowFooter(false);
        if (isCanLoadMore) {
            Message msg = handler.obtainMessage();
            isLoading = false;
            handler.sendMessageDelayed(msg, 300);
        }
    }


    /**
     * 获取Adapter的ItemCount
     *
     * @return 如果允许显示footer，返回实际数据个数+1；否则返回实际个数
     */
    @Override
    public int getItemCount() {
        if (getAdapter() == null)
            return 0;
        else {
            return getAdapter().getCount();
        }
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }


    public LoadMoreBaseAdapter getAdapter() {
        return loadMoreBaseAdapter;
    }
}
