package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieJar;
import net.smileycorp.phantasiai.common.entities.PixieData;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPixieJar extends BlockBase implements ITileEntityProvider {

    public BlockPixieJar() {
        super("pixie_jar", Constants.MODID, Material.GLASS, SoundType.GLASS, 0.3f, 1.5f, 0, Phantasiai.TAB);
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

    @Override
    public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
        for (int i = 0; i < getMaxMeta(); i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePixieJar();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TilePixieJar)) return;
        TilePixieJar jar = (TilePixieJar) tile;
        if (!jar.hasPixie()) jar.setPixie(PixieData.getDefault(PixieData.Variant.get((byte) stack.getMetadata())));
    }

    @Override
    public String byMeta(int meta) {
        return "pixie_jar_" + PixieData.Variant.get((byte) meta);
    }

    @Override
    public int getMaxMeta() {
        return PixieData.Variant.values().length;
    }

    @Override
    public boolean usesCustomItemHandler() {
        return true;
    }

    public static PixieData getPixie(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("BlockEntityTag")) return null;
        PixieData pixie = PixieData.fromNbt(nbt.getCompoundTag("BlockEntityTag").getCompoundTag("pixie"));
        pixie.setVariant(PixieData.Variant.get((byte) stack.getMetadata()));
        if (stack.hasDisplayName()) pixie.setName(stack.getDisplayName());
        nbt.setTag("pixie", pixie.toNbt());
        return pixie;
    }

    public static ItemStack jarPixie(PixieData pixie) {
        ItemStack stack = new ItemStack(PhantasiaiBlocks.PIXIE_JAR, 1, pixie.getVariant().ordinal());
        if (pixie.hasName()) stack.setStackDisplayName(pixie.getName());
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagCompound beTag = nbt.hasKey("BlockEntityTag") ? nbt.getCompoundTag("BlockEntityTag") : new NBTTagCompound();
        beTag.setTag("pixie", pixie.toNbt());
        nbt.setTag("BlockEntityTag", beTag);
        return stack;
    }

}
