package net.smileycorp.fbiomes.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPixieBottle extends ItemFBiomes implements IMetaItem {
    
    public ItemPixieBottle() {
        super("Pixie_Bottle");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }
    
    @Override
    public String byMeta(int meta) {
        return "pixie_bottle_" + EntityPixie.Variant.get((byte) meta);
    }
    
    @Override
    public int getMaxMeta() {
        return EntityPixie.Variant.values().length;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < getMaxMeta(); i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return EnumActionResult.SUCCESS;
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) return EnumActionResult.FAIL;
        BlockPos blockpos = pos.offset(facing);
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos).expand(0., -1, 0);
        List<AxisAlignedBB> aabbs = world.getCollisionBoxes(null, axisalignedbb);
        double offset = 0;
        if (!aabbs.isEmpty()) {
            offset = axisalignedbb.minY;
            for (AxisAlignedBB bb : aabbs) offset = Math.max(bb.maxY, offset);
           offset -= pos.getY();
        }
        if (spawnPixie(world, stack, blockpos.getX() + 0.5, blockpos.getY() + offset, blockpos.getZ() + 0.5)) {
            player.addStat(StatList.getObjectUseStats(this));
            if (!player.isCreative()) {
                stack.shrink(1);
                if (!player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)))
                    player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
            }
        }
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag flag) {
        tooltips.add(new TextComponentTranslation("item.fbiomes.pixie_bottle.tooltip.variant",
                new TextComponentTranslation("entity.fbiomes.pixie.variant."
                        + EntityPixie.Variant.get((byte) stack.getMetadata()).getName())).getFormattedText());
        super.addInformation(stack, world, tooltips, flag);
    }
    
    public static boolean spawnPixie(World world, ItemStack stack, double x, double y, double z) {
        EntityPixie pixie = new EntityPixie(world);
        if (ForgeEventFactory.doSpecialSpawn(pixie, world, (float) x, (float) y, (float) z, null)) return false;
        pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("entity")) pixie.readFromNBT(nbt.getCompoundTag("entity"));
        }
        pixie.setVariant(EntityPixie.Variant.get((byte) stack.getMetadata()));
        if (stack.hasDisplayName()) pixie.setCustomNameTag(stack.getDisplayName());
        pixie.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360f), 0);
        pixie.rotationYawHead = pixie.rotationYaw;
        pixie.renderYawOffset = pixie.rotationYaw;
        world.spawnEntity(pixie);
        pixie.playLivingSound();
        return pixie.isAddedToWorld();
    }
    
    public static ItemStack bottlePixie(EntityPixie pixie) {
        ItemStack stack = new ItemStack(FBiomesItems.PIXIE_BOTTLE, 1, pixie.getVariant().ordinal());
        if (pixie.hasCustomName()) stack.setStackDisplayName(pixie.getCustomNameTag());
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = pixie.writeToNBT(new NBTTagCompound());
        nbt.removeTag("Pos");
        nbt.removeTag("Motion");
        nbt.removeTag("Rotation");
        nbt.removeTag("FallDistance");
        nbt.removeTag("Air");
        nbt.removeTag("OnGround");
        nbt.removeTag("Dimension");
        nbt.removeTag("Invulnerable");
        nbt.removeTag("PortalCooldown");
        nbt.removeTag("UUID");
        stack.getTagCompound().setTag("entity", nbt);
        pixie.setDead();
        return stack;
    }
    
}
