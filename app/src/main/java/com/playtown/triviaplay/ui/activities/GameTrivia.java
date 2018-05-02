package com.playtown.triviaplay.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playtown.triviaplay.R;
import com.playtown.triviaplay.model.Trivia;
import com.playtown.triviaplay.model.User;
import com.playtown.triviaplay.utils.CountDownTrivia;
import com.playtown.triviaplay.utils.TimerViewTrivia;

import java.util.ArrayList;

/**
 * Created by albertsanchez on 25/10/17.
 */

public class GameTrivia extends AppCompatActivity implements View.OnTouchListener, CountDownTrivia.onListenerCountDown {


    /**
     * View
     **/
    private Button selectA, selectB;
    private DatabaseReference dbUser;
    private TextView tv_question, answer_correct, answer_incorrect, timerTrivia, triviaScore
            , triviaPts1, triviaPts2, triviaPts3, triviaTitle, triviaDescription;
    private ImageView btnCloseTrivia, triviaLogo;
    private LinearLayout triviaScreenInicial, triviaScreen, triviaScreenPts;

    /**
     * Flat
     **/
    private int initQuestionIndex, scoreCorrect, scoreIncorrect, scoreTotal, times;
    private static final int TIMER_LENGTH = 10;

    /**
     * Media & Timmer
     **/
    private CountDownTrivia countDownTrivia;
    private TimerViewTrivia mTimerView;
    private MediaPlayer soundCorrect;
    private Vibrator v;
    private ArrayList<Trivia> preguntas;
    private ArrayList<User> scoreUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();
        listenerView();
    }


    private void initView() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
        DatabaseReference dbQuestions = db.getReference("trivia");
        dbUser = db2.getReference("user");
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        selectA = (Button) findViewById(R.id.trivia_respuesta_a);
        selectB = (Button) findViewById(R.id.trivia_respuesta_b);
        answer_correct = (TextView) findViewById(R.id.trivia_respuesta_correcta);
        timerTrivia = (TextView) findViewById(R.id.trivia_timer);
        triviaScore = (TextView) findViewById(R.id.trivia_score);
        triviaTitle = (TextView) findViewById(R.id.trivia_title);
        triviaPts1 = (TextView) findViewById(R.id.trivia_pts1);
        triviaPts2 = (TextView) findViewById(R.id.trivia_pts2);
        triviaPts3 = (TextView) findViewById(R.id.trivia_pts3);
        triviaDescription = (TextView) findViewById(R.id.trivia_description);
        answer_incorrect = (TextView) findViewById(R.id.trivia_respuesta_incorrectas);
        tv_question = (TextView) findViewById(R.id.trivia_preguntas);
        triviaScreen = (LinearLayout) findViewById(R.id.trivia);
        triviaScreenInicial = (LinearLayout) findViewById(R.id.trivia_screen_inicial);
        triviaScreenPts = (LinearLayout) findViewById(R.id.trivia_pts);
        btnCloseTrivia = (ImageView) findViewById(R.id.img_btn_close_trivia);
        triviaLogo = (ImageView) findViewById(R.id.trivia_logo);
        scoreUser = new ArrayList<>();
        countDownTrivia = new CountDownTrivia(11000, 1000, this);

        Log.d("Firebase DB2", "aquiiiiiii");

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreUser.add(dataSnapshot.getValue(User.class));

                if (scoreUser.get(0).getIndex() == 40) {
                    dbUser.child("index").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d("Error Firebase DB", String.valueOf(databaseError));

            }
        });

        Log.d("Firebase DB", "aquiiiiiii");

        dbQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                preguntas = new ArrayList<>();

                for (DataSnapshot q : dataSnapshot.getChildren()) {
                    preguntas.add(q.getValue(Trivia.class));
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        triviaScreenInicial.setVisibility(View.GONE);
                        btnCloseTrivia.setVisibility(View.VISIBLE);
                        triviaScreen.setVisibility(View.VISIBLE);
//                        initQuestionIndex = (int) ((Math.random() * (preguntas.size())));
                        initQuestionIndex = scoreUser.get(0).getIndex();
                        Log.e("Index Inicial", String.valueOf(initQuestionIndex));
                        times = 0;
                        scoreCorrect = 0;
                        scoreIncorrect = 0;
                        showsQuestion(initQuestionIndex);
                    }
                }, 1000);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error Firebase DB", String.valueOf(databaseError));
            }

        });

    }

    private void listenerView() {
        btnCloseTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTrivia.cancel();
                finish();
            }
        });
        selectA.setOnTouchListener(this);
        selectB.setOnTouchListener(this);
    }


    private void showAnswerCorrect(final boolean correct, Button btn) {
        btn.setTextColor(getResources().getColor(R.color.colorWhite));
        if (correct) {
            soundCorrect.start();
            addPtosByTimer(timerTrivia.getText().toString());
            btn.setBackground(getResources().getDrawable(R.drawable.btn_trivia_correct));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    advance2();
                }
            }, 1000);
        } else {
            v.vibrate(400);
            scoreIncorrect = scoreIncorrect + 1;
            tv_question.setText(R.string.trivia_answer_incorrect);
            tv_question.setTextColor(getResources().getColor(R.color.colorRed));
            btn.setBackground(getResources().getDrawable(R.drawable.btn_trivia_incorrect));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    advance2();
                }
            }, 1000);
        }
    }


    @Override
    public void onCountDownTriviaFinish(String finish) {
        timerTrivia.setText(finish);
        times = times + 1;

        scoreIncorrect = scoreIncorrect + 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                advance2();
            }
        }, 1000);

    }

    @Override
    public void onShowCountDownTrivia(long countDownTrivia) {
        timerTrivia.setText(String.valueOf(countDownTrivia));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        finish();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        countDownTrivia.cancel();
        mTimerView.stop();
        super.onDestroy();
    }

    private void showsQuestion(int currentQuiestionIndex) {
        if (times <= 9) {
            tv_question.setText(preguntas.get(currentQuiestionIndex).getPregunta());
            selectA.setText(preguntas.get(currentQuiestionIndex).getRespuestaA());
            selectB.setText(preguntas.get(currentQuiestionIndex).getRespuestaB());
            triviaScore.setText(String.format(getString(R.string.score_total), scoreUser.get(0).getPuntuacion()));
            selectA.setTextColor(getResources().getColor(R.color.colorGreen));
            selectB.setTextColor(getResources().getColor(R.color.colorGreen));
            soundCorrect = MediaPlayer.create(GameTrivia.this, R.raw.correct);
            mTimerView = (TimerViewTrivia) findViewById(R.id.timer);
            tv_question.setTextColor(Color.parseColor(getString(R.string.color_trivia_txquestions)));
            selectA.setBackground(getResources().getDrawable(R.drawable.btn_trivia_default));
            selectB.setBackground(getResources().getDrawable(R.drawable.btn_trivia_default));
            mTimerView.start(TIMER_LENGTH);
            countDownTrivia.start();
            selectB.setEnabled(true);
            selectA.setEnabled(true);
            answer_correct.setText(String.format(getString(R.string.score_correct), scoreCorrect));
            answer_incorrect.setText(String.format(getString(R.string.score_incorrect), scoreIncorrect));
        } else {
            countDownTrivia.cancel();
            mTimerView.stop();
            triviaScreen.setVisibility(View.GONE);
            triviaScreenInicial.setVisibility(View.VISIBLE);
            showsfinishTrivia();
        }
    }

    private void advance() {
        int previousQuiestionIndex = initQuestionIndex;
        int showsQuestion;
        int newQuiestionIndex = (int) ((Math.random() * (preguntas.size())));

        if (newQuiestionIndex != previousQuiestionIndex) {
            showsQuestion = newQuiestionIndex;
            initQuestionIndex = showsQuestion;
        } else {
            showsQuestion = newQuiestionIndex + 1;
            initQuestionIndex = showsQuestion;
        }

        Log.e("Valor Nuevo", String.valueOf(showsQuestion));
        showsQuestion(showsQuestion);
    }

    private void advance2() {
        initQuestionIndex = initQuestionIndex + 1;
        showsQuestion(initQuestionIndex);
    }

    private boolean answerIsCorrect(String answer) {
        return preguntas.get(initQuestionIndex).isCorrectAnswer(answer);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        String answer;
        int action = motionEvent.getAction();
        switch (view.getId()) {

            case R.id.trivia_respuesta_a:
                if (action == MotionEvent.ACTION_DOWN) {
                    times = times + 1;
                    answer = "A";
                    selectB.setEnabled(false);
                    mTimerView.stop();
                    countDownTrivia.cancel();
                    showAnswerCorrect(answerIsCorrect(answer), selectA);
                }
                if (action == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                break;
            case R.id.trivia_respuesta_b:

                if (action == MotionEvent.ACTION_DOWN) {
                    times = times + 1;
                    answer = "B";
                    mTimerView.stop();
                    selectA.setEnabled(false);
                    countDownTrivia.cancel();
                    showAnswerCorrect(answerIsCorrect(answer), selectB);
                }

                if (action == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showsfinishTrivia() {
        triviaTitle.setText(R.string.trivia_finish_title);
        triviaDescription.setText(R.string.trivia_finish_description);
        triviaLogo.setVisibility(View.GONE);
        triviaScreenPts.setVisibility(View.VISIBLE);
        triviaPts1.setText(String.valueOf(scoreTotal));
        updateScoreUser(scoreTotal);
        triviaPts2.setVisibility(View.VISIBLE);
        triviaPts3.setVisibility(View.VISIBLE);
    }

    private void updateScoreUser(int scoreTotal) {
        dbUser.child("puntuacion").setValue(scoreTotal + scoreUser.get(0).getPuntuacion());
        dbUser.child("index").setValue(initQuestionIndex);
    }

    private void addPtosByTimer(String countDown) {
        scoreCorrect = scoreCorrect + 1;
        int countDownTimer = Integer.valueOf(countDown);
        if (countDownTimer <= 5) {
            scoreTotal = scoreTotal + 1;
        } else {
            scoreTotal = scoreTotal + 2;
        }
    }

}


