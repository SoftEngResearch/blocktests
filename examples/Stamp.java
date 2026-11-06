// BLOCKTEST EXAMPLE: https://github.com/Harium/keel/blob/8b427e277df4b6c891617e6a0b9eea59c9d348d9/src/main/java/com/harium/keel/effect/Stamp.java#L31-L40
package com.harium.keel.effect;

import com.harium.keel.core.Effect;
import com.harium.keel.core.helper.ColorHelper;
import com.harium.keel.core.source.BinarySource;
import com.harium.keel.core.source.ImageSource;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static org.blocktest.types.Flow.*;

public class Stamp implements Effect {
	
	private int x = 0, y = 0;
	private ImageSource stamp;
	private boolean seamless = false;
	
	@Override
	public ImageSource apply(ImageSource input) {
		if (stamp == null) {
			throw new RuntimeException("Stamp undefined.");
		}
		for (int i = 0; i < stamp.getHeight(); i++) {
			for (int j = 0; j < stamp.getWidth(); j++) {
				int sx = x + j;
				int sy = y + i;
				
				if (!seamless) {
					if (sx < 0 || sx >= input.getWidth()) {
						continue;
					}
					if (sy < 0 || sy >= input.getHeight()) {
						continue;
					}
				} else {
					blocktest().configParam("test_if", "BinarySource int int int", new Object[][] {
																				{new BinarySource(2, 2, new byte[]{0, 1, 2, 3}), 5, 1, 1},
																				{new BinarySource(6, 1, new byte[]{0, 1, 2, 3}), -5, 1, -4},
																				//{new BinarySource(0, 5, new byte[]{0, 1, 2, 3}), 0, undef, 0}, // We throw exception because we divide by zero (unsure if bug?)
																				{new BinarySource(7, 23, new byte[]{0, 1, 2, 3}), 8, 1, 8},
																				{new BinarySource(24, 54, new byte[]{0, 1, 2, 3}), -0, 0, 0},
																				{new BinarySource(35, 42, new byte[]{0, 1, 2, 3}), 8, 8, 8},
																				{new BinarySource(54, 32, new byte[]{0, 1, 2, 3}), -1, 53, 31},
																				{new BinarySource(43, 52, new byte[]{0, 1, 2, 3}), -20, 23, 32},
																			});

					blocktest().given(input, new BinarySource(2, 2, new byte[]{0, 1, 2, 3})).given(sx, 5).checkFlow(IfStmt().ElseIf().Then()).checkFalse(sx == 5);
					/* @blocktest().parameterized("test_if", input, sx, expectedBlock0, expectedBlock1)
					                  .assume(!(sx < 0) && sx >= input.getWidth())
									  .checkFlow(IfStmt().ElseIf().Then()).checkTrue(sx == expectedBlock0); */
					/* @blocktest().parameterized("test_if", input, sx, expectedBlock0, expectedBlock1)
					                  .assume(sx < 0)
									  .checkFlow(IfStmt().Then()).checkTrue(sx == expectedBlock0); */
					if (sx < 0) {
						sx += input.getWidth();
					} else if (sx >= input.getWidth()) {
						sx %= input.getWidth();
					}
					blocktest().given(input, new BinarySource(2, 2, new byte[]{0, 1, 2, 3})).given(sy, 5).checkEq(sy, 1);
					/* @blocktest().parameterized("test_if", input, sy, expectedBlock0, expectedBlock1)
					                  .assume(!(sy < 0) && sy >= input.getHeight())
									  .checkFlow(IfStmt().ElseIf().Then()).checkTrue(sy == expectedBlock1); */
					/* @blocktest().parameterized("test_if", input, sy, expectedBlock0, expectedBlock1)
					                  .assume(sy < 0)
									  .checkFlow(IfStmt().Then()).checkTrue(sy == expectedBlock1); */
					if (sy < 0) {
						sy += input.getHeight();
					} else if (sy >= input.getHeight()) {
						sy %= input.getHeight();
					}
				}
				
				int rgb = stamp.getRGB(j, i);
				int inputRGB = input.getRGB(sx, sy);
				
				input.setRGB(sx, sy, ColorHelper.mix(inputRGB, rgb));
			}
		}
		
		return input;
	}
	
	public Stamp stamp(ImageSource stamp) {
		this.stamp = stamp;
		return this;
	}
	
	public Stamp position(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Stamp x(int x) {
		this.x = x;
		return this;
	}
	
	public Stamp y(int y) {
		this.y = y;
		return this;
	}
	
	public Stamp seamless(boolean seamless) {
		this.seamless = seamless;
		return this;
	}
	
	public boolean seamless() {
		return seamless;
	}
	
}
