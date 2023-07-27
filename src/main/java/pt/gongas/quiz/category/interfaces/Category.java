package pt.gongas.quiz.category.interfaces;

import java.util.List;

public abstract class Punctuation {

    private String name;
    private int punctuation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPunctuation() { return punctuation; }

    public void setPunctuation(int punctuation) {
        this.punctuation = punctuation;
    }

    public abstract List<String> getDescription();

    public void addPunctuation(int punctuation) { setPunctuation(getPunctuation() + punctuation); }

}
