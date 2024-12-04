package net.smileycorp.fbiomes.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.fbiomes.client.ClientHandler;
import net.smileycorp.fbiomes.common.EnumFBiomesParticle;

public class FBiomesParticleMessage implements IMessage {
	
	private EnumFBiomesParticle type = EnumFBiomesParticle.PIXEL;
	private double x, y, z = 0;
	private Double[] data = {};
	
	public FBiomesParticleMessage() {}
	
	public FBiomesParticleMessage(EnumFBiomesParticle type, double x, double y, double z, Double... data) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = EnumFBiomesParticle.values()[buf.readByte()];
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		data = type.readBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((byte) type.ordinal());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		type.writeBytes(buf, data);
	}
	
	public IMessage process(MessageContext ctx) {
		if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> ClientHandler.spawnParticle(type, x, y, z, data));
		return null;
	}
	
}
