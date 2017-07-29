package binarychef.emitson.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import binarychef.emitson.mobile.dto.PlaybackStatus;
import binarychef.emitson.mobile.dto.StatusDTO;
import binarychef.emitson.mobile.dto.StatusSummary;
import binarychef.emitson.mobile.ui.ActivityRequestCode;
import binarychef.emitson.mobile.ui.MainMenuItem;
import binarychef.emitson.mobile.ui.MainMenuItemListAdapter;
import binarychef.emitson.mobile.utilities.EmitsonApplication;
import binarychef.emitson.mobile.utilities.EmitsonResponseReceiver;
import binarychef.emitson.mobile.utilities.EmitsonResponseReceiverCallback;
import binarychef.emitson.mobile.utilities.EmitsonServiceAction;
import binarychef.emitson.mobile.utilities.EmitsonSettings;
import binarychef.emitson.mobile.utilities.SettingsFile;
import binarychef.emitson.mobile.utilities.StringHelper;
import binarychef.emitson.mobile.utilities.UIHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import binarychef.emitson.mobile.utilities.Broadcast;

public class PlayerActivity extends BaseActivity implements EmitsonResponseReceiverCallback 
{
	private final static String TAG= "EmitsonPlayerActivity";
	
	private View _contentView;
	private ImageButton _btnPlay;
	private ImageButton _btnPause;
	private ImageButton _btnStop;
	private ImageButton _btnPrevious;
	private ImageButton _btnRewind;
	private ImageButton _btnForward;
	private ImageButton _btnNext;
	private ImageButton _btnVolumeDown;
	private ImageButton _btnVolumeUp;
	
	private EmitsonResponseReceiver _responseReceiver;
	private IntentFilter _responseReceiverIntentFilter;
	private TextView _txtVolume;
	private TextView _txtStatus;
	private TextView _txtElapsed;
	private TextView _txtTrackTotalLength;
	private ProgressBar _progressStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
			super.onCreate(savedInstanceState);
			_contentView = UIHelper.addContentLayoutToViewStub(this, R.layout.layout_player_activity_content, R.id.content_stub);
			initializeInputControls();
			if(savedInstanceState == null){
				EmitsonSettings settings = EmitsonApplication.getInstance().getSettingsFile().getSettings();
				EmitsonApplication.getInstance().initialize(
						settings.webServiceBaseUrl, 
						settings.connectionTimeout, 
						settings.socketTimeout,
						settings.volume);
			}
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initializeInputControls() throws Exception
	{
		_btnPlay = (ImageButton)_contentView.findViewById(R.id.btn_play);
		_btnPause = (ImageButton)_contentView.findViewById(R.id.btn_pause);
		_btnStop = (ImageButton)_contentView.findViewById(R.id.btn_stop);
		_btnPrevious = (ImageButton)_contentView.findViewById(R.id.btn_previous);
		_btnRewind = (ImageButton)_contentView.findViewById(R.id.btn_rewind);
		_btnForward = (ImageButton)_contentView.findViewById(R.id.btn_forward);
		_btnNext = (ImageButton)_contentView.findViewById(R.id.btn_next);
		_btnVolumeDown = (ImageButton)_contentView.findViewById(R.id.btn_volume_down);
		_btnVolumeUp = (ImageButton)_contentView.findViewById(R.id.btn_volume_up);
		_txtElapsed = (TextView)_contentView.findViewById(R.id.text_elapsed);
		_txtTrackTotalLength = (TextView)_contentView.findViewById(R.id.text_track_total_length);
		_progressStatus = (ProgressBar)_contentView.findViewById(R.id.progress_status);
		_progressStatus.setIndeterminate(false);
		_txtStatus = (TextView)_contentView.findViewById(R.id.text_status);
		_txtStatus.setText("");
		_txtVolume = (TextView)_contentView.findViewById(R.id.text_volume);
		String volume = String.valueOf(EmitsonApplication.getInstance().getSettingsFile().getSettings().volume);
		displayVolume(volume);
		
		_btnPlay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.Play, null);
			}
		});
		_btnPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.Pause, null);
			}
		});
		_btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.Stop, null);
			}
		});
		_btnPrevious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.PreviousTrack, null);	
			}
		});
		_btnRewind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.Rewind5Sec, null);
			}
		});
		_btnForward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.Forward5Sec, null);
			}
		});
		_btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAction(EmitsonServiceAction.NextTrack, null);
			}
		});
		_btnVolumeDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeVolume(false);
			}
		});
		_btnVolumeUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeVolume(true);
			}
		});
	}
	
	private void changeVolume(boolean increase){
		try
		{
			SettingsFile s = EmitsonApplication.getInstance().getSettingsFile();
			int volume = increase ? s.getSettings().volume + 10 : s.getSettings().volume - 10;
			if(volume > 100){
				volume = 100;
			}
			else if(volume < 0){
				volume = 0;
			}
			String value = String.valueOf(volume);
			sendAction(EmitsonServiceAction.Volume, value);
			s.getSettings().volume = volume;
			s.save();
			displayVolume(value);
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void displayVolume(String volume)
	{
		String value = StringHelper.padLeft(volume, 3);
		_txtVolume.setText(value + "%");
	}
	
	private void sendAction(EmitsonServiceAction action, String param){
		try
		{
			EmitsonApplication.getInstance().callEmitsonServiceAction(this, action, param);
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void registerEmitsonResponseReceiver(){
		if(_responseReceiver == null){
			_responseReceiverIntentFilter = new IntentFilter();
			_responseReceiverIntentFilter.addAction(Broadcast.SYNC_BROADCAST_ACTION);
			_responseReceiverIntentFilter.addAction(Broadcast.STATUS_BROADCAST_ACTION);
			_responseReceiver = new EmitsonResponseReceiver(this);
		}
		registerReceiver(_responseReceiver, _responseReceiverIntentFilter);
	}
	
	private void unregisterEmitsonResponseReceiver(){
		if(_responseReceiver != null){
			unregisterReceiver(_responseReceiver);
		}
	}
	
	@Override
	protected void onResume() {
		try
		{
			super.onResume();
			registerEmitsonResponseReceiver();
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onPause(){
		try
		{
			super.onPause();
			unregisterEmitsonResponseReceiver();
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSyncResponseReceived(int statusCode, String action, String contentText) 
	{
		try
		{
			switch(statusCode){
			case Broadcast.SUCCESS_STATUS_CODE_VALUE:
				if(action.equals(EmitsonServiceAction.GetStatus.toString())){
					updateStatus(contentText);
				}
				else if(action.equals(EmitsonServiceAction.Play.toString())){
					EmitsonApplication.getInstance().startStatusUpdater(this);
					_txtStatus.setText(contentText);
				}
				else if(action.equals(EmitsonServiceAction.Stop.toString())){
					EmitsonApplication.getInstance().stopStatusUpdater();
					_txtStatus.setText(contentText);
					resetStatus();
				}
				else{
					
				}
				break;
			case Broadcast.ERROR_STATUS_CODE_VALUE:
				if(action.equals(EmitsonServiceAction.GetStatus.toString())){
					EmitsonApplication.getInstance().stopStatusUpdater();
				}
				_txtStatus.setText("");
				Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
				resetStatus();
				EmitsonApplication.getInstance().stopStatusUpdater();
				break;
			case Broadcast.STATUS_UPDATE_STATUS_CODE:
//				Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
				break;
			case Broadcast.TRACK_POSITION:
//				_textTrackPosition.setText(contentText);
				break;
			case Broadcast.TRACK_LENGTH:
//				_textTrackLength.setText(contentText);
				break;
		}
//		if(_refreshTrackPosition){
//			refreshTrackPosition();
//		}
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			resetStatus();
			try{
				EmitsonApplication.getInstance().stopStatusUpdater();
			}
			catch(Exception e){
			}
		}
	}
	
	private void resetStatus(){
		_progressStatus.setMax(1);
		_progressStatus.setProgress(0);
		_txtElapsed.setText("");
		_txtTrackTotalLength.setText("");
	}
	
	private void updateStatus(String statusJson){
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		StatusDTO status = gson.fromJson(statusJson, StatusDTO.class);
		if(status == null){
			throw new NullPointerException("status received from server is null.");
		}
		StatusSummary statusSummary = new StatusSummary(status); 
		_txtElapsed.setText(statusSummary.getElapsedInfo());
		_txtTrackTotalLength.setText(statusSummary.getTrackLengthInfo());
		
		_progressStatus.setMax(statusSummary.getStatusDTO().trackLength); //Total seconds of the the current track.
		_progressStatus.setProgress(statusSummary.getTrackPositionTotalSeconds()); //Number of seconds that have elapsed. 
		
		_txtStatus.setText(statusSummary.getStatusDTO().currentSongTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try
		{
			MenuInflater inflater = new MenuInflater(this);
			inflater.inflate(R.layout.layout_main_activity_main_menu, menu);
			return true;
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try
		{
			switch(item.getItemId()){
			case R.id.menu_item_settings:
				settings();
			}
			return super.onOptionsItemSelected(item);
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	private void settings(){
		try
		{
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivityForResult(intentSettings, ActivityRequestCode.START_SETTINGS_ACTIVITY);
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try
		{
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == ActivityRequestCode.START_SETTINGS_ACTIVITY && resultCode == RESULT_OK){
				EmitsonSettings settings = EmitsonApplication.getInstance().getSettingsFile().getSettings();
				EmitsonApplication.getInstance().initialize(
						settings.webServiceBaseUrl, 
						settings.connectionTimeout, 
						settings.socketTimeout,
						settings.volume);
			}
		}
		catch(Exception ex)
		{
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
