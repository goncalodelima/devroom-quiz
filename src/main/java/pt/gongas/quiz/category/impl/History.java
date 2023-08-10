package pt.gongas.quiz.category.impl;

import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.category.Category;

import java.util.List;
import java.util.Random;

public class History extends Category {

    public History(int score) {
        setIdentifier("History");
        setScore(score);
    }

    @Override
    public String getIdentifier() {
        return super.getIdentifier();
    }

    public void generateQuestion() {
        List<String> questionsList = QuizPlugin.getInstance().getCategories().getStringList(this.getIdentifier() + ".questions");

        Random random = new Random();
        int randomIndex = random.nextInt(questionsList.size());

        String question = questionsList.get(randomIndex);

        int colonIndex = question.indexOf(":");
        if (colonIndex != -1)
            question = question.substring(0, colonIndex).trim();

        setQuestion(question);
    }

    public String getQuestion() {
        return super.getQuestion();
    }

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
