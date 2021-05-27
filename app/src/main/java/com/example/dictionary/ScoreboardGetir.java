package com.example.dictionary;

public class ScoreboardGetir {

    private String mail;
    private String nickname;
    private int score ;


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public ScoreboardGetir(String mail,String nickname,int score){
        this.mail=mail;
        this.nickname=nickname;
        this.score=score;
    }
}
