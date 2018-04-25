package app.witkey.live.tasks;

import android.content.Context;

import java.util.HashMap;

import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.utils.EnumUtils;


public class SampleWebserviceCall {

	private void callStreamActionService(final int action, final boolean showLoader, Context context) {
		HashMap<String, Object> serviceParams = new HashMap<String, Object>();
		HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();


		new WebServicesVolleyTask(context, showLoader, "",
				EnumUtils.ServiceName.AuthenticateUser,
				EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

			@Override
			public void onTaskComplete(TaskItem taskItem) {
				if (taskItem.isError()) {

				} else {
					try {


					} catch (Exception ex) {
						ex.printStackTrace();
					}
					// if response is successful then do something
				}
			}
		});
	}

}
