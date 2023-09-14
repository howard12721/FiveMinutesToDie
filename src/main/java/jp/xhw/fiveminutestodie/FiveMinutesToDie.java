package jp.xhw.fiveminutestodie;

import dev.jorel.commandapi.CommandAPIBukkitConfig;
import jp.xhw.fiveminutestodie.config.Settings;
import jp.xhw.fiveminutestodie.player.GamePlayerHandler;
import lombok.Getter;
import net.william278.annotaml.Annotaml;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public final class FiveMinutesToDie extends JavaPlugin {

    @Getter
    private static FiveMinutesToDie plugin;

    @Getter
    private GamePlayerHandler gamePlayerHandler;
    @Getter
    private Settings settings;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        loadConfig();

        this.gamePlayerHandler = new GamePlayerHandler();

        registerCommands();

    }

    @Override
    public void onDisable() {
        this.gamePlayerHandler.disable();
    }

    private void loadConfig() throws RuntimeException {
        try {
            this.settings = Annotaml.create(new File(getDataFolder(), "config.yml"), Settings.class).get();
        } catch (IOException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "コンフィグの読み込みに失敗しました", e);
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        new CommandAPICommand("timebomb")
                .withSubcommand(
                        new CommandAPICommand("enable")
                                .executes(((commandSender, commandArguments) -> {
                                    gamePlayerHandler.enable();
                                }))
                )
                .withSubcommand(
                        new CommandAPICommand("disable")
                                .executes(((commandSender, commandArguments) -> {
                                    gamePlayerHandler.disable();
                                }))
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .executes((commandSender, commandArguments) -> {
                                    gamePlayerHandler.disable();
                                    loadConfig();
                                    gamePlayerHandler.enable();
                                })
                )
                .register();
    }

}
