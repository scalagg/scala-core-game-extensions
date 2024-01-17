package gg.tropic.game.extensions.projectiletrails;

import me.lucko.helper.Helper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.BiConsumer;

public class ProjectileTrails {
    public static final ProjectileTrails INSTANCE = new ProjectileTrails();

    public void createNewSpiral(BiConsumer<Location, Player> action, Entity entity, Player shooter) {
        final int rotationSpeed = 30;
        final double radius = .5;

        createNewSpiral(action, entity, shooter, 0, rotationSpeed, radius, .1, false);
    }

    public void createNewSpiral(
            BiConsumer<Location, Player> action, Entity entity, Player shooter,
            int delay, int rotationSpeed, double radius, double spacing, boolean reverse
    ) {
        final double INCREMENT = (2 * Math.PI) / rotationSpeed;

        new BukkitRunnable() {
            double circlePointOffset = 0;

            Location lastLocation = shooter.getEyeLocation();

            @Override
            public void run() {
                if (entity.isDead() || entity.isOnGround()) {
                    this.cancel();
                    return;
                }

                Location startLoc = entity.getLocation();
                startLoc.setDirection(entity.getVelocity());

                double distance = Math.abs(startLoc.distance(lastLocation));
                double
                        pitch = (90 + startLoc.getPitch()) * 0.017453292F,
                        yaw = -startLoc.getYaw() * 0.017453292F;

                for (double i = 0; i <= distance; i += spacing) {
                    double x = radius * Math.cos(2 * Math.PI + circlePointOffset);
                    double z = radius * Math.sin(2 * Math.PI + circlePointOffset);

                    if (reverse) {
                        z = -z;
                        x = -x;
                    }

                    Vector vec = new Vector(x, i, z);
                    rotateAroundAxisX(vec, pitch);
                    rotateAroundAxisY(vec, yaw);

                    action.accept(startLoc.clone().add(vec), shooter);

                    circlePointOffset += INCREMENT;
                    if (circlePointOffset >= (2 * Math.PI)) {
                        circlePointOffset = 0;
                    }
                }

                lastLocation = startLoc;
            }
        }.runTaskTimer(Helper.hostPlugin(), 2, delay);
    }

    public static void rotateAroundAxisX(Vector vector, double angle) {
        double y = vector.getY();
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        vector.setY(y * cosAngle - vector.getZ() * sinAngle);
        vector.setZ(y * sinAngle + vector.getZ() * cosAngle);
    }

    public static void rotateAroundAxisY(Vector vector, double angle) {
        double x = vector.getX();
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        vector.setX(x * cosAngle + vector.getZ() * sinAngle);
        vector.setZ(-x * sinAngle + vector.getZ() * cosAngle);
    }
}
