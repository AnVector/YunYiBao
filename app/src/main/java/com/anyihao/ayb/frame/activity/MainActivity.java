package com.anyihao.ayb.frame.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.LogUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.ITaskCallback;
import com.anyihao.ayb.IWifiInfoManager;
import com.anyihao.ayb.R;
import com.anyihao.ayb.WifiInfo;
import com.anyihao.ayb.common.PollingService;
import com.anyihao.ayb.common.PresenterFactory;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;


/**
 * 1. 类内共享常量：直接在类内部private static final定义。
 * 2. 任何运算符左右必须加一个空格。
 * 3. 单行字符限制不超过120个，超出需要换行
 * 4. 运算符与下文一起换行
 * 5. 在括号前不要换行
 * 6. 多个参数超长，逗号后进行换行
 * 7. 方法参数在定义和传入时，多个参数逗号后边必须加空格。参见IDE自动生成的方法格式。
 * 8. 所有的覆写方法，必须加@Override注解
 * 9. 相同参数类型，相同业务含义，才可以使用Java的可变参数，避免使用Object.(可变参数必须放置在参数列表的最后。但尽量避免使用可变参数编程)
 * 10. 不要使用过时的类或方法。
 * 11. 构造方法里禁止加入任何业务逻辑，如果有初始化逻辑，请放在init方法中。
 * 12. 类内方法定义的顺序依次是：公有方法或保护方法->私有方法->getter/setter方法。
 * 13. setter方法中，参数名称与类成员变量名称一致，this.成员们=参数名。在getter/setter方法中尽量不要增加业务逻辑。
 * 14. 循环体内，字符串的连接方式，使用StringBuilder的append方法进行扩展。
 * 15. final可提高程序的响应效率，类方法确定不允许被重写，对象参数前加final，表示不允许修改引用的指向。
 * 16. 不要在条件判断中执行其他复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的boolean类型的变量，以提高可读性。
 * 17. 推荐尽量少用else
 */
public class MainActivity extends ABaseActivity {

    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.red)
    Button mRedBtn;
    @BindView(R.id.color)
    Button mColorBtn;
    private final String TAG = this.getClass().getSimpleName();
    private static final int NETWORK_CHANGED = 10001;
    private static final int NETWORK_DISCONNECTED = 10002;
    private IWifiInfoManager mWifiInfoManager;
    private boolean mIsBound = false;
    private UHandler mHandler = new UHandler(this);

    private static class UHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        private UHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case NETWORK_CHANGED:
                    if (mActivity.get() != null) {
                        ToastUtils.showToast(mActivity.get().getApplicationContext(), "msg.obj="
                                + msg.obj.toString());
                    }
                    break;
                case NETWORK_DISCONNECTED:
                    if (mActivity.get() != null) {
                        ToastUtils.showToast(mActivity.get().getApplicationContext(),
                                "安逸宝WIFI设备连接已断开");
                    }
                    break;
                default:
                    super.dispatchMessage(msg);
                    break;
            }
        }
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mWifiInfoManager == null)
                return;
            //解除Binder
            mWifiInfoManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mWifiInfoManager = null;
            //重新绑定远程service
            Intent bindIntent = new Intent(MainActivity.this, PollingService.class);
            bindService(bindIntent, mWifiManagerConnection, BIND_AUTO_CREATE);
            mIsBound = true;
        }
    };

    private ITaskCallback.Stub mTaskCallback = new ITaskCallback.Stub() {
        @Override
        public void onNetworkChanged(WifiInfo wifiInfo, int count, boolean isConnected) throws
                RemoteException {
            LogUtils.e(TAG, "Binder回调线程Tid=" + Thread.currentThread().getId());
            LogUtils.e("count=" + count + ";isConnect=" + isConnected);
            if (isConnected) {
                LogUtils.e("wifiInfo=" + wifiInfo.toString());
                mHandler.obtainMessage(NETWORK_CHANGED, wifiInfo).sendToTarget();
            } else {
                mHandler.obtainMessage(NETWORK_DISCONNECTED).sendToTarget();
            }
        }
    };

    private ServiceConnection mWifiManagerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWifiInfoManager = IWifiInfoManager.Stub.asInterface(service);
            LogUtils.e(TAG, "Binder绑定线程Tid=" + Thread.currentThread().getId());
            try {
                //设置死亡代理
                mWifiInfoManager.asBinder().linkToDeath(mDeathRecipient, 0);
                //客户端注册监听到服务端
                mWifiInfoManager.registerCallback(mTaskCallback);
                List<WifiInfo> mWifiInfoList = mWifiInfoManager.getWifiInfoList();
                if (mWifiInfoList != null && !mWifiInfoList.isEmpty()) {
                    LogUtils.e("code=" + mWifiInfoList.get(0).getCode());
                    LogUtils.e("description=" + mWifiInfoList.get(0).getDesc());
                    LogUtils.e("message=" + mWifiInfoList.get(0).getMsg());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务端与客户端连接断开，将客户端的Binder置为null
            mWifiInfoManager = null;
            LogUtils.e(TAG, "Binder解除绑定线程Tid=" + Thread.currentThread().getId());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getExtraParams() {

    }

    /**
     * 初始化WebView
     */
    @SuppressLint("JavascriptInterface")  //添加该字段
    @Override
    protected void initData() {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);  //设置运行使用JS
        ButtonClick click = new ButtonClick();
        //这里添加JS的交互事件，这样H5就可以调用原生的代码
        mWebview.addJavascriptInterface(click, click.toString());
//        mWebview.loadUrl("file:///android_asset/android&h5Text0.html"); //加载assets文件中的H5页面
    }

    @Override
    protected void initEvent() {
        mColorBtn.setText("");
        mColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mWebview.loadUrl("javascript:setColor('#00f','这是android 原生调用JS代码的触发事件')");
                Intent bindIntent = new Intent(MainActivity.this, PollingService.class);
                bindService(bindIntent, mWifiManagerConnection, BIND_AUTO_CREATE);
                mIsBound = true;
            }
        });
        mRedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebview.loadUrl("javascript:setRed()");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//                File file = new File(path+"/Tencent/QQfile_recv/vad-2.1.41.tgz");
                File file = new File(path + "/Tencent/QQfile_recv/vad-2402_2.1.41.tgz");
                if (file.exists()) {
                    PresenterFactory.getInstance().createPresenter(MainActivity.this)
                            .execute(new Task.TaskBuilder().setTaskType(TaskType.Method.PUT)
                                    .setUrl("http://192.168.1.102:8080/vad")
                                    .setFile(file)
                                    .setPage(1)
                                    .createTask());
                }
            }
        });
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    /**
     * H5页面按钮点击触发事件
     */
    class ButtonClick {

        //这是 button.click0() 的触发事件
        //H5调用方法：javascript:button.click0()
        @JavascriptInterface
        public void click0() {
            show("title", "");
        }

        //这是 button.click0() 的触发事件，可以传递待参数
        //H5调用方法：javascript:button.click0('参数1','参数2')
        @JavascriptInterface
        public void click0(String data1, String data2) {
            show(data1, data2);
        }


        @JavascriptInterface  //必须添加，这样才可以标志这个类的名称是 button
        public String toString() {
            return "button";
        }

        private void show(String title, String data) {
            new AlertDialog.Builder(getWindow().getContext())
                    .setTitle(title)
                    .setMessage(data)
                    .setPositiveButton("确定", null)
                    .create().show();
        }
    }

    @Override
    protected void onDestroy() {
        LogUtils.e(TAG, "Binder销毁线程Tid=" + Thread.currentThread().getId());
        //如果与服务端Binder没有断开，解除客户端消息监听绑定
        if (mWifiInfoManager != null && mWifiInfoManager.asBinder().isBinderAlive()) {
            try {
                mWifiInfoManager.unregisterCallback(mTaskCallback);
                //        解除客户端Service绑定
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (mIsBound) {
            unbindService(mWifiManagerConnection);
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
