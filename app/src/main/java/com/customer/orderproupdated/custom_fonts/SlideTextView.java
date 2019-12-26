package com.customer.orderproupdated.custom_fonts;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class SlideTextView extends View {
	private String showText = "Swipe Left";
//	<com.orderpro.customer.customfonts.SlideTextView

	private Paint paint;
	private int cycleNum = 0; // �߳�ѭ����sleep�Ĵ���

	// ��onDraw�����е���canvas.drawText��Ҫ����x/y���꣬�Ǹ��������������½ǵ����ꡣ
	private int firstLineOffset = 0;

	// drawText��y��ʼֵ�ǳ����ܣ�����stackoverflow�ķ������Ǹ�yֵҪͨ�����㵥������ռ�ݵĸ߶ȣ��űȽϿ�ѧ���������Ǵ���һ����ƫ��
	// ���extraPaddingTop���������ֲ�ƫ��ģ������������ȷ
	private int extraPaddingTop = PixValue.dip.valueOf(1f);

	// ����������Ҫͨ��xml�ļ�������
	private int textSize = PixValue.sp.valueOf(11); // �ֺ�
	private int textColor = Color.WHITE; // ������ɫ

	private int textWidth, textHeight;

	// �����ֵ���ߺ��ұߣ��������һЩ�ո���ռλ
	// rangeArea����˼����������ֲ���ռ�������ֲ���(�������ո�)������֮һ
	private String extraText, actualText;
	private int extraWidth;
	private float rangeArea;
	private float maxXDistance;
	private int darkColor;
	private Matrix trans;

	public SlideTextView(Context context) {
		this(context, null);
	}

	public SlideTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// 1. ��ʼ��paint����
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTextAlign(Align.LEFT);

		// 2. ��ʼ���Ǹ����ܵ�y����ֵ
		Rect rect = new Rect();
		paint.getTextBounds("��", 0, 1, rect); // ��һ�����͵ĺ���Ϊģ�壬����߶�
		firstLineOffset = (int) (rect.height() - rect.bottom) + extraPaddingTop; // stackoverflow����ĳ�˸��Ľ���

		textWidth = (int) paint.measureText(showText);
		textHeight = textSize;
		extraText = "            ";
		extraWidth = (int) paint.measureText(extraText);
		rangeArea = (float) textWidth / (textWidth + extraWidth * 2) / 3;
		actualText = extraText + showText + extraText; // drawTextʱ�������ֵ���ߺ��ұߣ��������һЩ�ո���ռλ
		maxXDistance = 1 + 2 * rangeArea;
		darkColor = Color.parseColor("#747474");
		trans = new Matrix();
		trans.setRotate(-90);
	}

	@Override
	protected void onFinishInflate() {
		new UIThread().start();
		super.onFinishInflate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int thisNum = 1 + cycleNum % circulationNum;

		// ����Ƶ�������[-rangeArea, 1 + rangeArea]
		float centerX = -rangeArea + maxXDistance * thisNum / circulationNum;
		Shader shader = new LinearGradient(0, 0, 0, textWidth, new int[] {
				darkColor, darkColor, Color.WHITE, darkColor, darkColor },
				new float[] { -1f, centerX - rangeArea, centerX,
						centerX + rangeArea, 2f }, TileMode.CLAMP);
		shader.setLocalMatrix(trans);
		paint.setShader(shader);
		canvas.drawText(actualText, -extraWidth + getPaddingLeft(),
				getPaddingTop() + firstLineOffset, paint);
	}

	private int circulationNum = 30; // ��������һ��ѭ���Ĵ���

	class UIThread extends Thread {

		public UIThread() {
			cycleNum = 0;
		}

		@Override
		public void run() {
			try {
				while (true) {
					sleep(40);
					// handler֪ͨui��������͸����
					if (cycleNum > 0 && cycleNum % circulationNum == 0) {
						sleep(1200);
					}

					Message msg = uiHandler.obtainMessage();
					cycleNum++;
					msg.sendToTarget();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	};

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// ˢ��View
			invalidate();
		};
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// ��дonMeasure������û����View���ֶ�����߶�
		setMeasuredDimension(textWidth + getPaddingLeft() + getPaddingRight(),
				textHeight + getPaddingTop() + getPaddingBottom());
	}
}
