package ryoryo.temporaryblock.item;

import java.util.List;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import ryoryo.polishedlib.item.ItemBase;
import ryoryo.polishedlib.util.Utils;
import ryoryo.polishedlib.util.libs.NumericalConstant;
import ryoryo.temporaryblock.block.ModBlocks;
import ryoryo.temporaryblock.util.References;

public class ItemTemporaryBlockPlacer extends ItemBase {

	public static final String NBT_DURABILITY = "Durability";
	private static final String RUN_OUT_DURABILITY = TextFormatting.RED + Utils.translatableString(References.CHAT_RUN_OUT_DURABILITY);
	private static final String WARNING_DROP = TextFormatting.YELLOW + Utils.translatableString(References.TOOLTIP_DROP);

	public ItemTemporaryBlockPlacer() {
		super("temporary_block_placer", CreativeTabs.TOOLS);
		this.setMaxStackSize(1);
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.INFINITY;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == this.getCreativeTab()) {
			items.add(getNewStackWithDurability(NumericalConstant.INT_MAX));
		}
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (hasModTagCompound(stack)) {
			int i = stack.getTagCompound().getCompoundTag(References.MOD_ID).getInteger(NBT_DURABILITY);
			tooltip.add(Utils.translatableString(References.TOOLTIP_REMAIN_USAGE, i));
		} else {
			tooltip.add(WARNING_DROP);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(References.MOD_ID)) {
			int i = stack.getTagCompound().getCompoundTag(References.MOD_ID).getInteger(NBT_DURABILITY);
			return 1.0 - MathHelper.clamp((double) i / 512.0, 0.0, 1.0);
		}

		return 0.0;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult result = this.rayTrace(world, player, true);

		if (result == null) {
			return EnumActionResult.PASS;
		}

		if (hasModTagCompound(stack)) {
			if (stack.getTagCompound().getCompoundTag(References.MOD_ID).getInteger(NBT_DURABILITY) <= 0) {
				Utils.sendPopUpMessage(player, RUN_OUT_DURABILITY);
				return EnumActionResult.PASS;
			}
		} else {
			Utils.sendPopUpMessage(player, WARNING_DROP);
			return EnumActionResult.PASS;
		}

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos rayPos = result.getBlockPos();

			if (!world.isBlockModifiable(player, rayPos) || !player.canPlayerEdit(rayPos.offset(result.sideHit), result.sideHit, stack)) {
				return EnumActionResult.FAIL;
			}

			BlockPos posu = rayPos.up();
			IBlockState state = world.getBlockState(rayPos);

			if (state.getMaterial() == Material.WATER || state.getMaterial() == Material.LAVA || state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase) {
				if (world.isAirBlock(posu)) {
					SoundType soundtype = world.getBlockState(posu).getBlock().getSoundType(world.getBlockState(posu), world, posu, player);
					world.playSound(player, posu, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

					world.setBlockState(posu, ModBlocks.BLOCK_TEMPORARY.getDefaultState());

					if (!Utils.isCreative(player) && !Utils.hasEnchant(stack, Enchantments.INFINITY)) {
						damageItem(stack);
					}

					return EnumActionResult.SUCCESS;
				}
			} else {
				if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
					pos = pos.offset(facing);

				if (!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack) && world.mayPlace(ModBlocks.BLOCK_TEMPORARY, pos, false, facing, (Entity) null)) {
					if (ModBlocks.BLOCK_TEMPORARY.canPlaceBlockAt(world, pos)) {
						world.setBlockState(pos, ModBlocks.BLOCK_TEMPORARY.getDefaultState());
						SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
						world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
						if (!Utils.isCreative(player) && !Utils.hasEnchant(stack, Enchantments.INFINITY)) {
							damageItem(stack);
						}

						return EnumActionResult.SUCCESS;
					}
				}
			}
		}

		return EnumActionResult.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.isRemote) {
			return super.onItemRightClick(world, player, hand);
		}

		if (hasModTagCompound(stack)) {
			if (stack.getTagCompound().getCompoundTag(References.MOD_ID).getInteger(NBT_DURABILITY) <= 0) {
				Utils.sendPopUpMessage(player, RUN_OUT_DURABILITY);
				return super.onItemRightClick(world, player, hand);
			}
		} else {
			Utils.sendPopUpMessage(player, WARNING_DROP);
			return super.onItemRightClick(world, player, hand);
		}

		BlockPos pos = getAvailablePos(world, player);
		AxisAlignedBB aabb = ModBlocks.BLOCK_TEMPORARY.getDefaultState().getCollisionBoundingBox(world, pos);

		if (world.isAirBlock(pos) && world.checkNoEntityCollision(aabb.offset(pos), (Entity) null)) {
			world.setBlockState(pos, ModBlocks.BLOCK_TEMPORARY.getDefaultState());
			SoundType soundtype = ModBlocks.BLOCK_TEMPORARY.getSoundType(ModBlocks.BLOCK_TEMPORARY.getDefaultState(), world, pos, player);
			world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			player.swingArm(hand);
			if (!Utils.isCreative(player) && !Utils.hasEnchant(stack, Enchantments.INFINITY)) {
				damageItem(stack);
			}
		}

		return super.onItemRightClick(world, player, hand);
	}

	public static BlockPos getAvailablePos(World world, EntityPlayer player) {
		if (player == null)
			return null;

		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult ray = rayTraceLiquid(world, player, true);
		RayTraceResult mouseOver = (ray != null) ? ray : mc.objectMouseOver;
		if (mouseOver == null)
			return null;

		if (mouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
			BlockPos rayPos = mouseOver.getBlockPos();
			IBlockState rayPosState = world.getBlockState(rayPos);
			if (rayPosState.getMaterial() == Material.WATER
					|| rayPosState.getMaterial() == Material.LAVA
					|| rayPosState.getBlock() instanceof BlockLiquid
					|| rayPosState.getBlock() instanceof BlockFluidBase) {
				return rayPos.up();
			}

			if (rayPosState.getBlock().isReplaceable(world, rayPos))
				return rayPos;

			return rayPos.offset(mouseOver.sideHit);
		} else {
			Vec3d look = player.getLookVec();
			int factor = 3;
			int x = (int) Math.floor(player.posX + look.x * factor);
			int y = (int) Math.floor(player.posY + player.getEyeHeight() + look.y * factor);
			int z = (int) Math.floor(player.posZ + look.z * factor);
			return new BlockPos(x, y, z);
		}
	}

	private static RayTraceResult rayTraceLiquid(World world, EntityPlayer player, boolean useLiquids) {
		float f = player.rotationPitch;
		float f1 = player.rotationYaw;
		double d0 = player.posX;
		double d1 = player.posY + (double) player.getEyeHeight();
		double d2 = player.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	public static ItemStack getNewStackWithDurability(int durability) {
		ItemStack placer = new ItemStack(ModItems.ITEM_TEMPORARY_BLOCK_PLACER);
		addDurabilityTag(placer, durability);

		return placer;
	}

	public static void addDurabilityTag(ItemStack stack, int durability) {
		if (!hasModTagCompound(stack)) {
			NBTTagCompound outer = new NBTTagCompound();
			NBTTagCompound inner = new NBTTagCompound();
			inner.setInteger(ItemTemporaryBlockPlacer.NBT_DURABILITY, durability);
			outer.setTag(References.MOD_ID, inner);
			stack.setTagCompound(outer);
		}
	}

	/**
	 * durability = 0
	 * @param stack
	 */
	public static void addDurabilityTag(ItemStack stack) {
		addDurabilityTag(stack, 0);
	}

	public static void setDurability(ItemStack stack, int durability) {
		if (hasModTagCompound(stack)) {
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag(References.MOD_ID);
			nbt.setInteger(NBT_DURABILITY, MathHelper.clamp(durability, 0, NumericalConstant.INT_MAX));
		} else {
			addDurabilityTag(stack, durability);
		}
	}

	public static void damageItem(ItemStack stack) {
		if (hasModTagCompound(stack)) {
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag(References.MOD_ID);
			if (nbt.getInteger(NBT_DURABILITY) > 0) {
				nbt.setInteger(NBT_DURABILITY, nbt.getInteger(NBT_DURABILITY) - 1);
			}
		}
	}

	public static boolean hasModTagCompound(ItemStack stack) {
		return Utils.hasTagCompound(stack, References.MOD_ID);
	}
}