package binarychef.emitson.mobile.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;

public class UIHelper {
	
	public static View addContentLayoutToViewStub(
			Activity currentActivity,
			int contentLayoutId, 
			int contentStubId){
		ViewStub viewStub = (ViewStub)currentActivity.findViewById(contentStubId);
		viewStub.setLayoutResource(contentLayoutId);
		return viewStub.inflate();
	}
	
	public static void hideKeyboard(Activity context){
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(context.getCurrentFocus() != null){
			inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	
		}
	}
	
	public static void showKeyboardInput(Activity context, View targetView){
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(targetView, 0);
	}
}
