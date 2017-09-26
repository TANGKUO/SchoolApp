package org.pointstone.cugapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;

public class RechargeSet extends Activity implements CompoundButton.OnCheckedChangeListener{

    private ImageView imageView_back;
    private Switch setSwitch,resetSwitch,balanceSwitch,consumeSwitch,abnormalSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_person);
        initView();
        imageView_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView(){
        imageView_back= (ImageView) findViewById(R.id.set_back);
        setSwitch= (Switch) findViewById(R.id.switch_set);
        resetSwitch= (Switch) findViewById(R.id.switch_reset);
        balanceSwitch= (Switch) findViewById(R.id.switch_balance);
        consumeSwitch= (Switch) findViewById(R.id.switch_consume);
        abnormalSwitch= (Switch) findViewById(R.id.switch_abnormal);
        if(InformationShared.getString("set_hand_pass").equals("1"))
            setSwitch.setChecked(true);
        if(InformationShared.getString("reset_hand_pass").equals("1"))
            resetSwitch.setChecked(true);
        if(InformationShared.getString("remind_balance").equals("1"))
            balanceSwitch.setChecked(true);
        if(InformationShared.getString("remind_consume").equals("1"))
            consumeSwitch.setChecked(true);
        if(InformationShared.getString("remind_abnormal").equals("1"))
            abnormalSwitch.setChecked(true);
        setSwitch.setOnCheckedChangeListener(this);
        resetSwitch.setOnCheckedChangeListener(this);
        balanceSwitch.setOnCheckedChangeListener(this);
        consumeSwitch.setOnCheckedChangeListener(this);
        abnormalSwitch.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked ) {
        int id=compoundButton.getId();
        switch(id){
            case R.id.switch_set:
                if(isChecked)
                    InformationShared.setString("set_hand_pass","1");
                else
                    InformationShared.setString("set_hand_pass","0");
                break;
            case R.id.switch_reset:

                if(isChecked)
                    InformationShared.setString("reset_hand_pass","1");
                else
                    InformationShared.setString("reset_hand_pass","0");
                break;
            case R.id.switch_balance:

                if(isChecked)
                    InformationShared.setString("remind_balance","1");
                else
                    InformationShared.setString("remind_balance","0");
                break;
            case R.id.switch_consume:

                if(isChecked)
                    InformationShared.setString("remind_consume","1");
                else
                    InformationShared.setString("remind_consume","0");
                break;
            case R.id.switch_abnormal:

                if(isChecked)
                    InformationShared.setString("remind_abnormal","1");
                else
                    InformationShared.setString("remind_abnormal","0");
                break;
            default:
                break;
        }
    }
}
