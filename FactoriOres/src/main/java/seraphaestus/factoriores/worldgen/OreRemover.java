package seraphaestus.factoriores.worldgen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoExposedOreFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;

public class OreRemover {
    private static List<Block> toRm = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE,
            Blocks.NETHER_GOLD_ORE);

    public static List<Supplier<ConfiguredFeature<?, ?>>> removed = new LinkedList<>();

    private static void featureRemover(Block targetBlock, Supplier<ConfiguredFeature<?, ?>> targetFeature) {
        if (targetBlock != null) {
            if (toRm.contains(targetBlock)) {
                removed.add(targetFeature);
            }
        }
    }

    public static void destroyFeature(List<Supplier<ConfiguredFeature<?, ?>>> features,
                                      List<Supplier<ConfiguredFeature<?, ?>>> destroy) {
        for (Supplier<ConfiguredFeature<?, ?>> feature : destroy) {
            features.remove(feature);
        }
    }

    public static ConfiguredFeature<?, ?> getFeature(ConfiguredFeature<?, ?> feature) {
        ConfiguredFeature<?, ?> currentFeature = feature;
        if (currentFeature.feature instanceof DecoratedFeature) {
            do {
                currentFeature = ((DecoratedFeatureConfig) currentFeature.getConfig()).feature.get();
            } while (currentFeature.feature instanceof DecoratedFeature);
        }
        return currentFeature;
    }

    public static List<Supplier<ConfiguredFeature<?, ?>>> filterFeatures(
            List<Supplier<ConfiguredFeature<?, ?>>> features) {
        for (Supplier<ConfiguredFeature<?, ?>> feature : features) {
            Block targetBlock = null;
            ConfiguredFeature<?, ?> targetFeature = getFeature(feature.get());

            if (targetFeature.feature instanceof OreFeature || targetFeature.feature instanceof NoExposedOreFeature) {
                // Normal ores
                OreFeatureConfig config = (OreFeatureConfig) targetFeature.config;
                targetBlock = config.state.getBlock();
                featureRemover(targetBlock, feature);
            } else if (targetFeature.feature instanceof ReplaceBlockFeature) {
                // Emeralds
                ReplaceBlockConfig config = (ReplaceBlockConfig) targetFeature.config;
                targetBlock = config.state.getBlock();
                featureRemover(targetBlock, feature);
            }

        }
        return removed;
    }
}
