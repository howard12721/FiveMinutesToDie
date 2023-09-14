package jp.xhw.fiveminutestodie.config;

import lombok.Getter;
import net.william278.annotaml.YamlFile;
import net.william278.annotaml.YamlKey;

@Getter
@YamlFile
public class Settings {

    @YamlKey("timelimit")
    private double timelimit;

    @YamlKey("bossbar-timer.enabled")
    private boolean bossBarEnabled;

    @YamlKey("bossbar-timer.display")
    private String bossBarDisplay;

    @YamlKey("death.explosion-radius")
    private int explosionRadius;

    @YamlKey("death.death-message.enabled")
    private boolean deathMessageEnabled;

    @YamlKey("death.death-message.content")
    private String deathMessage;

    @YamlKey("penalty.sneak")
    private double sneakPenalty;

    @YamlKey("penalty.walk")
    private double walkPenalty;

    @YamlKey("penalty.sprint")
    private double sprintPenalty;

    @YamlKey("penalty.swim")
    private double swimPenalty;

    @YamlKey("penalty.climb")
    private double climbPenalty;

    @YamlKey("penalty.jump")
    private double jumpPenalty;

    @YamlKey("penalty.damage")
    private double damagePenalty;

}
