package binarychef.emitson.mobile.utilities;

public interface EmitsonResponseReceiverCallback {
	
	void onSyncResponseReceived(int statusCode, String action, String contentText);
}
