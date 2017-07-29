package binarychef.emitson.mobile.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

public class EmitsonResponseReceiver extends BroadcastReceiver{
	
	public EmitsonResponseReceiver(){
	}
	
	public EmitsonResponseReceiver(EmitsonResponseReceiverCallback callback){
		_callback = callback;
	}
	
	private EmitsonResponseReceiverCallback _callback;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		showNotification(context, intent);
	}
	
	private void showNotification(Context context, Intent intent){
		int statusCode = intent.getIntExtra(Broadcast.EXTENDED_DATA_STATUS_CODE, -2);
		String action = intent.getStringExtra(Broadcast.EXTENDED_DATA_STATUS_EMITSON_ACTION);
		String contentText = intent.getStringExtra(Broadcast.EXTENDED_DATA_STATUS_CONTENT);
//		Resources resources = context.getResources();
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);
//		
//		NotificationCompat.Builder builder =
//			    new NotificationCompat.Builder(context)
//			    .setSmallIcon(android.R.drawable.ic_menu_report_image)
//			    .setContentTitle("Digistics Sync")
//			    .setContentText(contentText)
//			    .setContentIntent(pendingIntent)
//			    .setAutoCancel(true);
//		Notification notification = builder.build();
//		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(0, notification);
		if(_callback != null){
			_callback.onSyncResponseReceived(statusCode, action, contentText);
		}
	}
}