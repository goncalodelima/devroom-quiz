package pt.gongas.quiz.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material type , int amount, short data){
        this(new ItemStack(type , amount, data));
    }

    public ItemBuilder setSkull(String url){

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Notch");
        byte[] arrayOfByte = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        gameProfile.getProperties().put("textures", new Property("textures", new String(arrayOfByte)));
        Field field;
        try {
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        item.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder changeItemMeta(Consumer<ItemMeta> consumer){
        ItemMeta itemMeta = item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
        return this;
    }
    public ItemBuilder name(String name){
        return changeItemMeta(it -> it.setDisplayName(name.replace("&", "§")));
    }

    public ItemBuilder setLore(String... lore){
        return changeItemMeta(it -> it.setLore(Arrays.asList(lore)));
    }

    public ItemStack build(){
        return item;
    }

}
