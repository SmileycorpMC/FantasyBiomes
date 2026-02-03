package net.smileycorp.phantasiai.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.phantasiai.common.network.FBiomesParticleMessage;
import net.smileycorp.phantasiai.common.network.PacketHandler;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum EnumParticle {
    
    PIXEL((buf, data) -> {buf.writeInt((int)(double)data[0]); buf.writeInt((int)(double)data[1]); buf.writeDouble(data[2]);
        buf.writeDouble(data[3]); buf.writeDouble(data[4]); buf.writeDouble(data[5]);},
            buf -> new Double[] {(double) buf.readInt(), (double) buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()}),
    PIXEL_FULLBRIGHT((buf, data) -> {buf.writeInt((int)(double)data[0]); buf.writeInt((int)(double)data[1]); buf.writeDouble(data[2]);
        buf.writeDouble(data[3]); buf.writeDouble(data[4]);  buf.writeDouble(data[5]);},
            buf -> new Double[] {(double) buf.readInt(), (double) buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()}),
    TWINKLE((buf, data) -> {buf.writeInt((int)(double)data[0]); buf.writeDouble(data[1]); buf.writeDouble(data[2]); buf.writeDouble(data[3]);},
            buf -> new Double[] {(double) buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble()});
    
    private final BiConsumer<ByteBuf, Double[]> writeFunc;
    private final Function<ByteBuf, Double[]> readFunc;
    
    EnumParticle(BiConsumer<ByteBuf, Double[]> writeFunc, Function<ByteBuf, Double[]> readFunc) {
        this.writeFunc = writeFunc;
        this.readFunc = readFunc;
    }
    
    public void writeBytes(ByteBuf buf, Double[] data) {
        writeFunc.accept(buf, data);
    }
    
    public Double[] readBytes(ByteBuf buf) {
        return readFunc.apply(buf);
    }
    
    public void send(int dimension, double x, double y, double z, Double... data) {
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new FBiomesParticleMessage(this, x, y, z, data),
                new NetworkRegistry.TargetPoint(dimension, x, y, z, 32));
    }
    
}
