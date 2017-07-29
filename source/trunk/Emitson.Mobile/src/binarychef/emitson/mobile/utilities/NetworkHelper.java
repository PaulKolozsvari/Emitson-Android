package binarychef.emitson.mobile.utilities;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkHelper 
{
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		boolean result = connectivityManager.getBackgroundDataSetting() && connectivityManager.getActiveNetworkInfo() != null;
		return result;
	}
}
