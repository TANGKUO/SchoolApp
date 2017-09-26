package org.pointstone.cugapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.InformationShared;

/**
 * Created by Administrator on 2016/12/17.
 */

public class UnionidFragment extends Fragment{

    EditText UnionidEt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unionid, container, false);

        UnionidEt= (EditText) view.findViewById(R.id.unionid_et);
        UnionidEt.setText(InformationShared.getString("unionid"));
        return view;
    }
}
