package edu.guet.table.support;

public class ImageUtil {

	public static int max(int a, int b) {
		int temp;
		if (a > b) {
			temp = a;
		} else {
			temp = b;
		}
		return temp;
	}

	public static int min(int a, int b) {
		int temp;
		if (a < b) {
			temp = a;
		} else {
			temp = b;
		}
		return temp;
	}

	public static int distance(int r_color, int g_color, int b_color) {
		int big = max(r_color, max(g_color, b_color));
		int small = min(r_color, min(g_color, b_color));
		int middle = max(r_color, min(g_color, b_color));
		return big + middle - small;
	}

	public static int getRed(String color) {
		return Integer.parseInt(color.substring(2, 4), 16);
	}

	public static int getGreen(String color) {
		return Integer.parseInt(color.substring(4, 6), 16);
	}

	public static int getBlue(String color) {
		return Integer.parseInt(color.substring(6, 8), 16);
	}

}
