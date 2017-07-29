package binarychef.emitson.mobile.utilities;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SettingsFile {
	
	public SettingsFile(){
		_gson = new Gson();
		_settings = new EmitsonSettings();
	}
	
	public SettingsFile(EmitsonSettings settings){
		_settings = settings;
	}
	
	public final static String FILE_NAME = "Emitson.settings";
	
	private Gson _gson;
	private String _dataDirectoryPath;
	private String _filePath;
	private EmitsonSettings _settings;
	
	public EmitsonSettings getSettings(){
		return _settings;
	}
	
	private void initializeFilePath() throws IOException{
		if(!FileSystemHelper.isExternalStorageReadable()){
			throw new IOException("External storage is not writable or does not exist.");
		}
		File dataDirectory = Environment.getExternalStoragePublicDirectory(FileSystemHelper.DEFAULT_DATA_FOLDER);
		if(!dataDirectory.exists()){
			if(!dataDirectory.mkdirs()){
				throw new IOException("Could not create Emitson Data Folder");
			}
		}
		_dataDirectoryPath = FileSystemHelper.initializeDataDirectory(FileSystemHelper.DEFAULT_DATA_FOLDER);
		_filePath = _dataDirectoryPath + File.separator + FILE_NAME;
	}
	
	public void save() throws Exception{
		Gson gson = new Gson();
		String json = gson.toJson(_settings);
		String dataDirectory = Environment.getExternalStoragePublicDirectory(FileSystemHelper.DEFAULT_DATA_FOLDER).getAbsolutePath();
		FileSystemHelper.writeTextToFile(json, dataDirectory, FILE_NAME);
	}
	
	public static SettingsFile getFromFile() throws Exception{
		EmitsonSettings result = new EmitsonSettings();
//		String dataDirectory = Environment.getExternalStoragePublicDirectory(FileSystemHelper.DEFAULT_DATA_FOLDER).getAbsolutePath();
		String dataDirectory = FileSystemHelper.initializeDataDirectory(FileSystemHelper.DEFAULT_DATA_FOLDER);
		String settingsFilePath = dataDirectory + File.separator + FILE_NAME;
		if(!(new File(settingsFilePath).exists())){
			getDefaultSettings().save();
		}
		String json = FileSystemHelper.readTextFromFile(dataDirectory, FILE_NAME);
		Gson gson = new Gson();
		result = gson.fromJson(json, EmitsonSettings.class);
		return new SettingsFile(result);
	}
	
	public static SettingsFile getDefaultSettings(){
		SettingsFile result = new SettingsFile();
		result.getSettings().webServiceBaseUrl = "http://192.168.0.200:2985/Emitson";
		result.getSettings().connectionTimeout = 60000;
		result.getSettings().socketTimeout = 60000;
		result.getSettings().volume = 50;
		return result;
	}
}
