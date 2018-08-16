package com.example.android.a201808_animationdrawable;

import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView m_img_duke;
    private AnimationDrawable m_animationDrawable;  // 動畫物件
    private TextView m_tv_message;

    private View m_view_logo;
    private TextView m_tv_LogoName;
    private TextView m_tv_message2;

    private TypedArray m_arr_NbaLogos;      // 資源檔 陣列
    private int m_NbaLogosCount;        // 一共有多少張圖

    private Button m_btn_go;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initFrameAnimation();
        initNbaLogos();
    }

    private void init() {
        m_img_duke = (ImageView) findViewById(R.id.img_duke);
        m_tv_message = (TextView) findViewById(R.id.tv_message);

        m_view_logo = (View) findViewById(R.id.view_logo);
        m_tv_LogoName = (TextView) findViewById(R.id.tv_LogoName);
        m_tv_message2 = (TextView) findViewById(R.id.tv_message2);

        m_btn_go = (Button)findViewById(R.id.btn_go);
    }

    private void initFrameAnimation() {
        m_img_duke.setBackgroundResource(R.drawable.frame_animation);   //設定動畫資源
        m_animationDrawable = (AnimationDrawable) m_img_duke.getBackground();   //取得 ImageView 背景

    }

    private void initNbaLogos(){
        m_arr_NbaLogos = getResources().obtainTypedArray(R.array.arr_NbaLogos);     // 取得圖片陣列
        m_NbaLogosCount = m_arr_NbaLogos.length();
        m_view_logo.setBackground(m_arr_NbaLogos.getDrawable(0));       // 取得陣列第0項的圖，設定給 View 當背景

    }






    public void click (View view){
        switch(view.getId()){
            case R.id.btn_start:
                m_animationDrawable.start();
                break;
            case R.id.btn_stop:
                m_animationDrawable.stop();
                break;
            case R.id.btn_5secs:
                animation5secs();
                break;
        }
    }

    // Handler 處理待辦事項
    private Handler m_Handler = new Handler();

    private void animation5secs(){
        int delayTime = 5 * 1000; //5秒
        //建立工作
        Runnable task = new Task();
        // 交付工作, 並在指定時間執行
        boolean result = m_Handler.postDelayed(task,delayTime);

        m_tv_message.setText(result? "交付成功" : "交付失敗");
        m_animationDrawable.start();
    }

    private class Task implements Runnable{
        @Override
        public void run(){
            m_animationDrawable.stop();
            m_tv_message.setText("時間到");
        }
    }






    // 建立任務物件
    private Runnable m_Start= new Start();
    private Runnable m_Stop = new Stop();

    // 任務開始 隨機換圖
    private class Start implements Runnable{
        @Override
        public void run(){
            // 隨機產生 0~ 圖數量-1
            int index = (int)(Math.random()* m_NbaLogosCount);
            // 換圖
            m_view_logo.setBackground(m_arr_NbaLogos.getDrawable(index));
            // 0.1秒後再執行一次
            m_Handler.postDelayed(this, 100); // this 是 Start 物件自己
        }
    }

    // 任務停止
    private class Stop implements Runnable {
        @Override
        public void run(){
            m_Handler.removeCallbacks(m_Start);
            m_btn_go.setEnabled(true);
        }
    }

    // 按下GO
    public void clickgo (View view){
        m_Handler.post(m_Start);
        m_btn_go.setEnabled(false);
        m_Handler.postDelayed(m_Stop,3000);
    }
}
