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
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.anyihao.ayb.adapter.AdAdapter;
import com.anyihao.ayb.adapter.SignInAdapter;
import com.anyihao.ayb.frame.activity.ExchangeDetailsActivity;
import com.anyihao.ayb.listener.OnItemClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Arrays;
import java.util.List;

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
    @BindView(R.id.tv_points)
    TextView tvPoints;
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
    private AdAdapter mAdapter;
    private SignInAdapter signInAdapter;
    String[] weekendArray = new String[]{"1", "2", "3", "4", "5", "6", "7"};
    String[] array = new String[]{"2017现金红包", "2017现金红包", "2017现金红包", "2017现金红包", "2017现金红包",
            "2017现金红包", "2017现金红包",
            "2017现金红包", "2017现金红包", "2017现金红包"};
    private List<String> mData = Arrays.asList(array);
    private List<String> mWeekendData = Arrays.asList(weekendArray);

    @Override
    protected void initData() {
        titleMid.setText(getString(R.string.task));
        SpannableString spannableString = new SpannableString("我的积分：999分");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.285f);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(sizeSpan, 5, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, 5, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvPoints.setText(spannableString);
        mAdapter = new AdAdapter(getContext(), R.layout.item_task_ad);
        recyclerviewBottom.setAdapter(mAdapter);
        recyclerviewBottom.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        recyclerviewBottom.setHasFixedSize(true);
        mAdapter.add(0, mData.size(), mData);

        signInAdapter = new SignInAdapter(getContext(), R.layout.item_sign_history);
        recyclerview.setAdapter(signInAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .HORIZONTAL, false));
        recyclerview.setHasFixedSize(true);
        signInAdapter.add(0, mWeekendData.size(), mWeekendData);

    }

    @Override
    protected void initEvent() {

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeDetailsActivity.class);
                startActivity(intent);
            }
        });

        signInAdapter.setOnItemClickListener(new OnItemClickListener() {
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

    }

    private void showDialog() {
        Holder holder = new ViewHolder(R.layout.sign_dialog);
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

        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
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

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onSuccess(String result, int page, Integer actionType) {

    }

    @Override
    public void onFailure(String error, int page, Integer actionType) {

    }
}
