package com.example.boleyuedu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yuedulib.Book;
import com.example.yuedulib.DisplayUtil;
import com.example.yuedulib.DrawView;
import com.example.yuedulib.GetJsonDataUtil;
import com.example.yuedulib.ScreenTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements DrawView.PageOPER {

    private Paint paint;
    private int lianpand = 20;
    private int textSize = 50;
    private String content;
    private int size;
    private int dwidth;
    private int showline = 16;
    private int currentpage = 2;
    private int totalpage= 0;
    private int dheight;
    private int marginLeft= 60;
    private int marginright= 60;
    private int marginTop = 80;
    private int marginBotton = 0;
    private DrawView drawView;
    private ArrayList<Book> currentbooks;
    private ArrayList<String> undeldata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.imgv);
        drawView.setPageOPER(MainActivity.this);
        dwidth = ScreenTool.getScreenWidth(this);
        dheight = ScreenTool.getScreenHeight(this);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(textSize);
        DisplayUtil.getScreenRelatedInformation(MainActivity.this);
        DisplayUtil.getRealScreenRelatedInformation(MainActivity.this);

        size = paint.breakText("画布安徽科技手动阀画布安徽科技手动，画布安徽科,技手动阀画布布布布布布，，，，，，，，", true, dwidth-marginLeft-marginright, null);



        Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                content = new GetJsonDataUtil().getJson(MainActivity.this,"abcs.txt");
                undeldata = (ArrayList<String>) Dealdata();
                totalpage = undeldata.size() % showline == 0 ? undeldata.size() / showline : (undeldata.size() / showline) + 1;
                currentbooks = deldate(undeldata);
                drawPages(currentpage, currentbooks);
                emitter.onNext(content);
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("","");
            }
        });


    }
    public List<String> Dealdata (){
        ArrayList<String> strings = new ArrayList<>();

        int startindex = 0;
        content = content.replaceAll(" ","");
        content = content.replaceAll("\"","  ");
        while (startindex<content.length()){
            int endindex =startindex+size;
            if(startindex+size>content.length()){
                endindex = content.length();
            }
            String da = content.substring(startindex,endindex);
            if(!da.contains("\n")){
                strings.add(da);
                startindex+=size;
            }else {
                int datas= da.indexOf("\n");
                strings.add(da.substring(0,datas));
                startindex+=(datas+1);
            }
        }
        return strings;
    }
    public ArrayList<Book> deldate(ArrayList<String> undeldata){
        ArrayList<Book> bookArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if(currentpage==1&i==0){
                continue;
            }
            if(currentpage==totalpage&i==2){
                continue;
            }
            int cprepage = currentpage-2+i;//第一页往后面画
            Book book = new Book();
            if(i==0){
                book.setBooktype(Book.BOOKTYPE.PRE);
            }else if(i==1){
                book.setBooktype(Book.BOOKTYPE.CUR);
            }else if(i==2){
                book.setBooktype(Book.BOOKTYPE.NEXT);
            }
            List<String> sContent = new ArrayList<>();
            book.setTitle("abc");
            Bitmap b1 = Bitmap.createBitmap(dwidth, dheight, Bitmap.Config.ARGB_8888);
            Canvas c1 = new Canvas(b1);
            paint = new Paint();
            paint.setColor(Color.RED);
            c1.drawColor(this.getResources().getColor(R.color.grayyellow));
            paint.setTextSize(textSize);
            for (int j = cprepage*showline; j < (cprepage+1)*showline; j++) {
                if(j<undeldata.size()){
                    int textWidth = (int) paint.measureText(undeldata.get(j));
                    if(undeldata.get(j).startsWith("  ")){
                        c1.drawText(undeldata.get(j),dwidth-textWidth-marginright,(lianpand+textSize)*(j%showline)+textSize+marginTop, paint);
                    }else {
                        c1.drawText(undeldata.get(j),marginLeft,(lianpand+textSize)*(j%showline)+textSize+marginTop, paint);
                    }
                    sContent.add(undeldata.get(j));
                }
            }
            book.setStrings(sContent);
            book.setBitmap(b1);
            bookArrayList.add(book);
        }
        return bookArrayList;
    }
    public void drawPages(int currentpage,ArrayList<Book> arrayList){
        Collections.reverse(arrayList);
        int id = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).getBooktype()== Book.BOOKTYPE.CUR){
                id= i ;
            }
        }
        List<String> contents = arrayList .get(id).getStrings();
        Book coverbook = new Book();
        coverbook.setBooktype(Book.BOOKTYPE.COVERCUR);
        Bitmap b1 = Bitmap.createBitmap(dwidth, dheight, Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(b1);
        paint = new Paint();
        paint.setColor(this.getResources().getColor(R.color.lightred));
        c1.drawColor(this.getResources().getColor(R.color.debg));
        paint.setTextSize(textSize);
        for (int i = 0; i <contents.size() ; i++) {
            int textWidth = (int) paint.measureText(contents.get(i));
            if(contents.get(i).startsWith("  ")){
                c1.drawText(contents.get(i),dwidth-textWidth-marginright,(lianpand+textSize)*(i%showline)+textSize+marginTop, paint);
            }else {
                c1.drawText(contents.get(i),marginLeft,(lianpand+textSize)*(i%showline)+textSize+marginTop, paint);
            }
        }
        Bitmap coverBitmap = horverImage(b1,true,false);
        coverbook.setBitmap(coverBitmap);
        arrayList.add(coverbook);
        drawView.setCurrentBook(arrayList);//当前页
    }

    @Override
    public void prePage() {
        if(currentpage!=1){
            currentpage--;
        }else {
            Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();
        }
        currentbooks = deldate(undeldata);
        drawPages(currentpage, currentbooks);
    }

    @Override
    public void nextPage() {
        if(currentpage!=totalpage){
            currentpage++;
        }else {
            Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
        }
        currentbooks = deldate(undeldata);
        drawPages(currentpage, currentbooks);
    }
    public Bitmap horverImage(Bitmap bitmap, boolean H, boolean V)
    {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();

        if (H)
            matrix.postScale(-1, 1);   //水平翻转H

        if (V)
            matrix.postScale(1, -1);   //垂直翻转V

        if (H && V)
            matrix.postScale(-1, -1);   //水平&垂直翻转HV

        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);

        //matrix.postRotate(-90);  //旋转-90度
    }
}
