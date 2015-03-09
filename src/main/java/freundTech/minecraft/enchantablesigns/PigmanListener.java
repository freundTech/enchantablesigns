package freundTech.minecraft.enchantablesigns;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PigmanListener {
    Random rand;

    public PigmanListener() {
    	this.rand = new Random();
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {    	
    	if (event.entity instanceof EntityPigZombie) {
    		EntityPigZombie pigman = (EntityPigZombie) event.entity;
    		NBTTagCompound data = pigman.getEntityData();

    		if (!data.getBoolean(EnchantableSigns.MODID + "isEntityChanged")) {
    			if(rand.nextInt(100) == 0)
    			{
    				pigman.setCurrentItemOrArmor(0, EnchantmentHelper.addRandomEnchantment(rand, new ItemStack(EnchantableSigns.sign), 10 + rand.nextInt(21)));
    			}
    			SuperSecretEasterEgg.easteregg(this.rand, pigman);
    			data.setBoolean(EnchantableSigns.MODID + "isEntityChanged",	true);
    		}
    	}
    }
}
