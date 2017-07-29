package binarychef.emitson.mobile.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import binarychef.emitson.mobile.*;

public class MainMenuItemListAdapter extends BaseAdapter{
	
	public MainMenuItemListAdapter(Context context, List<MainMenuItem> items){
		_inflater = LayoutInflater.from(context);
		_items = items;
		_imageIds = new int[]{
				R.drawable.play,
				R.drawable.stop,
				R.drawable.pause,
				R.drawable.next,
				R.drawable.previous,
				R.drawable.forward,
				R.drawable.rewind,
				R.drawable.up,
				R.drawable.down
		};
	}
	
	private LayoutInflater _inflater;
	private List<MainMenuItem> _items;
	private int[] _imageIds;

	@Override
	public int getCount() {
		return _items.size();
	}

	@Override
	public Object getItem(int position) {
		return _items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = _inflater.inflate(R.layout.layout_main_menu_row, null);
			holder = new ViewHolder();
			holder.setTextView((TextView)convertView.findViewById(R.id.text_item_name));
			holder.setImageView((ImageView)convertView.findViewById(R.id.image_item));
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.getTextView().setText(_items.get(position).getName());
		int imageIndex = _items.get(position).getImageNumber();
		holder.getImageView().setImageResource((_imageIds[imageIndex]));
		return convertView;
	}
	
	public class ViewHolder{
		
		private ImageView _imageView;
		private TextView _txtView;
		
		public ImageView getImageView(){
			return _imageView;
		}
		
		public void setImageView(ImageView imageView){
			_imageView = imageView;
		}
		
		public TextView getTextView(){
			return _txtView;
		}
		
		public void setTextView(TextView textView){
			_txtView = textView;
		}
	}
}
