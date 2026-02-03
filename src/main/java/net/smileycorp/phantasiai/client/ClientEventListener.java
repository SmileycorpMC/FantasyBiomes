package net.smileycorp.phantasiai.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.EnumParticle;
import net.smileycorp.phantasiai.common.world.biomes.PhantasiaiBiomes;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class ClientEventListener {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.isGamePaused()) return;
        EntityPlayerSP player = mc.player;
        if (player == null) return;
        Random rand = player.getRNG();
        Biome biome = player.world.getBiome(player.getPosition());
        if (biome == PhantasiaiBiomes.DEAD_MARSH) {
            Vec3d pos = new Vec3d(player.posX + (rand.nextFloat() - 0.5) * 10, player.posY + rand.nextFloat() * 3,
                    player.posZ + (rand.nextFloat() - 0.5) * 10);
            Vec3d vec = DirectionUtils.getDirectionVec(pos, new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ));
            ClientHandler.spawnParticle(EnumParticle.PIXEL, pos.x, pos.y, pos.z, 0d, 20d,
                    rand.nextFloat() * vec.x, rand.nextFloat() * vec.y, rand.nextFloat() * vec.z, 0.5);
        }
        if (biome == PhantasiaiBiomes.ENCHANTED_THICKET) {
            ClientHandler.spawnParticle(EnumParticle.PIXEL, player.posX + (rand.nextFloat() - 0.5) * 10, player.posY + 5 + rand.nextFloat() * 3,
                    player.posZ + (rand.nextFloat() - 0.5) * 10, (double)0x570300, 60d, (rand.nextFloat() - 0.5f) * 0.2, - (rand.nextFloat() + 0.5) * 0.2,  (rand.nextFloat() - 0.5f) * 0.2, 0.5);
        }
    }

}
