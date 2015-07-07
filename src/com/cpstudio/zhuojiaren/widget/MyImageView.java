/**
 * MyImageView.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.cpstudio.zhuojiaren.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * �Զ���View��onMeasure������ȡͼƬ��͸�
 * 
 * @author likebamboo
 */
public class MyImageView extends ImageView {

    /**
     * ��¼�ؼ��Ŀ�͸�
     */
    private Point mPoint = new Point();

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPoint.x = getMeasuredWidth();
        mPoint.y = getMeasuredHeight();
    }

    /**
     * ����Point
     * 
     * @return
     */
    public Point getPoint() {
        return mPoint;
    }
}
