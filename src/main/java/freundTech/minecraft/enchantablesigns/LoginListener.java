package freundTech.minecraft.enchantablesigns;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LoginListener {
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if(EnchantableSigns.isOutdated)
		{
			if(EnchantableSigns.downloadURL.equals(""))
			{
				EnchantableSigns.downloadURL = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2376339";
			}
			ClickEvent link = new ClickEvent(Action.OPEN_URL, EnchantableSigns.downloadURL);
			event.player.addChatMessage(new ChatComponentText("This mod is Outdated. Please download the latest Version from ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).appendSibling(new ChatComponentText("here").setChatStyle(new ChatStyle().setUnderlined(true).setColor(EnumChatFormatting.RED).setChatClickEvent(link))));
		}
	}
}
