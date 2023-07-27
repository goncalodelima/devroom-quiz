package pt.gongas.quiz.category;

import pt.gongas.quiz.category.interfaces.Category;

public class FootballCategory extends Category {

    public FootballCategory(int punctuation){
        setName("Football Category");
        setPunctuation(punctuation);
    }

    @Override
    public String getName() {
        return "Football Category";
    }

}
