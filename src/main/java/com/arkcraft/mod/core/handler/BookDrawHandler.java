package com.arkcraft.mod.core.handler;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.machine.gui.GuiDosierScreen.CATEGORY;

public class BookDrawHandler {
	
	public enum DINO_NAME {
		ANKLOSAURUS("Ankylosaurus", "Ankylosaurus Crassacutis", "ark.book.ankylo.description", 0, 0),
		BRONTOSAURUS("Brontosaurus", "Brontosaurus Lazarus", "ark.book.bronto.description", 0, 1),
		CARNOTAURUS("Carnotaurus", "Carnotaurus Pressor", "ark.book.carno.description", 1, 2),
		DILOPHOSAURUS("Dilophosaur", "Dilophosaurus Sputatrix", "ark.book.dilo.description", 1, 3),
		UTAHRAPTOR("Raptor", "Utahraptor Prime", "ark.book.utahraptor.description", 1, 4),
		TRICERATOPS("Triceratops", "Triceratops Styrax", "ark.book.triceratops.description", 0, 5),
		TREX("Tyrannosaurus-Rex", "Tyrannosaurus Dominum", "ark.book.trex.description", 1, 6),
		CARBONEMYS("Carbonemys", "Carbonemys Obibimus", "ark.book.carbonemys.description", 0, 7),
		ICHTHYOSAURUS("Ichthyosaurus", "Ichthyosaurus Curiosa", "ark.book.ichthy.description", 1, 8);
		
		String description;
		String species;
		String name;
		int id;
		int type;
		
		DINO_NAME(String dinoName, String species, String description, int type, int id) {
			this.name = dinoName;
			this.id = id;
			this.type = type;
			this.species = species;
			this.description = description;
		}
		
		public String getName() { return name; }
		public int getID() { return id; }
		public String getDescription() { return StatCollector.translateToLocal(description); }
		public String getSpecies() { return species; }
		
		/***
		 * 
		 * @return Docile or Aggresive
		 */
		public String getType() {
			switch(type) {
				case 0: return "Docile";
				case 1: return "Aggresive";
				default: return null;
			}
		}
		
		/***
		 * 
		 * @return Carnivore, Herbivore or Omnivore
		 */
		public String getPreference() {
			switch(type) {
				case 0: return "Herbivore";
				case 1: return "Carnivore";
				case 2: return "Omnivore";
				default: return null;
			}
		}
	}
	
	public static int left = 132;
	public static int right = 256;
	
	public static void drawPages(FontRenderer renderer, int mouseX, int mouseY, int currentPage, CATEGORY currentCategory) {
		
		if(currentCategory == CATEGORY.DINOS) {
			if(currentPage == 0) {
				/* Page 1 / 2*/
				drawTitle(renderer, DINO_NAME.UTAHRAPTOR);
				GuiInventory.drawEntityOnScreen(getLeft() + 14, 90, 20, -350f, -5F, new EntityRaptor(Minecraft.getMinecraft().theWorld, 1));
				drawDiet(renderer, getLeft() + 40, 135, DINO_NAME.UTAHRAPTOR);
				drawSpecies(renderer, getLeft() + 15, 150, DINO_NAME.UTAHRAPTOR);
			}
			else if(currentPage == 2) {
				
			}
		}
	}
	
	public static void drawSpecies(FontRenderer renderer, int x, int y, DINO_NAME dinoName) {
		GlStateManager.pushMatrix();
		GL11.glScalef(0.7f, 0.7f, 1);
		renderer.drawString("Species: " + dinoName.getSpecies(), x, y, Color.darkGray.getRGB());
		GlStateManager.popMatrix();
	}
	
	public static void drawTemperment(FontRenderer renderer, int x, int y, DINO_NAME dinoName) {
		GlStateManager.pushMatrix();
		GL11.glScalef(0.7f, 0.7f, 1);
		renderer.drawString("Temperment: " + dinoName.getType(), x, y, Color.darkGray.getRGB());
		GlStateManager.popMatrix();
	}
	
	public static void drawDiet(FontRenderer renderer, int x, int y, DINO_NAME dinoName) {
        GlStateManager.pushMatrix(); /* Start the rendering */
		GL11.glScalef(0.7f, 0.7f, 1); /* 70% the normal size */
		renderer.drawString("Diet: " + dinoName.getPreference(), x, y, Color.darkGray.getRGB()); 
        GlStateManager.popMatrix(); /* End the rendering */
	}
	
	public static void drawTitle(FontRenderer renderer, DINO_NAME dinoName) {
		/* Draws this on left */renderer.drawString(dinoName.getName(), getLeft() + 5 - dinoName.getName().length(), 45, Color.darkGray.getRGB());
	}
	
	public static int getLeft() { return left; }
	public static int getRight() { return right; }
}