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
import android.view.View;

import com.taichuan.uilibrary.R;
import com.taichuan.uilibrary.bean.PieData;

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
    /**
     * 圆形最大半径
     */
    private int mCircleViewRadius;
    private List<PieData> mPieDataList;
    private int[] mColors;
    /**
     * 是否显示标签
     */
    private boolean isShowLabel = true;

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

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        mCircle = new CirclePoint();
        setBackgroundColor(Color.BLUE);
    }


    public void setPieData(List<PieData> pieDataList) {
        if (pieDataList == mPieDataList) {
            return;
        }
        mPieDataList = pieDataList;
    }

    public void setColors(int[] colors) {
        mColors = colors;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (mPieDataList == null) {
            // 未设置内容，显示文字说明
            drawNoSetDataText(canvas);
        } else {
            drawSector(canvas);
        }
    }

    private void drawNoSetDataText(Canvas canvas) {
        drawCenterText(canvas, "先设置pieData");
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

    private void drawSector(Canvas canvas) {
        int dataSize = mPieDataList.size();


        mPaint.setColor(Color.YELLOW);
        int diameter = Math.min(getWidth(), getHeight()) - 100;// 圆形的最大直径
        mCircle.x = getWidth() / 2;
        mCircle.y = getHeight() / 2;
        mCircle.radius = diameter / 2;
        // 居中画圆
        RectF rectF = new RectF(mCircle.x - mCircle.radius, mCircle.y - mCircle.radius, mCircle.x + mCircle.radius, mCircle.y + mCircle.radius);
        if (dataSize == 0) {// 无数据
            mPaint.setColor(Color.GRAY);
            canvas.drawArc(rectF, 0, 360, false, mPaint);
            drawCenterText(canvas, "无数据");
        } else {
            int gradient = (mCircle.radius - mCircle.radius / 5) / dataSize; // 梯度 = (最大半径-最小半径)/数据个数
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
                canvas.drawArc(rectF, startAngle, 360 * mPieDataList.get(i).getPercent(), true, mPaint);
                startAngle = (int) (startAngle + 360 * mPieDataList.get(i).getPercent());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.先获取父控件的宽高
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);// 父控件总宽度
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);// 父控件总高度
        // 2.再计算标签需要的的高度
        int labelHeight = 0;
        if (isShowLabel && mPieDataList != null) {
            labelHeight = mPieDataList.size() * 50;
        }
        // 3.最后计算饼形图的最大可用高度 = 父控件高度或者宽度 - 标签高度 - margin
        int circleMaxHeight = Math.min(parentWidth, parentHeight) - labelHeight - PieChartView.marginBetween;
        setMeasuredDimension(parentWidth, circleMaxHeight);
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
        int radius;
        int[] radiusLevel = new int[4];                 //四个半径等级
    }
}
