package com.anyihao.ayb.listener;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.ayb.common.UApplication;
import com.anyihao.ayb.constant.GlobalConsts;
import com.library.http.okhttp.OkHttpUtils;
import com.library.http.okhttp.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Admin on 2017/7/19.
 */

public class LocationListener implements AMapLocationListener {

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                saveLocationInfo(UApplication.getInstance(), aMapLocation);
                if (PreferencesUtils.getBoolean(UApplication.getInstance(), "isLogin", false)) {
                    sendLocationInfo(aMapLocation.getLatitude(), aMapLocation.getLongitude(),
                            aMapLocation.getCity(), aMapLocation.getCityCode(), aMapLocation
                                    .getProvince(), aMapLocation.getDistrict(), aMapLocation
                                    .getAdCode(), aMapLocation.getStreet(), aMapLocation
                                    .getStreet(), aMapLocation.getAddress());
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.d("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void saveLocationInfo(Context context, AMapLocation aMapLocation) {
        PreferencesUtils.putString(context, "latitude", aMapLocation.getLatitude() + "");
        PreferencesUtils.putString(context, "longitude", aMapLocation.getLongitude() + "");
    }

    private void sendLocationInfo(double latitude, double longtitude, String city, String
            cityCode, String province, String district, String adcode, String street, String
                                          number, String address) {

        Map<String, String> params = new HashMap<>();
        params.put("uid", PreferencesUtils.getString(UApplication.getInstance(), "uid", ""));
        params.put("cmd", "LOCATION");
        params.put("latitude", latitude + "");
        params.put("longtitude", longtitude + "");
        params.put("city", city);
        params.put("citycode", cityCode);
        params.put("province", province);
        params.put("district", district);
        params.put("adcode", adcode);
        params.put("street", street);
        params.put("number", number);
        params.put("address", address);
        OkHttpUtils
                .post()
                .url(GlobalConsts.PREFIX_URL)
                .addHeader("Content-Type", "text/plain")
                .params(params)
                .tag("")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.d(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.d(response);
                    }
                });

    }
}
