package net.smileycorp.fbiomes.common.entities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.entity.ai.EntityAIMoveRandomFlying;
import net.smileycorp.atlas.api.entity.ai.FlyingMoveControl;
import net.smileycorp.fbiomes.common.items.ItemPixieBottle;

public class EntityPixie extends EntityLiving {
    
    public static final byte VARIANTS = (byte)6;
    private static final DataParameter<Byte> VARIANT = EntityDataManager.createKey(EntityPixie.class, DataSerializers.BYTE);
    
    public EntityPixie(World world) {
        super(world);
        moveHelper = new FlyingMoveControl(this);
        setSize(0.5f, 0.5f);
        setVariant((byte)rand.nextInt(VARIANTS));
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(8, new EntityAIMoveRandomFlying(this));
    }
    
    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            stack.shrink(1);
            ItemStack newStack = ItemPixieBottle.bottlePixie(this);
            player.addStat(StatList.getObjectUseStats(Items.GLASS_BOTTLE));
            playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1) * 2f);
            if (!player.inventory.addItemStackToInventory(newStack))
                player.dropItem(newStack, false);
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(VARIANT, (byte)0);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        AbstractAttributeMap map = getAttributeMap();
        map.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
        map.registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        map.getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.1);
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {}
    
    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}
    
    @Override
    protected void playStepSound(BlockPos pos, Block block) {}
    
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.25f;
    }
    
    public void setVariant(byte variant) {
        if (variant >= VARIANTS) variant = 0;
        dataManager.set(VARIANT, variant);
    }
    
    public byte getVariant() {
        return dataManager.get(VARIANT);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("variant")) setVariant(nbt.getByte("variant"));
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("variant", getVariant());
    }
    
}
