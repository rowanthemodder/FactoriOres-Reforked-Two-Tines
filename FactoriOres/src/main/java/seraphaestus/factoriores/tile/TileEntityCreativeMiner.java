package seraphaestus.factoriores.tile;

import seraphaestus.factoriores.ConfigHandler;
import seraphaestus.factoriores.Registrar;

public class TileEntityCreativeMiner extends TileEntityMiner {

	public TileEntityCreativeMiner() {
		super(Registrar.tileCreativeMiner.get());
	}

	@Override
	public void tick() {
		boolean hasChanged = doMining();
		if (hasChanged) markDirty();
	}

	@Override
	public boolean calculateIsEnabled() {
		return super.calculateIsEnabled();
	}
	
	@Override
	protected int getRange() {
		return ConfigHandler.COMMON.minerRangeCreative.get();
	}

}
