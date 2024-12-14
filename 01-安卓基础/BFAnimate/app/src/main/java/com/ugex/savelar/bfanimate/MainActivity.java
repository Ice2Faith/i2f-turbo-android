package com.ugex.savelar.bfanimate;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/*
* Animate动画
* 逐帧动画
* 补间动画
* 属性动画
* */
/*
* 逐帧动画
* 一帧一帧的播放，形成的动画
* 使用：
* 添加资源文件夹anim
* 添加一个动画布局文件
* 根节点为：animation-list
* 但是这样在AS中行不通，需要放到drawable中
* 在使用的时候，设置控件的background属性即可
* */
/*
* 补间动画
* 针对移动，旋转等简单动作，只需要定义开始和结束状态，其他的每一帧通过计算得到
* 只需要给出关键帧即可，过渡帧计算而来
* 缩放，旋转，平移，透明度
 * 使用：
 * 添加资源文件夹anim
 * 添加一个动画布局文件
 * 根节点为：rotate（做旋转的时候）,alpha(做透明度的时候)
 *      scale(做缩放)，translate(做平移)
 *      特别的，综合使用的时候set,下面嵌套以上几种即可
* */
/*
* 属性动画
* ValueAnimator
* ObjectAnimator
*
* */
public class MainActivity extends AppCompatActivity {
    private ImageView ivFrameAnim;

    private ImageView ivSpinAnim;
    //补间动画
    private Animation animateSpin;

    private ImageView ivAlphaAnim;
    private Animation animateAlpha;

    private ImageView ivUniversalAnim;
    private Animation animateUniversal;

    //属性动画
    private ImageView ivAttrAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitActivity();
    }

    private void InitActivity() {
        //逐帧动画
        ivFrameAnim =findViewById(R.id.imageViewFrameAnim);
        aniDrawable=(AnimationDrawable) ivFrameAnim.getBackground();

        //补间动画
        //旋转动画
        ivSpinAnim =findViewById(R.id.imageViewSpinAnim);
        animateSpin = AnimationUtils.loadAnimation(this,R.anim.anim_spin);
        //透明度动画
        ivAlphaAnim =findViewById(R.id.imageViewAlphaAnim);
        animateAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_apha);
        //综合动画
        ivUniversalAnim =findViewById(R.id.imageViewUniversalAnim);
        animateUniversal = AnimationUtils.loadAnimation(this,R.anim.anim_universal);

        //属性动画
        ivAttrAnim=findViewById(R.id.imageViewAttrAnim);
    }

    //获取到动画，进行播放动画和停止动画（逐帧动画）
    private AnimationDrawable aniDrawable;
    public void onBtnStartAnimClicked(View view) {
        aniDrawable.start();
        ivSpinAnim.startAnimation(animateSpin);
        //animateAlpha.setFillAfter(true);//设置保留动画后最后一帧
        ivAlphaAnim.startAnimation(animateAlpha);
        ivUniversalAnim.startAnimation(animateUniversal);

    }

    public void onBtnStopAnimClicked(View view) {
        aniDrawable.stop();
    }

    //属性动画，需要API11以上支持
    public void onBtnValueAnimateClicked(View view) {
        //参数：只有变化参数（可变长）
        //因此就需要添加变化监听,来实现动画
        ValueAnimator animator=ValueAnimator.ofFloat(0F,1F);
        animator.setTarget(ivAttrAnim);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(Float)animation.getAnimatedValue();
                ivAttrAnim.setTranslationY(500*value);
            }
        });
        animator.start();
    }

    public void onBtnObjectAnimateClicked(View view) {
        //简单使用
        //参数：使用动画的对象,动画的属性名，属性的变化值(变长)float...
        //ivAttrAnim.setAlpha(alpha);
       /* ObjectAnimator.ofFloat(ivAttrAnim,"alpha",0F,1F)
                .setDuration(3000)
                .start();*/
       //综合使用,给定一个无效的属性（被忽略的）并通过监听变化来实现(无效属性会报红，但是不会影响编译使用)
        ObjectAnimator animator=ObjectAnimator.ofFloat(ivAttrAnim,"others",0F,1F);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(Float)animation.getAnimatedValue();
                ivAttrAnim.setScaleX(value);
                ivAttrAnim.setScaleY(value);
                ivAttrAnim.setAlpha(value);
            }
        });
        animator.start();
    }
}
