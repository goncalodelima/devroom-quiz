package pt.gongas.quiz.category;

import pt.gongas.quiz.category.interfaces.Category;

public class HistoryCategory extends Category {

    public HistoryCategory(int punctuation){
        setName("Football Category");
        setPunctuation(punctuation);
    }

    @Override
    public String getName() {
        return "Football Category";
    }

}
