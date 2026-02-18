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
					blocktest().given(input, new BinarySource(2, 2, new byte[]{0, 1, 2, 3})).given(sx, 5).checkFlow(IfStmt().ElseIf().Then()).checkFalse(sx == 5);
					if (sx < 0) {
						sx += input.getWidth();
					} else if (sx >= input.getWidth()) {
						sx %= input.getWidth();
					}
					blocktest().given(input, new BinarySource(2, 2, new byte[]{0, 1, 2, 3})).given(sy, 5).checkEq(sy, 1);
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
