package com.arkcraft.mod.core.entity;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

/***
 * 
 * @author wildbill22
 *
 */
public abstract class DinoTameable extends EntityTameable {

	public IInventory invDino;
	protected boolean isSaddled = false;
	protected int torpor = 0;
	protected int progress = 0;
	protected boolean isTameable = false;
	protected boolean isRideable = false;
//	protected EntityDinoAIFollowOwner dinoAIFollowOwner;
	protected EntityAIBase attackPlayerTarget;
	
	private int DINO_SADDLED_WATCHER = 22;
	public boolean isSaddled() {
		isSaddled = (this.dataWatcher.getWatchableObjectByte(DINO_SADDLED_WATCHER) & 1) != 0;
		return isSaddled;
	}
	public void setSaddled(boolean saddled) {
		if (!this.isChild() && this.isTamed()) {
			isSaddled = saddled;
			byte b0 = (byte) (saddled ? 1 : 0);
			this.dataWatcher.updateObject(DINO_SADDLED_WATCHER, Byte.valueOf(b0));
		}
	}

	protected DinoTameable(World worldIn) {
		super(worldIn);
		this.getDataWatcher().addObject(DINO_SADDLED_WATCHER, Byte.valueOf((byte) 0));
        this.isTameable = true;
	}
	
	/**
	 * Clears previous AI Tasks, so the ones defined above will
	 * actually perform.
	 */
	protected void clearAITasks() {
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
	}

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase target) {
        super.setAttackTarget(target);
        if (target == null) {
            this.setAngry(false);
        }
        else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean angry) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (angry) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 2)));
        }
        else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -3)));
        }
    }

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
		for (int k = 0; k < j; ++k) {
			this.dropItem(Items.feather, 1); // TODO: Dodo feather instead
		}
		if (this.isBurning()) {
			this.dropItem(GlobalAdditions.porkchop_cooked, 1);
		} else {
			this.dropItem(GlobalAdditions.porkchop_raw, 1);
		}
		if (this.isSaddled()) {
			this.dropItem(GlobalAdditions.saddle_large, 1);
			this.dropItemsInChest(this, this.invDino);
		}
	}
	
    private void dropItemsInChest(Entity entity, IInventory inventory) {
        if (inventory != null && !this.worldObj.isRemote) {
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = inventory.getStackInSlot(i);
                if (itemstack != null) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
    }

	@Override
	public void setTamed(boolean tamed) {
		if (tamed && attackPlayerTarget != null)
			this.targetTasks.removeTask(attackPlayerTarget);
		super.setTamed(tamed);
	}
    
	/* Plays the hearts / smoke depending on status. If progress is 100, we always are successful.*/
	protected void playTameEffect(boolean p_70908_1_) {
		super.playTameEffect(p_70908_1_);
    }

	public boolean isTameable() {
		return torpor > 0;
	}

	public void setTorpor(int i) {
		torpor = i;
	}

	public void addTorpor(int i) {
		torpor += i;
	}

	public void removeTorpor(int i) {
		torpor -= i;
	}

	public void setProgress(int i) {
		progress = i;
	}

	public void addProgress(int i) {
		progress += i;
	}

	public void removeProgress(int i) {
		progress -= i;
	}
	
    /**
     * Called when the entity is attacked. (Needed to attack other mobs)
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        else {
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                // Reduce damage if from player
            	if ((entity instanceof EntityPlayer) && this.isTamed()) {
            		damage = Math.min(damage, 1.0F);
            	}
            }
            return super.attackEntityFrom(damageSource, damage);
        }
    }

    /**
     * Called when attacking an entity
     */
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        super.attackEntityAsMob(entity);
        double attackDamage = this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getBaseValue();
        LogHelper.info("DinoTamable: Attacking with " + attackDamage + " attackDamage.");
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
    }
	
	/**
     * Determines if an entity can despawn, used on idle far away entities
     */
	@Override
    protected boolean canDespawn() {
        return false;
    }
	
    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();

		if (isTamed()) {
			player.addChatMessage(new ChatComponentText("DinoTameable: This dino is tamed."));
            if (itemstack != null) {
            	if (player.isSneaking()) {
					// Put saddle on Dino
					if (!isSaddled() && itemstack.getItem() == this.getSaddleType()) {
						if (!player.capabilities.isCreativeMode) {
							itemstack.stackSize--;
							if (itemstack.stackSize == 0)
			                    player.inventory.mainInventory[player.inventory.currentItem] = null;
						}
						setSaddled(true);
						return true;
					}
					else {
						player.addChatMessage(new ChatComponentText("This dino can only be saddled with: " + this.getSaddleType()));					
					}
				}
		        // Heal the Dino with meat
				else if (itemstack.getItem() instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood)itemstack.getItem();
                    if (!player.capabilities.isCreativeMode) {
                         --itemstack.stackSize;
                    }
                    this.heal((float)itemfood.getHealAmount(itemstack));
                    if (itemstack.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                    }
					player.addChatMessage(new ChatComponentText("This dino's health is: " + this.getHealth() + " Max is: " 
							+ this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue()));					
                    return true;
	            }				
			}
    		if (isSaddled()) {
    			player.openGui(Main.instance, GUI.INV_DODO.getID(), worldObj, player
    					.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
    			return true;
    		}
		}
        // Tame the dino with meat
        else if (itemstack != null && itemstack.getItem() == GlobalAdditions.porkchop_raw) {
            if (!player.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(2) == 0) {
                    this.setTamed(true);
        			player.addChatMessage(new ChatComponentText("DinoTameable: You have tamed the dino!"));
                    this.setAttackTarget((EntityLivingBase)null);
//                    this.aiSit.setSitting(true);
                    this.setHealth(25.0F);
                    this.setOwnerId(player.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else {
        			player.addChatMessage(new ChatComponentText("DinoTameable: Taming the dino failed, try again!"));
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        else {
			player.addChatMessage(new ChatComponentText("DinoTameable: Use a Raw Porkchop to tame the dino."));
        }
        return super.interact(player);
    }
    
    public abstract Item getSaddleType();
}