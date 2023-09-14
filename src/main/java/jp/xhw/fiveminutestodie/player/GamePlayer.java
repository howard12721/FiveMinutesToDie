package jp.xhw.fiveminutestodie.player;

import jp.xhw.fiveminutestodie.FiveMinutesToDie;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class GamePlayer {

    @Getter
    private final Player player;

    private double remainTime;
    private BossBar bossBar;

    public GamePlayer(Player player) {
        this.player = player;
        this.remainTime = FiveMinutesToDie.getPlugin().getSettings().getTimelimit();
        if (FiveMinutesToDie.getPlugin().getSettings().isBossBarEnabled()) {
            this.bossBar = Bukkit.createBossBar(formatTime(FiveMinutesToDie.getPlugin().getSettings().getBossBarDisplay()), BarColor.GREEN, BarStyle.SEGMENTED_6);
            this.bossBar.setProgress(1.0);
            this.bossBar.addPlayer(this.player);
        }
    }

    public void decreaseTime(double amount) {
        if (isGameOver()) {
            return;
        }
        remainTime -= amount;
        updateDisplay();
        if (isGameOver()) {
            final World world = player.getWorld();
            world.createExplosion(player.getLocation(), FiveMinutesToDie.getPlugin().getSettings().getExplosionRadius(), true, true);
            Bukkit.getScheduler().runTask(FiveMinutesToDie.getPlugin(), () -> {
                this.remainTime = FiveMinutesToDie.getPlugin().getSettings().getTimelimit();
                updateDisplay();
            });
        }
    }

    public boolean isGameOver() {
        return remainTime <= 0.0;
    }

    public void updateDisplay() {
        if (!FiveMinutesToDie.getPlugin().getSettings().isBossBarEnabled()) return;
        if (this.bossBar == null) return;
        this.bossBar.setTitle(formatTime(FiveMinutesToDie.getPlugin().getSettings().getBossBarDisplay()));
        this.bossBar.setProgress(Math.max(0.0, remainTime / FiveMinutesToDie.getPlugin().getSettings().getTimelimit()));
    }

    public void terminate() {
        if (this.bossBar != null) this.bossBar.removeAll();
    }

    private String formatTime(String display) {
        return display
                .replace("%MINUTES%", String.valueOf((int) remainTime / 60))
                .replace("%SECONDS%", String.valueOf((int) remainTime % 60));
    }

}
