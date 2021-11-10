package com.example.cargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //elements
    ImageView[] Cars;
    ImageButton arrowL, arrowR;
    ImageView[] hearts ;
    ImageView[][] obstacles;
    ImageView[] boom;
    private int numberOfLife=2;

    //position
    boolean RIGHT,LEFT,CENTER;

    //Timer
    private  static final int DELAY=1000;
    private Timer timer;
    private int clock=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MoveCar();
    }

    private void startTicker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("timeTick","Time: "+clock+"On Thread: "+Thread.currentThread().getName());
                runOnUiThread(() -> {
                    Log.d("timeTick","Time: "+clock+"On Thread: "+Thread.currentThread().getName());
                    moveObstacles();
                });
            }
        },0, DELAY);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }




    public void moveObstacles(){
        clock++;
        hideBooms();
        for(int i=0;i<=2;i++){
            if (obstacles[i][3].getVisibility()==View.VISIBLE) {
                obstacles[i][3].setVisibility(View.GONE);
            }
            for (int j = 2; j >= 0; j--) {
                if (obstacles[i][j].getVisibility() == View.VISIBLE) {
                    obstacles[i][j].setVisibility(View.INVISIBLE);
                    obstacles[i][j + 1].setVisibility(View.VISIBLE);
                }
            }
        }
        if(clock%2==0){
            int random=(int) ((Math.random()*3)+1);
            switch(random){
                case 1:{
                    obstacles[0][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 2:{
                    obstacles[1][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 3:{
                    obstacles[2][0].setVisibility(View.VISIBLE);
                    break;
                }

            }
        }
        checkHit();
    }
    public  void hideBooms(){

        for (int i=0;i<3;i++){
            boom[i].setVisibility(View.GONE);
        }
        if(LEFT==true&&Cars[0].getVisibility()==View.GONE){
            Cars[0].setVisibility(View.VISIBLE);
        }else if(CENTER==true&&Cars[1].getVisibility()==View.GONE){
            Cars[1].setVisibility(View.VISIBLE);
        }else if(RIGHT==true&&Cars[2].getVisibility()==View.GONE){
            Cars[2].setVisibility(View.VISIBLE);
        }
    }


    public void initView() {
        hearts = new ImageView[]{
                findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)
        };
        Cars = new ImageView[]{
                findViewById(R.id.LeftCar), findViewById(R.id.CenterCar), findViewById(R.id.RightCar)
        };
        obstacles = new ImageView[][]{
                // 0 RightObstacles   0              1                                 2                                 3
                {findViewById(R.id.rightObstacle1),findViewById(R.id.rightObstacle2),findViewById(R.id.rightObstacle3),findViewById(R.id.rightObstacle4)},
                // 1 CenterObstacles  0              1                                 2                                 3
                {findViewById(R.id.centerObstacle1),findViewById(R.id.centerObstacle2),findViewById(R.id.centerObstacle3),findViewById(R.id.centerObstacle4)},
                // 2 LeftObstacles    0              1                                 2                                 3
                {findViewById(R.id.leftObstacle1), findViewById(R.id.leftObstacle2), findViewById(R.id.leftObstacle3),findViewById(R.id.leftObstacle4)}
        };
        arrowR=findViewById(R.id.ArrowR);
        arrowL=findViewById(R.id.ArrowL);
        boom=new ImageView[]{
                findViewById(R.id.Boom1),findViewById(R.id.Boom2),findViewById(R.id.Boom3)
        };
        checkWhereIsCar();
    }

    public void MoveCar() {

        arrowL.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            if (RIGHT == true) {
                Cars[1].setVisibility(View.VISIBLE);
                Cars[2].setVisibility(View.INVISIBLE);
                CENTER = true;
                RIGHT=false;

            } else if ( CENTER == true) {
                Cars[1].setVisibility(View.INVISIBLE);
                Cars[0].setVisibility(View.VISIBLE);
                CENTER=false;
                LEFT=true;
            }

        });
        arrowR.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            if (CENTER == true) {
                Cars[1].setVisibility(View.INVISIBLE);
                Cars[2].setVisibility(View.VISIBLE);
                CENTER = false;
                RIGHT=true;
            } else if ( LEFT == true) {
                Cars[1].setVisibility(View.VISIBLE);
                Cars[0].setVisibility(View.INVISIBLE);
                CENTER=true;
                LEFT=false;
            }
        });
    }


    void restartGame(){
        numberOfLife=2;
            for (int i=0;i<3;i++){
                hearts[i].setVisibility(View.VISIBLE);
            }
        }


    void checkHit() {
        if (numberOfLife<0){
            Toast.makeText(this,"Restarting Game!",Toast.LENGTH_SHORT).show();
            restartGame();
        }
        if (obstacles[2][3].getVisibility() == View.VISIBLE&&Cars[0].getVisibility()==View.VISIBLE) {
            Cars[0].setVisibility(View.GONE);
            obstacles[2][3].setVisibility(View.GONE);
            boom[0].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[1][3].getVisibility() == View.VISIBLE&&Cars[1].getVisibility()==View.VISIBLE) {
            Cars[1].setVisibility(View.GONE);
            obstacles[1][3].setVisibility(View.GONE);
            boom[1].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[0][3].getVisibility() == View.VISIBLE&&Cars[2].getVisibility()==View.VISIBLE) {
            Cars[2].setVisibility(View.GONE);
            obstacles[0][3].setVisibility(View.GONE);
            boom[2].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        }


    }
    void toast(){

        switch (numberOfLife){
            case  0:  Toast.makeText(this,"Be careful one life left",Toast.LENGTH_SHORT).show();
                break;
            case  1:  Toast.makeText(this,"Ohhh one life has gone",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void vibrate() {
        Vibrator v;
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }


    void checkWhereIsCar() {
        if (Cars[0].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            LEFT = true;
        }
        if (Cars[1].getVisibility() == View.VISIBLE&&Cars[0].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            CENTER = true;
        }
        if (Cars[2].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[0].getVisibility() == View.INVISIBLE) {
            RIGHT = true;
        }
    }








}






