package com.ugex.savelar.scrolltext.Controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class ScrollTextView extends View {
    private Context mContext;
    private String mDisplayText="hello,你好呀！";
    private int mFontSize=60;
    private int mScrollSpeed=2;
    private int mLineTextLength=12;
    private int mBgcolor=0xffffffff;
    private int mFontColor=0xff000000;
    private Paint mPaint=new Paint();
    private int mTextPosX=820;
    private int mTextPosY=340;
    private int mRefreshMillTime=30;
    private float mTextStrokeWidth=0;
    private Timer mTimer=new Timer();
    private boolean isNeedReCalculSize =true;
    private TextToSpeech mTxtSpeech;
    private boolean isNeedToVoice=false;
    public boolean isNeedToVoice() {
        return isNeedToVoice;
    }

    public ScrollTextView setNeedToVoice(boolean needToVoice) {
        isNeedToVoice = needToVoice;
        return this;
    }


    public String getmDisplayText() {
        return mDisplayText;
    }

    public ScrollTextView setDisplayText(String DisplayText) {
        if(DisplayText==null || "".equals(DisplayText))
            return this;
        this.mDisplayText = DisplayText;
        isNeedReCalculSize=true;
        return this;
    }

    public int getScrollSpeed() {
        return mScrollSpeed;
    }

    public ScrollTextView setScrollSpeed(int ScrollSpeed) {
        if(ScrollSpeed<=0){
            return this;
        }
        this.mScrollSpeed = ScrollSpeed;
        return this;
    }

    public float getmTextStrokeWidth() {
        return mTextStrokeWidth;
    }

    public ScrollTextView setTextStrokeWidth(float TextStrokeWidth) {
        if(TextStrokeWidth<0){
            return this;
        }
        this.mTextStrokeWidth = TextStrokeWidth;
        return this;
    }

    public int getLineTextLength() {
        return mLineTextLength;
    }

    public ScrollTextView setLineTextLength(int LineTextLength) {
        if(LineTextLength<=0){
            return this;
        }
        this.mLineTextLength = LineTextLength;
        isNeedReCalculSize=true;
        return this;
    }

    public int getBgcolor() {
        return mBgcolor;
    }

    public ScrollTextView setBgcolor(int Bgcolor) {
        this.mBgcolor = Bgcolor;
        return this;
    }

    public int getFontColor() {
        return mFontColor;
    }

    public ScrollTextView setFontColor(int FontColor) {
        this.mFontColor = FontColor;
        return this;
    }

    public int getRefreshMillTime() {
        return mRefreshMillTime;
    }

    public ScrollTextView setRefreshMillTime(int RefreshMillTime) {
        if(RefreshMillTime<=0){
            return this;
        }
        this.mRefreshMillTime = RefreshMillTime;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTextPosX-=mScrollSpeed;
                posx2 -=mScrollSpeed;
                ScrollTextView.this.postInvalidate();
            }
        },300,mRefreshMillTime);
        return this;
    }

    public ScrollTextView(Context context) {
        super(context);
        InitView(context);
    }
    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitViewWithAttr(context,attrs);
    }
    public ScrollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitViewWithAttr(context,attrs);
    }
    class SpeechInit implements TextToSpeech.OnInitListener
    {
        @Override
        public void onInit(int status)
        {
            if(status==TextToSpeech.SUCCESS)
            {
                int res=mTxtSpeech.setLanguage(Locale.getDefault());
                if(res==TextToSpeech.LANG_MISSING_DATA||res==TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(mContext, "语言设置不成功，默认为英文模式！", Toast.LENGTH_SHORT).show();
                }else
                {
                    mTxtSpeech.setLanguage(Locale.US);
                }
            }
        }
    }
    private void InitView(Context context) {
        this.mContext =context;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTextPosX-=mScrollSpeed;
                posx2 -=mScrollSpeed;
                ScrollTextView.this.postInvalidate();
            }
        },300,mRefreshMillTime);
        mTxtSpeech=new TextToSpeech(this.mContext,new SpeechInit());
    }
    private void InitViewWithAttr(Context context, AttributeSet attrs) {
        InitView(context);
        parseAttrs(attrs);
    }

    private void parseAttrs(AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        isNeedReCalculSize=true;
    }
    private int posx2;
    private int mtxtdislen;
    private int wordsSepCount=2;
    private int toVoicePosX;
    private int[] lastPosX=new int[2];
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isNeedReCalculSize){
            mFontSize =(int)(getWidth()/mLineTextLength*2.0);

            mTextPosX=getWidth();
            mTextPosY=(int)((getHeight()-mFontSize)/2.0+mFontSize);

            toVoicePosX=getWidth()*2/3;

            mPaint.setTextSize(mFontSize);

            Rect bounds=new Rect();
            mPaint.getTextBounds(mDisplayText,0,mDisplayText.length(),bounds);
            mtxtdislen=bounds.width();

            posx2 =mTextPosX+mtxtdislen+wordsSepCount*mFontSize;

            isNeedReCalculSize =false;
        }


        mPaint.setColor(mBgcolor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(-1,-1,getWidth()+1,getHeight()+1,mPaint);

        mPaint.setColor(mFontColor);
        mPaint.setStrokeWidth(mTextStrokeWidth);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(mDisplayText,mTextPosX,mTextPosY,mPaint);

        canvas.drawText(mDisplayText, posx2,mTextPosY,mPaint);

        if(mTextPosX + mtxtdislen<0){
            mTextPosX= posx2 +mtxtdislen+wordsSepCount*mFontSize;
        }
        if(posx2 +mtxtdislen<0){
            posx2 =mTextPosX+mtxtdislen+wordsSepCount*mFontSize;
        }
        if (isNeedToVoice ){
            if((lastPosX[0]>toVoicePosX && mTextPosX<=toVoicePosX)
                ||(lastPosX[1]>toVoicePosX && posx2<=toVoicePosX))
                    mTxtSpeech.speak(mDisplayText,TextToSpeech.QUEUE_FLUSH,null);
        }
        lastPosX[0]=mTextPosX;
        lastPosX[1]=posx2;
    }
}
