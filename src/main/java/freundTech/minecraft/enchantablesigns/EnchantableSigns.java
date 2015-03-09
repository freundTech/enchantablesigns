package freundTech.minecraft.enchantablesigns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import scala.util.parsing.json.JSON;
import sun.util.logging.resources.logging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
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
	public static final String VERSION = "1.1";

	private FMLControlledNamespacedRegistry<Item> iItemRegistry;
	private Method addObjectRaw;

	public static ToolMaterial signMaterial;
	public static Item sign;
	
	public static boolean isOutdated = false;
	public static String downloadURL;

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
		
		FMLCommonHandler.instance().bus().register(new LoginListener());
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
		try {
			String json = JSONHelper.readUrl("http://freundtech.bplaced.net/files/enchantablesigns/updates.json");
			
			JsonElement jsonElement = new JsonParser().parse(json);
			String currentversion = jsonElement.getAsJsonObject().get("currentversion").getAsString();
			
			if(currentversion != EnchantableSigns.VERSION)
			{
				String[] versionarray = currentversion.split("\\.");
				String[] thisversion = EnchantableSigns.VERSION.split("\\.");
				
				for(int i = 0;i < Math.max(versionarray.length, thisversion.length); i++)
				{
					int v;
					int t;
					try {
						v = Integer.parseInt(versionarray[i]);
					} catch (Exception e) {
						v = 0;
					}
					try {
						t = Integer.parseInt(thisversion[i]);
					} catch (Exception e) {
						t = 0;
					}
					if(v > t)
					{
						isOutdated = true;
						break;
					}
					else if(v == t) {
						continue;
						
					}
					else {
						break;
					}
				}
			}
			
			if(isOutdated) {
				EnchantableSigns.downloadURL = jsonElement.getAsJsonObject().get("download").getAsString();
			}
			
		} catch (Exception e) {
			logger.warn("Couldn't check for updates");
			e.printStackTrace();
		}
		
		logger.info("|--------------------|");
		logger.info("|     Enchantable    |");
		logger.info("|       Signs        |");
		logger.info("|    by freundTech   |");
		logger.info("|--------------------|");
		logger.info("         |  |         ");
		logger.info("         |  |         ");
	}
}
