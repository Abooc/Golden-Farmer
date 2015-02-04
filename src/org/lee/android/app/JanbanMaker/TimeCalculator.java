package org.lee.android.app.JanbanMaker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCalculator {

	public static void main(String[] args) {

		JiabanCalculator iCalculator = new JiabanCalculator();
		iCalculator.add("18:18:03", "21:58:28");
		iCalculator.add("18:27:45", "22:06:25");
		iCalculator.add("18:50:03", "22:06:18");

		iCalculator.calculate();
		iCalculator.result();

		iCalculator.toShow();

	}

	/**
	 * 
	 * 8/5 9:18:03 21:58:28
	 * 
	 * 8/6 9:27:45 22:06:25
	 * 
	 * 8/7 9:32:57 22:03:28
	 * 
	 * 8/12 8:54:08 21:44:13
	 * 
	 * 8/14 9:30:57 22:18:09
	 * 
	 * 8/19 9:23:17 22:16:07
	 * 
	 * 8/20 9:35:55 21:32:00
	 */
	public static void test1() {
		long time = calculate("18:18:03", "21:58:28");
		show(time);

		time = calculate("18:27:45", "22:06:25");
		show(time);
		time = calculate("18:32:57", "22:03:28");
		show(time);

		time = calculate("18:00:00", "21:44:13");// "08:54:08"
		show(time);
		time = calculate("18:30:57", "22:18:09");
		show(time);
		time = calculate("18:23:17", "22:16:07");
		show(time);

		time = calculate("18:35:55", "21:32:00");
		show(time);
	}

	/**
	 * 8-5 09:41:22 21:58:20 2.5
	 * 
	 * 8-6 09:50:03 22:06:18 2.5
	 * 
	 * 8-7 09:52:21 22:03:55 2.5
	 * 
	 * 8-12 08:54:00 21:44:47 3
	 * 
	 * 8-14 09:30:52 22:17:59 3
	 * 
	 * 8-19 09:23:24 22:15:55 3
	 * 
	 * 8-20 09:36:06 21:31:39 2
	 * 
	 */
	public static void test2() {
		long time = calculate("18:41:22", "21:58:20");
		show(time);

		time = calculate("18:50:03", "22:06:18");
		show(time);
		time = calculate("18:52:21", "22:03:55");
		show(time);

		time = calculate("18:00:00", "21:44:47");// "08:54:00"
		show(time);
		time = calculate("18:30:52", "22:17:59");
		show(time);
		time = calculate("18:23:24", "22:15:55");
		show(time);

		time = calculate("18:36:06", "21:31:39");
		show(time);
	}

	public static long calculate(String start, String end) {
		SimpleDateFormat da = new SimpleDateFormat("HH:mm:ss");
		try {
			Date startData = da.parse(start);
			Date endData = da.parse(end);

			long startl = startData.getTime();
			long endl = endData.getTime();

			return endl - startl;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String show(long time) {
		int s = (int) (time / 1000);
		int m = s / 60;
		m = m - 30;
		int h = m / 60;
		int mm = m % 60;

		System.out.println("时间：" + h + "小时" + mm + "分钟");
		return null;
	}
}
