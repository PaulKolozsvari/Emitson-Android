package binarychef.emitson.mobile.utilities;

import com.google.gson.annotations.SerializedName;

public class EmitsonSettings {
	@SerializedName("webServiceBaseUrl")
	public String webServiceBaseUrl;
	
	@SerializedName("connectionTimeout")
	public int connectionTimeout;
	
	@SerializedName("socketTimeout")
	public int socketTimeout;
	
	@SerializedName("volume")
	public int volume;
}
