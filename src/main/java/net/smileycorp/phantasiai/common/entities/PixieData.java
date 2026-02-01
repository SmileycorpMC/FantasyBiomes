package net.smileycorp.phantasiai.common.entities;

import com.google.common.collect.Maps;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.phantasiai.common.Constants;

import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PixieData {

    public static final PixieData DEFAULT = new PixieData(Variant.SWALLOWTAIL, 1, PixieData.MAX_MOOD / 2);
    public static DataSerializer<PixieData> SERIALIZER = new Serializer();

    public static final float MAX_MOOD = 6;

    private float health, maxHealth, size;
    private Variant variant;
    private float mood;
    private UUID owner;
    private String name;

    PixieData(Variant variant, float size, float mood) {
        maxHealth = 10;
        health = maxHealth;
        this.variant = variant;
        this.size = size;
        this.mood = mood;
        owner = null;
    }

    private PixieData(NBTTagCompound nbt) {
        health = nbt.getFloat("health");
        maxHealth = nbt.getFloat("maxHealth");
        variant = Variant.get(nbt.getByte("variant"));
        size = nbt.getFloat("size");
        mood = nbt.getFloat("mood");
        owner = nbt.hasKey("owner") ? nbt.getUniqueId("owner") : null;
        name = nbt.hasKey("name") ? nbt.getString("name") : null;
    }

    public static float getRandomSize(Random rand) {
        return 1 + (float) rand.nextGaussian() * 0.3f;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = Math.round(size * 100f) * 0.01f;
    }

    public Variant getVariant() {
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

    public void setOwner(UUID owner) {
        this.owner = owner;
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

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public void setMood(float mood) {
        this.mood = MathHelper.clamp(mood, 0, MAX_MOOD);
    }

    public void addMood(float change) {
        if (change == 0) return;
        if (change < 0) {
            int gate = (int) Math.floor(mood);
            if (mood != gate && mood + change < gate) {
                setMood(gate);
                return;
            }
        } else {
            int gate = (int) Math.ceil(mood);
            if (mood != gate && mood + change > gate) {
                setMood(gate);
                return;
            }
        }
        setMood(mood + change);
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMoodDecay() {
        return (float) (Math.pow(1.0431f, getMoodLevel()) - 0.95f);
    }

    public float getMoodGain() {
        return (float) (0.3 - 0.02 * Math.pow(getMoodLevel() - 2.5, 2));
    }

    public void apply(EntityPixie pixie) {
        pixie.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
        pixie.setHealth(health);
        pixie.updateBoundingBox();
        if (name != null) pixie.setCustomNameTag(name);
    }

    public void addTooltips(List<String> tooltips) {
        tooltips.add(new TextComponentTranslation("tooltip.phantasiai.health",
                health, maxHealth).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.phantasiai.pixie_variant",
                new TextComponentTranslation("entity.phantasiai.pixie.variant."
                        + variant.getName())).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.phantasiai.size",
                size).getFormattedText());
        tooltips.add(new TextComponentTranslation("tooltip.phantasiai.mood",
                new TextComponentTranslation("entity.phantasiai.pixie.mood." + getMoodLevel())).getFormattedText());
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("health", health);
        nbt.setFloat("maxHealth", maxHealth);
        nbt.setByte("variant", (byte) (variant == null ? 0 : variant.ordinal()));
        nbt.setFloat("size", size);
        nbt.setFloat("mood", mood);
        if (owner != null) nbt.setUniqueId("owner", owner);
        if (name != null) nbt.setString("name", name);
        return nbt;
    }

    public static PixieData fromNbt(NBTTagCompound nbt) {
        return new PixieData(nbt);
    }

    public static PixieData newPixie(Random rand) {
        return newPixie(Variant.random(rand), rand);
    }

    public static PixieData newPixie(Variant variant, Random rand) {
        return new PixieData(variant, getRandomSize(rand), PixieData.MAX_MOOD / 2);
    }

    public static PixieData getDefault(Variant variant) {
        return variant.getDefault();
    }

    public EntityPixie spawn(World world, double x, double y, double z) {
        return spawn(world, x, y, z, false);
    }

    public EntityPixie spawn(World world, double x, double y, double z, boolean special) {
        EntityPixie pixie = new EntityPixie(world, this);
        if (special && ForgeEventFactory.doSpecialSpawn(pixie, world, (float) x, (float) y, (float) z, null)) return null;
        pixie.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360f), 0);
        pixie.rotationYawHead = pixie.rotationYaw;
        pixie.renderYawOffset = pixie.rotationYaw;
        world.spawnEntity(pixie);
        return pixie;
    }

    public enum Variant {
        SWALLOWTAIL("swallowtail", 35, 0xFF68F2),
        MONARCH("monarch", 25, 0x68C1FF),
        RED_SPOTTED("red_spotted", 20, 0x7AFF68),
        MARBLED("marbled", 14, 0xFFFF7D),
        HAIRSTREAK("hairstreak", 5, 0x68FFE6),
        SUNSET("sunset", 1, 0xF9FCFC),
        MALACHITE("malachite", 0, 0x9A72FF),
        GLASSWING("glasswing", 0, 0xDDBDEC);

        private static WeightedOutputs<Variant> table;

        private final String name;
        private final int weight, colour;
        private final ResourceLocation texture;
        private final PixieData defaultData;

        Variant(String name, int weight, int colour) {
            this.name = name;
            this.weight = weight;
            this.colour = colour;
            this.texture = Constants.loc("textures/entity/pixie/" + name + ".png");
            defaultData = new PixieData(this, 1, PixieData.MAX_MOOD / 2);
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

        public PixieData getDefault() {
            return defaultData;
        }

        public int getRandomTrailColour(Random rand) {
            float[] hsv = Color.RGBtoHSB((colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF, new float[3]);
            return Color.HSBtoRGB(hsv[0], rand.nextFloat() * 0.3f, hsv[2]) & 0xFFFFFF;
        }

        public static Variant get(byte val) {
            if (val >= values().length) val = 0;
            return values()[val];
        }

        public static Variant random(Random rand) {
            if (table == null) {
                EnumMap<Variant, Integer> map = Maps.newEnumMap(Variant.class);
                for (Variant variant : values()) if (variant.weight > 0) map.put(variant, variant.weight);
                table = new WeightedOutputs<>(map);
            }
            return table.getResult(rand);
        }

    }

    public static class Serializer implements DataSerializer<PixieData> {

        @Override
        public void write(PacketBuffer buf, PixieData data) {
            buf.writeByte(data.variant.ordinal());
            buf.writeFloat(data.getSize());
            buf.writeFloat(data.getMood());
        }

        @Override
        public PixieData read(PacketBuffer buf) throws IOException {
            return new PixieData(Variant.get(buf.readByte()), buf.readFloat(), buf.readFloat());
        }

        @Override
        public DataParameter<PixieData> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public PixieData copyValue(PixieData data) {
            return fromNbt(data.toNbt());
        }

    }

}
