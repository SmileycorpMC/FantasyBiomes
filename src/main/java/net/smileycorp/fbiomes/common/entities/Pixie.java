package net.smileycorp.fbiomes.common.entities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Pixie {

    public static final float MAX_MOOD = 6;

    private final float health, maxHealth, size;
    private EntityPixie.PixieVariant variant;
    private float mood = Pixie.MAX_MOOD / 2;
    private final UUID owner;
    private String name;

    Pixie(EntityPixie.PixieVariant variant, Random rand) {
        maxHealth = 10;
        health = maxHealth;
        this.variant = variant;
        size = EntityPixie.getRandomSize(rand);
        owner = null;
    }

    Pixie(EntityPixie pixie) {
        this.health = pixie.getHealth();
        this.maxHealth = pixie.getMaxHealth();
        this.variant = pixie.getVariant();
        this.size = pixie.getSize();
        this.mood = pixie.getMood();
        this.owner = pixie.getOwnerId();
        this.name = pixie.hasCustomName() ? pixie.getCustomNameTag() : null;
    }

    private Pixie(NBTTagCompound nbt) {
        health = nbt.getFloat("health");
        maxHealth = nbt.getFloat("maxHealth");
        variant = EntityPixie.PixieVariant.get(nbt.getByte("variant"));
        size = nbt.getFloat("size");
        mood = nbt.getFloat("mood");
        owner = nbt.hasKey("owner") ? nbt.getUniqueId("owner") : null;
        name = nbt.hasKey("name") ? nbt.getString("name") : null;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getSize() {
        return size;
    }

    public EntityPixie.PixieVariant getVariant() {
        return variant;
    }

    public float getMood() {
        return mood;
    }

    public int getMoodLevel() {
        return Math.round(mood);
    }

    public UUID getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public float getEfficiency() {
        return (float) (Math.pow(1.0641f, getMoodLevel()) + 0.05f);
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public boolean hasName() {
        return name != null;
    }

    public void setVariant(EntityPixie.PixieVariant variant) {
        this.variant = variant;
    }

    public void setMood(float mood) {
        if (mood < 0) mood = 0;
        if (mood > MAX_MOOD) mood = MAX_MOOD;
        this.mood = mood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMoodDecay() {
        int gate = (int) Math.floor(mood);
        float decay = (float) (Math.pow(1.0431f, getMoodLevel()) - 0.95f);
        if (mood > gate && mood - decay < gate) decay = mood - gate;
        return decay;
    }

    public float getMoodGain() {
        int gate = (int) Math.ceil(mood);
        float gain = (float) (0.3 - 0.02 * Math.pow(getMoodLevel() - 2.5, 2));
        if (mood != gate && mood + gain > gate) gain = gate - mood;
        return gain;
    }

    public void apply(EntityPixie pixie) {
        pixie.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
        pixie.setHealth(health);
        pixie.setVariant(variant);
        pixie.setSize(size);
        pixie.setMood(mood);
        if (owner != null) pixie.setOwner(owner);
        if (name != null) pixie.setCustomNameTag(name);
    }

    public void addTooltips(List<String> tooltips) {
        tooltips.add(new TextComponentTranslation("tooltip.fbiomes.health",
                health, maxHealth).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.fbiomes.pixie_variant",
                new TextComponentTranslation("entity.fbiomes.pixie.variant."
                        + variant.getName())).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.fbiomes.size",
                size).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.fbiomes.mood",
                new TextComponentTranslation("entity.fbiomes.pixie.mood." + getMoodLevel())).getFormattedText());
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("health", health);
        nbt.setFloat("maxHealth", maxHealth);
        nbt.setByte("variant", (byte) variant.ordinal());
        nbt.setFloat("size", size);
        nbt.setFloat("mood", mood);
        if (owner != null) nbt.setUniqueId("owner", owner);
        if (name != null) nbt.setString("name", name);
        return nbt;
    }

    public static Pixie fromNbt(NBTTagCompound nbt) {
        return new Pixie(nbt);
    }

    public static Pixie newPixie(EntityPixie.PixieVariant variant, Random rand) {
        return new Pixie(variant, rand);
    }

}
