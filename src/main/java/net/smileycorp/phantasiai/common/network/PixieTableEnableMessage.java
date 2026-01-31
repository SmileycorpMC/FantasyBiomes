package net.smileycorp.phantasiai.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieWorkshop;

public class PixieTableEnableMessage implements IMessage {

	private BlockPos pos;
	private boolean consume;

	public PixieTableEnableMessage() {}

	public PixieTableEnableMessage(BlockPos pos, boolean consume) {
		this.pos = pos;
		this.consume = consume;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		consume = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(consume);
	}
	
	public IMessage process(MessageContext ctx) {
		if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(pos);
			if (tile instanceof TilePixieWorkshop) ((TilePixieWorkshop)tile).setActive(consume);
		});
		return null;
	}
	
}
