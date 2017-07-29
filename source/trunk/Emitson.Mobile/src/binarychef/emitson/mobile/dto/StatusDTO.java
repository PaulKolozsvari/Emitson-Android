package binarychef.emitson.mobile.dto;

import com.google.gson.annotations.SerializedName;

public class StatusDTO {
	@SerializedName("PlayBackStatus")
	public int playbackStatus;
	
	@SerializedName("TrackPosition")
	public int trackPosition;
	
	@SerializedName("TrackLength")
	public int trackLength;
	
	@SerializedName("TrackCount")
	public int trackCount;
	
	@SerializedName("PlaylistPosition")
	public int playlistPosition;
	
	@SerializedName("CurrentSongTitle")
	public String currentSongTitle;
}
