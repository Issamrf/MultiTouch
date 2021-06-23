package com.example.multitouch;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Canvas;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.multitouch.views.CustomCircleView;



import java.math.BigDecimal;
import java.math.RoundingMode;

public class MultiTouchGameScreen extends AppCompatActivity implements Button.OnClickListener, View.OnTouchListener{


    private CustomCircleView touchFrame;
    private Button clearScreenButton;
    float xPos2 = -1;
    float yPos2 = -1;
    public static boolean upDown = false;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_touch_game_screen);

        clearScreenButton = findViewById(R.id.clearScreenButton_ID);
        clearScreenButton.setOnClickListener(this);
        touchFrame = findViewById(R.id.touchFrame_ID);
        touchFrame.setOnTouchListener(this);
    }


    /**
     * on click for clear screen button
     * @param v
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clearScreenButton_ID) {
            touchFrame.clearScreen();
        }

    }

    /**
     * @param f float value to convert
     * @param places how many decimales places
     * @return returns new float
     */
    private static float roundFloat(float f, int places) {

        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }

    /**
     * this method contains all neccesarry methods from our touchframe in order to display all animations
     * the right way, we also save duplicate code by calling all methods in this one.
     * @param tempx x coordinate
     * @param tempy y coordinate
     * @param pressure pressure as float
     */
    public void callTouchFrameMethods(float tempx, float tempy, float pressure){

        touchFrame.setAble2Draw(true);
        touchFrame.createCircle(tempx,tempy);
        touchFrame.scaleCircles();
        touchFrame.refreshScreen();
    }


    /**
     * handles on touch for single and multi touch
     * single and multi touch down call the touchframe methods
     * single and multi release create the shockwaves
     * @param v custom view here
     * @param event touchevent
     * @return
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //use action masked for pointer functionality
        int action = event.getActionMasked();
        // Get the index of the pointer associated with the action.
        int index = event.getActionIndex();
        float pressure;

        float xPos = -1;
        float yPos = -1;


        switch (action) {


            case MotionEvent.ACTION_DOWN:
                upDown = true;
                System.out.println("Single touch event");
                xPos2 = event.getX(index);
                yPos2 = event.getY(index);
                pressure = event.getPressure();
                callTouchFrameMethods(xPos2, yPos2, pressure);
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                upDown = true;

                System.out.println("Multi touch event");
                xPos = event.getX(index);
                yPos = event.getY(index);
                pressure = event.getPressure(index);
                callTouchFrameMethods(xPos, yPos, pressure);



            case MotionEvent.ACTION_UP:
                upDown = false;

                System.out.println("Single touch event");
                xPos2 = event.getX(index);
                yPos2 = event.getY(index);
                touchFrame.scaleCircles();
                touchFrame.refreshScreen();


                break;
           case MotionEvent.ACTION_POINTER_UP:
               upDown = false;


                   System.out.println("Released Finger:" + index);
                   touchFrame.scaleCircles();
                   touchFrame.refreshScreen();


                break;
        }
        return true;
    }



}