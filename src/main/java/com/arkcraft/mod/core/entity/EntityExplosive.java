package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * 
 * @author Vastatio
 *
 */
public class EntityExplosive extends EntityThrowable {

	public int explosionRadius = 2;

	public EntityExplosive(World w) {
		super(w);
	}

	public EntityExplosive(World w, EntityLivingBase par2) {
		super(w, par2);

	}

	public EntityExplosive(World w, double par2, double par3, double par4) {
		super(w, par2, par3, par4);

	}

	@Override
	protected void onImpact(MovingObjectPosition par1) {
		/* Explosion done here */
		/* Will there be damage on impact? */
		
		/**
		 * if(mop.entityHit != null)
		 * mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,
		 * this.getThrower()), dmg);
		 * 
		 * for(int i = 0; i < 4; i++)
		 * this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX,
		 * this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		 * 
		 */
		
		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius, true);
		if (worldObj.isRemote)
			this.setDead();
	}
}
