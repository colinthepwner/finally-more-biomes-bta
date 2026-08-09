package biomesoplenty.fluids;

import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.Fluid;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FluidHoney implements Fluid {

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FluidHoney;
	}

	@Override
	public int hashCode() {
		return FluidHoney.class.hashCode();
	}

	@Override
	public int tickDelay() {
		return 8;
	}

	@Override
	public byte getFlowDecayMod(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos) {
		return 7;
	}

	@Override
	public boolean canSpreadTo(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePos tilePos,
							   @NotNull Material material) {
		return material != Materials.WATER && material != Materials.LAVA
			&& material != Materials.ACID && material != Materials.PORTAL;
	}

	@Override
	public boolean canBecomeSource(@NotNull BlockLogicFluid logicFluid, @NotNull World world,
								   @NotNull TilePosc tilePos, @NotNull Random rand) {
		return false;
	}

	@Override
	public void onFlowIntoBlock(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePos tilePos,
								int meta) {
		world.getBlockType(tilePos).dropWithCause(
			world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
	}

	@Override
	public boolean checkForHarden(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos,
								  @NotNull Material encountered) {
		return false;
	}

	@Override
	public boolean shouldTick() {
		return false;
	}

	@Override
	public void updateTickStill(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos,
								@NotNull Random rand) {
	}

	@Override
	public void animationTick(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos,
							  @NotNull Random rand) {
	}
}
