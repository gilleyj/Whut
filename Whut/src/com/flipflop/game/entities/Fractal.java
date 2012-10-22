package com.flipflop.game.entities;

public class Fractal {
	public static boolean isPowerOf2(int val) {
		int bitCount = 0;
		for (; val > 0; val >>>= 1) {
			if ((val & 0x1) == 1)
				bitCount++;
		}
		if (bitCount == 1)
			return true;
		else
			return false;
	}

	public static void fill2DFractal(float[] fa, int size, int seedValue,
			float heightScale, float h) {
		int i, j;
		int stride;
		int oddline;
		int subSize;
		float ratio, scale;

		if (!isPowerOf2(size) || (size == 1)) {
			/* We can't tesselate the array if it is not a power of 2. */
			return;
		}

		/*
		 * subSize is the dimension of the array in terms of connected line
		 * segments, while size is the dimension in terms of number of vertices.
		 */
		subSize = size;
		size++;

		/*
		 * Set up our roughness constants. Random numbers are always generated
		 * in the range 0.0 to 1.0. 'scale' is multiplied by the randum number.
		 * 'ratio' is multiplied by 'scale' after each iteration to effectively
		 * reduce the randum number range.
		 */
		ratio = (float) Math.pow(2., -h);
		scale = heightScale * ratio;

		/*
		 * Seed the first four values. For example, in a 4x4 array, we would
		 * initialize the data points indicated by '*':
		 * 
		 * . . . *
		 * 
		 * . . . . .
		 * 
		 * . . . . .
		 * 
		 * . . . . .
		 * 
		 * . . . *
		 * 
		 * In terms of the "diamond-square" algorithm, this gives us "squares".
		 * 
		 * We want the four corners of the array to have the same point. This
		 * will allow us to tile the arrays next to each other such that they
		 * join seemlessly.
		 */

		stride = subSize / 2;
		fa[(0 * size) + 0] = fa[(subSize * size) + 0] = fa[(subSize * size)
				+ subSize] = fa[(0 * size) + subSize] = 0.f;

		/*
		 * Now we add ever-increasing detail based on the "diamond" seeded
		 * values. We loop over stride, which gets cut in half at the bottom of
		 * the loop. Since it's an int, eventually division by 2 will produce a
		 * zero result, terminating the loop.
		 */
		while (stride > 0) {
			/*
			 * Take the existing "square" data and produce "diamond" data. On
			 * the first pass through with a 4x4 matrix, the existing data is
			 * shown as "X"s, and we need to generate the "*" now:
			 * 
			 * X . . . X
			 * 
			 * . . . . .
			 * 
			 * . . * . .
			 * 
			 * . . . . .
			 * 
			 * X . . . X
			 * 
			 * It doesn't look like diamonds. What it actually is, for the first
			 * pass, is the corners of four diamonds meeting at the center of
			 * the array.
			 */
			for (i = stride; i < subSize; i += stride) {
				for (j = stride; j < subSize; j += stride) {
					fa[(i * size) + j] = (float) (scale * (Math.random() - .5f)
							+ avgSquareVals(i, j, stride, size, fa));
					j += stride;
				}
				i += stride;
			}

			/*
			 * Take the existing "diamond" data and make it into "squares". Back
			 * to our 4X4 example: The first time we encounter this code, the
			 * existing values are represented by "X"s, and the values we want
			 * to generate here are "*"s:
			 * 
			 * X . * . X
			 * 
			 * . . . . .
			 * 
			 * . X . *
			 * 
			 * . . . . .
			 * 
			 * X . * . X
			 * 
			 * i and j represent our (x,y) position in the array. The first
			 * value we want to generate is at (i=2,j=0), and we use "oddline"
			 * and "stride" to increment j to the desired value.
			 */
			oddline = 0;
			for (i = 0; i < subSize; i += stride) {
				oddline = (oddline == 0 ? 1 : 0);
				for (j = 0; j < subSize; j += stride) {
					if ((oddline > 0) && j == 0)
						j += stride;

					fa[(i * size) + j] = (float) (scale * (Math.random() - .5f)
							+ avgDiamondVals(i, j, stride, size, subSize, fa));

					if (i == 0)
						fa[(subSize * size) + j] = fa[(i * size) + j];
					if (j == 0)
						fa[(i * size) + subSize] = fa[(i * size) + j];

					j += stride;
				}
			}

			scale *= ratio;
			stride >>= 1;
		}
	}

	private static float avgSquareVals(int i, int j, int stride, int size,
			float fa[]) {
		return ((float) (fa[((i - stride) * size) + j - stride]
				+ fa[((i - stride) * size) + j + stride]
				+ fa[((i + stride) * size) + j - stride] + fa[((i + stride) * size)
				+ j + stride]) * .25f);
	}

	private static float avgDiamondVals(int i, int j, int stride, int size,
			int subSize, float fa[]) {
		if (i == 0)
			return ((float) (fa[(i * size) + j - stride]
					+ fa[(i * size) + j + stride]
					+ fa[((subSize - stride) * size) + j] + fa[((i + stride) * size)
					+ j]) * .25f);
		else if (i == size - 1)
			return ((float) (fa[(i * size) + j - stride]
					+ fa[(i * size) + j + stride]
					+ fa[((i - stride) * size) + j] + fa[((0 + stride) * size)
					+ j]) * .25f);
		else if (j == 0)
			return ((float) (fa[((i - stride) * size) + j]
					+ fa[((i + stride) * size) + j]
					+ fa[(i * size) + j + stride] + fa[(i * size) + subSize
					- stride]) * .25f);
		else if (j == size - 1)
			return ((float) (fa[((i - stride) * size) + j]
					+ fa[((i + stride) * size) + j]
					+ fa[(i * size) + j - stride] + fa[(i * size) + 0 + stride]) * .25f);
		else
			return ((float) (fa[((i - stride) * size) + j]
					+ fa[((i + stride) * size) + j]
					+ fa[(i * size) + j - stride] + fa[(i * size) + j + stride]) * .25f);
	}

}
