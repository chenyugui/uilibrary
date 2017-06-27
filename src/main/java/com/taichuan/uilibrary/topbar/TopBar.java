package com.taichuan.uilibrary.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taichuan.uilibrary.R;

/**
 * Created by gui on 2017/6/27.
 * 顶部菜单
 */
public class TopBar extends RelativeLayout {
    private String TAG = "TopBar";
    private Context mContext;
    private RelativeLayout rlt_left;
    private RelativeLayout rlt_right;
    private TextView txtTitle;
    private ImageView imgLeft;
    private ImageView imgRight;

    //    private int backgroundResId;
    private int leftSrcResId;
    private int leftBgResId;
    private int rightSrcResId;
    private int rightBgResId;

    public TopBar(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
        initAttr(attrs);
    }

    private void initViews() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.topbar, null, true);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mView.setLayoutParams(lp);
        rlt_left = (RelativeLayout) mView.findViewById(R.id.rlt_left);
        rlt_right = (RelativeLayout) mView.findViewById(R.id.rlt_right);
        txtTitle = (TextView) mView.findViewById(R.id.txtTitle);
        imgLeft = (ImageView) mView.findViewById(R.id.img_left);
        imgRight = (ImageView) mView.findViewById(R.id.img_right);
        addView(mView);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.background};

    private void initAttr(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TopBar);
        TypedArray aa = mContext.obtainStyledAttributes(attrs, ATTRS);
//        // 背景
//        initBackground(aa);
        // 左ImageView
        initLeftView(a);
        // 右ImageView
        initRightView(a);
        // 标题
        initTitle(a);
        a.recycle();
        aa.recycle();
    }


    private void initLeftView(TypedArray a) {
        //      左ImageView src
        leftSrcResId = a.getResourceId(R.styleable.TopBar_leftSrc, -1);
        //      左ImageView bg
        leftBgResId = a.getResourceId(R.styleable.TopBar_leftBackground, -1);
        if (leftSrcResId == -1 && leftBgResId == -1) {
            rlt_left.setVisibility(GONE);
        } else {
            rlt_left.setVisibility(VISIBLE);
        }

        if (leftSrcResId != -1) {
            imgLeft.setImageResource(leftSrcResId);
        }
        if (leftBgResId != -1) {
            imgLeft.setBackgroundResource(leftBgResId);
//            imgLeft.setBackgroundDrawable(getResources().getDrawable(leftBgResId));
        }

    }

    private void initRightView(TypedArray a) {
        //      右ImageView src
        rightSrcResId = a.getResourceId(R.styleable.TopBar_rightSrc, -1);
        //      右ImageView bg
        rightBgResId = a.getResourceId(R.styleable.TopBar_rightBackground, -1);
        if (rightSrcResId == -1 && rightBgResId == -1) {
            rlt_right.setVisibility(GONE);
        } else {
            rlt_right.setVisibility(VISIBLE);
        }

        if (rightSrcResId != -1) {
            imgRight.setImageResource(rightSrcResId);
        }
        if (rightBgResId != -1) {
            imgRight.setBackgroundResource(rightBgResId);
//            imgRight.setBackgroundDrawable(getResources().getDrawable(rightBgResId));
        }

    }

    //    }
//        backgroundResId=a.getResourceId(android.R.st)
    private void initTitle(TypedArray a) {
        String titleText = a.getString(R.styleable.TopBar_titleText);
        float textSize = a.getDimension(R.styleable.TopBar_titleTextSize, getResources().getDimension(R.dimen.titleTextSize));
        int titleTextColor = a.getColor(R.styleable.TopBar_titleTextColor, 1234567890);

        if (titleText == null) {
            titleText = "";
        }
        txtTitle.setText(titleText);
        txtTitle.setTextSize(textSize);
        if (titleTextColor != 1234567890) {
            txtTitle.setTextColor(titleTextColor);
        }
    }
}
