package binarychef.emitson.mobile.utilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class EmitsonService extends IntentService{
	
	public EmitsonService(){
		super("EmitsonService");
	}

	public EmitsonService(String name) {
		super(name);
	}
	
	private final static String TAG = "EmitsonService";

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = "";
		String param = "";
		try{
			action = intent.getStringExtra(EmitsonKeys.ACTION);
			param = intent.getStringExtra(EmitsonKeys.ACTION_PARAM);
			if(action == null || action.length() == 0){
				throw new Exception("No action specified on intent sent to Emitson Service.");
			}
			runEmitsonAction(intent, action, param);
		}
		catch(Exception ex){
			String message = ex.getLocalizedMessage();
			Log.e(TAG, message);
			broadcastResult(Broadcast.ERROR_STATUS_CODE_VALUE, action, ex.getLocalizedMessage());
		}
	}
	
	private void broadcastResult(int statusCode, String action, String content)
	{
		Intent localIntent = new Intent(Broadcast.SYNC_BROADCAST_ACTION);
		localIntent.putExtra(Broadcast.EXTENDED_DATA_STATUS_CODE, statusCode);
		localIntent.putExtra(Broadcast.EXTENDED_DATA_STATUS_EMITSON_ACTION, action);
		localIntent.putExtra(Broadcast.EXTENDED_DATA_STATUS_CONTENT, content);
		sendBroadcast(localIntent);
	}
	
	private void runEmitsonAction(Intent intent, String action, String param) throws Exception{
		if(!NetworkHelper.isNetworkAvailable(this))
		{
			broadcastResult(Broadcast.ERROR_STATUS_CODE_VALUE, action, "Network not available.");
			return;
		}
		String queryString = action;
		if(param != null && param.length() > 0){
			queryString += "/" + param;
		}
		WebServiceTaskInstruction i = new WebServiceTaskInstruction(queryString, "Calling " + action, WebRequestVerb.Get, null, null);
//		broadcastResult(Broadcast.STATUS_UPDATE_STATUS_CODE, i.getStatusMessage());
		EmitsonSettings settings = EmitsonApplication.getInstance().getSettingsFile().getSettings();
		WebServiceRestClient client = new WebServiceRestClient(settings.webServiceBaseUrl, settings.connectionTimeout, settings.socketTimeout);
		WebServiceTaskResult result = client.callWebService(i, true);
		if(result.getCompletedWebServiceTaskInstruction().getWebRequestVerb() == WebRequestVerb.Get){
//			if(action.equals(EmitsonServiceAction.GetTrackPosition.toString())){
//				broadcastResult(Broadcast.TRACK_POSITION, result.getResponseText());
//				return;
//			}
//			if(action.equals(EmitsonServiceAction.GetTrackLength.toString())){
//				broadcastResult(Broadcast.TRACK_LENGTH, result.getResponseText());
//				return;
//			}
			broadcastResult(Broadcast.SUCCESS_STATUS_CODE_VALUE, action, result.getResponseText());
		}
	}
}
