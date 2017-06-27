package com.anyihao.ayb.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.bean.CustomerBean;
import com.bigkoo.pickerview.OptionsPickerView;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

public class RdoActivity extends ABaseActivity {
    @BindView(R.id.btn_start_time)
    Button btnStartTime;
    @BindView(R.id.btn_end_time)
    Button btnEndTime;
    @BindView(R.id.btn_search)
    AppCompatButton btnSearch;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_customer)
    Button btnCustomer;
    @BindView(R.id.edt_client)
    EditText edtClient;
    @BindView(R.id.ll_aee)
    LinearLayout llAee;
    @BindView(R.id.btn_service_type)
    Button btnServiceType;
    @BindView(R.id.rbt_all)
    RadioButton rbtAll;
    @BindView(R.id.rbt_AEE)
    RadioButton rbtAEE;
    @BindView(R.id.rbt_other)
    RadioButton rbtOther;
    private ArrayList<CustomerBean> options1Items = new ArrayList<>();
    private ArrayList<CustomerBean> options2Items = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private OptionsPickerView serviceOptions;
    private Button button;
    private TimeSelector timeSelector;
    private String startTime;
    private String endTime;
    private int excludeAEE = 1;
    private String customerId = "hzzmdjf";
    private String clientId = "";
    private String serviceType = "pt_rdocash";
    private String customer = "杭州掌盟";
    private String service = "代计费";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rdo;
    }

    @Override
    protected void getExtraParams() {

    }

    @Override
    protected void initData() {
        initOptionData();
        initOptionPicker();
        initServiceOptionPicker();
        toolbarTitle.setText(getString(R.string.search));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        btnStartTime.setText(getTime(selectedDate.getTime()));
        btnEndTime.setText(getTime(selectedDate.getTime()));
        startTime = endTime = timeFormat(getTime(selectedDate.getTime()));
        btnServiceType.setText(service);
        btnCustomer.setText(customer);
        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
//                ToastUtils.showLongToast(getApplicationContext(), time);
                String timeFormat = timeFormat(time) + "00";
                if (button != null) {
                    button.setText(time + ":00");
                    if (button.getId() == R.id.btn_start_time) {
                        startTime = timeFormat;
                    }

                    if (button.getId() == R.id.btn_end_time) {
                        endTime = timeFormat;
                    }
                }
                button = null;
            }
        }, "2017-01-30 00:00", "2018-12-31 00:00");

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    private String timeFormat(String time) {
        return time.replace("-", "")
                .replace(":", "")
                .replace(" ", "");
    }

    @Override
    protected void initEvent() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button = (Button) v;
                timeSelector.show();
            }
        });

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button = (Button) v;
                timeSelector.show();
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show(v); //弹出条件选择器
            }
        });

        btnServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceOptions.show(v);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(serviceType)) {
                    ToastUtils.showLongToast(getApplicationContext(), "请选择业务类型！");
                    return;
                }

                if (Long.parseLong(startTime) > Long.parseLong(endTime)) {
                    ToastUtils.showLongToast(getApplicationContext(), "起始时间大于截止时间，请重新选择！");
                    return;
                }

                if (Long.parseLong(startTime) == Long.parseLong(endTime)) {
                    ToastUtils.showLongToast(getApplicationContext(), "起始时间与截止时间不能相同，请重新选择！");
                    return;
                }

                if (!TextUtils.isEmpty(edtClient.getText())) {
//                    ToastUtils.showLongToast(getApplicationContext(), "请输入客户端编号！");
//                    return;
                    clientId = edtClient.getText().toString().trim();
                }

                if (TextUtils.isEmpty(customerId)) {
                    ToastUtils.showLongToast(getApplicationContext(), "请选择公司名称！");
                    return;
                }

                if (customerId.contains("szzyzxl") || customerId.contains("njxbzxl")) {
                    ToastUtils.showLongToast(getApplicationContext(), "该公司不存在该业务，请重新选择！");
                    return;
                }

                if (rbtAll.isChecked()) {
                    excludeAEE = 0;
                }
                if (rbtOther.isChecked()) {
                    excludeAEE = 1;
                }

                if (rbtAEE.isChecked()) {
                    excludeAEE = 2;
                }

                Intent intent = new Intent(RdoActivity.this, ReportActivity.class);
                intent.putExtra(ReportActivity.SERVICE, service);
                intent.putExtra(ReportActivity.CUSTOMER, customer);
                intent.putExtra(ReportActivity.SERVICE_TYPE, serviceType);
                intent.putExtra(ReportActivity.FROM_DATE, startTime);
                intent.putExtra(ReportActivity.TO_DATE, endTime);
                intent.putExtra(ReportActivity.CUSTOMER_ID, customerId);
                intent.putExtra(ReportActivity.CLIENT_ID, clientId);
                intent.putExtra(ReportActivity.EXCLUDE_AEE, excludeAEE);
                startActivity(intent);
            }
        });

    }

    private void initOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items.add(new CustomerBean(0, "杭州掌盟", "hzzmdjf", "hzzmzxl"));
        options1Items.add(new CustomerBean(1, "南京修邦", "njxbdjf", "njxbzxl"));
        options1Items.add(new CustomerBean(2, "杭州平治", "hzpzdjf", "hzpzzxl"));
        options1Items.add(new CustomerBean(3, "杭州悦蓝", "hzyl", "hzylzxl"));
        options1Items.add(new CustomerBean(4, "深圳掌游", "szzydjf", "szzyzxl"));


        options2Items.add(new CustomerBean(0, "代计费", "pt_rdocash", "其他数据"));
        options2Items.add(new CustomerBean(1, "正向量", "pt_rdomarket", "其他数据"));

        /*--------数据源添加完毕---------*/
    }


    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
//                        + options2Items.get(options1).get(options2)
                       /* + options3Items.get(options1).get(options2).get(options3)
                       .getPickerViewText()*/;
                if (serviceType.equals("pt_rdocash")) {
                    customerId = options1Items.get(options1).getDescription();
                } else {
                    customerId = options1Items.get(options1).getOthers();
                }
                customer = tx;
                if (serviceType.equals("pt_rdocash") && customerId.equals("hzzmdjf")) {
                    llAee.setVisibility(View.VISIBLE);
                } else {
                    llAee.setVisibility(View.GONE);
                }
                btnCustomer.setText(tx);
            }
        })
                .setTitleText("公司选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(R.color.toolbar_title_color)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(R.color.toolbar_title_color)
                .setCancelColor(R.color.toolbar_title_color)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(R.color.toolbar_title_color)
//                .setTextColorCenter(Color.LTGRAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .build();

        //pvOptions.setSelectOptions(1,1);

        pvOptions.setPicker(options1Items);//一级选择器*/
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

    }

    private void initServiceOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        serviceOptions = new OptionsPickerView.Builder(this, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options2Items.get(options1).getPickerViewText()
//                        + options2Items.get(options1).get(options2)
                       /* + options3Items.get(options1).get(options2).get(options3)
                       .getPickerViewText()*/;
                serviceType = options2Items.get(options1).getDescription();
                if (serviceType.equals("pt_rdocash")) {
                    customerId = customerId.replace("zxl", "djf");
                } else {
                    customerId = customerId.replace("djf", "zxl");
                }
                if (serviceType.equals("pt_rdocash") && customerId.equals("hzzmdjf")) {
                    llAee.setVisibility(View.VISIBLE);
                } else {
                    llAee.setVisibility(View.GONE);
                }
                service = tx;
                btnServiceType.setText(tx);
            }
        })
                .setTitleText("业务类型选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(R.color.toolbar_title_color)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(R.color.toolbar_title_color)
                .setCancelColor(R.color.toolbar_title_color)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(R.color.toolbar_title_color)
//                .setTextColorCenter(Color.LTGRAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .build();

        //pvOptions.setSelectOptions(1,1);

        serviceOptions.setPicker(options2Items);//一级选择器*/
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
