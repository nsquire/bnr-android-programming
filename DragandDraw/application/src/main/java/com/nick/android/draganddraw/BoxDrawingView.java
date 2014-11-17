package com.nick.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final int INVALID_POINTER_ID = -1;

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    private int mSecondaryPointerId = INVALID_POINTER_ID;
    private int mScreenHeight;
    private int mScreenWidth;
    float mSecondaryPointerStartX;
    float mSecondaryPointerStartY;
    float mRotationDegrees = 0;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.setId(View.generateViewId());

        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

        // Calculate center of screen (for rotation point)
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            Log.d(TAG, "Multi-touch event");
        } else {
            Log.i(TAG, "Single-touch event");
        }

        PointF curr = new PointF(MotionEventCompat.getX(event, 0), MotionEventCompat.getY(event, 0));
        Log.i(TAG, "Received primary touch event at x=" + curr.x + ", y=" + curr.y + ":");

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, " ACTION_DOWN");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, " ACTION_MOVE");
                if (mCurrentBox != null && event.getPointerCount() == 1) {
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }

                if (event.getPointerCount() > 1) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(event, mSecondaryPointerId);

                    PointF secondCurr = new PointF(MotionEventCompat.getX(event, pointerIndex), MotionEventCompat.getY(event, pointerIndex));
                    Log.i(TAG, "Received secondary event up at x=" + secondCurr.x + ", y=" + secondCurr.y);
                    Log.i(TAG, "Primary at x=" + curr.x + ", y=" + curr.y);

                    // Calculate the distance moved (for calculating rotation)
                    final float dy = secondCurr.y - mSecondaryPointerStartY;

                    mRotationDegrees = dy / (mScreenHeight / 360);
                    Log.i(TAG, "Degrees to rotate: " + mRotationDegrees);
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, " ACTION_UP");
                mCurrentBox = null;
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, " ACTION_CANCEL");
                mCurrentBox = null;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, " ACTION_POINTER_DOWN");

                if (event.getPointerCount() > 1) {
                    mCurrentBox = null;

                    final int pointerIndex = MotionEventCompat.getActionIndex(event);
                    final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                    mSecondaryPointerId = pointerId;

                    PointF secondCurr = new PointF(MotionEventCompat.getX(event, pointerIndex), MotionEventCompat.getY(event, pointerIndex));
                    Log.i(TAG, "Primary pointer down at x=" + curr.x + ", y=" + curr.y);
                    Log.i(TAG, "Received secondary pointer down at x=" + secondCurr.x + ", y=" + secondCurr.y);

                    // Remember where we started (to calculate rotation)
                    mSecondaryPointerStartX = secondCurr.x;
                    mSecondaryPointerStartY = secondCurr.y;
                }

                break;

            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, " ACTION_POINTER_UP");
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            if (box != null && box.getOrigin() != null && box.getCurrent() != null) {
                float left = Math.min(box.getOrigin().x, box.getCurrent().x);
                float right = Math.max(box.getOrigin().x, box.getCurrent().x);
                float top = Math.min(box.getOrigin().y, box.getCurrent().y);
                float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

                canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.rotate(mRotationDegrees, mScreenWidth / 2, mScreenHeight / 2);
                canvas.drawRect(left, top, right, bottom, mBoxPaint);
                canvas.restore();
            }
        }
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Log.i(TAG, "In onSaveInstanceState");
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("instanceState", super.onSaveInstanceState());
//        bundle.putParcelableArrayList("boxesInstanceState", mBoxes);
//
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        Log.i(TAG, "In onRestoreInstanceState");
//        if (state instanceof Bundle) {
//            Log.i(TAG, "In if...");
//            Bundle bundle = (Bundle) state;
//            mBoxes = bundle.getParcelableArrayList("boxesInstanceState");
//
//            state = bundle.getParcelable("instanceState");
//        }
//
//        super.onRestoreInstanceState(state);
//
//    }
}
