package app.witkey.live.items;


import org.json.JSONException;
import org.json.JSONObject;

import app.witkey.live.utils.Keys;
import app.witkey.live.utils.EnumUtils;

/**
 * created by developer on 30/05/2017.
 */
public class TaskItem {

	private String response;
	private EnumUtils.ServiceName serviceName;
	private boolean isError;
	private String serviceStatusMessage;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private EnumUtils.ServiceResponseMessage serviceError;

	public String getServiceStatusMessage() {
		return serviceStatusMessage;
	}

	public void setServiceStatusMessage(String serviceStatusMessage) {
		this.serviceStatusMessage = serviceStatusMessage;
	}

	/**
	 * Returns the actual data object from the json response
	 * @return
	 */
	public String getResponse() {
		try {
			JSONObject jObj = new JSONObject(response);
			return jObj.getJSONObject(Keys.DATA_ITEM_JSON_WEBSERVICE).toString();
		} catch (JSONException e) {
		} catch (NullPointerException e) {
		}
		serviceError = EnumUtils.ServiceResponseMessage.InvalidResponse;
		return "";
	}

	//in case if we need the whole raw response...
	public String getRawResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public EnumUtils.ServiceName getServiceName() {
		return serviceName;
	}

	public void setServiceName(EnumUtils.ServiceName serviceName) {
		this.serviceName = serviceName;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public EnumUtils.ServiceResponseMessage getServiceError() {
		return serviceError;
	}

	public void setServiceError(EnumUtils.ServiceResponseMessage serviceError) {
		this.serviceError = serviceError;
	}
}
