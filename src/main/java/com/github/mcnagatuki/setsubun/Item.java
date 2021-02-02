package com.github.mcnagatuki.setsubun;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;


public class Item {
    public static class Mame extends ItemType{
        protected static String key = "mame";
        protected static String name = "豆";
        protected static List<String> lore = Arrays.asList("鬼は外！福は内！");
        protected static ItemStack itemStack = new ItemStack(Material.SNOWBALL);
        protected static NamespacedKey namespacedKey = new NamespacedKey(Setsubun.plugin, key);

        public static ItemStack toItemStack() {
            ItemStack ret = itemStack;

            ItemMeta itemMeta = ret.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);

            ret.setItemMeta(itemMeta);
            return ret;
        }

        public static ItemStack toItemStack(int amount) {
            ItemStack ret = toItemStack();
            ret.setAmount(amount);
            return itemStack;
        }
    }

    public static class Kanabo extends ItemType{
        protected static String key = "kanabo";
        protected static String name = "金棒";
        protected static List<String> lore = Arrays.asList("鬼に金棒！ブリに大根！");
        protected static ItemStack itemStack = new ItemStack(Material.STONE_SWORD);
        protected static NamespacedKey namespacedKey = new NamespacedKey(Setsubun.plugin, key);

        public static ItemStack toItemStack() {
            ItemStack ret = itemStack;

            ItemMeta itemMeta = ret.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);

            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 999, true);
            itemMeta.setUnbreakable(true);

            ret.setItemMeta(itemMeta);
            return ret;
        }

        public static ItemStack toItemStack(int amount) {
            ItemStack ret = toItemStack();
            ret.setAmount(amount);
            return itemStack;
        }
    }

    public static class ItemType {
        protected static String key;
        protected static String name;
        protected static List<String> lore;
        protected static ItemStack itemStack;
        protected static NamespacedKey namespacedKey;

        public static ItemStack toItemStack() {
            ItemStack ret = itemStack;
            ItemMeta itemMeta = ret.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
            ret.setItemMeta(itemMeta);
            return ret;
        }

        public static ItemStack toItemStack(int amount) {
            ItemStack ret = toItemStack();
            ret.setAmount(amount);
            return itemStack;
        }

        public Boolean equal(ItemStack target) {
            String targetKey = target.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            return key == targetKey;
        }
    }
}