package binarychef.emitson.mobile.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class WebServiceTaskInstruction implements Parcelable{
	
	public WebServiceTaskInstruction(
			String queryString,
			String statusMessage,
			WebRequestVerb webRequestVerb,
			String postContentType,
			String postContent){
		_queryString = queryString;
		_statusMessage = statusMessage;
		_webRequestVerb = webRequestVerb;
		_postContentType = postContentType;
		_postContent = postContent;
	}
	
	private String _entityName;
	private String _queryString;
	private String _statusMessage;
	private WebRequestVerb _webRequestVerb;
	private String _postContentType;
	private String _postContent;
	
	public String getQueryString(){
		return _queryString;
	}
	
	public String getStatusMessage(){
		return _statusMessage;
	}
	
	public WebRequestVerb getWebRequestVerb(){
		return _webRequestVerb;
	}
	
	public String getPostContentType(){
		return _postContentType;
	}
	
	public String getPostContent(){
		return _postContent;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_entityName);
		dest.writeString(_queryString);
		dest.writeString(_statusMessage);
		dest.writeString(_webRequestVerb.name());
		dest.writeString(_postContentType);
		dest.writeString(_postContent);
	}
	
	public WebServiceTaskInstruction(Parcel p){
		_entityName = p.readString();
		_queryString = p.readString();
		_statusMessage = p.readString();
		_webRequestVerb = WebRequestVerb.valueOf(p.readString());
		_postContentType = p.readString();
		_postContent = p.readString();
	}
	
	public static final Parcelable.Creator<WebServiceTaskInstruction> CREATOR = new Creator<WebServiceTaskInstruction>() {
		@Override
		public WebServiceTaskInstruction[] newArray(int size) {
			return new WebServiceTaskInstruction[size];
		}
		
		@Override
		public WebServiceTaskInstruction createFromParcel(Parcel source) {
			return new WebServiceTaskInstruction(source);
		}
	};
}
