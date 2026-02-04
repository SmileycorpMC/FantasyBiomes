package net.smileycorp.phantasiai.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class PhantasiaiCapabilities {

    @CapabilityInject(PixieStorage.class)
    public static final Capability<PixieStorage> PIXIE_STORAGE = null;

    public static void init() {
        CapabilityManager.INSTANCE.register(PixieStorage.class, PixieStorage.STORAGE, () -> null);
    }

}
