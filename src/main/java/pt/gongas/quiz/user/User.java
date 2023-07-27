package pt.gongas.quiz.user;

import lombok.Getter;
import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.category.interfaces.Category;

import java.util.List;

public record User(@Getter String name, @Getter List<Category> categories) {

    public User(String name, List<Category> categories) {
        this.name = name;
        this.categories = categories;
        QuizPlugin.getInstance().getUserManager().addUser(this);
    }

}
