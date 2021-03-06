package com.arkcraft.mod.core.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;

public class ARKArmorItem extends ItemArmor {

	public String texName;
	public boolean golden;
	public String[] tooltips;
	
	public ARKArmorItem(String name, ArmorMaterial mat, String texName, int type, boolean golden, String... tooltips) {
		super(mat, 0, type);
		this.golden = golden;
		this.tooltips = tooltips;
		this.setUnlocalizedName(name);
		this.texName = texName;
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return Main.MODID + ":textures/armor/" + this.texName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(golden) return EnumChatFormatting.GOLD + super.getItemStackDisplayName(stack);
		return super.getItemStackDisplayName(stack);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		for(String e : tooltips) tooltip.add(e);
	}
	
	
	
	
}
