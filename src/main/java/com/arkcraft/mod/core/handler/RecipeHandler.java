package com.arkcraft.mod.core.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class RecipeHandler {

	public RecipeHandler() {}
	
	public static void registerVanillaCraftingRecipes() {
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinHelm),
					"AAA",
					"A A",
					'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinChest),
					"A A", "AAA", "AAA", 'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinLegs),
					"AAA", "A A", "A A", 'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinBoots),
					"   ", "A A", "A A", 'A', GlobalAdditions.chitin);
			/*
			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(GlobalAdditions.narcoBerry), new ItemStack(GlobalAdditions.fiber,1));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.stoneSpear), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Blocks.stone));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.ironPike), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Items.iron_ingot));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.cobble_ball), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
	}
	
}