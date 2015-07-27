package tk.wurst_client.commands;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info (help="Add to/sets the efects of the held potion. Accepts arguments in groups of 3.",
	name="potion",
	syntax = {"[add] <id> <amplifier> <duration>", "[set] <id> <amplifier> <duration>"})

public class PotionCmd extends Cmd {

	@Override
	public void execute(String[] args) throws Error {
		
		if(args.length == 0 || (args.length - 1) % 3 != 0 || (!args[0].equalsIgnoreCase("add") &&
				!args[0].equalsIgnoreCase("set")))
			syntaxError();
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		ItemStack currentItem = 
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
		if(currentItem == null || Item.getIdFromItem(currentItem.getItem()) != 373)
			error("Please execute this command while holding a potion.");
		NBTTagList effectList = new NBTTagList();
		for(int i = 1; i < args.length; i++) {
			if(!MiscUtils.isInteger(args[i])) 
				syntaxError();
		}
		if(args[0].equalsIgnoreCase("add")) {
			List oldEffects = new ItemPotion().getEffects(currentItem);
			for(int i = 0; i < oldEffects.size(); i++) {
				PotionEffect temp = (PotionEffect) oldEffects.get(i);
				NBTTagCompound effect = new NBTTagCompound();
				effect.setInteger("Id", temp.getPotionID());
				effect.setInteger("Amplifier", temp.getAmplifier());
				effect.setInteger("Duration", temp.getDuration());
				effectList.appendTag(effect);
			}
		}
		for(int i = 0; i < (args.length - 1) / 3; i++) {
			int id = Integer.parseInt(args[1+(i*3)]);
			int amplifier = Integer.parseInt(args[2+(i*3)]);
			int duration = Integer.parseInt(args[3+(i*3)]);
			NBTTagCompound effect = new NBTTagCompound();
			effect.setInteger("Id", id);
			effect.setInteger("Amplifier", amplifier);
			effect.setInteger("Duration", duration*20);
			effectList.appendTag(effect);
		}
		currentItem.setTagInfo("CustomPotionEffects", effectList);
	}
}
