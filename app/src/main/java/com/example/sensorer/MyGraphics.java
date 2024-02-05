package com.example.sensorer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class MyGraphics extends View {

    int viewWidth, viewHeight, textSize;
    int xPos, yPos, radius;
    Paint paint = new Paint();

    public MyGraphics(Activity activity) {
        super(activity);

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        viewWidth = size.x;
        viewHeight = size.y;
        textSize = viewHeight / 20;

        xPos = size.x / 2;
        yPos = size.y / 2;
        radius = size.x / 4;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xFFFF0000);
        canvas.drawCircle(xPos, yPos, radius, paint);
    }

    int xPrevious, yPrevious;
    boolean moving = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xNew = (int) event.getX();
        int yNew = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int xDiff = xNew - xPos;
            int yDiff = yNew - yPos;
            // Er der ramt inden for cirklen?
            if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) < radius) {
                moving = true;
                xPrevious = xNew;
                yPrevious = yNew;
            }

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (moving) {
                xPos += xNew - xPrevious;
                yPos += yNew - yPrevious;
                xPrevious = xNew;
                yPrevious = yNew;
                invalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            moving = false;
        }
        // Er der to fingre på, så juster diameteren til afstanden imellem de to fingre
        if (event.getPointerCount() == 2) {
            int x1 = (int) event.getX(0);
            int y1 = (int) event.getY(0);
            int x2 = (int) event.getX(1);
            int y2 = (int) event.getY(1);
            int x1Rel = x1 - xPos;
            int y1Rel = y1 - yPos;
            int x2Rel = x2 - xPos;
            int y2Rel = y2 - yPos;
            // Er der ramt inden for cirklen?
            if (Math.sqrt(x1Rel * x1Rel + y1Rel * y1Rel) < radius
                    && Math.sqrt(x2Rel * x2Rel + y2Rel * y2Rel) < radius) {
                // Set radius til afstanden immellem fingrene?
                int xDiff = x1 - x2;
                int yDiff = y1 - y2;
                radius = (int) (Math.sqrt(xDiff * xDiff + yDiff * yDiff));
                invalidate();
            }
        }
        return true;
    }
}
