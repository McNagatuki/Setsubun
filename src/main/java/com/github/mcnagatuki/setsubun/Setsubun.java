package com.github.mcnagatuki.setsubun;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class Setsubun extends JavaPlugin implements Listener {
    public static Setsubun plugin;
    private boolean running = false;
    public BukkitTask task = null;
    public List<Player> oni = new ArrayList<>();
    public Player gameMaster;
    public int mameAmount = 64;


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("setsubun").setExecutor(new CommandManager());
    }


    // 雪玉が当たったときのイベント処理
    // 鬼なら遅く
    // 人なら早く？
    // ダメージは？
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!running) return;

//        if (event.getEntity() instanceof Player) {
//            boolean pass = false;
//        }


    }

    // ゲーム開始！
    public void start() {
        // 金棒と豆の自動配布
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (oni.contains(p)) {
                p.getInventory().addItem(Item.Kanabo.toItemStack());
            } else {
                p.getInventory().addItem(Item.Mame.toItemStack(mameAmount));
            }
        }

        // NOTE: 鬼をちょっとだけはやく(optional)
        running = true;
    }

    // ゲーム終了！
    public void stop() {
        // TODO: 緑文字で
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(new Title("ゲーム終了", null, 5, 80, 5));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.9f);
        }

        if (task != null) {
            getServer().getScheduler().cancelTask(task.getTaskId());
            task = null;
        }

        running = false;
    }

    public static class Schedule extends BukkitRunnable {
        int time;

        public Schedule(int i) {
            this.time = i;
        }

        @Override
        public void run() {
            if (time <= 0) {
                Setsubun.plugin.stop();
                return;
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendActionBar("残り " + String.valueOf(time) + "秒");
            }

            --time;
        }
    }
}
