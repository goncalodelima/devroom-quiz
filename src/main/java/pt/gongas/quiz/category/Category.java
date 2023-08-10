package pt.gongas.quiz.category;

import lombok.Data;
import pt.gongas.quiz.QuizPlugin;

import java.util.List;
import java.util.Random;

@Data
public abstract class Category {

    private String identifier;
    private int score;
    private String question;
    private String answer;

    public void addScore(int score) { setScore(getScore() + score); }

    public boolean checkAnswer(String quizAnswer) { return quizAnswer.equalsIgnoreCase(getAnswer()); }

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

}
