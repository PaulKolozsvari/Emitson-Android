package binarychef.emitson.mobile.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import android.os.Environment;

public class FileSystemHelper {
	
	public static String DEFAULT_DATA_FOLDER = "DigisticsSD";
	
	public static String initializeDataDirectory(String dataFolder) throws IOException{
		if(!FileSystemHelper.isExternalStorageReadable()){
			throw new IOException("External storage is not writable or does not exist.");
		}
		File dataDirectory = Environment.getExternalStoragePublicDirectory(dataFolder);
//		File dataDirectory = Environment.getDataDirectory();
		if(!dataDirectory.exists()){
			if(!dataDirectory.mkdirs()){
				throw new IOException("Could not create DigisticsSD Data Folder");
			}
		}
		return dataDirectory.getAbsolutePath();
	}
	
	public static boolean isExternalStorageWritable(){
		String state = Environment.getExternalStorageState();
		boolean result = Environment.MEDIA_MOUNTED.equals(state);
		return result;
	}
	
	public static boolean isExternalStorageReadable(){
		String state = Environment.getExternalStorageState();
		boolean result = Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
		return result;
	}
	
	public static void writeTextToFile(String fileContents, String directory, String fileName) throws Exception{
		FileOutputStream fs  = null;
		PrintStream ps = null;
		try{
			String filePath = directory + File.separator + fileName;
			File file = new File(filePath);
			fs = new FileOutputStream(file, false);
			ps = new PrintStream(fs);
			ps.print(fileContents);
		}
		finally{
			if(ps != null){
				ps.flush();
				ps.close();
			}
			if(fs != null){
				fs.close();
			}
		}
	}
	
	public static String readTextFromFile(String directory, String fileName) throws IOException{
		BufferedReader br = null;
		StringBuilder result = null;
		try{
			String filePath = directory + File.separator + fileName;
			File file = new File(filePath);
			if(!file.exists()){
				throw new FileNotFoundException(String.format("Could not find file %s.", filePath));
			}
			br = new BufferedReader(new FileReader(file));
			result = new StringBuilder();
			String line = null;
			while((line = br.readLine())!= null){
				result.append(line);
				result.append('\n');
			}
		}
		finally{
			if(br != null){
				br.close();
			}
		}
		return result == null ? "" : result.toString();
	}
}
