package net.smileycorp.fbiomes.common.entities;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

public class EntityWitchTrader extends EntityAgeable implements IMerchant, INpc {

    private EntityPlayer buyingPlayer;
    private MerchantRecipeList offers;

    public EntityWitchTrader(World world) {
        super(world);
        this.setSize(0.6f, 1.8f);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == Items.NAME_TAG) {
            stack.interactWithEntity(player, this, hand);
            return true;
        }
        if (holdingSpawnEggOfClass(stack, getClass()) |! isEntityAlive() || isTrading() || player.isSneaking()) return super.processInteract(player, hand);
        if (world.isRemote) return true;
        if (offers == null) populateBuyingList();
        if (hand == EnumHand.MAIN_HAND) player.addStat(StatList.TALKED_TO_VILLAGER);
        if (offers.isEmpty()) return super.processInteract(player, hand);
        setCustomer(player);
        player.displayVillagerTradeGui(this);
        return true;
    }

    public boolean isTrading() {
        return buyingPlayer != null;
    }

    private void populateBuyingList() {
        offers = new MerchantRecipeList();
        offers.add(new MerchantRecipe(new ItemStack(FBiomesBlocks.TOADSTOOL, 16), new ItemStack(FBiomesItems.PIXIE_HOUSING, 1, 1)));
        offers.add(new MerchantRecipe(new ItemStack(FBiomesBlocks.PURPLE_SHROOM, 20), new ItemStack(FBiomesBlocks.GREEN_SHROOM, 20),
                new ItemStack(FBiomesItems.PIXIE_HOUSING, 1, 2)));
    }

    @Override
    public void setCustomer(EntityPlayer player) {
        buyingPlayer = player;
    }

    @Override
    public EntityPlayer getCustomer() {
        return buyingPlayer;
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        return ForgeEventFactory.listTradeOffers(this, player, offers);
    }

    @Override
    public void setRecipes(MerchantRecipeList trades) {
        offers = trades;
    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {
        recipe.incrementToolUses();
        livingSoundTime = -getTalkInterval();
        if (!recipe.getRewardsExp()) return;
        world.spawnEntity(new EntityXPOrb(world, posX, posY + 0.5, posZ, 3 + rand.nextInt(4)));
    }

    @Override
    public void verifySellingItem(ItemStack stack) {
        if (world.isRemote || livingSoundTime <= 20 -getTalkInterval()) return;
        livingSoundTime = -getTalkInterval();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return getPosition();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public int getGrowingAge() {
        return 0;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

}
