package seraphaestus.factoriores.worldgen;


import java.util.LinkedList;
import java.util.List;


import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import seraphaestus.factoriores.ConfigHandler;
import seraphaestus.factoriores.Registrar;

@Mod.EventBusSubscriber
public class WorldGenEventHandler {
	private static final List<GenerationStage.Decoration> decorations = new LinkedList<>();
	static {
		decorations.add(GenerationStage.Decoration.UNDERGROUND_ORES);
		decorations.add(GenerationStage.Decoration.UNDERGROUND_DECORATION);
	}
	
	@SubscribeEvent
	public static void onBiomeLoad(final BiomeLoadingEvent event) {
		if (!ConfigHandler.COMMON.worldgenEnabled.get()) return;
		BiomeGenerationSettingsBuilder settings = event.getGeneration();



		if(ConfigHandler.COMMON.worldgenDefault.get()) {
			for (GenerationStage.Decoration deco : decorations) {
				OreRemover.destroyFeature(settings.getFeatures(deco),
						OreRemover.filterFeatures(settings.getFeatures(deco)));
			}
		}
		
		for (ConfiguredFeature<?, ?> configuredFeatureOreDeposit : Registrar.configuredFeaturesDeposits) {
			settings.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> configuredFeatureOreDeposit);
		}
	}
}
