package net.smileycorp.fbiomes.common.entities;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.entity.ai.EntityAIMoveRandomFlying;
import net.smileycorp.atlas.api.entity.ai.FlyingMoveControl;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.fbiomes.common.EnumParticle;
import net.smileycorp.fbiomes.common.items.ItemPixieBottle;
import net.smileycorp.fbiomes.common.network.FBiomesParticleMessage;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.EnumMap;
import java.util.Random;

public class EntityPixie extends EntityLiving {
    
    private static final DataParameter<Byte> VARIANT = EntityDataManager.createKey(EntityPixie.class, DataSerializers.BYTE);
    
    public EntityPixie(World world) {
        super(world);
        moveHelper = new FlyingMoveControl(this);
        setSize(0.5f, 0.5f);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
        setVariant(Variant.random(rand));
        return super.onInitialSpawn(difficulty, data);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(8, new EntityAIMoveRandomFlying(this));
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
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            stack.shrink(1);
            ItemStack newStack = ItemPixieBottle.bottlePixie(this);
            player.addStat(StatList.getObjectUseStats(Items.GLASS_BOTTLE));
            playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1) * 2f);
            if (!player.inventory.addItemStackToInventory(newStack)) player.dropItem(newStack, false);
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) return;
        if (motionX == 0 && motionZ == 0) return;
        if (ticksExisted % 5 > 0) return;
        if (rand.nextBoolean()) return;
        EnumParticle.TWINKLE.send(dimension, posX, posY, posZ, (double) getVariant().getRandomTrailColour(rand),
                -motionX * 0.3 -0.1, -motionZ * 0.3);
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
    
    public void setVariant(Variant variant) {
        dataManager.set(VARIANT, (byte)variant.ordinal());
    }
    
    public Variant getVariant() {
        return Variant.get(dataManager.get(VARIANT));
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("variant")) dataManager.set(VARIANT, nbt.getByte("variant"));
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("variant", dataManager.get(VARIANT));
    }
    
    public NBTTagCompound storeInItem() {
        NBTTagCompound nbt = super.writeToNBT(new NBTTagCompound());
        nbt.removeTag("Pos");
        nbt.removeTag("Motion");
        nbt.removeTag("Rotation");
        nbt.removeTag("FallDistance");
        nbt.removeTag("Air");
        nbt.removeTag("UUIDMost");
        nbt.removeTag("UUIDLeast");
        nbt.removeTag("Leashed");
        nbt.removeTag("Leash");
        nbt.setFloat("MaxHealth", getMaxHealth());
        return nbt;
    }
    
    public enum Variant {
        SWALLOWTAIL("swallowtail", 35, 0xFF68F2),
        MONARCH("monarch", 25, 0x68C1FF),
        RED_SPOTTED("red_spotted", 20, 0x7AFF68),
        MARBLED("marbled", 14, 0xFFFF7D),
        HAIRSTREAK("hairstreak", 5, 0x68FFE6),
        SUNSET("sunset", 1, 0xE3E5E5),
        MALACHITE("malachite", 0, 0x654AA8);
        
        private static WeightedOutputs<Variant> table;
        
        private final String name;
        private final int weight, colour;
        
        Variant(String name, int weight, int colour) {
            this.name = name;
            this.weight = weight;
            this.colour = colour;
        }
        
        public String getName() {
            return name;
        }
        
        public int getRandomTrailColour(Random rand) {
            float multiplier = 1 + rand.nextFloat() * 0.3f;
            int r = Math.min((int)(multiplier * (float)(colour >> 16 & 0xFF)), 255);
            int g = Math.min((int)(multiplier * (float)(colour >> 8 & 0xFF)), 255);
            int b = Math.min((int)(multiplier * (float)(colour & 0xFF)), 255);
            return r << 16 + g << 16 + b;
        }
        
        public static Variant get(byte val) {
            if (val >= values().length) val = 0;
            return values()[val];
        }
        
        public static Variant random(Random rand) {
            if (table == null) {
                EnumMap<Variant, Integer> map = Maps.newEnumMap(Variant.class);
                for (Variant variant : values()) map.put(variant, variant.weight);
                table = new WeightedOutputs<>(map);
            }
            return table.getResult(rand);
        }
        
    }
    
}
