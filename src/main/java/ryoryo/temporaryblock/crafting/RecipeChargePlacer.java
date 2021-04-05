package ryoryo.temporaryblock.crafting;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import ryoryo.polishedlib.util.libs.NumericalConstant;
import ryoryo.temporaryblock.TemporaryBlock;
import ryoryo.temporaryblock.item.ItemTemporaryBlockPlacer;
import ryoryo.temporaryblock.item.ModItems;
import ryoryo.temporaryblock.util.References;

public class RecipeChargePlacer extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private static Map<Block, Integer> itemsToCount = Maps.newHashMap();

	public RecipeChargePlacer() {}

	/**
	 * Recipe
	 * [i i i]
	 * [i i i] -> [o]
	 * [i i i]
	 *
	 * search all stacks in slots, check that all ingredients equal to the input
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); i ++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (!stack.isEmpty()) {
				list.add(stack);

				if (stack.getItem() == ModItems.ITEM_TEMPORARY_BLOCK_PLACER && stack.getCount() == 1) {
					continue;
				}

				if (itemsToCount.containsKey(Block.getBlockFromItem(stack.getItem())) && stack.getCount() > 0) {
					continue;
				}

				return false;
			}
		}

		return list.size() == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		List<ItemStack> list = Lists.newArrayList();
		ItemStack placer = ItemStack.EMPTY;
		ItemStack charger = ItemStack.EMPTY;
		int chargerIdx = 0;

		for (int i = 0; i < inv.getSizeInventory(); i ++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (!stack.isEmpty()) {
				list.add(stack);

				if (stack.getItem() == ModItems.ITEM_TEMPORARY_BLOCK_PLACER && stack.getCount() == 1) {
					placer = stack.copy();
					continue;
				}

				if (itemsToCount.containsKey(Block.getBlockFromItem(stack.getItem())) && stack.getCount() > 0) {
					charger = stack;
					chargerIdx = i;
					continue;
				}

				return ItemStack.EMPTY;
			}
		}

		if (list.size() == 2 && !placer.isEmpty() && !charger.isEmpty()) {
			long newDurability = (long) itemsToCount.get(Block.getBlockFromItem(charger.getItem()));
			newDurability *= charger.getCount();
			newDurability += placer.getTagCompound().getCompoundTag(References.MOD_ID).getInteger(ItemTemporaryBlockPlacer.NBT_DURABILITY);

			if (newDurability > NumericalConstant.INT_MAX) {
				newDurability = NumericalConstant.INT_MAX;
			}

			ItemTemporaryBlockPlacer.setDurability(placer, (int) newDurability);

			return placer;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height == 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	// recipe register
	public static void addRecipe(String name) {
		RecipeChargePlacer recipe = new RecipeChargePlacer();
		recipe.setRegistryName(References.MOD_ID, name);
		ForgeRegistries.RECIPES.register(recipe);
	}

	public static void reloadMap(List<String> listFormMap) {
		for (String stringMap : listFormMap) {
			String blockName = stringMap.substring(0, stringMap.indexOf('>'));
			Block block = Block.REGISTRY.getObject(new ResourceLocation(blockName));

			int count = 1;
			try {
				count = Integer.parseInt(stringMap.substring(stringMap.indexOf('>')));
			} catch (Exception e) {
				TemporaryBlock.LOGGER.error("Unable to parse count");
			}

			itemsToCount.put(block, count);
		}
	}
}