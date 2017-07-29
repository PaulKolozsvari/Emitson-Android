package binarychef.emitson.mobile.utilities;

public class Broadcast {
	
	public final static String SYNC_BROADCAST_ACTION = "binarychef.emitson.mobile.SyncBroadcast";
	public final static String STATUS_BROADCAST_ACTION = "binarychef.emitson.mobile.StatusBroadcast";
	public final static String EXTENDED_DATA_STATUS_CODE = "binarychef.emitson.mobile.STATUS";
	public final static String EXTENDED_DATA_STATUS_EMITSON_ACTION = "binarychef.com.emitson.mobile.ACTION";
	public final static String EXTENDED_DATA_STATUS_CONTENT = "binarychef.emitson.mobile.ERROR";
	
	public final static int SUCCESS_STATUS_CODE_VALUE = 0;
	public final static int STATUS_UPDATE_STATUS_CODE = 1;
	public final static int TRACK_POSITION = 2;
	public final static int TRACK_LENGTH = 3;
	public final static int ERROR_STATUS_CODE_VALUE = -1;
}
