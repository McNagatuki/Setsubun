package com.github.mcnagatuki.setsubun;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    public Config config;


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        config = new Config();
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
        if (!(source instanceof Player)) return;

        Player shooter = (Player) source;

        ItemStack thrown = shooter.getInventory().getItemInMainHand();
        if (!Item.Mame.equal(thrown)) return;

        // 雪玉を豆に
        Item.Mame.setMetadataToEntity(mame);
    }

    // 雪玉が当たったときのイベント処理
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

        // あたったのが鬼だった場合
        if (oni.contains(hitPlayer)) {
            // 鈍足のポ―ション効果を付与
            PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, config.oniPotionTime, config.oniPotionAmp);
            hitPlayer.addPotionEffect(effect);
            return;
        }

        // あたったのがプレイヤーだった場合
        // 俊足のポーション効果を付与
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, config.playerPotionTime, config.playerPotionAmp);
        hitPlayer.addPotionEffect(effect);
    }

    // ゲーム開始！
    public void start() {
        if (running) return;

        // 金棒と豆の自動配布
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (oni.contains(p)) {
                p.getInventory().addItem(Item.Kanabo.toItemStack());

                // 鬼をちょっとだけはやく
                p.setWalkSpeed(config.oniSpeed);

            } else {
                p.getInventory().addItem(Item.Mame.toItemStack(config.mameAmount));
            }
        }

        getServer().broadcastMessage("豆まき開始！");

        running = true;
    }

    // ゲーム終了！
    public void stop() {
        // TODO: 緑文字で
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(new Title("ゲーム終了", null, 5, 80, 5));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 0.9f);

            // スピードを元に戻す
            p.setWalkSpeed(0.2f);
        }

        if (task != null) {
            getServer().getScheduler().cancelTask(task.getTaskId());
            task = null;
        }

        running = false;
    }

    public class Config {
        // changeable params
        public int mameAmount = 256;
        public float oniSpeed = 0.3f;
        public int oniPotionAmp = 3;
        public int oniPotionTime = 60;
        public int playerPotionAmp = 1;
        public int playerPotionTime = 40;
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
