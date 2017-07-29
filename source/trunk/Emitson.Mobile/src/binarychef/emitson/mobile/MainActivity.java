package binarychef.emitson.mobile;

import java.util.ArrayList;
import java.util.List;

import binarychef.emitson.mobile.ui.ActivityRequestCode;
import binarychef.emitson.mobile.ui.MainMenuItem;
import binarychef.emitson.mobile.ui.MainMenuItemListAdapter;
import binarychef.emitson.mobile.utilities.EmitsonApplication;
import binarychef.emitson.mobile.utilities.EmitsonResponseReceiver;
import binarychef.emitson.mobile.utilities.EmitsonResponseReceiverCallback;
import binarychef.emitson.mobile.utilities.EmitsonServiceAction;
import binarychef.emitson.mobile.utilities.EmitsonSettings;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import binarychef.emitson.mobile.utilities.Broadcast;

public class MainActivity extends BaseActivity implements EmitsonResponseReceiverCallback{

	private final static String TAG= "EmitsonMainActivity";
	
	private View _contentView;
	private ListView _listMainMenu;
	
	private EmitsonResponseReceiver _responseReceiver;
	private IntentFilter _responseReceiverIntentFilter;
	private TextView _textTrackPosition;
	private TextView _textTrackLength;
	private boolean _refreshTrackPosition = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			_contentView = UIHelper.addContentLayoutToViewStub(this, R.layout.layout_main_activity_content, R.id.content_stub);
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
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void initializeInputControls(){
		_textTrackPosition = (TextView)_contentView.findViewById(R.id.text_track_position);
		_textTrackLength = (TextView)_contentView.findViewById(R.id.text_track_length);
		_listMainMenu = (ListView)_contentView.findViewById(R.id.list_main_menu);
	}
	
	@Override
	protected void onResume() {
		try{
			super.onResume();
			registerEmitsonResponseReceiver();
			_listMainMenu.setAdapter(new MainMenuItemListAdapter(this, getMenuItems()));
			_listMainMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onMenuItemClicked(parent, view, position, id);
				}
			});
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onPause() {
		try{
			super.onPause();
			unregisterSyncResponseReceiver();
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
//	private void refreshTrackPosition(){
//		try{
//			EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.GetTrackPosition, null);
//			EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.GetTrackLength, null);
//		}
//		catch(Exception ex){
//			Log.e(TAG, ex.getLocalizedMessage());
//			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//		}
//	}
	
	private void onMenuItemClicked(AdapterView<?> parent, View view, int position, long id){
		try{
			MainMenuItem item = (MainMenuItem)_listMainMenu.getItemAtPosition(position);
			if(item.getName().equalsIgnoreCase(getResources().getString(R.string.play))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.Play, null);
//				_refreshTrackPosition = true;
//				refreshTrackPosition();
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.stop))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.Stop, null);
//				_refreshTrackPosition = false;
//				refreshTrackPosition();
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.pause))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this,  EmitsonServiceAction.Pause, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.next))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.NextTrack, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.previous))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.PreviousTrack, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.forward))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.Forward5Sec, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.rewind))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.Rewind5Sec, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.volume_up))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.VolumeUp, null);
			}
			else if(item.getName().equalsIgnoreCase(getResources().getString(R.string.volume_down))){
				EmitsonApplication.getInstance().callEmitsonServiceAction(this, EmitsonServiceAction.VolumeDown, null);
			}
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private List<MainMenuItem> getMenuItems(){
		List<MainMenuItem> result = new ArrayList<MainMenuItem>();
		
		MainMenuItem play = new MainMenuItem();
		play.setName(getResources().getString(R.string.play));
		play.setImageNumber(0);
		result.add(play);
		
		MainMenuItem stop = new MainMenuItem();
		stop.setName(getResources().getString(R.string.stop));
		stop.setImageNumber(1);
		result.add(stop);
		
		MainMenuItem pause = new MainMenuItem();
		pause.setName(getResources().getString(R.string.pause));
		pause.setImageNumber(2);
		result.add(pause);
		
		MainMenuItem next = new MainMenuItem();
		next.setName(getResources().getString(R.string.next));
		next.setImageNumber(3);
		result.add(next);
		
		MainMenuItem previous = new MainMenuItem();
		previous.setName(getResources().getString(R.string.previous));
		previous.setImageNumber(4);
		result.add(previous);
		
		MainMenuItem forward = new MainMenuItem();
		forward.setName(getResources().getString(R.string.forward));
		forward.setImageNumber(5);
		result.add(forward);
		
		MainMenuItem rewind = new MainMenuItem();
		rewind.setName(getResources().getString(R.string.rewind));
		rewind.setImageNumber(6);
		result.add(rewind);
		
		MainMenuItem volumeUp = new MainMenuItem();
		volumeUp.setName(getResources().getString(R.string.volume_up));
		volumeUp.setImageNumber(7);
		result.add(volumeUp);
		
		MainMenuItem volumeDown = new MainMenuItem();
		volumeDown.setName(getResources().getString(R.string.volume_down));
		volumeDown.setImageNumber(8);
		result.add(volumeDown);
		
		return result;
	}
	
	private void registerEmitsonResponseReceiver(){
		if(_responseReceiver == null){
			_responseReceiverIntentFilter = new IntentFilter();
			_responseReceiverIntentFilter.addAction(Broadcast.SYNC_BROADCAST_ACTION);
			_responseReceiver = new EmitsonResponseReceiver(this);
		}
		registerReceiver(_responseReceiver, _responseReceiverIntentFilter);
	}
	
	private void unregisterSyncResponseReceiver(){
		if(_responseReceiverIntentFilter != null){
			unregisterReceiver(_responseReceiver);
		}
	}
	
	@Override
	public void onSyncResponseReceived(int statusCode, String action, String contentText) {
		switch(statusCode){
			case Broadcast.SUCCESS_STATUS_CODE_VALUE:
//				Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
				break;
			case Broadcast.ERROR_STATUS_CODE_VALUE:
				Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
				_refreshTrackPosition = false;
				break;
			case Broadcast.STATUS_UPDATE_STATUS_CODE:
//				Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
				break;
			case Broadcast.TRACK_POSITION:
				_textTrackPosition.setText(contentText);
				break;
			case Broadcast.TRACK_LENGTH:
				_textTrackLength.setText(contentText);
				break;
		}
//		if(_refreshTrackPosition){
//			refreshTrackPosition();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
			MenuInflater inflater = new MenuInflater(this);
			inflater.inflate(R.layout.layout_main_activity_main_menu, menu);
			return true;
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try{
			switch(item.getItemId()){
			case R.id.menu_item_settings:
				settings();
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	private void settings(){
		try{
			Intent intentMainActivity = new Intent(this, SettingsActivity.class);
			startActivityForResult(intentMainActivity, ActivityRequestCode.START_SETTINGS_ACTIVITY);
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
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
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
