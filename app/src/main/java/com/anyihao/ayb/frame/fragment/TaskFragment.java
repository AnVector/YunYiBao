package com.anyihao.ayb.frame.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.androidbase.mvp.Task;
import com.anyihao.androidbase.mvp.TaskType;
import com.anyihao.androidbase.utils.GsonUtils;
import com.anyihao.androidbase.utils.PreferencesUtils;
import com.anyihao.androidbase.utils.ToastUtils;
import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.LendAdapter;
import com.anyihao.ayb.adapter.NormalAdapter;
import com.anyihao.ayb.adapter.SignAdapter;
import com.anyihao.ayb.bean.SignBean;
import com.anyihao.ayb.bean.TaskListBean;
import com.anyihao.ayb.bean.TaskListBean.DataBean;
import com.anyihao.ayb.bean.TaskListBean.DataBean.LendBean;
import com.anyihao.ayb.bean.TaskListBean.DataBean.LimitedBean;
import com.anyihao.ayb.bean.TaskListBean.DataBean.NormalBean;
import com.anyihao.ayb.common.PresenterFactory;
import com.anyihao.ayb.constant.GlobalConsts;
import com.anyihao.ayb.frame.activity.BriberyMoneyActivity;
import com.anyihao.ayb.frame.activity.ExchangeDetailsActivity;
import com.anyihao.ayb.frame.activity.LoginActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.anyihao.ayb.ui.CropCircleTransformation;
import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends ABaseFragment {

    @BindView(R.id.toolbar_title_mid)
    TextView titleMid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_limited_exchange_hint)
    TextView tvLimitedExchangeHint;
    @BindView(R.id.iv_points_exchange)
    ImageView ivPointsExchange;
    @BindView(R.id.tv_points_exchange_hint)
    TextView tvPointsExchangeHint;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_advertisement)
    ImageView ivAdvertisement;
    @BindView(R.id.tv_advertisement_hint)
    TextView tvAdvertisementHint;
    @BindView(R.id.line_right_top)
    View lineRightTop;
    @BindView(R.id.iv_ticket_left)
    ImageView ivTicketLeft;
    @BindView(R.id.tv_ticket_left_hint)
    TextView tvTicketLeftHint;
    @BindView(R.id.tv_ticket_left_desc)
    TextView tvTicketLeftDesc;
    @BindView(R.id.line_right_bottom)
    View lineRightBottom;
    @BindView(R.id.iv_ticket_right)
    ImageView ivTicketRight;
    @BindView(R.id.tv_ticket_right_hint)
    TextView tvTicketRightHint;
    @BindView(R.id.tv_ticket_right_desc)
    TextView tvTicketRightDesc;
    @BindView(R.id.recyclerview_bottom)
    RecyclerView recyclerviewBottom;
    @BindView(R.id.btn_sign_in)
    AppCompatButton btnSignIn;
    @BindView(R.id.imb_sign)
    ImageButton imbSign;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.recyclerview_middle)
    RecyclerView recyclerviewMiddle;
    @BindView(R.id.tv_points)
    TextView tvPoints;
    @BindView(R.id.tv_my_points)
    TextView tvMyPoints;
    private static final int REQUEST_LOGIN_CODE = 0x0001;
    private NormalAdapter mNormalAdapter;
    private LendAdapter mLendAdapter;
    private SignAdapter mSignAdapter;
    private List<LendBean> mLendData = new ArrayList<>();
    private List<NormalBean> mNormalData = new ArrayList<>();
    private List<String> mWeekData = new ArrayList<>();
    private int isSign = 0;
    private boolean isLogin;

    @Override
    protected void initData() {
        toolbar.setNavigationIcon(null);
        titleMid.setText(getString(R.string.task));
        fakeStatusBar.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mNormalAdapter = new NormalAdapter(mContext, R.layout.item_task_ad, mNormalData);
        recyclerviewBottom.setNestedScrollingEnabled(false);
        recyclerviewBottom.setAdapter(mNormalAdapter);
        recyclerviewBottom.setLayoutManager(new GridLayoutManager(mContext, 2,
                GridLayoutManager.VERTICAL, false));

        mLendAdapter = new LendAdapter(mContext, R.layout.item_task_ad, mLendData);
        recyclerviewMiddle.setNestedScrollingEnabled(false);
        recyclerviewMiddle.setAdapter(mLendAdapter);
        recyclerviewMiddle.setLayoutManager(new GridLayoutManager(mContext, 2,
                GridLayoutManager.VERTICAL, false));

        initDefaultWeek();
        setPoints("我的积分：0分");
        mSignAdapter = new SignAdapter(mContext, R.layout.item_sign_history, mWeekData);
        recyclerview.setAdapter(mSignAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager
                .HORIZONTAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getTaskList();
    }

    private void initDefaultWeek() {
        mWeekData.clear();
        for (int i = 0; i < 7; i++) {
            mWeekData.add(i, (i + 1) + "0");
        }
    }

    private void startActivityForLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_CODE);
    }

    private void setPoints(String txt) {
        if (TextUtils.isEmpty(txt) || tvMyPoints == null) {
            return;
        }
        tvMyPoints.setText(txt);
        SpannableString spannableString = new SpannableString(txt);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.285f);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(sizeSpan, 5, txt.length() - 1, Spanned
                .SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, 5, txt.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvMyPoints.setText(spannableString);
    }


    @Override
    protected void initEvent() {

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSign == 1) {
                    ToastUtils.showToast(mContext.getApplicationContext(), "已签到");
                    return;
                }
                sign();
            }
        });

        mSignAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                showDialog();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        imbSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        tvAdvertisementHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                Intent intent = new Intent(mContext, BriberyMoneyActivity
                        .class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        ivAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                Intent intent = new Intent(mContext, BriberyMoneyActivity
                        .class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        mNormalAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (o instanceof NormalBean) {
                    startExchangeActivity(((NormalBean) o).getExchangeId());
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        mLendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (o instanceof LendBean) {
                    startExchangeActivity(((LendBean) o).getExchangeId());
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        ivPointsExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (v.getTag(R.id.tag_exhange_img) instanceof Integer) {
                    startExchangeActivity((int) v.getTag(R.id.tag_exhange_img));
                }
            }
        });

        ivTicketLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (v.getTag(R.id.tag_left_img) instanceof Integer) {
                    startExchangeActivity((int) v.getTag(R.id.tag_left_img));
                }
            }
        });

        ivTicketRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = PreferencesUtils.getBoolean(mContext.getApplicationContext(),
                        "isLogin", false);
                if (!isLogin) {
                    startActivityForLogin();
                    return;
                }
                if (v.getTag(R.id.tag_right_img) instanceof Integer) {
                    startExchangeActivity((int) v.getTag(R.id.tag_right_img));
                }
            }
        });

    }

    private void startExchangeActivity(int exchangeId) {
        Intent intent = new Intent(mContext, ExchangeDetailsActivity.class);
        intent.putExtra("exchangeId", exchangeId);
        startActivity(intent);
    }

    private void updateSignHistory(int count) {
        for (int i = 1; i <= count; i++) {
            if (i < count) {
                mWeekData.set(i - 1, i + "1");
            } else {
                mWeekData.set(i - 1, i + "2");
            }
        }
        mSignAdapter.update(0, count);
    }

    private void getTaskList() {

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "EXCHANGELIST");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        params.put("userType", PreferencesUtils.getString(mContext.getApplicationContext(),
                "userType", ""));
        postForm(params, 1, 0);
    }

    private void postForm(Map<String, String> params, int page, int actionType) {
        PresenterFactory.getInstance().createPresenter(this)
                .execute(new Task.TaskBuilder()
                        .setTaskType(TaskType.Method.POST)
                        .setUrl(GlobalConsts.PREFIX_URL)
                        .setParams(params)
                        .setPage(page)
                        .setActionType(actionType)
                        .createTask());
    }

    private void sign() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", "SIGN");
        params.put("uid", PreferencesUtils.getString(mContext.getApplicationContext(), "uid", ""));
        postForm(params, 1, 1);
    }

    private void showDialog() {
        Holder holder = new ViewHolder(R.layout.dialog_sign_explain);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_got_it:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setOnClickListener(clickListener)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentBackgroundResource(R.drawable.dialog_bg)
                .create();
        dialog.show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

        if (actionType == 0) {
            TaskListBean bean = GsonUtils.getInstance().transitionToBean(result,
                    TaskListBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                DataBean dataBean = bean.getData();
                if (dataBean == null) {
                    return;
                }
                setLimitedData(dataBean.getLimited());
                setLendData(dataBean.getLend());
                setNormalData(dataBean.getNormal());
                if (PreferencesUtils.getBoolean(mContext.getApplicationContext(), "isLogin",
                        false)) {
                    setOnlineInfo(dataBean);
                } else {
                    setDefaultInfo();
                }
            }

        }
        if (actionType == 1) {
            SignBean bean = GsonUtils.getInstance().transitionToBean(result, SignBean.class);
            if (bean == null) {
                return;
            }
            if (bean.getCode() == 200) {
                btnSignIn.setText("已签到");
                setPoints("我的积分：" + bean.getPoint() + "分");
                isSign = 1;
                updateSignHistory(bean.getDay());
            } else if (bean.getCode() == 435) {
                ToastUtils.showToast(mContext.getApplicationContext(), "未登录，请先登录");
            } else {
                ToastUtils.showToast(mContext.getApplicationContext(), "未登录，请先登录");
            }
        }
    }

    private void setOnlineInfo(DataBean bean) {
        if (bean == null) {
            return;
        }
        if (ivUserProfile != null) {
            Glide.with(mContext)
                    .load(bean.getAvatar())
                    .crossFade()
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.user_profile)
                    .into(ivUserProfile);
        }

        setPoints("我的积分：" + bean.getPoint() + "分");
        isSign = bean.getSignStatus();
        if (isSign == 1 && btnSignIn != null) {
            btnSignIn.setText("已签到");
        }
        updateSignHistory(bean.getDay());
    }

    private void setDefaultInfo() {
        if (ivUserProfile != null) {
            ivUserProfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable
                    .user_profile));
        }
        setPoints("我的积分：0分");
        if (btnSignIn != null) {
            btnSignIn.setText("签到领积分");
        }
        initDefaultWeek();
        mSignAdapter.notifyDataSetChanged();
    }

    private void setLimitedData(List<LimitedBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        if (beans.size() == 4) {
            if (ivPointsExchange != null) {
                Glide.with(mContext)
                        .load(beans.get(0).getImage())
                        .crossFade()
                        .into(ivPointsExchange);
            }

            if (tvPointsExchangeHint != null) {
                tvPointsExchangeHint.setText(String.format(mContext.getString(R.string
                        .convertible_of), beans.get(0).getExContent()));
            }

            if (tvPoints != null) {
                tvPoints.setText(String.format(mContext.getString(R.string.points), beans.get(0)
                        .getIntegral()));
            }

            if (ivPointsExchange != null) {
                ivPointsExchange.setTag(R.id.tag_exhange_img, beans.get(0).getExchangeId());
            }

            if (ivAdvertisement != null) {
                Glide.with(mContext)
                        .load(beans.get(1).getImage())
                        .crossFade()
                        .into(ivAdvertisement);
            }

            if (ivTicketLeft != null) {
                Glide.with(mContext)
                        .load(beans.get(2).getImage())
                        .crossFade()
                        .into(ivTicketLeft);
            }

            if (tvTicketLeftDesc != null) {
                tvTicketLeftDesc.setText(String.format(mContext.getString(R.string
                        .convertible_of), beans.get(2).getExContent()));
            }
            if (tvTicketLeftHint != null) {
                tvTicketLeftHint.setText(String.format(mContext.getString(R.string.points), beans
                        .get(2).getIntegral()));
            }

            if (ivTicketLeft != null) {
                ivTicketLeft.setTag(R.id.tag_left_img, beans.get(2).getExchangeId());
            }

            if (ivTicketRight != null) {
                Glide.with(mContext)
                        .load(beans.get(3).getImage())
                        .crossFade()
                        .into(ivTicketRight);
            }
            if (tvTicketRightDesc != null) {
                tvTicketRightDesc.setText(String.format(mContext.getString(R.string
                        .convertible_of), beans.get(3).getExContent()));
            }
            if (tvTicketRightHint != null) {
                tvTicketRightHint.setText(String.format(mContext.getString(R.string.points),
                        beans.get(3).getIntegral()));
            }

            if (ivTicketRight != null) {
                ivTicketRight.setTag(R.id.tag_right_img, beans.get(3).getExchangeId());
            }
        }

    }

    private void setLendData(List<LendBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        mLendData.clear();
        mLendData.addAll(beans);
        mLendAdapter.notifyDataSetChanged();
    }

    private void setNormalData(List<NormalBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        mNormalData.clear();
        mNormalData.addAll(beans);
        mNormalAdapter.notifyDataSetChanged();
    }
}
