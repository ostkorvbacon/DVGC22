package com.example.test3.DatabaseHandler;

public class Questionnaire {
    private String username;
    private boolean[] questionAnswers;
    private boolean approved;

    public Questionnaire(String username, boolean[] questionAnswers, boolean approved){
        this.approved = approved;
        this.questionAnswers = questionAnswers;
        this.username = username;
    }

    public Questionnaire(String username, boolean[] questionAnswers){
        this.approved = false;
        this.questionAnswers = questionAnswers;
        this.username = username;
    }

    @Override
    public String toString(){
        return "Username: " + username +
                "\nApproved: " + approved +
                questBoolsToString();
    }

    private String questBoolsToString(){
        String ret = "";
        for (boolean q : questionAnswers){
            ret += "\nAnswer to question 1: " + q;
        }
        return ret;
    }

    public String getUsername() {
        return username;
    }

    public boolean[] getQuestionAnswers() {
        return questionAnswers;
    }

    public boolean isApproved() {
        return approved;
    }

}
