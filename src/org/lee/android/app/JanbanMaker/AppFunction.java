package org.lee.android.app.JanbanMaker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class AppFunction {
	/**
	 * 为程序创建桌面快捷方式。
	 * 
	 * @param activity
	 *            指定当前的Activity为快捷方式启动的对象
	 * @param nameId
	 *            快捷方式的名称
	 * @param iconId
	 *            快捷方式的图标
	 * @param appendFlags
	 *            需要在快捷方式启动应用的Intent中附加的Flag
	 */
	public static void addShortcut(Activity activity, int nameId, int iconId, int appendFlags) {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(nameId));
		shortcut.putExtra("duplicate", false);
		// 指定当前的Activity为快捷方式启动的对象
		ComponentName comp = new ComponentName(activity.getPackageName(), activity.getClass().getName());
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		if (appendFlags != 0) {
			intent.addFlags(appendFlags);
		}
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		ShortcutIconResource iconRes = ShortcutIconResource.fromContext(activity, iconId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		activity.sendBroadcast(shortcut);
	}

	public static void addShortcut(Activity activity) {
		addShortcut(activity, R.string.app_name, R.drawable.ic_launcher, 1);
	}

	public static boolean hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		return false;
	}

	public static boolean showInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.showSoftInput(view, 0);
		}

		return false;
	}

}
