package seraphaestus.factoriores.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import seraphaestus.factoriores.ConfigHandler;
import seraphaestus.factoriores.Registrar;
import seraphaestus.factoriores.block.BlockBurnerMiner;
import seraphaestus.factoriores.data.StateDataBurnerMiner;
import seraphaestus.factoriores.inventory.InventoryBurnerMiner;

public class TileEntityBurnerMiner extends TileEntityMiner {

	private static final int FUEL_SLOT = 1;

	public TileEntityBurnerMiner() {
		super(Registrar.tileBurnerMiner.get());
	}

	@Override
	public void init() {
		NUM_SLOTS = 2;
		items = InventoryBurnerMiner.createForTileEntity(NUM_SLOTS, this::canPlayerAccessInventory, this::markDirty);
		minerStateData = new StateDataBurnerMiner();
		minerStateData.miningTotalTime = getTotalMiningTime();
		//itemHandlerLazy = LazyOptional.of(() -> new InvWrapper((InventoryBurnerMiner)items));
		handlers = SidedInvWrapper.create(items, Direction.UP);
	}

	@Override
	public void tick() {
		boolean isBurningFlag = isBurning();
		boolean hasChanged = false;
		StateDataBurnerMiner stateData = (StateDataBurnerMiner) minerStateData;
		
		if (this.isBurning()) {
			++stateData.fuelBurnProgress;
		}
		
		if (!world.isRemote) {
			hasChanged = doMining() ? true : hasChanged;
			if (stateData.fuelBurnProgress == stateData.fuelBurnTotalTime) {
				stateData.fuelBurnProgress = 0;
				if (canKickstart()) doKickstart();
			}
			
			if (isBurningFlag != isBurning()) {
				hasChanged = true;
				world.setBlockState(pos, world.getBlockState(pos).with(BlockBurnerMiner.LIT, Boolean.valueOf(this.isBurning())), 3);
			}
		}
		
		if (hasChanged) markDirty();
	}

	@Override
	public boolean calculateIsEnabled() {
		return isBurning() && super.calculateIsEnabled();
	}

	private boolean isBurning() {
		return ((StateDataBurnerMiner)minerStateData).fuelBurnProgress > 0;
	}

	@Override
	public boolean canKickstart() {
		ItemStack itemstack = this.items.getStackInSlot(FUEL_SLOT);
		if (itemstack.isEmpty()) return false;
		return ForgeHooks.getBurnTime(itemstack) > 0;
	}
	
	@Override
	public void doKickstart() {
		ItemStack itemstack = this.items.getStackInSlot(FUEL_SLOT);
		((StateDataBurnerMiner) minerStateData).fuelBurnTotalTime = ForgeHooks.getBurnTime(itemstack);
		((StateDataBurnerMiner) minerStateData).fuelBurnProgress = 1;
		items.decrStackSize(FUEL_SLOT, 1);
		super.doKickstart();
	}
	
	public boolean addFuel(ItemStack fuel) {
		ItemStack fuelSlot = items.getStackInSlot(FUEL_SLOT);
		if (fuelSlot.isEmpty()) {
			items.setInventorySlotContents(FUEL_SLOT, fuel);
			return true;
		}
		if (fuelSlot.isItemEqual(fuel) && fuelSlot.getCount() < fuelSlot.getMaxStackSize()) {
			items.increaseStackSize(FUEL_SLOT, fuel);
			return true;
		}
		return false;
	}
	
	@Override
	protected int getRange() {
		return ConfigHandler.COMMON.minerRangeBurner.get();
	}

}