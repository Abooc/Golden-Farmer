package org.lee.android.app.JanbanMaker.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 14-09-05.
 *
 * 加班计算器
 */
public class JiabanCalculator {

    private long rest = (9*1000*60*60+30*60*1000);
//    private long rest = 0;

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
	private Cache mCache = new Cache();
	private static final String DATE_FORMATE = "HH:mm:ss";

	/**
	 * 添加一条加班记录
	 *
	 * @param start
	 * @param end
	 */
	public void add(String date, String start, String end) {
		Recorder recorder = new Recorder(date, start, end);
		mCache.add(recorder);
	}

    public Recorder remove(int location){
        return mCache.iRecordertArray.remove(location);
    }

    public List<Recorder> getRecords(){
        return mCache.iRecordertArray;
    }

	/**
	 * 进行计算
	 */
	public void calculate() {
		int length = mCache.count();
		for (int i = 0; i < length; i++) {
			Recorder recorder = mCache.getItem(i);
			long time = calculate(recorder.start, recorder.end);
			recorder.iOvertime = toOvertime(time - rest);
		}
	}

    public boolean clear(){
        mCache.iRecordertArray.clear();
        return true;
    }

	/**
	 * 输出总结果
	 */
	public float result() {
		float hours = 0;
		int length = mCache.count();
		for (int i = 0; i < length; i++) {
			Recorder recorder = mCache.getItem(i);
			hours += recorder.iOvertime.total();
		}
        return hours;
	}

	/**
	 * 输出详细清单
	 */
	public String toShow() {
		StringBuffer buffer = new StringBuffer();

		int length = mCache.count();
		for (int i = 0; i < length; i++) {
			Recorder recorder = mCache.getItem(i);
			buffer.append(recorder.startDate + " " + recorder.start + "-" + recorder.end
					+ "共:" + recorder.iOvertime.hour + "小时"
					+ recorder.iOvertime.minute + "分钟	计"
					+ recorder.iOvertime.total() + "小时\n");
		}
        return buffer.toString();
	}

	/**
	 * 负责累计加班记录
	 *
	 * @author dayu E-Mail:allnet@live.cn
	 * @date 2014-9-1
	 *
	 */
	class Cache {
		private List<Recorder> iRecordertArray = new ArrayList<Recorder>();

		public void add(Recorder recorder) {
			iRecordertArray.add(recorder);
		}

		public Recorder getItem(int position) {
			return iRecordertArray.get(position);
		}

		public int count() {
			return iRecordertArray == null ? 0 : iRecordertArray.size();
		}
	}

	/**
	 * 加班记录
	 *
	 * @author dayu E-Mail:allnet@live.cn
	 * @date 2014-9-1
	 *
	 */
	class Recorder {
		public final String startDate;
		public final String start;
		public final String end;
		/**
		 * 该加班记录所产生的加班值
		 */
		public Overtime iOvertime;

		/**
		 *
		 * @param date
		 * @param start
		 *            加班开始时间
		 * @param end
		 *            下班的时间
		 */
		public Recorder(String date, String start, String end) {
			this.startDate = date;
			this.start = start;
			this.end = end;
		}

	}

	/**
	 * 加班超时承载体
	 *
	 * @author dayu E-Mail:allnet@live.cn
	 * @date 2014-9-1
	 *
	 */
	static class Overtime {
		/**
		 * 超时小时数，不满1小时的会计算成分钟数
		 */
		public int hour;
		/**
		 * 剩余分钟数，不满1分钟的会计算成秒
		 */
		public int minute;
		/**
		 * 剩余秒
		 */
		public int second;
		/**
		 * 总数，单位毫秒
		 */
		public final long totalInMillis;

		public Overtime(long totalInMillis) {
			this.totalInMillis = totalInMillis;
		}

		/**
		 * 把不满1小时的时间，换算成半小时
		 *
		 * @return 大于30分钟计为0.5小时
		 */
		public float total() {
			if (minute >= 30) {
				return (float) (hour + 0.5);
			}
			return hour;
		}
	}

	/**
	 * 计算时间差
	 *
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 返回时间差，单位毫秒
	 */
	public static long calculate(String start, String end) {
		SimpleDateFormat da = new SimpleDateFormat(DATE_FORMATE);
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

	/**
	 * 将long型时间差换算为加班时间
	 *
	 * @param timeMillis
	 *            时间数值，单位毫秒
	 * @return 返回经过计算后的结果
	 */
	public static Overtime toOvertime(long timeMillis) {
		int s = (int) (timeMillis / 1000);// 换算成秒
		int m = s / 60;// 换算成分钟
//		m = m - 30;// 减去30分钟的就餐时间
		int h = m / 60;// 换算成小时
		int mm = m % 60;// 不满1小时的剩余分钟数

		Overtime iOvertime = new Overtime(timeMillis);
		iOvertime.second = s;
		iOvertime.minute = mm;
		iOvertime.hour = h;
		return iOvertime;
	}

}
