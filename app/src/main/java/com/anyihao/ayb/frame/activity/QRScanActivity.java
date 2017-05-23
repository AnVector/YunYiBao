package com.anyihao.ayb.frame.activity;

import com.anyihao.ayb.R;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScanActivity extends CaptureActivity {

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_qrscan);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }
}
