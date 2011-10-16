package com.zacharydenton.beatgrid;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {

	private static final String OPT_WIDTH = "width";
	private static int OPT_WIDTH_DEF = 4;
	private static final String OPT_HEIGHT = "height";
	private static int OPT_HEIGHT_DEF = 4;
	private static final String OPT_HAPTIC = "haptic";
	private static boolean OPT_HAPTIC_DEF = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	public static int getWidth(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(OPT_WIDTH, OPT_WIDTH_DEF);
	}
	
	public static int getHeight(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(OPT_HEIGHT, OPT_HEIGHT_DEF);
	}

	public static boolean getHaptic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_HAPTIC, OPT_HAPTIC_DEF);
	}

}
