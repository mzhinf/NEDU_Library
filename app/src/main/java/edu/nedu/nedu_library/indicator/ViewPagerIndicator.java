package edu.nedu.nedu_library.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;


/**
 * Created by 小呓的欧尼酱 on 2017/3/22.
 * 改动后实现需要实现
 *  setVisibleTabs 设置tab个数
 *  setViewPager 设置pager
 *  setViewPagerListener 设置Listener
 */

public class ViewPagerIndicator extends LinearLayout {
    public static final int DEFAULT_VISIBLE_TABS=4;
    private static final double RADIO_TRIANGEL_WIDTH=1/6f;//写死了三角形大小 所以不需要该参数
    private Paint paint;
    private Path path;
    //三角形的宽
    private int mTriangleWidth;
    //三角形的高
    private int mTriangleHeight;
    private int mInitTranslationX;
    private int mTranslationX=0;
    //visibleTabs 表示tab个数
    private int visibleTabs=DEFAULT_VISIBLE_TABS;

    /**
     * 防止用户覆盖ViewPagerIndicator中的ViewPager.OnPageChangeListener，所以在这里设置一个回调接口。
     */
    private ViewPagerListener viewPagerListener;

    public interface ViewPagerListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        //三角形的颜色
        paint.setColor(Color.parseColor("#fdf6e3"));
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 设置显示的tab个数
     * tab的个数会影响初始tab的位置
     *
     * @param count
     */
    public void setVisibleTabs(int count) {
        if (count <= 0) {
            visibleTabs = ViewPagerIndicator.DEFAULT_VISIBLE_TABS;
        } else {
            this.visibleTabs = count;
        }
    }

    /*
        该方法实现动态添加tab 可以在LinearLayout事先添加tab 不需要再动态添加
    //动态添加tab
    public void addTabs(List<String> titles)
    {
        for(String title:titles)
        {
            TextView tv=new TextView(getContext());
            int tabWidth=getScreenWith()/visibleTabs;
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(lp);
            tv.setTextColor(Color.parseColor("#ffffffff"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            tv.setText(title);
            tv.setGravity(Gravity.CENTER);
            addView(tv);
        }
    }
    */

    /**
     * 设置三角形的初始位置和宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //根据获取的大小和tab个数绘制三角形大小
        //mTriangleWidth = (int) (w / visibleTabs * RADIO_TRIANGEL_WIDTH);
        //mTriangleHeight = mTriangleWidth / 2;

        //------ 这里我写死了大小
        mTriangleWidth = 40;
        mTriangleHeight = 20;
        //-----

        mInitTranslationX = w / visibleTabs / 2 - mTriangleWidth / 2;

        initTriangle();
    }

    /**
     * 绘制三角形
     */
    private void initTriangle() {
        path = new Path();
        path.moveTo(0, 0);
        path.lineTo(mTriangleWidth, 0);
        path.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        path.close();
    }

    /**
     * 更新三角形的位置
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(path, paint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 设置tab滑动到未显示的区域
     *
     * @param position
     * @param offset
     */
    public void scrollTo(int position, float offset) {

        int tabWidth = getScreenWith() / visibleTabs;
        mTranslationX = (int) (tabWidth * (offset + position));

        if ((position + 2) >= visibleTabs && offset > 0 && visibleTabs < getChildCount()) {
            if (visibleTabs != 1) {
                this.scrollTo((int) (tabWidth * (offset + position + 2 - visibleTabs)), 0);
            } else {
                this.scrollTo(mTranslationX, 0);
            }
        }
        invalidate();
    }

    /**
     * 设置ViewPager
     *
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollTo(position, positionOffset);
                if (viewPagerListener != null) {
                    viewPagerListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPagerListener != null) {
                    viewPagerListener.onPageSelected(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (viewPagerListener != null) {
                    viewPagerListener.onPageScrollStateChanged(state);
                }
            }
        });
    }


    /**
     * viewPagerListener 可以在调用类设置viewPagerListener
     *
     * @param viewPagerListener
     */
    public void setViewPagerListener(ViewPagerListener viewPagerListener) {
        this.viewPagerListener = viewPagerListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private int getScreenWith() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }
}
