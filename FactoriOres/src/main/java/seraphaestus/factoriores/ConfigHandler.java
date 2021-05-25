package seraphaestus.factoriores;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	
	public static class Client {
		
		public final ForgeConfigSpec.BooleanValue staticDrills;
		public final ForgeConfigSpec.DoubleValue miningParticleFrequency;
		public final ForgeConfigSpec.DoubleValue furnaceParticleFrequency;
		public final ForgeConfigSpec.BooleanValue enableLixiviantPonder;
		public final ForgeConfigSpec.BooleanValue enableFluidMiningPonder;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("rendering");
			staticDrills = builder
					.comment("Set this to true if you are using lots of miners and are experiencing rendering lag. Will disable animations and render them statically.")
					.define("staticDrills", false);
			builder.pop();
			
			builder.push("particles");
			miningParticleFrequency = builder
					.comment("The frequency in which block-breaking particles spawn while miners are active")
					.defineInRange("minerFrequency", 0.75, Double.MIN_VALUE, Double.MAX_VALUE);
			furnaceParticleFrequency = builder
					.comment("The frequency in which flame particles spawn while burner miners are active")
					.defineInRange("burnerFrequency", 0.75, Double.MIN_VALUE, Double.MAX_VALUE);
			builder.pop();
			
			builder.push("pondering");
			enableLixiviantPonder = builder
					.comment("Set this to false to disable this ponder scene, for if your pack doesn't use the lixiviant mechanics")
					.define("enableLixiviantPonder", true);
			enableFluidMiningPonder = builder
					.comment("Set this to false to disable this ponder scene, for if your pack doesn't use fluid deposits")
					.define("enableFluidMiningPonder", true);
			builder.pop();
		}
	}

	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Common {
		public final ForgeConfigSpec.IntValue minerMaxDepth;
		public final ForgeConfigSpec.IntValue minerRangeCreative;
		public final ForgeConfigSpec.IntValue minerRangeBurner;
		public final ForgeConfigSpec.IntValue minerRangeElectrical;
		public final ForgeConfigSpec.IntValue minerRangeMechanical;
		public final ForgeConfigSpec.IntValue fluidDepositAmount;
		public final ForgeConfigSpec.BooleanValue silentMiners;
		public final ForgeConfigSpec.BooleanValue sulfurEnabled;
		public final ForgeConfigSpec.BooleanValue worldgenEnabled;
		public final ForgeConfigSpec.IntValue genDistanceNear;
		public final ForgeConfigSpec.IntValue genDistanceMid;
		public final ForgeConfigSpec.IntValue genDistanceFar;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> oresWhichRequireLiquidMining;
		public final ForgeConfigSpec.IntValue lixiviantDrainedPerUnitProgress;
		
		public Common(ForgeConfigSpec.Builder builder) {
			sulfurEnabled = builder
					.comment("Set this to false to disable Sulfur and Sulfuric Acid")
					.define("sulfurEnabled", true);
			
			builder.push("miners");
			minerMaxDepth = builder
					.comment("The maximum depth below a miner which it will check for mineable ores")
					.defineInRange("minerMaxDepth", 10, 1, 255);
			
			minerRangeCreative = builder
					.comment("The range that this type of miner will have when checking for mineable ores. (0 = 1x1, 1 = 3x3, 2 = 5x5, etc.)")
					.defineInRange("minerRangeCreative", 0, 0, 3);
			minerRangeBurner = builder
					.comment("The range that this type of miner will have when checking for mineable ores. (0 = 1x1, 1 = 3x3, 2 = 5x5, etc.)")
					.defineInRange("minerRangeBurner", 1, 0, 3);
			minerRangeElectrical = builder
					.comment("The range that this type of miner will have when checking for mineable ores. (0 = 1x1, 1 = 3x3, 2 = 5x5, etc.)")
					.defineInRange("minerRangeElectrical", 2, 0, 3);
			minerRangeMechanical = builder
					.comment("The range that this type of miner will have when checking for mineable ores. (0 = 1x1, 1 = 3x3, 2 = 5x5, etc.)")
					.defineInRange("minerRangeMechanical", 1, 0, 3);
			
			fluidDepositAmount = builder
					.comment("The amount in mb of fluid that is extracted in one mining action")
					.defineInRange("fluidDepositAmount", 1000, 1, Integer.MAX_VALUE);
			
			silentMiners = builder
					.comment("Set this to true to disable the burner miner furnace sound")
					.define("silent", false);
			builder.pop();
			
			builder.push("worldgen");
			worldgenEnabled = builder
					.comment("Set this to false to disable ore worldgen. More fine-tuned customization should be done with datapacks.")
					.define("worldgen", true);
			
			genDistanceNear = builder
					.comment("The distance from spawn beyond which ore deposits in the NEAR category will start to spawn")
					.defineInRange("genDistanceNear", 320, 0, Integer.MAX_VALUE);
			genDistanceMid = builder
					.comment("The distance from spawn beyond which ore deposits in the MID category will start to spawn")
					.defineInRange("genDistanceMid", 640, 0, Integer.MAX_VALUE);
			genDistanceFar = builder
					.comment("The distance from spawn beyond which ore deposits in the FAR category will start to spawn")
					.defineInRange("genDistanceFar", 1024, 0, Integer.MAX_VALUE);
			
			builder.pop();
			
			builder.push("lixiviant");
			oresWhichRequireLiquidMining = builder
					.comment("List of ore deposit registry names that will require a Lixiviant liquid to be mined.")
					.defineList("oresWhichRequireLiquidMining", Collections.singletonList("factoriores:uranium_ore"), s -> s instanceof String && ResourceLocation.tryCreate((String) s) != null);
			lixiviantDrainedPerUnitProgress = builder
					.comment("The amount in mb of lixiviant drained per unit of progress made while mining (out of 200 per mining action)")
					.defineInRange("lixiviantDrainedPerUnitProgress", 1, 1, 10);
			builder.pop();
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static void onConfigLoad() {
		FactoriOres.configLoaded = true;
	}
}
