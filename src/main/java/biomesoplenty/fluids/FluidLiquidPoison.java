package biomesoplenty.fluids;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.Fluid;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FluidLiquidPoison implements Fluid {

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FluidLiquidPoison;
	}

	@Override
	public int hashCode() {
		return FluidLiquidPoison.class.hashCode();
	}

	@Override
	public int tickDelay() {
		return 25;
	}

	@Override
	public byte getFlowDecayMod(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos) {
		return 2;
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

	public static final float[] PARTICLE_RGB = {0x5B / 255.0F, 0x55 / 255.0F, 0x7A / 255.0F};

	@Override
	public void animationTick(@NotNull BlockLogicFluid logicFluid, @NotNull World world, @NotNull TilePosc tilePos,
							  @NotNull Random rand) {
		int data = world.getBlockData(tilePos) & 15;
		TilePos queryPos = new TilePos();

		if (data > 0 && data < 8 && rand.nextInt(64) == 0) {
			world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS,
				tilePos.x() + 0.5F, tilePos.y() + 0.5F, tilePos.z() + 0.5F,
				"liquid.water", rand.nextFloat() * 0.2F + 0.2F, rand.nextFloat() * 0.4F + 0.3F);
		}

		Block<?> below;
		if (rand.nextInt(4) == 0
			&& world.getBlockType(tilePos.down(queryPos)).solid()
			&& !(below = world.getBlockType(tilePos.down(new TilePos()).down())).solid()
			&& !Block.hasLogicClass(below, BlockLogicFluid.class)) {
			world.spawnParticle("puffrgb",
				tilePos.x() + rand.nextFloat() * 0.9F + 0.05F,
				tilePos.y() - 1 - 0.01F,
				tilePos.z() + rand.nextFloat() * 0.9F + 0.05F,
				PARTICLE_RGB[0], PARTICLE_RGB[1], PARTICLE_RGB[2], 0, false);
		}
	}
}
