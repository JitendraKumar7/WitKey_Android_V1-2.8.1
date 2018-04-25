package app.witkey.live.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * created by developer on 5/25/2016.
 */
public class NetworkOP {
    //Check the internet connection

    String IPaddress;
    Context mContext;


    public String NetwordDetect(Context context) {
        mContext = context;
        boolean WIFI = false;
        boolean MOBILE = false;

        try{
            ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

            for (NetworkInfo netInfo : networkInfo) {

                if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (netInfo.isConnected())
                        WIFI = true;
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (netInfo.isConnected())
                        MOBILE = true;
            }
            if(WIFI == true)
            {
                IPaddress = GetDeviceipWiFiData();
                return  IPaddress;
            }
            if(MOBILE == true)
            {
                IPaddress = GetDeviceipMobileData();
                return  IPaddress;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }


    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData()
    {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }
}
