package binarychef.emitson.mobile.utilities;

public class WebServiceTaskResult {
	
	public WebServiceTaskResult(
			WebServiceTaskInstruction completedWebServiceTaskInstruction, 
			String responseText){
		_completedWebServiceTaskInstruction = completedWebServiceTaskInstruction;
		_responseText = responseText;
	}
	
	protected WebServiceTaskInstruction _completedWebServiceTaskInstruction;
	protected String _responseText;
	
	public WebServiceTaskInstruction getCompletedWebServiceTaskInstruction(){
		return _completedWebServiceTaskInstruction;
	}
	
	public String getResponseText(){
		return _responseText;
	}
}