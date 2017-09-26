package org.pointstone.cugapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.tendcloud.tenddata.TCAgent;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.DrawerActivity;
import org.pointstone.cugapp.activities.SettingActivity;
import org.pointstone.cugapp.utils.InformationShared;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Administrator on 2017/1/5.
 */

public class SettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_setting);


     /*   final CheckBoxPreference checkboxRecharge= (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.recharge_switch));
        checkboxRecharge.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            /**
             * @param preference The changed Preference.
             * @param newValue   The new value of the Preference.
             * @return True to update the state of the Preference with the new value.
             */
        //     @Override
      /*      public boolean onPreferenceChange(Preference preference, Object newValue) {
              boolean checked = Boolean.valueOf(newValue.toString());


             SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
                OwnToast.Short((prefs.getBoolean("grade_switch",false))+"");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("grade_switch",false);
                editor.commit();
                return true;
            }
        });*/


    /*    final CheckBoxPreference checkboxGrade = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.grade_switch));
        checkboxGrade.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {


            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中

                if(checked)
                {
                    OwnToast.Long("!");
                }else
                {
                    OwnToast.Long("0");
                }
                return true;
            }
        });
*/

        Preference QuitTv = getPreferenceManager().findPreference(getString(R.string.quit));
        QuitTv.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.getInstance());
                builder.setMessage("确认注销吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        InformationShared.setInt("login", 0);
                        InformationShared.setInt("status", 0);
                        SettingActivity.getInstance().finish();
                        DrawerActivity.getInstannce().refresh();


                        YunBaManager.setAlias(DrawerActivity.getInstannce(), "",
                                new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        //  DemoUtil.showToast("success", getApplicationContext());
                                        //  InformationShared.setInt("Alias", 1);
                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                        if (exception instanceof MqttException) {
                                            MqttException ex = (MqttException) exception;
                                            // String msg =  "setAlias failed with error code : " + ex.getReasonCode();
                                            //  DemoUtil.showToast(msg, getApplicationContext());
                                        }
                                    }
                                }
                        );


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
                return false;
            }
        });
        // headCourseIv= getPreferenceManager().findPreference(getId( ));
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("Recharge"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageStart(getActivity(), "Setting");
    }

    @Override
    public void onPause() {
        super.onPause();
        //  MobclickAgent.onPageStart("Recharge"); //统计页面，"MainScreen"为页面名称，可自定义
        TCAgent.onPageEnd(getActivity(), "Setting");
    }
}
