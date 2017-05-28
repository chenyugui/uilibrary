uilibrary
==========================================================
实现一些比较通用的android UI组件

### com.taichuan.uilibrary.loadmoreview
加载更多的组件<br>


- CommonAdapter：<br>
通用的BaseAdapter，内部实现了ViewHolder优化、ItemType判断、支持上拉加载更多，使用示例：
```
        User user = new User();
        user.setItemType(0);// 设置0代表分组的item
        user.setGroupName("这是分组");
        datas.add(user);
        user = new User();
        user.setItemType(1);// 设置1代表数据item
        user.setName("这是数据item");
        datas.add(user);
        // 第二个参数表示是否支持加载更多（为true时，需要配合LoadMoreListView使用，普通的ListView无效）
        // 第三个参数是每种分组类型的itemLayoutID，从0开始计算，如下：
        // R.layout.item_lv是itemType为0的itemLayoutID,  R.layout.item_lv2是itemType为1的itemLayoutID
        CommonAdapter adapter = new CommonAdapter<User>(datas, false, R.layout.item_lv, R.layout.item_lv2) {
            @Override
            public void showView(SuperViewHolder viewHolder, User data, int position, int itemType) {
                switch (itemType) {
                    case 0:// 分组
                        viewHolder.setText(R.id.tv, data.getGroupName());
                        break;
                    case 1:// 数据
                        viewHolder.setText(R.id.tv2, data.getName());
                        break;
                }
            }
        };
        lv.setAdapter(adapter);
```

- LoadMoreListView：<br>
支持上拉加载更多的ListView，需配合CommonAdapter一起使用。 <br>
注意如果要setOnScrollListener的话，请使用：
```
setOnScrollListener(ListScrollListener listScrollListener)
``` 
&emsp;&emsp;&emsp;使用示例：
```
        loadMoreListView = findView(R.id.lv);
        loadMoreDataFromServer();
        // 第二个参数表示是否支持加载更多（为true时，需要配合LoadMoreListView使用，普通的ListView无效）
        // 第三个参数是每种分组类型的itemLayoutID，从0开始计算，如下：
        // R.layout.item_lv是itemType为0的itemLayoutID,  R.layout.item_lv2是itemType为1的itemLayoutID
        // 分组
        adapter = new CommonAdapter<User>(datas, true, R.layout.item_lv, R.layout.item_lv2) {
            @Override
            public void showView(SuperViewHolder viewHolder, User data, int position, int itemType) {
                switch (itemType) {
                    case 0:// 分组
                        viewHolder.setText(R.id.tv, data.getGroupName());
                        break;
                    case 1:// 数据
                        viewHolder.setText(R.id.tv2, data.getName());
                        break;
                }
            }
        };
        loadMoreListView.setAdapter(adapter);
        loadMoreListView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMoreData() {
                // 获取更多数据
                loadMoreDataFromServer();
                adapter.notifyDataSetChanged();
                // 设置加载结束，同时设置是否再次加载更多
                loadMoreListView.loadMoreFinish(true);
            }
        });
```

- LoadMoreRecycleAdapter
- LoadMoreRecyclerView




