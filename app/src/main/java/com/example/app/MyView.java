package com.example.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

public class MyView extends View {
    Paint paint;//畫筆
    private LinkedList<LinkedList<HashMap<String,Float>>> lines,garbage_can;//接收的xy軸
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        lines = new LinkedList<>();
        garbage_can = new LinkedList<>();
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
    }

    //畫畫
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (LinkedList<HashMap<String,Float>>line:lines){
            for (int i=1;i<line.size();i++){
                HashMap<String,Float> p0 = line.get(i-1);
                HashMap<String,Float> p1 = line.get(i);
                canvas.drawLine(p0.get("x"),p0.get("y"),p1.get("x"),p1.get("y"),paint);
            }
        }
    }

    //監聽
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            garbage_can.clear();//加入新的線的時候刪除垃圾桶內的線,防止產生新線時,又給予回覆下一動
            LinkedList<HashMap<String,Float>> line = new LinkedList<>();
            lines.add(line);
        }

        float ex = event.getX();
        float ey = event.getY();

        //加入監聽的xy軸
        HashMap<String,Float> post = new HashMap<>();
        post.put("x",ex);
        post.put("y",ey);
        lines.getLast().add(post);//將最後一個值放入post

        invalidate();
        return true;
    }
    public void clear(){
        lines.clear();
        invalidate();
    }
    public void undo(){
        if (lines.size()>0){
            garbage_can.add(lines.removeLast());
            invalidate();
        }

    }
    public void redo(){
        if (garbage_can.size()>0){
            lines.add(garbage_can.removeLast());
            invalidate();
        }
    }
}
