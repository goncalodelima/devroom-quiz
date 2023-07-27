package pt.gongas.quiz.category;

import pt.gongas.quiz.category.interfaces.Category;

public class Geography extends Category {

    public Geography(int score){
        setIdentifier("Geography");
        setScore(score);
    }

    @Override
    public String getIdentifier() {
        return super.getIdentifier();
    }

    public String getQuestion() { return super.getQuestion(); }

    public String getAnswer() {

        // question not found
        if (getQuestion() == null)
            return null;

        int colonIndex = getQuestion().indexOf(":");
        if (colonIndex != -1) {
            String answer = getQuestion().substring(colonIndex + 1).trim();
            setAnswer(answer);
        }

        // Question in invalid format
        return null;
    }

}
