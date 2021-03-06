package com.taichuan.uilibrary.loadmoreview.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taichuan.uilibrary.R;

import java.util.List;


/**
 * Created by gui on 2017/3/20.
 * 集成"加载更多"的RecycleViewAdapter
 */
public abstract class LoadMoreRecycleAdapter extends RecyclerView.Adapter {
    private static final String TAG = "LoadMoreRecycleAdapter";
    /**
     * recycleView的实际数据（不包括Footer等）
     */
    private List mDatas;
    private boolean isCanLoadMore;
    /**
     * footItem的itemType
     */
    private int footItemType = Integer.MAX_VALUE - 110;
    /**
     * footItem的layoutID
     */
    private int footLayoutID;
    private GridSpanSizeLookup mGridSpanSizeLookup;
    private GridLayoutManager gridLayoutManager;


    public LoadMoreRecycleAdapter(List datas, boolean isCanLoadMore) {
        this.mDatas = datas;
        this.isCanLoadMore = isCanLoadMore;
        this.footLayoutID = R.layout.loadmore_footer;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType;
        if (mDatas.size() > position) {// not footItem
            itemType = getItemType(position);
        } else if (isCanLoadMore) {// is footItem
            itemType = footItemType;
        } else {
            itemType = super.getItemViewType(position);
        }
        return itemType;
    }

    /**
     * 在这里findView
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder;
        if (viewType == footItemType) {// is FootView
            View view = LayoutInflater.from(parent.getContext()).inflate(footLayoutID, parent, false);
            viewHolder = new FootViewHolder(view);
        } else {// not FootView
            viewHolder = createMViewHolder(parent, viewType);
        }
        return viewHolder;
    }


    /**
     * 根据ViewHolder显示视图（注意：这里不应该出现findView代码）
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mDatas.size() > position) {// not FootView
            onBindMViewHolder(holder, position);
        }
    }

    public abstract RecyclerView.ViewHolder createMViewHolder(ViewGroup parent, int viewType);

    /**
     * 根据ViewHolder设置视图内容（注意：这里不应该出现findView代码，findView代码应该出现在createMViewHolder里或者ViewHolder的构造函数里）<br>
     * 你可以根据viewType把参数的ViewHolder强转成你在createMViewHolder里返回的ViewHolder。
     */
    public abstract void onBindMViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract int getItemType(int position);

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        if (mDatas == null)
            return 0;
        if (isCanLoadMore)
            return mDatas.size() + 1;
        else
            return mDatas.size();
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setIsShowFooter(boolean isShowFooter) {
        if (this.isCanLoadMore != isShowFooter) {
            this.isCanLoadMore = isShowFooter;
            notifyItemChanged(getItemCount());
        }
    }

    public void setFootLayoutID(int footLayoutID) {
        this.footLayoutID = footLayoutID;
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        private FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            gridLayoutManager = ((GridLayoutManager) manager);
            if (mGridSpanSizeLookup == null) {
                mGridSpanSizeLookup = new GridSpanSizeLookup();
            }
            gridLayoutManager.setSpanSizeLookup(mGridSpanSizeLookup);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        // 重写此方法，为了修改瀑布流模式下，让footerItem的宽度铺满
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    /**
     * 判断是否是瀑布流layout
     */
    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        return layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams;
    }

    /**
     * 修改footItem的参数，让此宽度铺满RecycleView
     */
    private void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder && position >= mDatas.size()) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }

    private class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int position) {
            int spanCount;
            if (mDatas == null || mDatas.size() == 0 || mDatas.size() <= position) {// is footerItem
                // 让此Item占满Grid设置的span个数
                spanCount = gridLayoutManager.getSpanCount();
            } else {// not footerItem
                spanCount = 1;
            }
            return spanCount;
        }
    }
}
