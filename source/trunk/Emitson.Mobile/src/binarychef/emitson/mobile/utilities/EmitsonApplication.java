package binarychef.emitson.mobile.utilities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EmitsonApplication {

	private static EmitsonApplication _instance;
	
	public static EmitsonApplication getInstance() throws Exception{
		if(_instance == null){
			_instance = new EmitsonApplication();
		}
		return _instance;
	}
	
	private EmitsonApplication() throws Exception{
		_gson = createGson();
		_settings = SettingsFile.getFromFile();
	}
	
	private final static String TAG = "EmitsonApplication";
	private SettingsFile _settings;
	private Gson _gson;
	private String _webServiceBaseUrl;
	private int _connectionTimeout;
	private int _socketTimeout;
	private double _volume;
	
	private Timer _timer;
	
	private Context _statusContext;
	private boolean _statusUpdaterRunning;
	
	
	public SettingsFile getSettingsFile(){
		return _settings;
	}
	
	public void setGson(Gson gson){
		_gson = gson;
	}
	
	public Gson createGson(){
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Gson result = b.create();
		return result;
	}
	
	public void initialize(
			String webServiceBaseUrl,  
			int connectionTimeout, 
			int socketTimeout,
			double volume){
		if(webServiceBaseUrl == null || webServiceBaseUrl == ""){
			throw new NullPointerException("Web Service Base URL may not be null or empty.");
		}
		_webServiceBaseUrl = webServiceBaseUrl;
		_connectionTimeout = connectionTimeout;
		_socketTimeout = socketTimeout;
		_volume = volume;
	}
	
	public boolean isNetworkAvailable(Context context, boolean makeToastOnFalse){
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		boolean isNetworkAvailable = connectivityManager.getBackgroundDataSetting() && connectivityManager.getActiveNetworkInfo() != null;
		if(!isNetworkAvailable){
			if(makeToastOnFalse){
				Toast.makeText(context, "Network not available.", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		return true;
	}
	
	public void callEmitsonServiceAction(Context context, EmitsonServiceAction action, String param){
		String actionName = action.toString();
		Intent serviceIntent = new Intent(context, EmitsonService.class);
		serviceIntent.putExtra(EmitsonKeys.ACTION, actionName);
		serviceIntent.putExtra(EmitsonKeys.ACTION_PARAM, param);
		context.startService(serviceIntent);
	}
	
	public void stopEmitsonService(Context context){
		Intent serviceIntent = new Intent(context, EmitsonService.class);
		context.stopService(serviceIntent);
	}
	
//	public void startStatusUpdater(Context statusContext){
//		if(_statusUpdaterRunning){
//			return;
//		}
//		_statusContext = statusContext;
//		_statusUpdaterRunning = true;
//		Executors.newSingleThreadExecutor().execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while(_statusUpdaterRunning){
//						callEmitsonStatusServiceAction(_statusContext, EmitsonServiceAction.GetStatus, null);
//						Thread.sleep(1000);	
//					}
//				} catch (Exception ex) {
//					Log.e(TAG, ex.getLocalizedMessage());
//				}
//			}
//		});
//	}
	
	public void startStatusUpdater(final Context context){
		if(_timer != null){
			return;
		}
		else{
			_timer = new Timer(true);
		}
		_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					callEmitsonStatusServiceAction(context, EmitsonServiceAction.GetStatus, null);
				}
				catch(Exception ex){
					Log.e(TAG, ex.getLocalizedMessage());
				}
			}
		}, 
		0, 
		1000);
	}
	
	public void stopStatusUpdater(){
		_statusUpdaterRunning = false;
		if(_timer != null){
			_timer.cancel();
			_timer.purge();
			_timer = null;
		}
	}
	
	public void callEmitsonStatusServiceAction(Context context, EmitsonServiceAction action, String param){
		String actionName = action.toString();
		Intent serviceIntent = new Intent(context,  EmitsonStatusService.class);
		serviceIntent.putExtra(EmitsonKeys.ACTION, actionName);
		serviceIntent.putExtra(EmitsonKeys.ACTION_PARAM, param);
		context.startService(serviceIntent);
	}
	
	public void stopEmitsonStatusService(Context context){
		Intent serviceIntent = new Intent(context, EmitsonStatusService.class);
		context.stopService(serviceIntent);
	}
}
