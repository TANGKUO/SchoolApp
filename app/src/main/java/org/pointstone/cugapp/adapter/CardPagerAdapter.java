package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.CourseAddAlterActivity;
import org.pointstone.cugapp.activities.CourseDetailActivity;
import org.pointstone.cugapp.fragments.CourseFragment;
import org.pointstone.cugapp.utils.GradeYear;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context mContext;
    private Button CourseDeleteBtn;


    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        mContext=context;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter_card, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        CourseDeleteBtn= (Button) view.findViewById(R.id.course_delete_btn);


        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CourseAddAlterActivity.class);
                intent.putExtra("data", mData.get(position).getText());
                intent.putExtra("time", CourseDetailActivity.getInstannce().getTime());
                mContext.startActivity(intent);
                CourseDetailActivity.getInstannce().finish();
            }
        });

        CourseDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailActivity.getInstannce());
                builder.setMessage("确认删除吗？");

                builder.setTitle("提示");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String data=mData.get(position).getText();
                        data = data.replace("\n\n", "");
                        String time=CourseDetailActivity.getInstannce().getTime();
                        String xn= GradeYear.getCurrentXN(Integer.parseInt(time));
                        String xq=GradeYear.getCurrentXQ(Integer.parseInt(time));
                        String[] c = data.split("\n");
                        String Place=c[0];
                        String CourseName=c[1];
                        String TeacherName=c[2];
                        String Time=c[3];
                        String kcb=CourseName+"<br>"+Time+"<br>"+TeacherName+"<br>"+Place;
                        CourseDetailActivity.getInstannce().DeleteCourse(xn,xq,kcb);
                        CourseDetailActivity.getInstannce().finish();
                        CourseFragment.getInstannce().setCourse();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();



            }
        });
        if(position==mData.size()-1)
            CourseDeleteBtn.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        contentTextView.setText(item.getText());
    }

}
