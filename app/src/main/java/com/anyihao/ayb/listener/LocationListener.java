package com.anyihao.ayb.listener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;

/**
 * Created by Admin on 2017/3/25.
 */

public class LocationListener implements AMapLocationListener {

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
            //可在其中解析amapLocation获取相应内容。
                Logger.e(aMapLocation.getCity());
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Logger.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
