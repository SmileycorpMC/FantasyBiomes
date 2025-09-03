package net.smileycorp.fbiomes.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.fbiomes.common.Constants;

public class PacketHandler {

	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);

	public static void initPackets() {
		NETWORK_INSTANCE.registerMessage(FBiomesParticleMessage::process, FBiomesParticleMessage.class, 0, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PixieTableEnableMessage::process, PixieTableEnableMessage.class, 1, Side.SERVER);
	}

}
