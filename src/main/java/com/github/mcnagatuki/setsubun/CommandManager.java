package com.github.mcnagatuki.setsubun;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private boolean same(String a, String b) {
        return a.equalsIgnoreCase(b);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0)
            return false;

        // ON on
        if (same(args[0], "on")) {
            Bukkit.dispatchCommand(sender, "team add oni \"鬼\"");
            Bukkit.dispatchCommand(sender, "team modify oni color red");
            Setsubun.plugin.oni.clear();

            return true;
        }

        // OFF off
        if (same(args[0], "off")) {
            Bukkit.dispatchCommand(sender, "team remove oni");
            Setsubun.plugin.oni.clear();

            return true;
        }

        // 鬼の追加 add player
        if (same(args[0], "add") && args.length == 2) {
            // 指定市からプレイヤーを取得
            List<Entity> targets = Bukkit.selectEntities(sender, args[1]);
            for (Entity e : targets) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    Setsubun.plugin.oni.add(p);
                    Bukkit.dispatchCommand(sender, "team join oni " + p.getName());
                    Setsubun.plugin.getServer().broadcastMessage("" + p.getName() + "は鬼だ！");
                }
            }
            return true;
        }

        // 鬼の除去 del player
        if (same(args[0], "del") && args.length == 2) {
            // 指定市からプレイヤーを取得
            List<Entity> targets = Bukkit.selectEntities(sender, args[1]);
            for (Entity e : targets) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    Setsubun.plugin.oni.remove(p);
                    Bukkit.dispatchCommand(sender, "team leave " + p.getName());
                    Setsubun.plugin.getServer().broadcastMessage("" + p.getName() + "は人間だね");
                }
            }
            return true;
        }

        // 全ての鬼のクリア clear
        if (same(args[0], "clear")) {
            Bukkit.dispatchCommand(sender, "team empty oni");
            sender.sendMessage("鬼は殲滅された");
            Setsubun.plugin.oni.clear();
        }

        // スタート start [count]
        if (same(args[0], "start")) {
            Setsubun.plugin.gameMaster = (Player) sender;

            // 終了時刻が指定されている場合はセットする
            if (args.length == 2) {
                try {
                    int time = Integer.parseInt(args[1]);
                    Setsubun.plugin.task = new Setsubun.Schedule(time).runTaskTimer(Setsubun.plugin, 10L, 20L);
                } catch (Exception ignore) {
                    return false;
                }
            }

            Setsubun.plugin.start();
            return true;
        }

        // 終了
        if (same(args[0], "stop")) {
            Setsubun.plugin.stop();
            return true;
        }

        // 豆の数の設定 set mame <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "mame")) {
            try {
                int amount = Integer.parseInt(args[2]);
                Setsubun.plugin.config.mameAmount = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("配布する豆の数を" + args[2] + "に設定しました。");
            return true;
        }

        // 鬼のスピードの設定 set oni_speed <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "oni_speed")) {
            try {
                float amount = Float.parseFloat(args[2]);
                Setsubun.plugin.config.oniSpeed = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("鬼の速さを" + args[2] + "に設定しました。");
            return true;
        }

        // 鬼遅延時間の設定 set oni_potion_time <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "oni_potion_time")) {
            try {
                int amount = Integer.parseInt(args[2]);
                Setsubun.plugin.config.oniPotionTime = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("鬼の低速化持続時間を" + args[2] + "に設定しました。");
            return true;
        }

        // 鬼遅延強度の設定 set oni_potion_amp <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "oni_potion_amp")) {
            try {
                int amount = Integer.parseInt(args[2]);
                Setsubun.plugin.config.oniPotionAmp = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("鬼の低速化強度を" + args[2] + "に設定しました。");
            return true;
        }

        // プレイヤー加速時間の設定 set oni_potion_time <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "player_potion_time")) {
            try {
                int amount = Integer.parseInt(args[2]);
                Setsubun.plugin.config.playerPotionTime = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("プレイヤー加速時間を" + args[2] + "に設定しました。");
            return true;
        }

        // プレイヤー加速強度の設定 set oni_potion_amp <number>
        if (args.length == 3 && same(args[0], "set") && same(args[1], "player_potion_amp")) {
            try {
                int amount = Integer.parseInt(args[2]);
                Setsubun.plugin.config.playerPotionAmp = amount;
            } catch (Exception ignore) {
                return false;
            }
            sender.sendMessage("プレイヤー加速強度を" + args[2] + "に設定しました。");
            return true;
        }

        // 豆の付与 give <Player> mame <number>
        if (args.length == 4 && same(args[0], "give") && same(args[2], "mame")) {
            try {
                int amount = Integer.parseInt(args[3]);

                List<Entity> targets = Bukkit.selectEntities(sender, args[1]);
                for (Entity e : targets) {
                    if (!(e instanceof Player)) continue;
                    Player p = (Player) e;
                    p.getInventory().addItem(Item.Mame.toItemStack(amount));
                    sender.sendMessage(p.getName() + "に豆を" + args[3] + "個付与しました。");
                }

            } catch (Exception ignore) {
                return false;
            }
            return true;
        }

        // 金棒の付与 give <Player> kanabo <number>
        if (args.length == 4 && same(args[0], "give") && same(args[2], "kanabo")) {
            try {
                int amount = Integer.parseInt(args[3]);

                List<Entity> targets = Bukkit.selectEntities(sender, args[1]);
                for (Entity e : targets) {
                    if (!(e instanceof Player)) continue;
                    Player p = (Player) e;
                    p.getInventory().addItem(Item.Kanabo.toItemStack(amount));
                    sender.sendMessage(p.getName() + "に金棒を" + args[3] + "個付与しました。");
                }

            } catch (Exception ignore) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        String[] cmds = {
                "on",
                "off",
                "add",
                "del",
                "clear",
                "start",
                "set",
                "give",
                "stop",
        };

        if (args.length == 1) {
            for (String cmd : cmds) {
                if (cmd.startsWith(args[0])) {
                    result.add(cmd);
                }
            }
        }

        if (args.length == 2 && (same(args[0], "add") || same(args[0], "del"))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                String name = p.getName();
                if(name.startsWith(args[1])) {
                    result.add(p.getName());
                }
            }
        }

        if (args.length == 2 && same(args[0], "start")) {
            result.add("<秒数>");
        }

        if(args.length > 1 && same(args[0], "set")){
            if(args.length == 2){
                if("mame".startsWith(args[1])){
                    result.add("mame");
                }
                if("oni_speed".startsWith(args[1])){
                    result.add("oni_speed");
                }
                if("oni_potion_time".startsWith(args[1])){
                    result.add("oni_potion_time");
                }
                if("oni_potion_amp".startsWith(args[1])){
                    result.add("oni_potion_amp");
                }
                if("player_potion_time".startsWith(args[1])){
                    result.add("player_potion_time");
                }
                if("player_potion_amp".startsWith(args[1])){
                    result.add("player_potion_amp");
                }
            }

            if(args.length == 3){
                if("mame".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.mameAmount));
                }
                if("oni_speed".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.oniSpeed));
                }
                if("oni_potion_time".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.oniPotionTime));
                }
                if("oni_potion_amp".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.oniPotionAmp));
                }
                if("player_potion_time".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.playerPotionTime));
                }
                if("player_potion_amp".startsWith(args[1])){
                    result.add(String.valueOf(Setsubun.plugin.config.playerPotionAmp));
                }
            }
        }

        if(args.length > 1 && same(args[0], "give")){
            if(args.length == 2){
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(!p.getName().startsWith(args[1])) continue;
                    result.add(p.getName());
                }
            }

            if(args.length == 3){
                if("mame".startsWith(args[2])) result.add("mame");
                if("kanabo".startsWith(args[2])) result.add("kanabo");
            }

            if(args.length == 4){
                result.add("1");
                result.add("64");
            }
        }

        return result;
    }
}
