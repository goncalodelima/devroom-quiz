package pt.gongas.quiz.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.inventory.QuizInventory;

import java.util.Objects;

public class QuizCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Objects.requireNonNull(QuizPlugin.getInstance().getLang().getString("no-command")).replace("&", "§"));
            return true;
        }

        QuizPlugin.getInstance().getViewFrame().open(QuizInventory.class, player);
        return false;
    }

}
