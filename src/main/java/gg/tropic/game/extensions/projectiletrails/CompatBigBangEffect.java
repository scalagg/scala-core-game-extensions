package gg.tropic.game.extensions.projectiletrails;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class CompatBigBangEffect extends Effect {
    public FireworkEffect.Type fireworkType;
    public Color color2;
    public Color color3;
    public Color fadeColor;
    public int intensity;
    public float radius;
    public int explosions;
    public int soundInterval;
    public Sound sound;
    public float soundVolume;
    public float soundPitch;
    protected int step;
    protected FireworkEffect firework;

    public CompatBigBangEffect(EffectManager effectManager) {
        super(effectManager);
        this.fireworkType = Type.BURST;
        this.color2 = Color.ORANGE;
        this.color3 = Color.BLACK;
        this.fadeColor = Color.BLACK;
        this.intensity = 2;
        this.radius = 2.0F;

        this.explosions = 10;
        this.soundInterval = 10;
        this.sound = Sound.FIREWORK_BLAST;
        this.soundVolume = 1;
        this.soundPitch = 1.0F;
        this.step = 0;
        this.color = Color.RED;
        this.type = EffectType.REPEATING;
        this.period = 2;
        this.iterations = 400;
        this.asynchronous = false;
    }

    public void reset() {
        this.step = 0;
    }

    public void onRun() {
        Location location = this.getLocation();
        if (location != null && location.getWorld() != null) {
            if (this.firework == null) {
                FireworkEffect.Builder b = FireworkEffect.builder().with(this.fireworkType);
                b.withColor(this.color).withColor(this.color2).withColor(this.color3);
                b.withFade(this.fadeColor);
                b.trail(true);
                this.firework = b.build();
            }

            for(int i = 0; i < this.explosions; ++i) {
                Vector v = RandomUtils.getRandomVector().multiply(this.radius);
                this.detonate(location, v);
                if (this.soundInterval != 0 && this.step % this.soundInterval == 0) {
                    location.getWorld().playSound(location, this.sound, this.soundVolume, this.soundPitch);
                }
            }

            ++this.step;
        } else {
            this.cancel();
        }
    }

    protected void detonate(Location location, Vector v) {
        if (location != null && location.getWorld() != null) {
            Firework firework = (Firework)location.getWorld().spawnEntity(location.add(v), EntityType.FIREWORK);
            location.subtract(v);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(0);

            for(int i = 0; i < this.intensity; ++i) {
                meta.addEffect(this.firework);
            }

            firework.setFireworkMeta(meta);
            firework.detonate();
        }

    }
}
