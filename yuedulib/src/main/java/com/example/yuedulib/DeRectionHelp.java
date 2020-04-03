package com.example.yuedulib;

public class   DeRectionHelp {

    enum Derection{
        LEFT,UP,DOWN,RIGHT,LEFTUP,LEFTDOWN,RIGHTUP,RIGHTDOWN,NONE;
    }
    enum PAGETYPE{
        COVER,BOOKCOVER
    }
   static int  mindis = 20;
    DeRectionHelp() {
    }
    public static Derection judgeDerection(MyPoint startpoint, MyPoint endpoint){
        float startx = startpoint.x;
       float starty = startpoint.y;
       float endx = endpoint.x;
       float endy = endpoint.y;
     float disx = Math.abs(startx-endx);//x轴上的滑动距离
     float disy = Math.abs(starty-endy);//y轴上的滑动距离
        if(disx<mindis&disy>mindis&starty>endy){
          return Derection.UP;
        }else if(disx<mindis&disy>mindis&starty<endy){
            return Derection.DOWN;
        }else if(disy<mindis&disx>mindis&startx>endx){
            return Derection.LEFT;
        }else if(disy<mindis&disx>mindis&startx<endx){
            return Derection.RIGHT;
        }else if(disx>mindis&disy>mindis&startx>endx&starty>endy){
            return Derection.LEFTUP;
        }else if(disx>mindis&disy>mindis&startx<endx&starty>endy){
            return Derection.RIGHTUP;
        }else if (disx>mindis&disy>mindis&startx>endx&starty<endy){
            return Derection.LEFTDOWN;
        }else if(disx>mindis&disy>mindis&startx<endx&starty<endy){
            return Derection.RIGHTDOWN;
        }else {
            return Derection.NONE;
        }
    }
    public static boolean isLeft(Derection derection){
        if(derection== Derection.LEFT||derection== Derection.LEFTUP||derection== Derection.LEFTDOWN){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isLeft(MyPoint startpoint,MyPoint endpoint){
        Derection derection = judgeDerection(startpoint,endpoint);
        if(derection== Derection.LEFT||derection== Derection.LEFTUP||derection== Derection.LEFTDOWN){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isRight(Derection derection){
        if(derection== Derection.RIGHT||derection== Derection.RIGHTUP||derection== Derection.RIGHTDOWN){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isRight(MyPoint startpoint,MyPoint endpoint){
        Derection derection = judgeDerection(startpoint,endpoint);
        if(derection== Derection.RIGHT||derection== Derection.RIGHTUP||derection== Derection.RIGHTDOWN){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isUp(Derection derection){
        if(derection== Derection.UP||derection== Derection.LEFTUP||derection== Derection.RIGHTUP){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isDOWN(Derection derection){
        if(derection== Derection.DOWN||derection== Derection.LEFTDOWN||derection== Derection.RIGHTDOWN){
            return true;
        }else {
            return false;
        }
    }
    public static MyPoint getdis(MyPoint startpoint, MyPoint endpoint){
        MyPoint myPoint = new MyPoint();
        float disx = Math.abs(startpoint.x-endpoint.x);//x轴上的滑动距离
        float disy = Math.abs(startpoint.y-endpoint.y);//y轴上的滑动距离
        myPoint.x = disx;
        myPoint.y = disy;
        return  myPoint;
    }
}
