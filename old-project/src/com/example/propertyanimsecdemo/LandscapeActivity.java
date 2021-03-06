package com.example.propertyanimsecdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class LandscapeActivity extends Activity {
	protected static final String TAG = "LandscapeActivity";
	private ImageView flower;
	private ValueAnimator valueAnimator;
	private ImageView number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landscape);
		
		
		flower = (ImageView) findViewById(R.id.flower);
		number = (ImageView) findViewById(R.id.number_im);
		
		final Button button = (Button) findViewById(R.id.button1);		
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				
				ObjectAnimator anim1 = ObjectAnimator.ofFloat(flower, "scaleX",
						1.0f, 2f);
				ObjectAnimator anim2 = ObjectAnimator.ofFloat(flower, "scaleY",
						1.0f, 2f);
				AnimatorSet animSet = new AnimatorSet();
				animSet.play(anim1).with(anim2).with(valueAnimator);
				animSet.setDuration(1500);
				animSet.start();
				
				
			}
		});
		valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(),new PointF(883,215), new PointF(432,215));//第一个pointF：开始点，第二个PointF：终点
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {			
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF pointF = (PointF)animation.getAnimatedValue();
				flower.setX(pointF.x-flower.getWidth()/2);
				flower.setY(pointF.y-flower.getHeight()/2);
			}
		});
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "onAnimationStart");
				flower.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG, "onAnimationEnd");
				flower.setVisibility(View.GONE);
				
				number.setVisibility(View.VISIBLE);
				
				//数字加1动画：位移动画从指定坐标点移动到指定目标坐标点，并带有透明度变化
				PropertyValuesHolder ofFloaty = PropertyValuesHolder.ofFloat("y", 215-flower.getHeight()/2,215-flower.getHeight()/2-50f);//Y坐标轴：第二个参数是起始点，第三个是结束点坐标，下行X轴同理
				PropertyValuesHolder ofFloatx = PropertyValuesHolder.ofFloat("x", getResources().getDisplayMetrics().widthPixels/2+flower.getWidth()/2,getResources().getDisplayMetrics().widthPixels/2+flower.getWidth()/2);
				PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("alpha", 1f,0.1f);//透明度，过渡到0.1f透明度
				ObjectAnimator animEnd = ObjectAnimator.ofPropertyValuesHolder(number,ofFloatx,ofFloaty,ofFloat);//创建objectAnimator对象
				//目标对象变大，从X轴方向和Y轴方向
				ObjectAnimator x = ObjectAnimator.ofFloat(number, "scaleX",1.0f, 1.3f);
				ObjectAnimator y = ObjectAnimator.ofFloat(number, "scaleY",1.0f, 1.3f);
				//动画集合，把多个动画组合到一起
				AnimatorSet set = new AnimatorSet();
				set.play(animEnd).with(x).with(y);
				set.setDuration(900);
				set.start();
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						number.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	class BezierEvaluator implements TypeEvaluator<PointF>{

		@Override
		public PointF evaluate(float fraction, PointF startValue,
				PointF endValue) {
			final float t = fraction;	
			Log.e(TAG, "fraction:"+fraction);
			float oneMinusT = 1.0f - t;
			PointF point = new PointF();	//返回计算好的点
			
			PointF point0 = (PointF)startValue;	//开始出现的点
			
			PointF point1 = new PointF();	//贝塞尔曲线控制点
			point1.set(getResources().getDisplayMetrics().widthPixels/2-flower.getWidth(), 20);
			
			Log.i(TAG, "111x:"+(getResources().getDisplayMetrics().widthPixels/2-flower.getWidth()));
			Log.i(TAG, "111hhhhh:"+number.getHeight());
			
			PointF point3 = (PointF)endValue;	//结束终点
			
			//B0(t) = (1-t)2P0 + 2(1-t)tC1 + t2P1    (0 ≤ t ≤ 1) 二次贝塞尔曲线方程
			
			point.x = oneMinusT*oneMinusT*(point0.x)+2*oneMinusT*t*(point1.x)+t*t*(point3.x);
			point.y = oneMinusT*oneMinusT*(point0.y)+2*oneMinusT*t*(point1.y)+t*t*(point3.y);
			return point;
		}	
	}

}
