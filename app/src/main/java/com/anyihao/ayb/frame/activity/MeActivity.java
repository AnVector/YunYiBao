package com.anyihao.ayb.frame.activity;

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
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.DensityUtils;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.UserInfoAdapter;
import com.anyihao.ayb.bean.ProvinceBean;
import com.anyihao.ayb.bean.ResultBean;
import com.anyihao.ayb.bean.UserInfoBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private List<String> mData = new LinkedList<>();
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
    private String mGender;
    private String mPhoneNum;
    private TimePickerView pvDate;
    private Dialog bottomDialog;
    private View dialogContentView;
    private TextView tvMale;
    private TextView tvFemale;
    private TextView tvCancel;

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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initCity();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    isLoaded = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        getUserInfo();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        titleMid.setText(getString(R.string.about_me));
        mAdapter = new UserInfoAdapter(this, R.layout.item_user_info);
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
        pvDate = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
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
                .setDividerType(WheelView.DividerType.WRAP)
                .setTextColorCenter(Color.parseColor("#333333"))
                .setLineSpacingMultiplier(1.2f)
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
                if (view.getTag() instanceof String) {
                    switch (view.getTag().toString()) {
                        case "头像":
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
                            if (pvDate != null) {
                                pvDate.show(tvValue);
                            }
                            break;
                        case "我的二维码":
                            showCodeDialog();
                            break;
                        case "押金退款":
                            if ("未缴纳".equals(o.toString())) {
                                ToastUtils.showToast(getApplicationContext(), o.toString());
                                return;
                            }
                            showConfirmDialog();
                            break;
                        case "手机号码":
                            Intent intent1 = new Intent(MeActivity.this, GetVerifyCodeActivity
                                    .class);
                            intent1.putExtra("title", "验证原手机");
                            intent1.putExtra("action", "ORIGINAL");
                            intent1.putExtra("phoneNum", mPhoneNum);
                            startActivity(intent1);
                            break;
                        case "性别":
                            if (bottomDialog != null) {
                                bottomDialog.show();
                            }
                            break;
                        default:
                            Intent intent = new Intent(MeActivity.this, UpdateInfoActivity
                                    .class);
                            intent.putExtra(UpdateInfoActivity.INFORMATION_KEY, view.getTag()
                                    .toString());
                            intent.putExtra(UpdateInfoActivity.INFORMATION_VALUE, o.toString());
                            startActivity(intent);
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
                updateInfo(3, "男");
                mGender = "男";
                if (bottomDialog != null) {
                    bottomDialog.dismiss();
                }
            }
        });

        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(3, "女");
                mGender = "女";
                if (bottomDialog != null) {
                    bottomDialog.dismiss();
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

    private void getUserInfo() {
        progressbarCircular.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).start();
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "PERSON");
        params.put("uid", uid);
        params.put("userType", userType);
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
                        .setActionType(0)
                        .createTask());
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
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(1)
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
                .setDividerType(WheelView.DividerType.WRAP)
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

    private void showCodeDialog() {
        Holder holder = new ViewHolder(R.layout.me_qr_code_dialog);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "dismiss");
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "cancel");
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentBackgroundResource(R.drawable.qr_info_dialog_bg)
                .create();
        dialog.show();
    }


    private void showConfirmDialog() {
        Holder holder = new ViewHolder(R.layout.refund_deposit_dialog);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.btn_confirm:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "dismiss");
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
//                ToastUtils.showLongToast(getActivity(), "cancel");
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }


    private void initBottomDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        dialogContentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender_circle, null);
        tvMale = (TextView) dialogContentView.findViewById(R.id.tv_male);
        tvFemale = (TextView) dialogContentView.findViewById(R.id.tv_female);
        tvCancel = (TextView) dialogContentView.findViewById(R.id.tv_cancle);
        bottomDialog.setContentView(dialogContentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dialogContentView
                .getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,
                16f);
        params.bottomMargin = DensityUtils.dp2px(this, 8f);
        dialogContentView.setLayoutParams(params);
        if (bottomDialog.getWindow() != null) {
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        if (actionType == 0) {
            UserInfoBean bean = GsonUtils.getInstance().transitionToBean(result, UserInfoBean
                    .class);
            if (bean == null) {
                ToastUtils.showToast(getApplicationContext(), "暂无数据", R.layout.toast, R.id
                        .tv_message);
                return;
            }
            if (bean.getCode() == 200) {
                mPhoneNum = bean.getPhoneNumber();
                mData.add(bean.getAvatar());
                mData.add(bean.getNickname());
                mData.add("QRCODE");
                mData.add(bean.getSex());
                mData.add(bean.getBirthday());
                mData.add(mPhoneNum);
                mData.add(bean.getEmail());
                mData.add(bean.getArea());
                mData.add(bean.getDeposit());
                mAdapter.add(0, mData.size(), mData);
            } else {
                ToastUtils.showToast(getApplicationContext(), bean.getMsg(), R.layout.toast, R.id
                        .tv_message);
            }
        } else {
            ResultBean bean = GsonUtils.getInstance().transitionToBean(result, ResultBean.class);
            if (bean == null)
                return;
            ToastUtils.showToast(getApplicationContext(), bean.getMsg());
            if (bean.getCode() == 200) {
                if (tvValue == null)
                    return;
                if (actionType == 1) {
                    tvValue.setText(mArea);
                } else if (actionType == 2) {
                    tvValue.setText(mDate);
                } else {
                    tvValue.setText(mGender);
                }

            }

        }


    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {
        ((CircularProgressDrawable) progressbarCircular.getIndeterminateDrawable()).stop();
        progressbarCircular.setVisibility(View.GONE);
        ToastUtils.showToast(getApplicationContext(), error);
    }
}
