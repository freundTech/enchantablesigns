package freundTech.minecraft.enchantablesigns;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;

@Mod(modid = EnchantableSigns.MODID, version = EnchantableSigns.VERSION, name = EnchantableSigns.NAME)
public class EnchantableSigns {
	public static final String MODID = "enchantablesigns";
	public static final String NAME = "Enchantable Signs";
	public static final String VERSION = "1.0";

	private FMLControlledNamespacedRegistry<Item> iItemRegistry;
	private Method addObjectRaw;

	public static ToolMaterial signMaterial;
	public static Item sign;

	@EventHandler
	public void init(FMLInitializationEvent event) throws Exception {
		signMaterial = EnumHelper.addToolMaterial("sign", 0, -1, 1F, -4F, 30);
		sign = new BattleSign(signMaterial).setUnlocalizedName("sign");

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(sign, 0,	new ModelResourceLocation("minecraft:sign", "inventory"));

		addObjectRaw.invoke(iItemRegistry, 323, new ResourceLocation("sign"), sign);

		ItemStack planks = new ItemStack(Blocks.planks);
		ItemStack sticks = new ItemStack(Items.stick);

		GameRegistry.addRecipe(new ItemStack(sign, 3),
		"OOO",
		"OOO",
		" | ",
		'O', planks, '|', sticks);
		
		Field signItem = Items.class.getDeclaredField("sign");
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(signItem, signItem.getModifiers() &~Modifier.FINAL);
		signItem.set(null, sign);
		
		System.out.println(Items.sign == sign);
		
		MinecraftForge.EVENT_BUS.register(new PigmanListener());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		try {
			Method getMain = GameData.class.getDeclaredMethod("getMain");
			getMain.setAccessible(true);
			GameData gameData = (GameData) getMain.invoke(null);

			Field f = GameData.class.getDeclaredField("iItemRegistry");
			f.setAccessible(true);
			iItemRegistry = (FMLControlledNamespacedRegistry<Item>) f
					.get(gameData);

			addObjectRaw = FMLControlledNamespacedRegistry.class
					.getDeclaredMethod("addObjectRaw", Integer.TYPE,
							Object.class, Object.class);
			addObjectRaw.setAccessible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Logger logger = FMLLog.getLogger();
		logger.info("|--------------------|");
		logger.info("|     Enchantable    |");
		logger.info("|       Signs        |");
		logger.info("|    by freundTech   |");
		logger.info("|--------------------|");
		logger.info("         |  |         ");
		logger.info("         |  |         ");
	}
}
