package com.example.multitouch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.multitouch.MultiTouchGameScreen;
import com.example.multitouch.R;

import java.util.ArrayList;


public class CustomCircleView extends View {

    public void setAble2Draw(boolean able2Draw) {
        this.able2Draw = able2Draw;
    }

    private boolean able2Draw = false;
    private Handler mHandler;
    private Paint backgroundPaint;
    private Paint circlePaint;//enables anti aliasing, blurds edges.

    private final ArrayList<Point> circleList = new ArrayList<Point>();


    public class Point {
        private float xPos;
        private float yPos;
        private boolean ableToDecrease = false;
        private int radius = 1;

        public void setColor(int color) {
            this.color = color;
        }

        private int color = 1;


        public int getColor() {
            return color;
        }


        public boolean isAbleToDecrease() {
            return ableToDecrease;
        }

        public void setAbleToDecrease(boolean ableToDecrease) {
            this.ableToDecrease = ableToDecrease;
        }


        public float getxPos() {
            return xPos;
        }

        public void setxPos(float xPos) {
            this.xPos = xPos;
        }

        public float getyPos() {
            return yPos;
        }

        public void setyPos(float yPos) {
            this.yPos = yPos;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }


        public Point() {
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[" + xPos + "/" + yPos + "/" + ableToDecrease + "]";
        }


    }


    public CustomCircleView(Context context) {
        super(context);
        init();
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * init method to call from all construktors above.
     * saves duplicate code.
     * when we use custom circle in view, due to layoutinflation, the different construktors are called.
     * assign the paints here:
     */

    private void init() {


        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.multitouch_grey));
        backgroundPaint.setStyle(Paint.Style.FILL);
        circlePaint = new Paint();


    }

    public void clearScreen() {
        if (!circleList.isEmpty()) {
            circleList.clear();
            able2Draw = false;
            mHandler.removeCallbacksAndMessages(null); //stop all handlers.
            backgroundPaint.setColor(getResources().getColor(R.color.multitouch_grey));
            postInvalidate();
        } else {
            System.out.println("empty list");
        }


    }


    /**
     * updates the radius of all points +1 by getting the current point rad, increasing it and saving
     * it back into the point object again until a point has reached 100 radius.
     */
    public void updateAllPoints() {

        Point p = circleList.get(circleList.size() - 1);

        int currentRad = p.getRadius();
        if (!p.isAbleToDecrease()) {
            if (MultiTouchGameScreen.upDown) {
                currentRad++;
                p.setRadius(currentRad);
                circlePaint.setARGB(255, currentRad, currentRad / 2, currentRad / 3);
                p.setColor(circlePaint.getColor());
                Log.e("rrrrrr", " " + p.getRadius());

            } else {
                //mHandler.removeCallbacksAndMessages(null);
                p.setAbleToDecrease(true);


            }
        }
    }

    /**
     * uses all the methods which update the points in a looper every 10ms with 10ms intervalls
     * therfore bringing animations to life.
     */

    public void scaleCircles() {
        mHandler = new Handler();


        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if (able2Draw && !circleList.isEmpty()) {

                    updateAllPoints();
                }
                mHandler.postDelayed(this, 10);
                postInvalidate();
            }

        };
        mHandler.postDelayed(r, 10);

    }


    /**
     * @param x changed from currentXPos/currentYpos
     * @param y changed from currentXPos/currentYpos
     */
    public void createCircle(float x, float y) {
        Point p = new Point();
        p.setxPos(x);
        p.setyPos(y);
        circleList.add(p);

    }


    /**
     * functions for drawning the shockwave circles and normal circles
     *
     * @param canvas
     */
    public void drawCircles(Canvas canvas) {

        for (Point p : circleList) {
            circlePaint.setColor(p.getColor());
            canvas.drawCircle(p.getxPos(), p.getyPos(), p.getRadius(), circlePaint);

        }
        postInvalidate();

    }


    /**
     * to refresh screen outside this class
     */
    public void refreshScreen() {
        postInvalidate();
    }


    /**
     * when scaler is used in on draw, it updates more frequently and faster somehow,
     * due to ondraw getting called exponentially?.
     * when used outside in touchEvents, its more slowly and gets faster the more circles exist.
     *
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(backgroundPaint);
        if (able2Draw) {

            drawCircles(canvas);
        }


    }


}
