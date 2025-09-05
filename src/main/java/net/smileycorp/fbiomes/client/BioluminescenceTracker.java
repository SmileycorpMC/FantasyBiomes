package net.smileycorp.fbiomes.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.potion.FBiomesPotions;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class BioluminescenceTracker {

    public static BioluminescenceTracker instance = new BioluminescenceTracker();

    private Set<BlockPos> positions = Sets.newHashSet();
    private AtomicBoolean running = new AtomicBoolean(false);

    private BioluminescenceTracker() {}

    public boolean isRunning() {
        return running.get();
    }

    public void tick(World world) {
        List<Entity> entities = Lists.newArrayList(world.loadedEntityList);
        entities.addAll(world.playerEntities);
        Thread thread = new Thread(() -> {
            running.set(true);
            Set<BlockPos> positions = Sets.newHashSet();
            for (Entity entity : entities) {
                System.out.println(entity);
                if (!(entity instanceof EntityLivingBase)) continue;
                if (((EntityLivingBase) entity).isPotionActive(FBiomesPotions.BIOLUMINESCENCE))
                    positions.add(entity.getPosition());
            }
            this.positions = positions;
            running.set(false);
        });
        thread.setPriority(1);
        thread.start();
    }

    public boolean isLightSource(BlockPos pos) {
        return positions.contains(pos);
    }

}
