package jp.xhw.fiveminutestodie.player;

import jp.xhw.fiveminutestodie.FiveMinutesToDie;
import jp.xhw.fiveminutestodie.config.Settings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GamePlayerHandler implements Listener {

    private final Map<UUID, GamePlayer> gamePlayerMap = new HashMap<>();

    private boolean isEnabled = false;
    private BukkitTask task;

    public GamePlayerHandler() {
        final Plugin plugin = FiveMinutesToDie.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void enable() {
        final Plugin plugin = FiveMinutesToDie.getPlugin();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            gamePlayerMap.putIfAbsent(player.getUniqueId(), new GamePlayer(player));
        }

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (GamePlayer gamePlayer : gamePlayerMap.values()) {
                    gamePlayer.decreaseTime(1.0);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
        this.isEnabled = true;
    }

    public void disable() {
        task.cancel();
        gamePlayerMap.values().forEach(GamePlayer::terminate);
        gamePlayerMap.clear();
        this.isEnabled = false;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (!this.isEnabled) return;

        gamePlayerMap.put(event.getPlayer().getUniqueId(), new GamePlayer(event.getPlayer()));
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        if (!this.isEnabled) return;

        final GamePlayer gamePlayer = gamePlayerMap.get(event.getPlayer().getUniqueId());
        if (gamePlayer != null) {
            gamePlayer.terminate();
            gamePlayerMap.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if (!this.isEnabled) return;

        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = gamePlayerMap.get(player.getUniqueId());
        if (gamePlayer == null) return;
        if (event.getTo() == null) return;
        final Settings settings = FiveMinutesToDie.getPlugin().getSettings();
        final double moveDistance = event.getFrom().distance(event.getTo());

        if (player.isSwimming()) {
            gamePlayer.decreaseTime(moveDistance * settings.getSwimPenalty());
        } else if (player.isSneaking()) {
            gamePlayer.decreaseTime(moveDistance * settings.getSneakPenalty());
        } else if (player.isSprinting()) {
            gamePlayer.decreaseTime(moveDistance * settings.getSprintPenalty());
        } else if (player.isClimbing()) {
            gamePlayer.decreaseTime(moveDistance * settings.getClimbPenalty());
        }
    }

    @EventHandler
    public void on(PlayerStatisticIncrementEvent event) {
        if (!this.isEnabled) return;

        if (event.getStatistic().getKey().getKey().equals("jump")) {
            final GamePlayer gamePlayer = gamePlayerMap.get(event.getPlayer().getUniqueId());
            gamePlayer.decreaseTime(FiveMinutesToDie.getPlugin().getSettings().getJumpPenalty());
        }
    }


    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!this.isEnabled) return;

        final Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            final GamePlayer gamePlayer = gamePlayerMap.get(player.getUniqueId());
            if (gamePlayer == null) return;
            gamePlayer.decreaseTime(event.getFinalDamage() * FiveMinutesToDie.getPlugin().getSettings().getDamagePenalty());
        }
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        if (!this.isEnabled) return;

        if (!FiveMinutesToDie.getPlugin().getSettings().isDeathMessageEnabled()) return;

        final GamePlayer gamePlayer = gamePlayerMap.get(event.getEntity().getUniqueId());
        if (gamePlayer == null) return;
        if (gamePlayer.isGameOver()) {
            final String deathMessage = FiveMinutesToDie.getPlugin().getSettings().getDeathMessage();
            if (!deathMessage.equals("")) {
                event.setDeathMessage(deathMessage.replace("%PLAYER%", gamePlayer.getPlayer().getName()));
            }
        }
    }

}
