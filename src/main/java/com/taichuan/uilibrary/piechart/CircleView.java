package com.taichuan.uilibrary.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.taichuan.uilibrary.R;
import com.taichuan.uilibrary.bean.PieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gui on 2017/6/29.
 * 扇形组合视图
 */
public class CircleView extends View {
    private static final String TAG = "CircleView";
    private Paint mPaint;
    private Rect mBounds;
    private CirclePoint mCircle;
    private int gradient;// 半径梯度
    /**
     * 圆形最大半径
     */
    private int mCircleViewRadius;
    private List<PieData> mPieDataList;
    /**
     * 保存横线字体位置的集合
     */
    private List<List<Integer>> listTextPosition = new ArrayList<>();

    private int[] mColors;
    /**
     * 是否显示标签
     */
    private boolean isShowLabel = true;
    /**
     * 最小的半径
     */
    private int minRadius;
    //TODO
    private int circleMargin = 400;


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private float downX;
    private float downY;
    private int touchPosition;

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        mCircle = new CirclePoint();
        setBackgroundColor(Color.BLUE);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchPosition = -1;
                        if (mCircle.radius > 0) {
                            downX = event.getX();
                            downY = event.getY();
                            if (checkTouchIsInCircle(downX, downY)) {// 判断是否在圆内
                                double angle = getAngleToCircle(downX, downY);// 在圆内的话则获取角度
                                // 判断点的是哪个扇形
                                touchPosition = getTouchSectorPosition(angle);
                                return true;
                            } else if (checkIsTouchText(downX, downY) != -1) {// 判断点击是否是横线字体

                            } else {
                                return false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCircle.radius > 0) {
                            if (event.getX() == downX && event.getY() == downY) {
                                if (touchPosition != -1) {
                                    // 处理点击扇形事件

                                }
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 判断触摸的是否是横线字体
     */
    private int checkIsTouchText(float touchX, float touchY) {
        for (int i = 0; i < listTextPosition.size(); i++) {

            int lX = listTextPosition.get(i).get(0);
            int rX = listTextPosition.get(i).get(1);
            int tY = listTextPosition.get(i).get(2);
            int bY = listTextPosition.get(i).get(3);
            if (touchX >= lX && touchX <= rX && touchY >= tY && touchY <= bY) {
                Log.d(TAG, "checkIsTouchText: 触摸了文字:" + i);
                return i;
            }
        }
        Log.d(TAG, "checkIsTouchText: 没有触摸文字");
        return -1;
    }


    /**
     * 判断触摸的是否在圆内
     */
    private boolean checkTouchIsInCircle(float touchX, float touchY) {
        // 求点到圆心的距离和角度
        if (touchX == mCircle.x && touchY == mCircle.y && mCircle.radius > 0) {// 点就在圆心上
            return false;
        }
        // 求点到圆心的距离
        double length = Math.sqrt(Math.pow(mCircle.x - touchX, 2) + Math.pow(mCircle.y - touchY, 2));
        if (length > mCircle.radius) {
            return false;
        } else {
            return true;
        }
    }

    private double getAngleToCircle(float x, float y) {
        // 求点到圆心的角度（ 正右方向为0度，顺时针）
        double angle = 0;
        if (x > mCircle.x && y == mCircle.y) {// 正右方向
            angle = 0;
        } else if (x > mCircle.x && y > mCircle.y) {// 点在圆心的右下方
            angle = Math.toDegrees(Math.atan((y - mCircle.y) / (x - mCircle.x)));
        } else if (x == mCircle.x && y > mCircle.y) {// 点在圆心的正下方
            angle = 90;
        } else if (x < mCircle.x && y > mCircle.y) {// 点在圆心的左下方
            angle = 180 - Math.toDegrees(Math.atan((y - mCircle.y) / (mCircle.x - x)));
        } else if (x < mCircle.x && y == mCircle.y) {// 点在圆的正左方
            angle = 180;
        } else if (x <= mCircle.x && y <= mCircle.y) {// 点在圆心的左上方
            angle = 180 + Math.toDegrees(Math.atan((mCircle.y - y) / (mCircle.x - x)));
        } else if (x == mCircle.x && y < mCircle.y) {// 点在圆的正上方
            angle = 270;
        } else if (x >= mCircle.x && y <= mCircle.y) {// 点在圆心的右上方
            angle = 360 - Math.toDegrees(Math.atan((mCircle.y - y) / (x - mCircle.x)));
        }
        Log.i(TAG, "checkPointIsInCircle: 角度：" + angle);
        return angle;
    }

    private int getTouchSectorPosition(double angle) {
        if (mPieDataList == null || mPieDataList.size() == 0)
            return -1;
        double totalAngle = 0;
        for (int i = 0; i < mPieDataList.size(); i++) {
            totalAngle = totalAngle + mPieDataList.get(i).getPercent() * 360;

            if (totalAngle >= angle) {
                Log.d(TAG, "getTouchSectorPosition: 点击了第" + i + "个扇形");
                return i;
            }
        }
        return -1;
    }

    public void setPieData(List<PieData> pieDataList) {
        if (pieDataList == mPieDataList) {
            return;
        }
        mPieDataList = pieDataList;
        changeSmallAngle();
    }

    public void setColors(int[] colors) {
        mColors = colors;
    }

    /**
     * 修正过小的度数，防止相邻两个扇形度数都太小，导致文字叠加在一起
     */
    private void changeSmallAngle() {
        if (mPieDataList == null || mPieDataList.size() < 3) {// 小于三个不用修正
            return;
        }
        List<Integer> smallIndexList = new ArrayList<>();// 需要加大角度的扇形的列表
        int bigestPieData = 0;// 最大的扇形的索引
        int secondBigPieData = 1;// 第二大的扇形索引
        float bigestPercent = mPieDataList.get(0).getPercent();// 最大的扇形的百分比
        float secondBigestPercent = mPieDataList.get(1).getPercent();// 第二大的扇形的半分比
        for (int i = 0; i < mPieDataList.size(); i++) {
            PieData pieData = mPieDataList.get(i);
            if (pieData.getPercent() > bigestPercent) {
                secondBigPieData = bigestPieData;
                secondBigestPercent = bigestPercent;


                bigestPercent = pieData.getPercent();
                bigestPieData = i;
            }
            if (pieData.getPercent() <= 0.02) {// 0.02 为7.2度
                if (i < mPieDataList.size() - 1) {
                    if (mPieDataList.get(i + 1).getPercent() <= 0.02) {// 相邻两个扇形度数都小于7.2度
                        Log.d(TAG, "changeSmallAngle: 需要加大角度的扇形index：" + (i + 1));
                        smallIndexList.add(i + 1);
                        i++;
                    }
                }
            }
        }
        for (Integer smallIndex : smallIndexList) {// 给每个需要添加角度的扇形添加0.01的百分比
            mPieDataList.get(smallIndex).setPercent(mPieDataList.get(smallIndex).getPercent() + 0.01f);
            mPieDataList.get(bigestPieData).setPercent(mPieDataList.get(bigestPieData).getPercent() - 0.01f);
        }
        Log.d(TAG, "changeSmallAngle: 最大的扇形的索引:" + bigestPieData);
        Log.d(TAG, "changeSmallAngle: 第二大的扇形的索引:" + secondBigPieData);
        Log.d(TAG, "changeSmallAngle: 最大的扇形的百分比:" + bigestPercent);
        Log.d(TAG, "changeSmallAngle: 第二大的扇形的百分比:" + secondBigestPercent);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (mPieDataList == null) {
            // 未设置内容，显示文字说明
            drawCenterText(canvas, "先设置pieData");
        } else {
            drawSector(canvas);
            drawLineAndText(canvas);
        }
    }


    private void drawCenterText(Canvas canvas, String text) {
        mBounds = new Rect();
        mPaint.setColor(getResources().getColor(R.color.textColor_noSetPieData));
        mPaint.setTextSize(getResources().getDimension(R.dimen.textSize_noSetPieData));
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(
                text,
                getWidth() / 2 - textWidth / 2,
                getHeight() / 2 + textHeight / 2,
                mPaint);
    }

    /**
     * 画扇形
     *
     * @param canvas
     */
    private void drawSector(Canvas canvas) {
        int dataSize = mPieDataList.size();


        mPaint.setColor(Color.YELLOW);
        int diameter = Math.min(getWidth(), getHeight()) - circleMargin;// 圆形的最大直径
        mCircle.x = getWidth() / 2;
        mCircle.y = getHeight() / 2;
        mCircle.radius = diameter / 2;
        Log.i(TAG, "drawSector: 圆的圆心：x:" + mCircle.x + "  y:" + mCircle.y);
        Log.i(TAG, "drawSector: 圆的半径：" + mCircle.radius);

        // 居中画圆
        RectF rectF = new RectF(mCircle.x - mCircle.radius, mCircle.y - mCircle.radius, mCircle.x + mCircle.radius, mCircle.y + mCircle.radius);
        if (dataSize == 0) {// 无数据
            mPaint.setColor(Color.GRAY);
            canvas.drawArc(rectF, 0, 360, false, mPaint);
            drawCenterText(canvas, "无数据");
        } else {
            minRadius = mCircle.radius / 5;
            gradient = (mCircle.radius - minRadius) / dataSize; // 梯度 = (最大半径-最小半径)/数据个数
            gradient=0;
            int color;
            int startAngle = 0;
            for (int i = 0; i < dataSize; i++) {
                if (mColors == null || mColors.length - 1 < i) {
                    color = Color.LTGRAY;
                } else {
                    color = mColors[i];
                }
                mPaint.setColor(color);
                // 按梯度减小半径
                rectF = new RectF(mCircle.x - (mCircle.radius - gradient * i), mCircle.y - (mCircle.radius - gradient * i), mCircle.x + (mCircle.radius - gradient * i), mCircle.y + (mCircle.radius - gradient * i));
                float angel;
                if (i == mPieDataList.size() - 1) {// 最后一个扇形铺满
                    angel = 360 - startAngle;
                } else {
                    angel = 360 * mPieDataList.get(i).getPercent();
                }
                canvas.drawArc(rectF, startAngle, angel, true, mPaint);
                startAngle = (int) (startAngle + angel);
            }
        }
    }

    /**
     * 度数转弧度
     *
     * @param degrees
     * @return
     */
    private double degrees2Radian(int degrees) {
        return 2 * Math.PI / 360 * (degrees);
    }

    /**
     * 画线和文字
     */
    private void drawLineAndText(Canvas canvas) {
        listTextPosition.clear();
        if (mPieDataList == null || mPieDataList.size() == 0)
            return;
        int baseAngle = 0;// 扇形开始角度
        int src;
        int mSlashLineLength = mCircle.radius + 30;// 斜线的长度(从圆心开始画)
        mPaint.setTextSize(getResources().getDimension(R.dimen.textSize_pieDataName));
        for (int i = 0; i < mPieDataList.size(); i++) {
            PieData pieData = mPieDataList.get(i);
            src = canvas.save();
            if (pieData.getPercent() <= 0) {
                continue;
            }
            // 颜色
            int color;
            if (mColors == null || mColors.length - 1 < i) {
                color = Color.LTGRAY;
            } else {
                color = mColors[i];
            }
            mPaint.setColor(color);
            //
            int rotateAngle = (int) (baseAngle + pieData.getPercent() * 360 / 2); //斜线的角度
            //刚好在坐标轴上的情况，画斜线会变成垂直水平的线  不好看  稍微偏移一下
            if (rotateAngle % 90 == 0) {
                rotateAngle = rotateAngle + 3;
            }
            int slashLineEndY = 0;
            int slashLineEndX = 0;
            // 计算斜线终点坐标
            if (rotateAngle > 270 && rotateAngle < 360) {
                slashLineEndY = (int) (mCircle.y - (Math.sin(degrees2Radian(360 - rotateAngle)) * mSlashLineLength));
                slashLineEndX = (int) (Math.cos(degrees2Radian(360 - rotateAngle)) * mSlashLineLength + mCircle.x);
            } else if (rotateAngle > 180 && rotateAngle < 270) {
                slashLineEndY = (int) (mCircle.y - Math.sin(degrees2Radian(rotateAngle - 180)) * mSlashLineLength);
                slashLineEndX = (int) (mCircle.x - Math.cos(degrees2Radian(rotateAngle - 180)) * mSlashLineLength);
            } else if (rotateAngle > 90 && rotateAngle < 180) {
                slashLineEndY = (int) (Math.sin(degrees2Radian(180 - rotateAngle)) * mSlashLineLength + mCircle.y);
                slashLineEndX = (int) (mCircle.x - Math.cos(degrees2Radian(180 - rotateAngle)) * mSlashLineLength);
            } else if (rotateAngle > 0 && rotateAngle < 90) {
                slashLineEndY = (int) (Math.sin(degrees2Radian(rotateAngle)) * mSlashLineLength + mCircle.y);
                slashLineEndX = (int) (Math.cos(degrees2Radian(rotateAngle)) * mSlashLineLength + mCircle.x);
            }
//            canvas.rotate(rotateAngle, mCircle.x, mCircle.y);
            //
            canvas.drawLine(mCircle.x, mCircle.y, slashLineEndX, slashLineEndY, mPaint);        //画斜线

            // 获取字体宽度和高度
            mBounds = new Rect();
            String text = pieData.getName() == null ? "" : pieData.getName();
            mPaint.getTextBounds(text, 0, text.length(), mBounds);
            int textWidth = mBounds.width();
            int textHeight = mBounds.height();
            //
            int horizonLineEndX;
            int textStartY;
            int textStartX;
            //画水平直线时要考虑在左边还是右边，两边起点和终点是有区别的
            if (rotateAngle > 90 && rotateAngle < 270) {// 在左边
                horizonLineEndX = slashLineEndX - textWidth;
                if (horizonLineEndX < 0) {//防止其超出左边边界
                    horizonLineEndX = 0;
                }
                textStartX = horizonLineEndX;
            } else {// 在右边
                horizonLineEndX = slashLineEndX + textWidth;
                if (horizonLineEndX > getWidth()) {//防止超出右边界
                    horizonLineEndX = getWidth();
                }
                textStartX = slashLineEndX + 5;
            }
            Log.i(TAG, "horizontal start " + slashLineEndX + " " + horizonLineEndX);
            canvas.drawLine(slashLineEndX, slashLineEndY, horizonLineEndX, slashLineEndY, mPaint);  //画直线
            //画字时考虑上半圆和下半圆时，上半圆字在上面，下半圆字在下面
            if (rotateAngle > 180) {// 在横线上方
                textStartY = slashLineEndY - 10;
            } else {
                textStartY = slashLineEndY + textHeight;
            }
            canvas.drawText(pieData.getName(), textStartX, textStartY, mPaint);
            // 保存字体左上右下位置
            saveTextPosition(textStartX, textStartX + textWidth, textStartY - textHeight, textStartY);
            // 累加角度
            baseAngle += (pieData.getPercent() * 360);
        }
    }

    /**
     * @param lX 左边的X坐标
     * @param rX 右边的X坐标
     * @param tY 上边的Y坐标
     * @param bY 下边的Y坐标
     */
    private void saveTextPosition(int lX, int rX, int tY, int bY) {
        List<Integer> list = new ArrayList<>();
        list.add(lX);
        list.add(rX);
        list.add(tY);
        list.add(bY);
        listTextPosition.add(list);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.先获取父控件的宽高
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);// 父控件总宽度
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);// 父控件总高度
        // 2.再计算标签需要的的高度
        int labelHeight = 0;
        if (isShowLabel && mPieDataList != null) {
            //TODO
//            labelHeight = mPieDataList.size() * 50;
            labelHeight =0;
        }
        // 3.最后计算饼形图的最大可用高度 = 父控件高度或者宽度 - 标签高度 - margin
        int circleMaxHeight = Math.min(parentWidth, parentHeight) - labelHeight - PieChartView.marginBetween;
        //TODO
//        setMeasuredDimension(parentWidth, circleMaxHeight);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 圆心类，标注圆心得位置
     */
    private class CirclePoint {
        int x;
        int y;
        int radius = -1;
    }
}
