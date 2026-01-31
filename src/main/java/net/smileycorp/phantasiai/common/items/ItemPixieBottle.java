package net.smileycorp.phantasiai.common.items;

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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.phantasiai.common.entities.EntityPixie;
import net.smileycorp.phantasiai.common.entities.PixieData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPixieBottle extends ItemFBiomes {
    
    public ItemPixieBottle() {
        super("Pixie_Bottle");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }
    
    @Override
    public String byMeta(int meta) {
        return "pixie_bottle_" + PixieData.Variant.get((byte) meta);
    }
    
    @Override
    public int getMaxMeta() {
        return PixieData.Variant.values().length;
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
        PixieData pixie = getPixie(stack);
        if (pixie != null) pixie.addTooltips(tooltips);
        else tooltips.add(new TextComponentTranslation("tooltip.phantasiai.pixie_variant",
                new TextComponentTranslation("entity.phantasiai.pixie.variant."
                        + PixieData.Variant.get((byte) stack.getMetadata()).getName())).getFormattedText());
        super.addInformation(stack, world, tooltips, flag);
    }
    
    public static boolean spawnPixie(World world, ItemStack stack, double x, double y, double z) {
        PixieData data = getPixie(stack);
        if (data == null) {
            data = PixieData.newPixie(PixieData.Variant.get((byte) stack.getMetadata()), world.rand);
            if (stack.hasDisplayName()) data.setName(stack.getDisplayName());
        }
        EntityPixie pixie = data.spawn(world, x, y, z, true);
        if (pixie == null) return false;
        pixie.playLivingSound();
        return pixie.isAddedToWorld();
    }

    public static PixieData getPixie(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("pixie")) return null;
        PixieData pixie = PixieData.fromNbt(nbt.getCompoundTag("pixie"));
        pixie.setVariant(PixieData.Variant.get((byte) stack.getMetadata()));
        if (stack.hasDisplayName()) pixie.setName(stack.getDisplayName());
        nbt.setTag("pixie", pixie.toNbt());
        return pixie;
    }
    
    public static ItemStack bottlePixie(PixieData pixie) {
        ItemStack stack = new ItemStack(PhantasiaiItems.PIXIE_BOTTLE, 1, pixie.getVariant().ordinal());
        if (pixie.hasName()) stack.setStackDisplayName(pixie.getName());
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("pixie", pixie.toNbt());
        return stack;
    }
    
}
