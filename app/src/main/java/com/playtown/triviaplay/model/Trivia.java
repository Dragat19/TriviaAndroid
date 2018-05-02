package com.playtown.triviaplay.model;

import java.util.ArrayList;

/**
 * Created by albertsanchez on 25/10/17.
 */

public class Trivia {
    private String pregunta;
    private String respuestaA;
    private String respuestaB;
    private String correcta;
    private boolean creditAlreadyGiven;



    public Trivia() {

    }

    public Trivia(String pregunta, String respuestaA, String respuestaB, String correcta) {
        this.pregunta = pregunta;
        this.respuestaA = respuestaA;
        this.respuestaB = respuestaB;
        this.correcta = correcta;
        this.creditAlreadyGiven = false;

    }

    public String getPregunta() {
        return pregunta;
    }


    public String getRespuestaA() {
        return respuestaA;
    }


    public String getRespuestaB() {
        return respuestaB;
    }


    public String getCorrecta() {
        return correcta;
    }


    public boolean isCreditAlreadyGiven() {
        return creditAlreadyGiven;
    }

    public void setCreditAlreadyGiven(boolean creditAlreadyGiven) {
        this.creditAlreadyGiven = creditAlreadyGiven;
    }


    public boolean isCorrectAnswer(String selectedAnswer){
        return (selectedAnswer.equals(correcta));
    }

    public String toString(){
        return pregunta;
    }


}
