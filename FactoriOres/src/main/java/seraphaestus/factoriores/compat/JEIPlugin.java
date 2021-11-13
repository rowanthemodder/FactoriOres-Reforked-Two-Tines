package seraphaestus.factoriores.compat;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import seraphaestus.factoriores.FactoriOres;
import net.minecraft.item.crafting.IRecipeType;

import java.util.Collection;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
	private static final ResourceLocation ID = new ResourceLocation(FactoriOres.MOD_ID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
	
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		ItemStack item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(FactoriOres.MOD_ID, "sulfur_block")).getDefaultInstance();
		registry.addIngredientInfo(item, VanillaTypes.ITEM, FactoriOres.MOD_ID + ".jei.sulfur_process");
		FluidStack fluid = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(FactoriOres.MOD_ID, "sulfuric_acid")).getFluid(), 1);
		registry.addIngredientInfo(fluid, VanillaTypes.FLUID, FactoriOres.MOD_ID + ".jei.sulfur_process");
	}


}
