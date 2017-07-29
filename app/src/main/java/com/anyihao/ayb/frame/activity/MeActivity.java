package com.anyihao.ayb.frame.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UserInfoAdapter;
import com.anyihao.ayb.bean.KeyValueBean;
import com.anyihao.ayb.bean.ProvinceBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.bean.UserInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MeActivity extends ABaseActivity {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.progressbar_circular)
    CircularProgressBar progressbarCircular;
    private UserInfoAdapter mAdapter;
    private List<KeyValueBean> mData = new LinkedList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private String uid;
    private String userType;
    private TextView tvValue;
    private String mArea;
    private String mDate;
    private String mPhoneNum;
    private TimePickerView mPvDate;
    private Dialog bottomDialog;
    private TextView tvMale;
    private TextView tvFemale;
    private TextView tvCancel;
    private String mAvatarUrl;
    private String mNickName;
    private String mGender;
    private String mZone;
    private static final int REQUEST_UPDATE_CODE = 0x0004;
    private static final int RC_CAMERA_PERM = 0x0005;
    private static final int REQUEST_CODE_CHOOSE = 0x0006;
    private boolean isGender = true;
    private String depositMoney;
    private UHandler mHandler = new UHandler(this);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_me;
    }

    @Override
    protected void getExtraParams() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        uid = intent.getStringExtra("uid");
        userType = intent.getStringExtra("userType");
    }

    private static class UHandler extends Handler {
        private WeakReference<MeActivity> mActivity;

        private UHandler(MeActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            final MeActivity activity = mActivity.get();
            if (mActivity != null) {
                switch (msg.what) {
                    case MSG_LOAD_DATA:
                        if (activity.thread == null) {//如果已创建就不再重新创建子线程了
                            activity.thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 写子线程中的操作,解析省市区数据
                                    activity.initCity();
                                }
                            });
                            activity.thread.start();
                        }
                        break;
                    case MSG_LOAD_SUCCESS:
                        activity.isLoaded = true;
                        break;
                    case MSG_LOAD_FAILED:
                        activity.isLoaded = false;
                        break;
                    default:
                        break;
                }
            }

        }
    }

    @Override
    protected void initData() {
        getUserInfo();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.about_me));
        mAdapter = new UserInfoAdapter(this, R.layout.item_user_info, mData);
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        initTimePicker();
        initBottomDialog();
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 1, 1);
        //时间选择器
        mPvDate = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                mDate = fmt.format(date);
                updateInfo(2, mDate);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.5f)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentSize(16)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
    }

    @Override
    protected void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (o instanceof KeyValueBean) {
                    switch (((KeyValueBean) o).getTitle()) {
                        case "头像":
//                            if (bottomDialog != null) {
//                                isGender = false;
//                                tvMale.setText(getString(R.string.from_album));
//                                tvFemale.setText(getString(R.string.photo));
//                                bottomDialog.show();
//                            }
                            break;
                        case "地区":
                            tvValue = (TextView) view.findViewById(R.id.value);
                            if (isLoaded) {
                                showPickerView(tvValue);
                            } else {
                                mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                            }
                            break;
                        case "生日":
                            tvValue = (TextView) view.findViewById(R.id.value);
                            if (mPvDate != null) {
                                mPvDate.show(tvValue);
                            }
                            break;
                        case "我的二维码":
                            Intent intent1 = new Intent(MeActivity.this, QRActivity.class);
                            intent1.putExtra("uid", uid);
                            intent1.putExtra("avatar", mAvatarUrl);
                            intent1.putExtra("nickname", mNickName);
                            intent1.putExtra("gender", mGender);
                            intent1.putExtra("zone", mZone);
                            startActivity(intent1);
                            break;
                        case "押金退款":
                            tvValue = (TextView) view.findViewById(R.id.value);
                            if ("未缴纳".equals(((KeyValueBean) o).getValue())) {
                                ToastUtils.showToast(getApplicationContext(), ((KeyValueBean) o)
                                        .getValue());
                                return;
                            } else {
                                depositMoney = ((KeyValueBean) o).getValue();
                                showConfirmDialog();
                            }
                            break;
                        case "手机号码":
                            Intent intent2 = new Intent(MeActivity.this, GetVerifyCodeActivity
                                    .class);
                            intent2.putExtra("title", "验证原手机");
                            intent2.putExtra("action", "ORIGINAL");
                            intent2.putExtra("phoneNum", mPhoneNum);
                            startActivity(intent2);
                            break;
                        case "性别":
                            tvValue = (TextView) view.findViewById(R.id.value);
                            if (bottomDialog != null) {
                                isGender = true;
                                tvMale.setText(getString(R.string.male));
                                tvFemale.setText(getString(R.string.female));
                                bottomDialog.show();
                            }
                            break;
                        default:
                            Intent intent3 = new Intent(MeActivity.this, UpdateInfoActivity
                                    .class);
                            intent3.putExtra(UpdateInfoActivity.INFORMATION_KEY, ((KeyValueBean) o)
                                    .getTitle());
                            intent3.putExtra(UpdateInfoActivity.INFORMATION_VALUE, ((KeyValueBean)
                                    o).getValue());
                            startActivityForResult(intent3, REQUEST_UPDATE_CODE);
                            break;
                    }
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGender) {
                    mGender = "男";
                    updateInfo(3, mGender);
                    if (bottomDialog != null) {
                        bottomDialog.dismiss();
                    }
                } else {
                    permissionsRequest();
                }

            }
        });

        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGender) {
                    mGender = "女";
                    updateInfo(3, mGender);
                    if (bottomDialog != null) {
                        bottomDialog.dismiss();
                    }
                } else {

                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomDialog != null) {
                    bottomDialog.dismiss();
                }
            }
        });
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    protected void permissionsRequest() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permissions)) {
//            Matisse.from(this)
//                    .choose(MimeType.allOf())
//                    .countable(true)
//                    .capture(true)
//                    .captureStrategy(
//                            new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
//                    .maxSelectable(9)
//                    .gridExpectedSize(
//                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                    .thumbnailScale(0.85f)
//                    .imageEngine(new GlideEngine())
//                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "打开摄像头",
                    RC_CAMERA_PERM, permissions);
        }
    }

    private void getUserInfo() {
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PERSON");
        params.put("uid", uid);
        params.put("userType", userType);
        postForm(params, 1, 0);
    }

    private void updateInfo(int actionType, String info) {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PERSONSAVE");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(getApplicationContext(), "userType",
                ""));
        if (actionType == 1) {
            params.put("area", info);
        } else if (actionType == 2) {
            params.put("birthday", info);
        } else {
            params.put("sex", info);
        }
        postForm(params, 1, actionType);
    }

    private void deposit() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "DEPOSIT");
        params.put("uid", PreferencesUtils.getString(getApplicationContext(), "uid", ""));
        postForm(params, 1, 4);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {
        PresenterFactory.getInstance().createPresenter(this).execute(new Task.TaskBuilder()
                .setTaskType(TaskType.Method.POST)
                .setUrl(GlobalConsts.PREFIX_URL)
                .setParams(params)
                .setPage(page)
                .setActionType(actionType)
                .createTask());
    }

    private void initCity() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = getCityFromJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<ProvinceBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size();
                         d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    private void showPickerView(View v) {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mArea = options1Items.get(options1).getPickerViewText() + " " +
                        options2Items.get(options1).get(options2) + " " +
                        options3Items.get(options1).get(options2).get(options3);
                updateInfo(1, mArea);
            }
        })

                .setDividerColor(Color.parseColor("#C8C8C8"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(16)
                .setLineSpacingMultiplier(2.0f)
                .setSubmitText("确定")
                .setCancelText("取消")
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show(v);
    }

    private String getCityFromJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public ArrayList<ProvinceBean> parseData(String result) {//Gson 解析
        ArrayList<ProvinceBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            for (int i = 0; i < data.length(); i++) {
                ProvinceBean entity = new Gson().fromJson(data.optJSONObject(i).toString(),
                        ProvinceBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void showConfirmDialog() {
        Holder holder = new ViewHolder(LayoutInflater.from(this).inflate(R.layout.confirm_dialog,
                null));
        TextView tvTitle = (TextView) holder.getInflatedView().findViewById(R.id.dia_title);
        Button btnLeft = (Button) holder.getInflatedView().findViewById(R.id.btn_cancel);
        Button btnRight = (Button) holder.getInflatedView().findViewById(R.id.btn_ok);
        tvTitle.setText(String.format(getString(R.string.refund_deposit_hint), depositMoney));
        btnLeft.setText(getString(R.string.deposit_confirm));
        btnRight.setText(getString(R.string.deposit_cancel));
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        deposit();
                        dialog.dismiss();
                        break;
                    case R.id.btn_ok:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(DensityUtils.dp2px(this, 298f))
                .setContentHeight(DensityUtils.dp2px(this, 195f))
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    private void initBottomDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender_circle, null);
        tvMale = (TextView) contentView.findViewById(R.id.tv_male);
        tvFemale = (TextView) contentView.findViewById(R.id.tv_female);
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView
                .getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,
                16f);
        params.bottomMargin = DensityUtils.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        if (bottomDialog.getWindow() != null) {
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE_CODE) {
            if (resultCode == UpdateInfoActivity.RESULT_UPDATE_SUCCESS_CODE) {
                getUserInfo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        if (actionType == 0) {
            UserInfoBean bean = GsonUtils.getInstance().transitionToBean(result, UserInfoBean
                    .class);
            if (bean == null)
                return;
            if (bean.getCode() == 200) {
                mPhoneNum = bean.getPhoneNumber();
                mAvatarUrl = bean.getAvatar();
                mNickName = bean.getNickname();
                mGender = bean.getSex();
                mZone = bean.getArea();
                mData.clear();
                mData.addAll(convert2ProfileBean(bean));
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            }
        } else {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            if (bean.getCode() == 200) {
                if (tvValue == null)
                    return;
                switch (actionType) {
                    case 1:
                        tvValue.setText(mArea);
                        break;
                    case 2:
                        tvValue.setText(mDate);
                        break;
                    case 3:
                        tvValue.setText(mGender);
                        break;
                    case 4:
                        tvValue.setText("未缴纳");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private List<KeyValueBean> convert2ProfileBean(UserInfoBean bean) {
        List<KeyValueBean> beans = new LinkedList<>();
        beans.add(0, new KeyValueBean().setTitle("头像").setValue(bean.getAvatar()));
        beans.add(1, new KeyValueBean().setTitle("昵称").setValue(bean.getNickname()));
        beans.add(2, new KeyValueBean().setTitle("我的二维码").setValue(""));
        beans.add(3, new KeyValueBean().setTitle("性别").setValue(bean.getSex()));
        beans.add(4, new KeyValueBean().setTitle("生日").setValue(bean.getBirthday()));
        beans.add(5, new KeyValueBean().setTitle("手机号码").setValue(bean.getPhoneNumber()));
        beans.add(6, new KeyValueBean().setTitle("邮箱").setValue(bean.getEmail()));
        beans.add(7, new KeyValueBean().setTitle("地区").setValue(bean.getArea()));
        beans.add(8, new KeyValueBean().setTitle("押金退款").setValue(bean.getDeposit()));
        return beans;
    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        super.onFailure(error, page, actionType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
