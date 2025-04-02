package net.smileycorp.fbiomes.common.blocks.tile;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.smileycorp.atlas.api.util.RecipeUtils;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;
import net.smileycorp.fbiomes.common.items.ItemPixieBottle;
import net.smileycorp.fbiomes.common.recipe.IPixieRecipe;
import net.smileycorp.fbiomes.common.recipe.PixieRecipeManager;

import javax.annotation.Nullable;
import java.util.List;

public class TileMysticStump extends TileEntity implements ITickable {
    
    //Credit: slava_110
    
    public final InventoryPixieTable inventory = new InventoryPixieTable(this);
    private IRecipe currentRecipe = null;
    private int recipeProgress = 0;
    private float progressPercent = 0;
    private List<EntityPixie> pixies = Lists.newArrayList();
    
    @Override
    public void update() {
        if(currentRecipe == null) return;
        if(currentRecipe.matches(inventory.getCraftingWrapper(), world)) {
            tryCraft();
        }
    }
    
    public void tryFindingRecipe() {
        currentRecipe = PixieRecipeManager.findRecipe(inventory, world);
        System.out.println(currentRecipe);
        if(currentRecipe != null) tryCraft();
    }
    
    public void tryCraft() {
        int recipeDuration = (currentRecipe instanceof IPixieRecipe ? ((IPixieRecipe)currentRecipe).getCraftingDuration() : 500);
        progressPercent = (float) recipeProgress / (float) recipeDuration;
        if(recipeProgress++ >= recipeDuration) {
            ItemStack result = currentRecipe.getCraftingResult(inventory.getCraftingWrapper());
            if (result.isEmpty()) return;
            for (int i = 9; i < 13; i++) if (RecipeUtils.compareItemStacksCanFit(result, inventory.getStackInSlot(i))) {
                result.setCount(result.getCount() + inventory.getStackInSlot(i).getCount());
                inventory.setStackInSlot(i, result);
                for (int j = 0; j < 9; j++) {
                    ItemStack stack = inventory.getStackInSlot(j);
                    if (stack.isEmpty()) continue;
                    ItemStack container = stack.getItem().getContainerItem(stack);
                    if (container.isEmpty()) stack.shrink(1);
                    else  inventory.setStackInSlot(j, container);
                }
                recipeProgress = 0;
                progressPercent = 0;
                break;
            }
        }
    }
    
    public float getCraftingProgress() {
        return progressPercent;
    }
    
    public int getPixieCount() {
        return pixies.size();
    }
    
    public boolean addPixie(ItemStack stack) {
        if (pixies.size() > 3) return false;
        EntityPixie pixie = new EntityPixie(world);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("EntityData")) pixie.readFromNBT(nbt.getCompoundTag("EntityData"));
        }
        pixie.setVariant(EntityPixie.Variant.get((byte) stack.getMetadata()));
        if (stack.hasDisplayName()) pixie.setCustomNameTag(stack.getDisplayName());
        pixies.add(pixie);
        return true;
    }
    
    public ItemStack getPixie() {
        int count = getPixieCount();
        if (count == 0) return ItemStack.EMPTY;
        EntityPixie pixie = pixies.get(count - 1);
        pixies.remove(count - 1);
        return ItemPixieBottle.bottlePixie(pixie);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase nbt : compound.getTagList("pixies", 10)) {
            EntityPixie pixie = new EntityPixie(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0));
            pixie.readFromNBT((NBTTagCompound) nbt);
            pixies.add(pixie);
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        NBTTagList pixies = new NBTTagList();
        for (EntityPixie pixie : this.pixies) {
            NBTTagCompound nbt = new NBTTagCompound();
            pixie.writeEntityToNBT(nbt);
            pixies.appendTag(nbt);
        }
        return super.writeToNBT(compound);
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
        super.deserializeNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase nbt : compound.getTagList("pixies", 10)) {
            EntityPixie pixie = new EntityPixie(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0));
            pixie.readFromNBT((NBTTagCompound) nbt);
            pixies.add(pixie);
        }
        progressPercent = compound.getFloat("recipe_progress");
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setTag("inventory", inventory.serializeNBT());
        NBTTagList pixies = new NBTTagList();
        for (EntityPixie pixie : this.pixies) {
            NBTTagCompound nbt = new NBTTagCompound();
            pixie.writeEntityToNBT(nbt);
            pixies.appendTag(nbt);
        }
        compound.setFloat("recipe_progress", progressPercent);
        return compound;
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN);
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN))
            return (T) inventory;
        return null;
    }
    
}
