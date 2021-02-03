package com.github.mcnagatuki.setsubun;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class Item {
    public static _Mame Mame = new _Mame();
    public static _Kanabo Kanabo = new _Kanabo();

    public static class _Mame extends ItemType {
        public _Mame() {
            super("mame", "豆", Arrays.asList("鬼は外！福は内！"), new ItemStack(Material.SNOWBALL));
        }
    }

    public static class _Kanabo extends ItemType {
        public _Kanabo() {
            super("kanabo", "金棒", Arrays.asList("鬼に金棒！ブリに大根！"), new ItemStack(Material.STONE_SWORD));
        }

        public ItemStack toItemStack() {
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
    }

    public static class ItemType {
        protected String key;
        protected String name;
        protected List<String> lore;
        protected ItemStack itemStack;
        protected NamespacedKey namespacedKey;

        public ItemType(String key, String name, List<String> lore, ItemStack itemStack) {
            this.key = key;
            this.name = name;
            this.lore = lore;
            this.itemStack = itemStack;
            this.namespacedKey = new NamespacedKey(Setsubun.plugin, "setsubun");
        }

        public void setMetadataToEntity(Entity target) {
            target.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
        }

        public ItemStack toItemStack() {
            ItemStack ret = itemStack;
            ItemMeta itemMeta = ret.getItemMeta();
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
            ret.setItemMeta(itemMeta);
            return ret;
        }

        public ItemStack toItemStack(int amount) {
            ItemStack ret = toItemStack();
            ret.setAmount(amount);
            return itemStack;
        }

        public Boolean equal(ItemStack target) {
            String targetKey = target.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            return key == targetKey;
        }

        public Boolean equal(Entity target) {
            String targetKey = target.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            return key == targetKey;
        }
    }
}