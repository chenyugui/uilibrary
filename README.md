uilibrary
==========================================================
实现一些比较通用的android UI组件

## com.taichuan.uilibrary.loadmoreview
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
&emsp;&emsp;使用示例：
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



## PieChartView（自定义饼状图）
-   使用：
    +   像普通View一样使用即可，setPieData是必须设置的，否则无数据显示。
    +   其他api根据需要调用。
-   可用API：
	+   ```pieChart.setColors(colors);// 设置扇形颜色值```
	+   ```pieChart.setCircleMargin(getResources().getDimension(R.dimen.margin_pieChart));// 设置饼状图圆形四周的间距```
	+   ```pieChart.setCenterTextSize(xxx);// 设置中间文字大小```
	+   ```pieChart.setPieDataTextSize(200);// 设置横线文字大小```
	+   ```pieChart.setDescription(description);// 设置描述文字```
	+   ```pieChart.setDescriptionTextColor(xxx);// 设置描述信息文字颜色```
	+   ```pieChart.setOnSelectedListener(xxx)// 设置饼状图扇形选择事件监听```
	+   ```pieChart.setPieData(pieDataList);// 设置饼状图扇形数据集合,要比以上API晚调用```
