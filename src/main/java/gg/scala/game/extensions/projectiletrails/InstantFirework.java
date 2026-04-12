package gg.scala.game.extensions.projectiletrails;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class InstantFirework {

   public InstantFirework(FireworkEffect fe, Location loc) {
        Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(fe);
        f.setFireworkMeta(fm);
        f.detonate();
   }

}
