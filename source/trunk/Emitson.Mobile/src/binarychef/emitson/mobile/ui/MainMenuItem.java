package binarychef.emitson.mobile.ui;

public class MainMenuItem {
	
	private String _name;
	private String _description;
	private int _imageNumber;
	
	public String getName(){
		return _name;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public String getDescription(){
		return _description;
	}
	
	public void setDescription(String description){
		_description = description;
	}
	
	public int getImageNumber(){
		return _imageNumber;
	}
	
	public void setImageNumber(int imageNumber){
		_imageNumber = imageNumber;
	}
}
