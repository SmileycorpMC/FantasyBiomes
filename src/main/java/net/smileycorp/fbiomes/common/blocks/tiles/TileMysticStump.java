package net.smileycorp.fbiomes.common.blocks.tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.smileycorp.atlas.api.util.RecipeUtils;
import net.smileycorp.fbiomes.common.entities.EntityPixie;
import net.smileycorp.fbiomes.common.entities.Pixie;
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
    private float recipeProgress = 0;
    private float progressPercent = 0;
    private List<Pixie> pixies = Lists.newArrayList();
    private float baseEfficiency = 1;
    private float efficiency = 1;
    private int foodTimer = 0;
    private ItemStack consumedFood = ItemStack.EMPTY;
    private boolean isActive = false;
    
    @Override
    public void update() {
        if (foodTimer >= 0) foodTimer--;
        if (foodTimer == 0) {
            baseEfficiency = 1;
            consumedFood = ItemStack.EMPTY;
            calculateEfficiency();
        }
        if(currentRecipe == null |!isActive) return;
        if(currentRecipe.matches(inventory.getCraftingWrapper(), world)) tryCraft();
        if (foodTimer <= 0) tryConsumeFood();
        if (foodTimer < 0) {
            foodTimer--;
            if (foodTimer % 20 == 0) for (Pixie pixie : pixies) pixie.setMood(pixie.getMood() - pixie.getMoodDecay());
        }
    }

    private void tryConsumeFood() {
        for (int slot = 14; slot < 19; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof ItemFood)) continue;
            if (consumeFood(slot)) return;
        }
    }

    public boolean consumeFood(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        float efficiency = PixieRecipeManager.getFoodEfficiency(stack);
        if (efficiency <= 1) return false;
        baseEfficiency = efficiency;
        for (Pixie pixie : pixies) pixie.setMood(pixie.getMood() + pixie.getMoodGain());
        consumedFood = stack.splitStack(1);
        foodTimer = 200;
        calculateEfficiency();
        return true;
    }

    public void tryFindingRecipe() {
        currentRecipe = PixieRecipeManager.findRecipe(inventory, world);
        if (currentRecipe != null) tryCraft();
        else {
            recipeProgress = 0;
            progressPercent = 0;
            markDirty();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        markDirty();
    }
    
    public void tryCraft() {
        int recipeDuration = (currentRecipe instanceof IPixieRecipe ? ((IPixieRecipe)currentRecipe).getCraftingDuration() : 500);
        progressPercent = recipeProgress / (float) recipeDuration;
        if ((recipeProgress += efficiency) >= recipeDuration) {
            ItemStack result = currentRecipe.getCraftingResult(inventory.getCraftingWrapper());
            if (result.isEmpty()) return;
            for (int i = 9; i < 13; i++) if (!RecipeUtils.compareItemStacksCanFit(result, inventory.getStackInSlot(i))) {
                result.setCount(result.getCount() + inventory.getStackInSlot(i).getCount());
                inventory.setStackInSlot(i, result);
                for (int j = 0; j < 9; j++) {
                    ItemStack stack = inventory.getStackInSlot(j);
                    if (stack.isEmpty()) continue;
                    ItemStack container = stack.getItem().getContainerItem(stack);
                    if (container.isEmpty()) stack.shrink(1);
                    else inventory.setStackInSlot(j, container);
                }
                recipeProgress = 0;
                progressPercent = 0;
                break;
            }
        }
    }

    private void calculateEfficiency() {
        efficiency = baseEfficiency;
        for (Pixie pixie : pixies) efficiency *= pixie.getEfficiency();
        markDirty();
    }

    public float getCraftingProgress() {
        return progressPercent;
    }

    public int getFoodTimerProgress() {
        return foodTimer <= 0 ? 0 : 1 + (int) (((float)foodTimer / 200f) * 7);
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getBaseEfficiency() {
        return baseEfficiency;
    }

    public ItemStack getLastConsumedFood() {
        return consumedFood;
    }
    
    public int getPixieCount() {
        return pixies.size();
    }
    
    public boolean addPixie(ItemStack stack) {
        if (pixies.size() > 3) return false;
        Pixie pixie = ItemPixieBottle.getPixie(stack);
        if (pixie == null) {
            pixie = Pixie.newPixie(EntityPixie.PixieVariant.get((byte) stack.getMetadata()), world.rand);
            if (stack.hasDisplayName()) pixie.setName(stack.getDisplayName());
        }
        pixies.add(pixie);
        markDirty();
        return true;
    }
    
    public Pixie getPixie(int index) {
        return pixies.get(index);
    }
    
    public ItemStack bottlePixie() {
        int count = getPixieCount();
        if (count == 0) return ItemStack.EMPTY;
        Pixie pixie = pixies.get(count - 1);
        pixies.remove(count - 1);
        markDirty();
        return ItemPixieBottle.bottlePixie(pixie);
    }

    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        calculateEfficiency();
        super.markDirty();
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase pixie : nbt.getTagList("pixies", 10)) pixies.add(Pixie.fromNbt((NBTTagCompound) pixie));
        recipeProgress = nbt.getFloat("recipeProgress");
        foodTimer = nbt.getInteger("foodTimer");
        if (nbt.hasKey("consumedFood")) {
            consumedFood = new ItemStack(nbt.getCompoundTag("consumedFood"));
            baseEfficiency = PixieRecipeManager.getFoodEfficiency(consumedFood);
        }
        isActive = nbt.getBoolean("isActive");
        calculateEfficiency();
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", inventory.serializeNBT());
        NBTTagList pixies = new NBTTagList();
        this.pixies.forEach(pixie -> pixies.appendTag(pixie.toNbt()));
        nbt.setTag("pixies", pixies);
        nbt.setFloat("recipeProgress", recipeProgress);
        nbt.setFloat("baseEfficiency", baseEfficiency);
        nbt.setInteger("foodTimer", foodTimer);
        if (!consumedFood.isEmpty()) nbt.setTag("consumedFood", consumedFood.writeToNBT(new NBTTagCompound()));
        nbt.setBoolean("isActive", isActive);
        return super.writeToNBT(nbt);
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase pixie : nbt.getTagList("pixies", 10)) pixies.add(Pixie.fromNbt((NBTTagCompound) pixie));
        progressPercent = nbt.getFloat("recipe_progress");
        efficiency = nbt.getFloat("efficiency");
        baseEfficiency = nbt.getFloat("baseEfficiency");
        foodTimer = nbt.getInteger("foodTimer");
        if (nbt.hasKey("consumedFood")) consumedFood = new ItemStack(nbt.getCompoundTag("consumedFood"));
        isActive = nbt.getBoolean("isActive");
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("inventory", inventory.serializeNBT());
        NBTTagList pixies = new NBTTagList();
        this.pixies.forEach(pixie -> pixies.appendTag(pixie.toNbt()));
        nbt.setTag("pixies", pixies);
        nbt.setFloat("recipe_progress", progressPercent);
        nbt.setFloat("efficiency", efficiency);
        nbt.setFloat("baseEfficiency", baseEfficiency);
        nbt.setInteger("foodTimer", foodTimer);
        if (!consumedFood.isEmpty()) nbt.setTag("consumedFood", consumedFood.writeToNBT(new NBTTagCompound()));
        nbt.setBoolean("isActive", isActive);
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
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
