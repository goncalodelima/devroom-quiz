package pt.gongas.quiz.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.category.impl.Football;
import pt.gongas.quiz.category.impl.Geography;
import pt.gongas.quiz.category.impl.History;
import pt.gongas.quiz.category.Category;
import pt.gongas.quiz.user.User;

import java.util.Arrays;
import java.util.Objects;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        User user = QuizPlugin.getInstance().getUserManager().getUser(player.getName());

        if (user == null)
            new User(player.getName(), Arrays.asList(new Football(0), new Geography(0), new History(0)));

    }

    @EventHandler
    void onAsyncPlayerChat(AsyncPlayerChatEvent event){

        Player player = event.getPlayer();
        User user = QuizPlugin.getInstance().getUserManager().loadUser(player);

        if (!QuizPlugin.getInstance().getQuiz().containsKey(user)) return;
        event.setCancelled(true);

        String quizAnswer;
        switch (event.getMessage()) {
            case "true" -> quizAnswer = "true";
            case "false" -> quizAnswer = "false";
            default -> {
                player.sendMessage(Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("invalid-value")).replace("&", "§"));
                return;
            }
        }

        Category category = QuizPlugin.getInstance().quiz.get(user);
        if (category.checkAnswer(quizAnswer)) {
            category.addScore(1);
            player.sendMessage(Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString(category.getScore() < 9 ? "right-question" : "quiz-finished")).replace("&", "§"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            //The player finished the quiz with the maximum score
            if (category.getScore() >= 9) {
                QuizPlugin.getInstance().getQuiz().remove(user);
                return;
            }

            category.generateQuestion(); //generates a new question and the quiz goes to the next question
            QuizPlugin.getInstance().getLang().getStringList("quiz-question").forEach(message -> player.sendMessage(message.replace("&", "§").replace("%question%", category.getQuestion())));
            return;
        }

        player.sendMessage(Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("wrong-question")).replace("&", "§").replace("%score%", String.valueOf(category.getScore())));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        QuizPlugin.getInstance().getQuiz().remove(user); //The player finished the quiz, but with a score of less than 10

    }

}
