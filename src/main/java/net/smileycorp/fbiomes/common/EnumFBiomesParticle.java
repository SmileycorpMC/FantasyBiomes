package net.smileycorp.fbiomes.common;

import io.netty.buffer.ByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum EnumFBiomesParticle {
    
    PIXEL((buf, data) -> {buf.writeInt((int)(double)data[0]); buf.writeInt((int)(double)data[1]); buf.writeDouble(data[2]);
        buf.writeDouble(data[3]); buf.writeDouble(data[4]);},
            buf -> new Double[] {(double) buf.readInt(), (double) buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble()}),;
    
    private final BiConsumer<ByteBuf, Double[]> writeFunc;
    private final Function<ByteBuf, Double[]> readFunc;
    
    EnumFBiomesParticle(BiConsumer<ByteBuf, Double[]> writeFunc, Function<ByteBuf, Double[]> readFunc) {
        this.writeFunc = writeFunc;
        this.readFunc = readFunc;
    }
    
    public void writeBytes(ByteBuf buf, Double[] data) {
        writeFunc.accept(buf, data);
    }
    
    public Double[] readBytes(ByteBuf buf) {
        return readFunc.apply(buf);
    }
    
}
