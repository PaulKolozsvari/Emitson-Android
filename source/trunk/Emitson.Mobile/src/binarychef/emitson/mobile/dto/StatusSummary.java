package binarychef.emitson.mobile.dto;

public class StatusSummary {
	public StatusSummary(StatusDTO statusDTO){
		_statusDTO = statusDTO;
		calculate();
	}
	
	private StatusDTO _statusDTO;
	private PlaybackStatus _playbackStatus;
	
	private int _trackPositionTotalSeconds;
	private int _trackPositionMinutes; 
	private int _trackPositionSeconds;
	
	private int _trackLengthMinutes;
	private int _trackLengthSeconds;
	
	private int _remainingTotalSeconds;
	private int _remainingMinutes;
	private int _remainingSeconds;
	
	private String _elapsedInfo;
	private String _trackLengthInfo;
	
	public StatusDTO getStatusDTO(){
		return _statusDTO;
	}
	
	public PlaybackStatus getPlaybackStatus(){
		return _playbackStatus;
	}
	
	public int getTrackPositionTotalSeconds(){
		return _trackPositionTotalSeconds;
	}
	
	public int getTrackPositionMinutes(){
		return _trackPositionMinutes;
	}
	
	public int getTrackPositionSeconds(){
		return _trackPositionSeconds;
	}
	
	public int getTrackLengthMinutes(){
		return _trackLengthMinutes;
	}
	
	public int getTrackLengthSeconds(){
		return _trackLengthSeconds;
	}
	
	public int getRemainingTotalSeconds(){
		return _remainingTotalSeconds;
	}
	
	public int getRemainingMinutes(){
		return _remainingMinutes;
	}
	
	public int getRemainingSeconds(){
		return _remainingSeconds;
	}
	
	public String getElapsedInfo(){
		return _elapsedInfo;
	}
	
	public String getTrackLengthInfo(){
		return _trackLengthInfo;
	}
	
	private void calculate(){
		PlaybackStatus[] statuses = PlaybackStatus.values();
		_playbackStatus = statuses[_statusDTO.playbackStatus];
		if(_playbackStatus == PlaybackStatus.Play || _playbackStatus == PlaybackStatus.Paused){
			double temp = (((double)_statusDTO.trackPosition) / 1000.0);
			_trackPositionTotalSeconds = (int)temp;
			_trackPositionMinutes = _trackPositionTotalSeconds / 60;
			_trackPositionSeconds = _trackPositionTotalSeconds - (_trackPositionMinutes * 60);
			
			_trackLengthMinutes = _statusDTO.trackLength / 60;
			_trackLengthSeconds = _statusDTO.trackLength - (_trackLengthMinutes * 60);
			
			_remainingTotalSeconds = _statusDTO.trackLength - _trackPositionTotalSeconds;
			_remainingMinutes = _remainingTotalSeconds / 60;
			_remainingSeconds = _remainingTotalSeconds - (_remainingMinutes * 60);
		}
		else if(_playbackStatus == PlaybackStatus.Stopped){
			_trackPositionTotalSeconds = 0;
			_trackPositionMinutes = 0;
			_trackPositionSeconds = 0;
			
			_trackLengthMinutes = _statusDTO.trackLength / 60;
			_trackLengthSeconds = _statusDTO.trackLength - (_trackLengthMinutes * 60);
			
			_remainingTotalSeconds = 0;
			_remainingMinutes = 0;
			_remainingSeconds = 0;
		}
		setInfoMessages();
	}
	
	private void setInfoMessages(){
		String elapsedSeconds = String.valueOf(_trackPositionSeconds);
		String remainingSeconds = String.valueOf(_remainingSeconds);
		if(elapsedSeconds.length() < 2){
			elapsedSeconds = "0" + elapsedSeconds;
		}
		if(remainingSeconds.length() < 2){
			remainingSeconds = "0" + remainingSeconds;
		}
		_elapsedInfo = _trackPositionMinutes + ":" + elapsedSeconds + " / -" +
				_remainingMinutes + ":" + remainingSeconds;
		
		String secondsOfTrack = String.valueOf(_trackLengthSeconds);
		if(secondsOfTrack.length() < 2){
			secondsOfTrack = "0" + secondsOfTrack;
		}
		_trackLengthInfo = "Track "  + String.valueOf(_statusDTO.playlistPosition + 1) + "/" + String.valueOf(_statusDTO.trackCount) + ": " +
				_trackLengthMinutes + ":" + secondsOfTrack;
	}
}
