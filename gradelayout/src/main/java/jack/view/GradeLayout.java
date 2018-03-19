package jack.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * email- luodijack@163.com
 * github- https://github.com/LuodiJackShen
 * blog- http://blog.csdn.net/a199581
 * by- LuodiJackShen
 */

public class GradeLayout extends LinearLayout implements View.OnTouchListener {
    private static final String TAG = "GradeLayout";
    private static final int DEFAULT_MAX_GRADE = 10;
    private static final int DEFAULT_UNCHOSEN_COLOR = 0xFF888888;
    private static final int DEFAULT_CHOSEN_COLOR = 0xFFE12219;
    private static final float DEFAULT_GRADE_ICO_SIZE = 5;
    private static final float DEFAULT_NAV_BUTTON_SIZE = 30;
    private static final float DEFAULT_GAP = 16;
    private static final float DEFAULT_GRADE_TEXT_SIZE = 15;
    private static final float DEFAULT_GRADE_ICO_PADDING = 9;
    private static final float DEFAULT_NAV_LINE_CHOSEN_WIDTH = 4;
    private static final float DEFAULT_NAV_LINE_UNCHOSEN_WIDTH = 2;

    private static int MAX_LEFT_MARGIN = 0;

    private ImageView mPullButton;
    private Paint mUnchosenPaint;
    private Paint mChosenPaint;
    private Canvas mCanvas;
    private Drawable mGradeChosenIco;
    private Drawable mGradeUnchosenIco;
    private TextView mLastChosenTv;
    private TextView mNowChosenTv;
    private LayoutParams mPullButtonParams;

    private OnGradeUpdateListener mListener;
    private List<TextView> mTextViews;
    private List<String> mGradeTexts;
    private int mMaxGrade = DEFAULT_MAX_GRADE;
    private float mDensity = -1F;
    private int mGradeChosenColor;
    private int mGradeUnchosenColor;
    private int mGradeChosenIcoId;
    private int mGradeUnchosenIcoId;
    private float mGradeIcoSize;
    private int mNavLineChosenColor;
    private int mNavLineUnchosenColor;
    private int mNavButtonIcoId;
    private float mNavButtonSize;
    private float mGradeTextSize;
    private float mGradeIcoPadding;
    private float mNavLineChosenWidth;
    private float mNavLineUnchosenWidth;
    private boolean isFirstDraw = true;
    private int mExtraLeftMargin = 0;
    private float mGap;

    public GradeLayout(Context context) {
        this(context, null);
    }

    public GradeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setWillNotDraw(false);//让ViewGroup调用onDraw方法,ViewGroup默认不调用onDraw()。
        setOrientation(VERTICAL);//设置方向。

        //加载属性
        loadAttrs(context, attrs);

        //数据初始化
        mTextViews = new ArrayList<>();
        mGradeTexts = new ArrayList<>();
        for (int i = 1; i <= mMaxGrade; i++) {
            mGradeTexts.add(String.valueOf(i));
        }

        mGradeUnchosenIco = context.getResources().getDrawable(mGradeUnchosenIcoId);
        mGradeUnchosenIco.setBounds(0, 0, (int) mGradeIcoSize, (int) mGradeIcoSize);

        mGradeChosenIco = context.getResources().getDrawable(mGradeChosenIcoId);
        mGradeChosenIco.setBounds(0, 0, (int) mGradeIcoSize, (int) mGradeIcoSize);

        //初始化画笔
        mUnchosenPaint = new Paint();
        mUnchosenPaint.setColor(mNavLineUnchosenColor);
        mUnchosenPaint.setStrokeWidth(mNavLineUnchosenWidth);

        mChosenPaint = new Paint();
        mChosenPaint.setColor(mNavLineChosenColor);
        mChosenPaint.setStrokeWidth(mNavLineChosenWidth);

        //构建分数布局
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout gradeLy = new LinearLayout(context);
        gradeLy.setLayoutParams(params);
        gradeLy.setOrientation(HORIZONTAL);

        try {
            for (int i = 1; i <= mMaxGrade; i++) {
                CharSequence str = mGradeTexts.get(i - 1);
                TextView tv = buildTextView(context, str);
                mTextViews.add(tv);
                gradeLy.addView(tv);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Maybe mGradeTexts.size() != mMaxGrade", e);
        }
        addView(gradeLy);

        //构建滑块布局
        mPullButton = new ImageView(context);
        mPullButton.setBackgroundDrawable(
                context.getResources().getDrawable(mNavButtonIcoId));
        mPullButtonParams
                = new LayoutParams((int) mNavButtonSize, (int) mNavButtonSize);
        mPullButtonParams.topMargin = (int) mGap;
        mPullButton.setOnTouchListener(this);
        addView(mPullButton, mPullButtonParams);


        mLastChosenTv = mTextViews.get(0);
        mLastChosenTv.setTextColor(mGradeChosenColor);
        mLastChosenTv.setCompoundDrawables(null, null, null, mGradeChosenIco);
        mNowChosenTv = mLastChosenTv;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        TextView first = mTextViews.get(0);
        TextView last = mTextViews.get(mMaxGrade - 1);
        int startX = first.getLeft() + (first.getRight() - first.getLeft()) / 2;
        int startY = (mPullButton.getBottom() - mPullButton.getTop()) / 2 + mPullButton.getTop();
        int grayEndX = last.getRight() - (last.getRight() - last.getLeft()) / 2;
        int redEndX = mPullButton.getRight() - (mPullButton.getRight() - mPullButton.getLeft()) / 2;
        //绘制滑动线段
        drawLine(startX, startY, grayEndX, startY, mUnchosenPaint);
        drawLine(startX, startY, redEndX, startY, mChosenPaint);
        //绘制线段两端的圆弧
        canvas.drawCircle(startX, startY, mNavLineChosenWidth / 2, mChosenPaint);
        canvas.drawCircle(grayEndX, startY, mNavLineUnchosenWidth / 2, mUnchosenPaint);

        if (isFirstDraw) {
            isFirstDraw = false;
            MAX_LEFT_MARGIN = last.getLeft() + mExtraLeftMargin;
            mExtraLeftMargin = startX - mPullButton.getRight() / 2;
            mPullButtonParams.leftMargin = mExtraLeftMargin
                    + mNowChosenTv.getLeft()
                    + (mNowChosenTv.getRight() - mNowChosenTv.getRight()) / 2;
            mPullButton.requestLayout();
        }

    }

    private int mLastX = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);//处理滑动冲突,让父控件把滑动事件交给自己处理。
        int x = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                mPullButtonParams.leftMargin += deltaX;
                if (mPullButtonParams.leftMargin < mExtraLeftMargin) {
                    mPullButtonParams.leftMargin = mExtraLeftMargin;
                }
                if (mPullButtonParams.leftMargin > MAX_LEFT_MARGIN) {
                    mPullButtonParams.leftMargin = MAX_LEFT_MARGIN;
                }
                mPullButton.requestLayout();
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int nowMargin = mPullButtonParams.leftMargin;
                //根据mPullButtonParams的leftMargin来计算出当前分数的index。
                int index = (nowMargin * (mMaxGrade - 1) * 2 + MAX_LEFT_MARGIN)
                        / (2 * MAX_LEFT_MARGIN) + 1;
                updateUI(index - 1);
                notifyGradeHasChanged(mTextViews.get(index - 1).getText().toString());
                break;
            default:
                break;
        }
        return true;
    }

    private TextView buildTextView(Context context, CharSequence grade) {
        TextView textView = new TextView(context);
        textView.setText(grade);
        textView.setTextSize(px2dip(context, mGradeTextSize));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(mGradeUnchosenColor);
        textView.setCompoundDrawables(null, null, null, mGradeUnchosenIco);
        textView.setCompoundDrawablePadding((int) mGradeIcoPadding);
        LayoutParams params =
                new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        textView.setLayoutParams(params);
        return textView;
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradeLayout);
        mMaxGrade = ta.getInt(R.styleable.GradeLayout_max_grade, DEFAULT_MAX_GRADE);
        mGradeChosenColor =
                ta.getColor(R.styleable.GradeLayout_grade_color_chosen, DEFAULT_CHOSEN_COLOR);
        mGradeUnchosenColor =
                ta.getColor(R.styleable.GradeLayout_grade_color_unchosen, DEFAULT_UNCHOSEN_COLOR);
        mGradeChosenIcoId = ta.getResourceId(
                R.styleable.GradeLayout_grade_ico_chosen, R.drawable.redpoint_icon);
        mGradeUnchosenIcoId = ta.getResourceId(
                R.styleable.GradeLayout_grade_ico_unchosen, R.drawable.graypoint_icon);
        mGradeIcoSize = ta.getDimension(
                R.styleable.GradeLayout_grade_ico_size, dip2px(context, DEFAULT_GRADE_ICO_SIZE));
        mNavLineChosenColor = ta.getColor(
                R.styleable.GradeLayout_nav_line_chosen_color, DEFAULT_CHOSEN_COLOR);
        mNavLineUnchosenColor = ta.getColor(
                R.styleable.GradeLayout_nav_line_unchosen_color, DEFAULT_UNCHOSEN_COLOR);
        mNavButtonIcoId = ta.getResourceId(
                R.styleable.GradeLayout_nav_button_ico, R.drawable.nav_button_icon);
        mNavButtonSize = ta.getDimension(
                R.styleable.GradeLayout_nav_button_size, dip2px(context, DEFAULT_NAV_BUTTON_SIZE));
        mGap = ta.getDimension(R.styleable.GradeLayout_gap, dip2px(context, DEFAULT_GAP));
        mGradeIcoPadding = ta.getDimension(R.styleable.GradeLayout_grade_ico_padding,
                dip2px(context, DEFAULT_GRADE_ICO_PADDING));
        mNavLineChosenWidth = ta.getDimension(R.styleable.GradeLayout_nav_line_chosen_width,
                dip2px(context, DEFAULT_NAV_LINE_CHOSEN_WIDTH));
        mNavLineUnchosenWidth = ta.getDimension(R.styleable.GradeLayout_nav_line_unchosen_width,
                dip2px(context, DEFAULT_NAV_LINE_UNCHOSEN_WIDTH));
        mGradeTextSize = ta.getDimension(R.styleable.GradeLayout_grade_text_size,
                dip2px(context, DEFAULT_GRADE_TEXT_SIZE));
        ta.recycle();
    }

    private void updateUI(int index) {
        mLastChosenTv.setTextColor(mGradeUnchosenColor);
        mLastChosenTv.setCompoundDrawables(null, null, null, mGradeUnchosenIco);
        mLastChosenTv = mTextViews.get(index);

        mNowChosenTv = mTextViews.get(index);
        mNowChosenTv.setTextColor(mGradeChosenColor);
        mNowChosenTv.setCompoundDrawables(null, null, null, mGradeChosenIco);

        mPullButtonParams.leftMargin = mNowChosenTv.getLeft() + mExtraLeftMargin;
        mPullButton.requestLayout();
    }

    private void drawLine(int startX, int startY, int endX, int endY, Paint paint) {
        if (mCanvas != null) {
            mCanvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    public void setOnGradeUpdateListener(OnGradeUpdateListener listener) {
        mListener = listener;
    }

    public void setGradeTexts(List<String> gradeTexts) {
        if (gradeTexts.size() != mMaxGrade) {
            Log.e(TAG, "error: ",
                    new IllegalArgumentException(
                            "gradeTexts.size() != max_grade" +
                                    "(what you set is app:max_grade=\""
                                    + mMaxGrade + "\" in the xml."));
            return;
        }

        mGradeTexts = gradeTexts;
        for (int i = 0; i < mTextViews.size(); i++) {
            mTextViews.get(i).setText(mGradeTexts.get(i));
        }
    }

    /***
     * 通过分数的index来设置被选中的分数
     * @param index the index of grade(from 0).
     */
    public void setChosenGrade(int index) {
        if (index < 0 || index >= mTextViews.size()) {
            Log.e(TAG, "error ", new IllegalArgumentException("index<0 or index>=max_grade"));
            return;
        }

        updateUI(index);
        String grade = mTextViews.get(index).getText().toString();
        notifyGradeHasChanged(grade);

    }

    /***
     * 通过具体的分数设置被选中的分数
     * @param grade 具体的分数
     */
    public void setChosenGrade(String grade) {
        for (int i = 0; i < mGradeTexts.size(); i++) {
            if (mGradeTexts.get(i).equals(grade)) {
                setChosenGrade(i);
                break;
            }
        }
    }

    private void notifyGradeHasChanged(String grade) {
        if (mListener != null) {
            mListener.onGradeUpdate(this, grade);
        }
    }

    private float getDensity(Context context) {
        if (mDensity <= 0F) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }

    public int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5F);
    }

    public int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5F);
    }

    public interface OnGradeUpdateListener {
        void onGradeUpdate(GradeLayout view, String grade);
    }
}
