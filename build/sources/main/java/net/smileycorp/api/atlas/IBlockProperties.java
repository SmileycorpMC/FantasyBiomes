package net.smileycorp.api.atlas;

public interface IBlockProperties {
	
	public default int getMaxMeta(){
		return 0;
	}
	
	public default boolean usesCustomItemHandler(){
		return false;
	}
}
