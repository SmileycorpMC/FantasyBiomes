package net.smileycorp.phantasiai.common.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
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
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.smileycorp.atlas.api.util.RecipeUtils;
import net.smileycorp.phantasiai.common.entities.PixieData;
import net.smileycorp.phantasiai.common.inventory.InventoryPixieTable;
import net.smileycorp.phantasiai.common.items.ItemPixieBottle;
import net.smileycorp.phantasiai.common.recipe.IPixieRecipe;
import net.smileycorp.phantasiai.common.recipe.PixieRecipeManager;

import javax.annotation.Nullable;

public class TilePixieWorkshop extends TileEntity implements ITickable {

    public final InventoryPixieTable inventory = new InventoryPixieTable(this);
    private IRecipe currentRecipe;

    private final NonNullList<ItemStack> lastRecipe = NonNullList.withSize(9, ItemStack.EMPTY);
    private float recipeProgress = 0;
    private float progressPercent = 0;
    private NonNullList<PixieData> pixies = NonNullList.create();
    private float baseEfficiency = 1;
    private float efficiency = 1;
    private int foodTimer = 0;
    private ItemStack consumedFood = ItemStack.EMPTY;
    private boolean isActive = false;

    @Override
    public void update() {
        if (world.isRemote) return;
        if (foodTimer > 0) foodTimer -= getPixieCount();
        if (foodTimer == 0) {
            baseEfficiency = 1;
            consumedFood = ItemStack.EMPTY;
            calculateEfficiency();
        }
        if(currentRecipe == null |!isActive) return;
        if(currentRecipe.matches(inventory.getCraftingWrapper(), world)) tryCraft();
        if (foodTimer > 0) return;
        if (tryConsumeFood()) return;
        foodTimer--;
        if (foodTimer % 20 != 0) return;
        for (PixieData pixie : pixies) pixie.setMood(pixie.getMood() - pixie.getMoodDecay());
        calculateEfficiency();
    }

    private boolean tryConsumeFood() {
        if (pixies.isEmpty()) return false;
        for (int slot = 12; slot < 19; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            if (consumeFood(slot)) return true;
        }
        return false;
    }

    public boolean consumeFood(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        float efficiency = PixieRecipeManager.getFoodEfficiency(stack);
        if (efficiency <= 1) return false;
        baseEfficiency = efficiency;
        for (PixieData pixie : pixies) pixie.setMood(pixie.getMood() + pixie.getMoodGain());
        consumedFood = stack.splitStack(1);
        foodTimer = 600;
        calculateEfficiency();
        markDirty();
        return true;
    }

    public void tryFindingRecipe() {
        currentRecipe = PixieRecipeManager.findRecipe(inventory, world);
        if (currentRecipe != null) return;
        recipeProgress = 0;
        progressPercent = 0;
        markDirty();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        recipeProgress = 0;
        progressPercent = 0;
        markDirty();
    }
    
    public void tryCraft() {
        int recipeDuration = (currentRecipe instanceof IPixieRecipe ? ((IPixieRecipe)currentRecipe).getCraftingDuration() : 500);
        progressPercent = recipeProgress / (float) recipeDuration;
        markDirty();
        if ((recipeProgress += efficiency) >= recipeDuration) {
            FakePlayer player = FakePlayerFactory.getMinecraft((WorldServer) world);
            recipeProgress = recipeDuration;
            ItemStack result = currentRecipe.getCraftingResult(inventory.getCraftingWrapper());
            result.onCrafting(world, player, result.getCount());
            FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, inventory.getCraftingWrapper());
            if (result.isEmpty()) return;
            for (int i = 9; i < 12; i++) {
                if (!RecipeUtils.compareItemStacksCanFit(result, inventory.getStackInSlot(i))) continue;
                result.setCount(result.getCount() + inventory.getStackInSlot(i).getCount());
                inventory.setStackInSlot(i, result);
                NonNullList<ItemStack> remainingItems = currentRecipe.getRemainingItems(inventory.getCraftingWrapper());
                for (int j = 0; j < 9; j++) {
                    ItemStack stack = inventory.getStackInSlot(j);
                    ItemStack cache = stack.copy();
                    cache.setCount(1);
                    lastRecipe.set(0, cache);
                    if (stack.isEmpty()) continue;
                    ItemStack container = remainingItems.get(j);
                    if (container.isEmpty()) stack.shrink(1);
                    else inventory.setStackInSlot(j, container.copy());
                }
                recipeProgress = 0;
                progressPercent = 0;
                break;
            }
        }
    }

    private void calculateEfficiency() {
        if (pixies.isEmpty()) {
            efficiency = 1;
            return;
        }
        efficiency = baseEfficiency;
        for (PixieData pixie : pixies) efficiency *= pixie.getEfficiency();
    }

    public void destroy() {
        for (PixieData pixie : pixies) pixie.spawn(world, pos.getX() + world.rand.nextInt(2) - 1,
                pos.getY() + 0.5, pos.getZ() + world.rand.nextInt(2) - 1);
        for (int slot = 0; slot < inventory.getSlots(); slot ++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        }
    }

    public boolean isCrafting() {
        return recipeProgress > 0;
    }

    public NonNullList<ItemStack> getLastRecipe() {
        return lastRecipe;
    }

    public float getCraftingProgress() {
        return progressPercent;
    }

    public int getFoodTimerProgress() {
        return foodTimer <= 0 ? 0 : 1 + (int) (((float)foodTimer / 600f) * 7);
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
        PixieData pixie = ItemPixieBottle.getPixie(stack);
        if (pixie == null) {
            pixie = PixieData.newPixie(PixieData.Variant.get((byte) stack.getMetadata()), world.rand);
            if (stack.hasDisplayName()) pixie.setName(stack.getDisplayName());
        }
        pixies.add(pixie);
        calculateEfficiency();
        markDirty();
        return true;
    }
    
    public PixieData getPixie(int index) {
        return pixies.get(index);
    }
    
    public ItemStack bottlePixie() {
        int count = getPixieCount();
        if (count == 0) return ItemStack.EMPTY;
        PixieData pixie = pixies.get(count - 1);
        pixies.remove(count - 1);
        calculateEfficiency();
        markDirty();
        return ItemPixieBottle.bottlePixie(pixie);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!hasWorld()) return;
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase pixie : nbt.getTagList("pixies", 10)) pixies.add(PixieData.fromNbt((NBTTagCompound) pixie));
        recipeProgress = nbt.getFloat("recipeProgress");
        foodTimer = nbt.getInteger("foodTimer");
        if (nbt.hasKey("consumedFood")) {
            consumedFood = new ItemStack(nbt.getCompoundTag("consumedFood"));
            baseEfficiency = PixieRecipeManager.getFoodEfficiency(consumedFood);
        }
        isActive = nbt.getBoolean("isActive");
        ItemStackHelper.loadAllItems(nbt.getCompoundTag("lastRecipe"), lastRecipe);
        calculateEfficiency();
        tryFindingRecipe();
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
        nbt.setTag("lastRecipe", ItemStackHelper.saveAllItems(new NBTTagCompound(), lastRecipe));
        return super.writeToNBT(nbt);
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        pixies.clear();
        for (NBTBase pixie : nbt.getTagList("pixies", 10)) pixies.add(PixieData.fromNbt((NBTTagCompound) pixie));
        progressPercent = nbt.getFloat("recipeProgress");
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
        nbt.setFloat("recipeProgress", progressPercent);
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
            return (T) inventory.getCapabilityWrapper();
        return null;
    }

}
