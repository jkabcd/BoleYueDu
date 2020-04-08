package com.example.yuedulib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class DrawView extends View {
    ObservableEmitter<Object> emitter;
    ObservableEmitter<Object> emitter2;
    MyPoint downpoint ,movepoint,uppoint;
    PageOPER pageOPER;
    Scroller scroller;
    int sharedownwidth= 30;
    int maxdis = 100;
    private GradientDrawable mNextShadowLR;
    private GradientDrawable mNextShadowLT;
    private int pages = 0;
    private DeRectionHelp.Derection firstderection = DeRectionHelp.Derection.NONE;
    private DeRectionHelp.PAGETYPE firPAGETYPE = DeRectionHelp.PAGETYPE.BOOKCOVER;
    public DrawView.PageOPER getPageOPER() {
        return pageOPER;
    }

    public void setPageOPER(DrawView.PageOPER pageOPER) {
        this.pageOPER = pageOPER;
    }

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

private ArrayList<Book> currrentBooks;
public void setCurrentBook(ArrayList<Book> currentBook){
    this.currrentBooks =currentBook;
    pages =currrentBooks.size();
     firstderection = DeRectionHelp.Derection.NONE;
     downpoint = new MyPoint();
     movepoint = new MyPoint();
     uppoint = new MyPoint();
    postInvalidate();
}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
public void init(Context context){
    downpoint= new MyPoint();
    movepoint= new MyPoint();
    uppoint = new MyPoint();
    scroller = new Scroller(context);
    int[] nextShadowColors = new int[]{0x88111111, 0x00111111}; //从深到
    int[] nextShadowColors2 = new int[]{0x00111111, 0x88111111}; //从浅到深
    mNextShadowLR = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, nextShadowColors);
    mNextShadowLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

    mNextShadowLT = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, nextShadowColors2);
    mNextShadowLT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    rejist();
    rejist2();
}
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(currrentBooks!=null&&currrentBooks.size()>0){
            if(firPAGETYPE== DeRectionHelp.PAGETYPE.COVER){
                drawCover(canvas);
            }else if(firPAGETYPE == DeRectionHelp.PAGETYPE.BOOKCOVER){
                drawBookCover(canvas);
            }
        }
    }
public void drawCover(Canvas canvas){
    Paint paint =  new Paint();
    for (int i = 0; i < currrentBooks.size(); i++) {
        if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.NEXT){
            canvas.drawBitmap(currrentBooks.get(i).getBitmap(),0,0,paint);//最底层
        }else if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.CUR){
            if(firstderection==DeRectionHelp.Derection.LEFT){//左滑动
                canvas.drawBitmap(currrentBooks.get(i).getBitmap(),-(downpoint.x-movepoint.x),0,paint);//当前层
                mNextShadowLR.setBounds((int) (-(downpoint.x-movepoint.x)+ScreenTool.getScreenWidth(getContext())), 0, (int) (-(downpoint.x-movepoint.x)+ScreenTool.getScreenWidth(getContext()))+sharedownwidth, ScreenTool.getScreenHeight(getContext()));
                mNextShadowLR.draw(canvas);
                float fdis=  (downpoint.x-movepoint.x)-ScreenTool.getScreenWidth(this.getContext());
                if(Math.abs(fdis)<10){
                    emitter.onNext(firstderection);//这边加
                }
            }
            else {//右滑动当前层不动
                canvas.drawBitmap(currrentBooks.get(i).getBitmap(),0,0,paint);//当前层
            }
        }else if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.PRE){
            if(firstderection==DeRectionHelp.Derection.RIGHT){//右滑
                canvas.drawBitmap(currrentBooks.get(i).getBitmap(), -(ScreenTool.getScreenWidth(this.getContext())-DeRectionHelp.getdis(downpoint,movepoint).x), 0,paint);//上一层
                mNextShadowLR.setBounds((int)DeRectionHelp.getdis(downpoint,movepoint).x,
                        0,
                        (int)DeRectionHelp.getdis(downpoint,movepoint).x+sharedownwidth,
                        ScreenTool.getScreenHeight(getContext()));
                mNextShadowLR.draw(canvas);
                float fdis=  (ScreenTool.getScreenWidth(this.getContext())-DeRectionHelp.getdis(downpoint,movepoint).x);
                if(Math.abs(fdis)<10){
                    emitter.onNext(firstderection);//这边加
                }
            }else {
                canvas.drawBitmap(currrentBooks.get(i).getBitmap(),-ScreenTool.getScreenWidth(this.getContext()),0,paint);//上一层保持最远距离
            }
        }
    }
}

    public void drawBookCover(Canvas canvas){
        Paint paint =  new Paint();
        for (int i = 0; i < currrentBooks.size(); i++) {
            if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.NEXT){
                canvas.drawBitmap(currrentBooks.get(i).getBitmap(),0,0,paint);//最底层
            }else if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.CUR){
                if(firstderection== DeRectionHelp.Derection.LEFT){
                    Rect  rectsrc = new Rect(0,0, (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    Rect  rectdst = new Rect(0,0, (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    canvas.drawBitmap(currrentBooks.get(i).getBitmap(),rectsrc,rectdst,paint);
                }else {
                    canvas.drawBitmap(currrentBooks.get(i).getBitmap(),0,0,paint);
                }
            }else if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.PRE){
                if(firstderection== DeRectionHelp.Derection.RIGHT){
                    Rect  rectsrc = new Rect(0,0, (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    Rect  rectdst = new Rect(0,0, (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    canvas.drawBitmap(currrentBooks.get(i).getBitmap(),rectsrc,rectdst,paint);
                }
            }else if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.COVERCUR){
                if(firstderection== DeRectionHelp.Derection.LEFT){
                    Rect  rectsrc = new Rect(0,0, (int) ((int) (ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    Rect  rectdst = new Rect((int) (movepoint.x),0, (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),ScreenTool.getScreenHeight(getContext()));
                    canvas.drawBitmap(currrentBooks.get(i).getBitmap(),rectsrc,rectdst,paint);
                    if(Math.abs(getScreenWidth()-movepoint.x)>1){
                        mNextShadowLR.setBounds((int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5),
                                0,
                                (int) (movepoint.x+(ScreenTool.getScreenWidth(getContext())-movepoint.x)/2.5)+sharedownwidth, ScreenTool.getScreenHeight(getContext()));
                        mNextShadowLR.draw(canvas);

                        mNextShadowLT.setBounds((int) (movepoint.x-sharedownwidth),
                                0,
                                (int) movepoint.x, ScreenTool.getScreenHeight(getContext()));
                        mNextShadowLT.draw(canvas);
                    }
                }else if(firstderection== DeRectionHelp.Derection.RIGHT){
                    Rect  rectsrc = new Rect(0,0, (int) ((int)(ScreenTool.getScreenWidth(getContext())-(movepoint.x))/2.5),ScreenTool.getScreenHeight(getContext()));
                    Rect  rectdst = new Rect((int) (movepoint.x),0, (int) ((int) (movepoint.x)+(int)(ScreenTool.getScreenWidth(getContext())-(movepoint.x))/2.5),ScreenTool.getScreenHeight(getContext()));
                    canvas.drawBitmap(currrentBooks.get(i).getBitmap(),rectsrc,rectdst,paint);
                    if(Math.abs(getScreenWidth()-movepoint.x)>1){
                        mNextShadowLT.setBounds((int) (movepoint.x)-sharedownwidth,
                                0,
                                (int) (movepoint.x), ScreenTool.getScreenHeight(getContext()));
                        mNextShadowLT.draw(canvas);

                        mNextShadowLR.setBounds((int) (movepoint.x+((int)(ScreenTool.getScreenWidth(getContext())-(movepoint.x))/2.5)),
                                0,
                                (int) (movepoint.x+((int)(ScreenTool.getScreenWidth(getContext())-(movepoint.x))/2.5))+sharedownwidth, ScreenTool.getScreenHeight(getContext()));
                        mNextShadowLR.draw(canvas);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);
         switch (event.getAction()){
             case MotionEvent.ACTION_DOWN:
                 if(scroller.isFinished()){
                     firstderection = DeRectionHelp.Derection.NONE;
                     downpoint.x = event.getX();
                     downpoint.y = event.getY();

                 }else {
                     return false;
                 }
                 break;
             case MotionEvent.ACTION_MOVE:
                 if(scroller.isFinished()){
                            movepoint.x = event.getX();
                            movepoint.y = event.getY();
                        if(firPAGETYPE== DeRectionHelp.PAGETYPE.COVER){
                            if(DeRectionHelp.isRight(downpoint,new MyPoint(event.getX(),event.getY()))&&!isHaveLeftPage()){
                                emitter2.onNext("已经是第一页了");
                                firstderection= DeRectionHelp.Derection.NONE;
                                return true;
                            } else if(DeRectionHelp.isLeft(downpoint,new MyPoint(event.getX(),event.getY()))&&!isHaveRightPage()){
                                emitter2.onNext("已经是最后一页了");
                                firstderection= DeRectionHelp.Derection.NONE;
                                return true;
                            }
                            if(DeRectionHelp.isLeft(downpoint,movepoint)&firstderection==DeRectionHelp.Derection.NONE){
                                    firstderection = DeRectionHelp.Derection.LEFT;//确定左移动方向
                            }else if(DeRectionHelp.isRight(downpoint,movepoint)&firstderection==DeRectionHelp.Derection.NONE) {
                                firstderection = DeRectionHelp.Derection.RIGHT;//确定右移动方向
                            }
                        }else if(firPAGETYPE== DeRectionHelp.PAGETYPE.BOOKCOVER){
                            if(DeRectionHelp.judgeDerection(downpoint,movepoint)==DeRectionHelp.Derection.NONE){
                                return true;//直接点击不做判断
                            }
                            if(DeRectionHelp.isLeft(downpoint,movepoint)){
                                if(!isHaveRightPage()&firstderection== DeRectionHelp.Derection.NONE){
                                    emitter2.onNext("已经是最后一页了");
                                    firstderection= DeRectionHelp.Derection.NONE;
                                    return true;
                                }else if(firstderection== DeRectionHelp.Derection.NONE) {
                                    firstderection= DeRectionHelp.Derection.LEFT;
                                }
                            }else if(DeRectionHelp.isRight(downpoint,movepoint)){
                                if(!isHaveLeftPage()&firstderection== DeRectionHelp.Derection.NONE){
                                    emitter2.onNext("已经是第一页了");
                                    return true;
                                }else if(firstderection== DeRectionHelp.Derection.NONE){
                                    firstderection= DeRectionHelp.Derection.RIGHT;
                                }
                            }
                        }
                     postInvalidate();
                 }
                 break;
             case MotionEvent.ACTION_UP:
                 if(scroller.isFinished()){
                     uppoint.x = event.getX();
                     uppoint.y = event.getY();
                     if(firPAGETYPE== DeRectionHelp.PAGETYPE.COVER&&firstderection!=DeRectionHelp.Derection.NONE){
                         colverStartAnimal();
                     }else if(firPAGETYPE== DeRectionHelp.PAGETYPE.BOOKCOVER){
                         colverBookStartAnimal();
                     }
                 }else {
                     return false;
                 }
                 break;
         }
         return true;
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            movepoint.x = scroller.getCurrX();
            movepoint.y =scroller.getCurrY();
            postInvalidate();
        }
    }
    public void rejist(){
      Observable.create(new ObservableOnSubscribe<Object>() {
          @Override
          public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
              DrawView.this.emitter = emitter;
          }
      }).throttleFirst(250, TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
          @Override
          public void accept(Object o) throws Exception {
              if(o==DeRectionHelp.Derection.LEFT){
                  pageOPER.nextPage();
              }else if(o==DeRectionHelp.Derection.RIGHT){
                  pageOPER.prePage();
              }

          }
      });
    }
    public void rejist2(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                DrawView.this.emitter2 = emitter;
            }
        }).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(DrawView.this.getContext(), ""+o, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isHaveLeftPage(){
        for (int i = 0; i < currrentBooks.size(); i++) {
            if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.PRE){
                return true;
            }
        }
        return false;
    }

    /*覆盖滑动动画*/
    public void colverStartAnimal(){
        if(firstderection==DeRectionHelp.Derection.LEFT&DeRectionHelp.isLeft(downpoint,uppoint)){//左滑
            if(DeRectionHelp.getdis(downpoint,uppoint).x>maxdis){
                scroller.startScroll((int) uppoint.x,0,(int)(downpoint.x-uppoint.x)-ScreenTool.getScreenWidth(this.getContext()),0,250);//改变方向
            }else {
                scroller.startScroll((int) uppoint.x,0, (int)(downpoint.x-uppoint.x),0,250);//复位
            }
            postInvalidate();
        }else if(firstderection==DeRectionHelp.Derection.RIGHT&DeRectionHelp.isRight(downpoint,uppoint)) {//右滑动
            if(DeRectionHelp.getdis(downpoint,uppoint).x>maxdis){
                scroller.startScroll((int) uppoint.x,0, (int) (ScreenTool.getScreenWidth(this.getContext())-(uppoint.x-downpoint.x)),0,250);//改变方向
            }else {
                scroller.startScroll((int) uppoint.x,0, (int)(downpoint.x-uppoint.x),0,250);//复位
            }
            postInvalidate();
        }
    }
    /*仿真滑动动画*/
    public void colverBookStartAnimal(){
        if(firstderection== DeRectionHelp.Derection.LEFT){
            if(uppoint.x>getScreenWidth()/1.2){
                scroller.startScroll((int)uppoint.x,(int)uppoint.y,(int)(getScreenWidth()-uppoint.x),(int)(ScreenTool.getScreenHeight(getContext())-uppoint.y),500);
                handler.sendEmptyMessageDelayed(3,500);
            }else {
                handler.sendEmptyMessageDelayed(2,500);
                scroller.startScroll((int)uppoint.x,(int)uppoint.y, (int) -uppoint.x,(int)(ScreenTool.getScreenHeight(getContext())-uppoint.y),500);
            }
        }else if(firstderection== DeRectionHelp.Derection.RIGHT){
            if(uppoint.x>getScreenWidth()/2){
                scroller.startScroll((int)uppoint.x,(int)uppoint.y,(int)(getScreenWidth()-uppoint.x),(int)(ScreenTool.getScreenHeight(getContext())-uppoint.y),500);
                handler.sendEmptyMessageDelayed(1,500);
            }else {
                scroller.startScroll((int)uppoint.x,(int)uppoint.y, (int) -uppoint.x,(int)(ScreenTool.getScreenHeight(getContext())-uppoint.y),500);
                handler.sendEmptyMessageDelayed(3,500);
            }
        }else {
            firstderection= DeRectionHelp.Derection.NONE;
        }
    }
    public boolean isHaveRightPage(){
        for (int i = 0; i < currrentBooks.size(); i++) {
            if(currrentBooks.get(i).getBooktype()== Book.BOOKTYPE.NEXT){
                return true;
            }
        }
        return false;
    }
    public interface PageOPER{
     void  prePage();
     void nextPage();
     void reflush();
    }
    public int getScreenWidth(){
        return ScreenTool.getScreenWidth(getContext());
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    pageOPER.prePage();
                    break;
                case 2:
                    pageOPER.nextPage();
                    break;
                case 3:
                    pageOPER.reflush();
                    break;
            }
        }
    };
}
