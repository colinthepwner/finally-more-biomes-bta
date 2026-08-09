package com.betteroplenty.client;

import java.util.Random;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;

public class DynamicTexturePromisedPortal extends DynamicTexture {

	private static final int FRAMES = 32;

	private static final int PEAK_BLUE = 167;

	private static final int BASE_ALPHA = 128;

	private final byte[][] frames = new byte[FRAMES][];

	private int tick;

	public DynamicTexturePromisedPortal(@NotNull IconCoordinate texture) {
		super(texture);
	}

	@Override
	public void postInit() {
		this.initTexture();

		Random rand = new Random(100L);
		int width = this.targetTexture.width;
		int height = this.targetTexture.height;

		for (int frame = 0; frame < FRAMES; frame++) {
			this.frames[frame] = new byte[this.targetTexture.getArea() * 4];

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					float f = 0.0F;

					for (int lobe = 0; lobe < 2; lobe++) {
						float swirlX = lobe * (width / 2.0F);
						float swirlY = lobe * (height / 2.0F);

						float dx = (x - swirlX) / width * 2.0F;
						float dy = (y - swirlY) / height * 2.0F;
						if (dx < -1.0F) {
							dx += 2.0F;
						}
						if (dx >= 1.0F) {
							dx -= 2.0F;
						}
						if (dy < -1.0F) {
							dy += 2.0F;
						}
						if (dy >= 1.0F) {
							dy -= 2.0F;
						}

						float rSq = dx * dx + dy * dy;
						float angle = (float) Math.atan2(dy, dx)
							+ (frame / (float) FRAMES * (float) Math.PI * 2.0F - rSq * 10.0F
								+ lobe * 2) * (lobe * 2 - 1);
						angle = (MathHelper.sin(angle) + 1.0F) / 2.0F;
						angle /= rSq + 1.0F;
						f += angle * 0.5F;
					}

					f += rand.nextFloat() * 0.1F;

					int r = (int) (f * 100.0F + 155.0F);
					int g = (int) (f * f * 200.0F + 55.0F);
					int b = (int) (f * f * f * f * PEAK_BLUE);
					int a = (int) (f * (255 - BASE_ALPHA) + BASE_ALPHA);

					int i = (y * width + x) * 4;
					this.frames[frame][i] = (byte) clamp(r);
					this.frames[frame][i + 1] = (byte) clamp(g);
					this.frames[frame][i + 2] = (byte) clamp(b);
					this.frames[frame][i + 3] = (byte) clamp(a);
				}
			}
		}
	}

	@Override
	public void update() {
		this.tick++;
		byte[] frame = this.frames[this.tick & FRAMES - 1];
		byte[] image = this.getImageData(this.layerColor);
		System.arraycopy(frame, 0, image, 0, frame.length);
	}

	private static int clamp(int channel) {
		return Math.min(255, Math.max(0, channel));
	}
}
