package freundTech.minecraft.enchantablesigns;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SuperSecretEasterEgg {
	public static void easteregg(Random rand, EntityPigZombie pigman) {
		if(rand.nextInt(1000) == 0)
		{
			ItemStack stack = new ItemStack(Items.lava_bucket);
			stack.addEnchantment(Enchantment.fireAspect, 2);
			pigman.setCurrentItemOrArmor(0, stack);
		}
	}
}
