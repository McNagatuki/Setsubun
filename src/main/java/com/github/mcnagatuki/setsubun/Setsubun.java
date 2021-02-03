package com.github.mcnagatuki.setsubun;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
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

    // 豆の投射
    @EventHandler
    public void onSnowballShoot(ProjectileLaunchEvent event) {
        // 投げられたのは雪玉か確認
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.SNOWBALL) return;
        if (!(entity instanceof Snowball)) return;
        Snowball mame = (Snowball) entity;

        // 投げられたのが豆かどうか確認
        ProjectileSource source = mame.getShooter();
        if(!(source instanceof Player)) return;

        Player shooter = (Player) source;

        ItemStack thrown = shooter.getInventory().getItemInMainHand();
        if(!Item.Mame.equal(thrown)) return;

        // 雪玉を豆に
        Item.Mame.setMetadataToEntity(mame);
    }

    // 雪玉が当たったときのイベント処理
    // 鬼なら遅く
    // 人なら早く？
    // ダメージは？
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!running) return;

        // who got hit
        Entity hitEntity = event.getEntity();
        if (!(hitEntity instanceof Player)) return;
        Player hitPlayer = (Player) hitEntity;

        // damager
        Entity damagerEntity = event.getDamager();
        if (damagerEntity.getType() != EntityType.SNOWBALL) return;
        if (!Item.Mame.equal(damagerEntity)) return;
        Projectile mame = (Projectile) damagerEntity;

//        // shooter
//        ProjectileSource shooterEntity = mame.getShooter();
//        if(!(shooterEntity instanceof Player)) return;
//        Player shooterPlayer = (Player) shooterEntity;

        hitPlayer.sendMessage("豆が当たった");

        // あたったのが鬼だった場合
        if (oni.contains(hitPlayer.getName())) {
            // 減速処理
            return;
        }

        // あたったのがプレイヤーだった場合
        // 加速処理
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

        // TODO: 鬼をちょっとだけはやく
        running = true;
    }

    // ゲーム終了！
    public void stop() {
        // TODO: 緑文字で
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(new Title("ゲーム終了", null, 5, 80, 5));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.9f);

            // TODO: エンチャントの効果を消す
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
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendActionBar("残り " + String.valueOf(time) + "秒");
            }

            if (time <= 0) {
                Setsubun.plugin.stop();
                return;
            }

            --time;
        }
    }
}
