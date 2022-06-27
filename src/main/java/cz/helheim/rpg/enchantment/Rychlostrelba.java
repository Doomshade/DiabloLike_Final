package cz.helheim.rpg.enchantment;

import com.rit.sucy.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class Rychlostrelba extends CustomEnchantment {
	public Rychlostrelba() {
		super("Rychlostrelba", "Navysi rychlost sipu");
		this.setMaxLevel(6);
		this.setBase(1.1);
		this.setInterval(0.1);
		this.setNaturalMaterials(new Material[] {Material.BOW});
		this.setTableEnabled(false);
	}

	@Override
	public void applyProjectileEffect(LivingEntity entity, int level, ProjectileLaunchEvent event) {
		event.getEntity()
		     .setVelocity(event.getEntity()
		                       .getVelocity()
		                       .multiply(this.getBase() + this.getInterval() * (double) (level - 1)));
	}
}
