package net.smileycorp.fbiomes.common.entities;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.entity.ai.EntityAIMoveRandomFlying;
import net.smileycorp.atlas.api.entity.ai.FlyingMoveControl;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.fbiomes.client.ClientHandler;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.EnumParticle;
import net.smileycorp.fbiomes.common.entities.ai.EntityAIPixieFollowOwner;
import net.smileycorp.fbiomes.common.entities.ai.EntityAIPixieHealOwner;
import net.smileycorp.fbiomes.common.items.ItemPixieBottle;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.EnumMap;
import java.util.Random;
import java.util.UUID;

public class EntityPixie extends EntityLiving implements IEntityOwnable {

    private static final DataParameter<PixieData> PIXIE_DATA = EntityDataManager.createKey(EntityPixie.class, PixieData.SERIALIZER);

    private Entity owner;
    private int spellCooldown;

    public EntityPixie(World world, PixieData data) {
        this(world);
        setPixieData(data);
    }

    public EntityPixie(World world) {
        super(world);
        moveHelper = new FlyingMoveControl(this);
        setSize(0.5f, 0.5f);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
        setPixieData(PixieData.newPixie(rand));
        return super.onInitialSpawn(difficulty, data);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPixieFollowOwner(this));
        tasks.addTask(2, new EntityAIPixieHealOwner(this));
        tasks.addTask(4, new EntityAIMoveRandomFlying(this));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PIXIE_DATA, PixieData.DEFAULT);
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
            if (hasOwner()) if (!player.equals(getOwner())) {
                return false;
            }
            stack.shrink(1);
            addMood(-0.25f);
            ItemStack newStack = ItemPixieBottle.bottlePixie(getPixieData());
            player.addStat(StatList.getObjectUseStats(Items.GLASS_BOTTLE));
            playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1) * 2f);
            if (!player.inventory.addItemStackToInventory(newStack)) player.dropItem(newStack, false);
            setDead();
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (spellCooldown > 0) spellCooldown--;
            if (getMood() < 3 && ticksExisted % 10 == 0) addMood(0.05f * (float) Math.ceil(getMood()));
            if (getPixieData().getMaxHealth() != getMaxHealth()) getPixieData().setMaxHealth(getMaxHealth());
            return;
        }
        if (ticksExisted % 5 > 0) return;
        if (rand.nextBoolean()) return;
        ClientHandler.spawnParticle(EnumParticle.TWINKLE, posX + 0.1f * (rand.nextFloat() * 2f - 1), posY + height * 0.5 + 0.1f * (rand.nextFloat() * 2f - 1),
                posZ + 0.1f * (rand.nextFloat() * 2f - 1), (double) getVariant().getRandomTrailColour(rand), -motionX * 0.3, -0.01, -motionZ * 0.3);
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {}
    
    @Override
    protected void updateFallState(double y, boolean grounded, IBlockState state, BlockPos pos) {}
    
    @Override
    protected void playStepSound(BlockPos pos, Block block) {}
    
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.25f * getSize();
    }

    /*
        do not use for modifying pixie data unless you use setPixieData
        health, max health and bounding box will become desynced
    */
    public PixieData getPixieData() {
        return dataManager.get(PIXIE_DATA);
    }

    public void setPixieData(PixieData data) {
        dataManager.set(PIXIE_DATA, data);
        data.apply(this);
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        getPixieData().setHealth(getHealth());
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        getPixieData().setName(getCustomNameTag());
    }

    public void setVariant(PixieVariant variant) {
        getPixieData().setVariant(variant);
    }

    public void setSize(float size) {
        PixieData data = getPixieData();
        data.setSize(size);
        updateBoundingBox();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (!PIXIE_DATA.equals(key)) return;
        updateBoundingBox();
    }

    public void updateBoundingBox() {
        float size = getSize();
        setSize(size * 0.5f, size * 0.5f);
    }

    public void addMood(float change) {
       getPixieData().addMood(change);
    }

    public void setMood(float mood) {
        getPixieData().setMood(mood);
    }

    public void setOwner(UUID uuid) {
        getPixieData().setOwner(uuid);
    }
    
    public PixieVariant getVariant() {
        return getPixieData().getVariant();
    }

    public float getSize() {
        return getPixieData().getSize();
    }

    public float getMood() {
        return getPixieData().getMood();
    }

    public boolean hasOwner() {
        return getOwner() != null;
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return getPixieData().getOwner();
    }

    @Nullable
    @Override
    public EntityLivingBase getOwner() {
        if (owner == null && getOwnerId() != null && world instanceof WorldServer)
            owner = ((WorldServer) world).getEntityFromUuid(getOwnerId());
        return owner instanceof EntityLivingBase ? (EntityLivingBase) owner : null;
    }

    public void setOwner(EntityLivingBase owner) {
        this.owner = owner;
        setOwner(owner.getUniqueID());
    }

    public int getSpellCooldown() {
        return spellCooldown;
    }

    public void setSpellCooldown(int spellCooldown) {
        this.spellCooldown = spellCooldown;
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("data")) setPixieData(PixieData.fromNbt(nbt.getCompoundTag("data")));
        if (nbt.hasKey("spellCooldown")) spellCooldown = nbt.getInteger("spellCooldown");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("data", getPixieData().toNbt());
        nbt.setInteger("spellCooldown", spellCooldown);
    }

    public enum PixieVariant {
        SWALLOWTAIL("swallowtail", 35, 0xFF68F2),
        MONARCH("monarch", 25, 0x68C1FF),
        RED_SPOTTED("red_spotted", 20, 0x7AFF68),
        MARBLED("marbled", 14, 0xFFFF7D),
        HAIRSTREAK("hairstreak", 5, 0x68FFE6),
        SUNSET("sunset", 1, 0xF9FCFC),
        MALACHITE("malachite", 0, 0x9A72FF),
        GLASSWING("glasswing", 0, 0xDDBDEC);
        
        private static WeightedOutputs<PixieVariant> table;
        
        private final String name;
        private final int weight, colour;
        private final ResourceLocation texture;
        
        PixieVariant(String name, int weight, int colour) {
            this.name = name;
            this.weight = weight;
            this.colour = colour;
            this.texture = Constants.loc("textures/entity/pixie/" + name + ".png");
        }
        
        public String getName() {
            return name;
        }

        public ResourceLocation getTexture() {
            return texture;
        }

        public int getColour() {
            return colour;
        }
        
        public int getRandomTrailColour(Random rand) {
            float[] hsv = Color.RGBtoHSB((colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF, new float[3]);
            return Color.HSBtoRGB(hsv[0], rand.nextFloat() * 0.3f, hsv[2]) & 0xFFFFFF;
        }
        
        public static PixieVariant get(byte val) {
            if (val >= values().length) val = 0;
            return values()[val];
        }
        
        public static PixieVariant random(Random rand) {
            if (table == null) {
                EnumMap<PixieVariant, Integer> map = Maps.newEnumMap(PixieVariant.class);
                for (PixieVariant variant : values()) if (variant.weight > 0) map.put(variant, variant.weight);
                table = new WeightedOutputs<>(map);
            }
            return table.getResult(rand);
        }

    }

}
