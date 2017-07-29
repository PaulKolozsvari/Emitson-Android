package binarychef.emitson.mobile;

import binarychef.emitson.mobile.utilities.EmitsonApplication;
import binarychef.emitson.mobile.utilities.SettingsFile;
import binarychef.emitson.mobile.utilities.UIHelper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {

	private final static String TAG = "EmitsonSettingsActivity";
	
	private View _contentView;
	private EditText _edtWebServiceBaseUrl;
	private EditText _edtConnectionTimeout;
	private EditText _edtSocketTimeout;
	private EditText _edtVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			_contentView = UIHelper.addContentLayoutToViewStub(this, R.layout.layout_settings_activity_content, R.id.content_stub);
			initializeInputControls();
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void initializeInputControls() throws Exception{
		_edtWebServiceBaseUrl = (EditText)_contentView.findViewById(R.id.edit_text_web_service_base_url);
		_edtConnectionTimeout = (EditText)_contentView.findViewById(R.id.edit_text_connection_timeout);
		_edtSocketTimeout = (EditText)_contentView.findViewById(R.id.edit_text_socket_timeout);
		_edtVolume = (EditText)_contentView.findViewById(R.id.edit_text_volume);
		
		SettingsFile settingsFile = EmitsonApplication.getInstance().getSettingsFile();
		String webServiceBaseUrl = settingsFile.getSettings().webServiceBaseUrl;
		int connectionTimeout = settingsFile.getSettings().connectionTimeout;
		int socketTimeout = settingsFile.getSettings().socketTimeout;
		double volume = settingsFile.getSettings().volume;
		
		_edtWebServiceBaseUrl.setText(webServiceBaseUrl);
		_edtConnectionTimeout.setText(String.valueOf(connectionTimeout));
		_edtSocketTimeout.setText(String.valueOf(socketTimeout));
		_edtVolume.setText(String.valueOf(volume));
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
			super.onCreateOptionsMenu(menu);
			MenuInflater inflater = new MenuInflater(this);
			inflater.inflate(R.layout.layout_settings_activity_main_menu, menu);
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
			super.onOptionsItemSelected(item);	
			switch (item.getItemId()) {
			case R.id.menu_item_save:
				save();
				break;
			default:
				break;
			}
			return true;
		}
		catch(Exception ex){
			Log.e(TAG, ex.getLocalizedMessage());
			Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			return true;
		}
	}
	
	private void save() throws Exception{
		String url = _edtWebServiceBaseUrl.getText().toString();
		String connectionTimeout = _edtConnectionTimeout.getText().toString();
		String socketTimeout = _edtSocketTimeout.getText().toString();
		String volume = _edtVolume.getText().toString();
		if(url == null || url == ""){
			Toast.makeText(this, "Web Service Base Url may not be null.", Toast.LENGTH_LONG).show();
		}
		SettingsFile settingsFile = EmitsonApplication.getInstance().getSettingsFile();
		settingsFile.getSettings().webServiceBaseUrl = url;
		try{
			settingsFile.getSettings().connectionTimeout = Integer.parseInt(connectionTimeout);
		}
		catch(Exception ex){
			Toast.makeText(this, String.format("%s is not a valid value for Connection Timeout.", connectionTimeout), Toast.LENGTH_SHORT).show();
			return;
		}
		try{
			settingsFile.getSettings().socketTimeout = Integer.parseInt(socketTimeout);
		}
		catch(Exception ex){
			Toast.makeText(this, String.format("%s is not a valid value for Socket Timeout.", socketTimeout), Toast.LENGTH_SHORT).show();
			return;
		}
		try{
			settingsFile.getSettings().volume = Integer.parseInt(volume);
		}
		catch(Exception ex){
			Toast.makeText(this, String.format("%s is not a valid value for Volume.", volume), Toast.LENGTH_SHORT).show();
		}
		settingsFile.save();
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		Intent intentLoginActivity = new Intent(this, MainActivity.class);
		setResult(RESULT_OK, intentLoginActivity);
		finish();
	}
}
