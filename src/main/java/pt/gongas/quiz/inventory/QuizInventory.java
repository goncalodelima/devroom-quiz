package pt.gongas.quiz.inventory;

import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.category.interfaces.Category;
import pt.gongas.quiz.user.User;
import pt.gongas.quiz.utils.ItemBuilder;

import java.util.Objects;


public class QuizInventory extends View {

    public QuizInventory() {
        super(QuizPlugin.getInstance().getInventories().getInt("quiz-inventory.size"), QuizPlugin.getInstance().getInventories().getString("quiz-inventory.title"));
        setCancelOnClick(true);
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        Player player = context.getPlayer();
        User user = QuizPlugin.getInstance().getUserManager().getUser(player.getName());

        int slot = 13;
        for (Category category : user.categories()) {

            context.slot(slot, new ItemBuilder(Material.PLAYER_HEAD, 1, (short) 3)
                    .setSkull(category.getScore() == 10 ? "http://textures.minecraft.net/texture/92c2f6fa7ec530435c431572938b9feb959c42298e5554340263c65271" : "http://textures.minecraft.net/texture/a3826181ce9012b665865f3ac0066307b4d02da281540104e0461ffefa7459fd")
                    .name(category.getScore() == 10 ? "§c" : "§a" + Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("quiz-inventory.category.name")).replace("&", "§").replace("%category_identifier%", category.getIdentifier()))
                    .setLore(
                            "§fScore: §7" + category.getScore() + "/10",
                            "",
                            category.getScore() == 10 ? Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("maximum-score")).replace("&", "§") : Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("click-to-play")).replace("&", "§")
                    ).build()).onClick(click -> {

                //max score
                if (category.getScore() == 10) {
                    close();
                    player.sendMessage(Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("maximum-score")).replace("&", "§"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }

                //quiz started
                close();

                category.generateQuestion();
                QuizPlugin.getInstance().getLang().getStringList("quiz-started").forEach(message -> player.sendMessage(message.replace("&", "§").replace("%question%", category.getQuestion())));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                QuizPlugin.getInstance().getQuiz().put(user, category);
            });

            slot++;
        }

    }

}
