package org.pointstone.cugapp.view;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.fragments.CourseFragment;
import org.pointstone.cugapp.utils.InformationShared;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CourseDayMenu  extends BasePopupWindow implements View.OnClickListener{

    public  CourseDayMenu (Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.course_week).setOnClickListener(this);
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
        //return null;
    }

    @Override
    public Animator initShowAnimator() {
       /* AnimatorSet set=new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mAnimaView,"scaleX",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"scaleY",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"alpha",0.0f,1.0f).setDuration(300*3/2));*/
        return null;
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-(getPopupViewWidth() - v.getWidth() / 2));
        setOffsetY(v.getHeight() / 2);
        super.showPopupWindow(v);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.course_day_menu);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_contianer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.course_week:
                dismiss();
                DrawerActivity.getInstannce().getFragmentManager().beginTransaction().replace(R.id.content_layout, new CourseFragment())
                        .commit();//初始界面
                InformationShared.setInt("isDay",0);
                break;
            default:
                break;
        }

    }
}
