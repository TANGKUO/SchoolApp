package org.pointstone.cugapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.activities.GradeDetailActivity;
import org.pointstone.cugapp.utils.DBGradeManager;
import org.pointstone.cugapp.utils.Grade;
import org.pointstone.cugapp.utils.OwnToast;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class GradeCardViewAdapter extends RecyclerView.Adapter<GradeCardViewAdapter.GradeViewHolder> implements View.OnClickListener {


    static final int NO_MEAU = 0;
    static final int SIMPLE_MEAU = 1;
    static final int SINGLE_DLETE_MEAU = 2;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<Grade> grades;
    private static Context context;
    private Context mContext;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public GradeCardViewAdapter(List<Grade> grades, Context context) {
        this.grades = grades;
        this.context = context;
        mContext = context;

    }


    //自定义ViewHolder类
    static class GradeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView kcmc;
        TextView xf;
        TextView jd;
        TextView cj;
        TextView yc;

        public GradeViewHolder(final View itemView) {
            super(itemView);
            //itemView.setBackgroundColor(context.getResources().getColor(R.color.course_grid_color2));
            cardView = (CardView) itemView.findViewById(R.id.grade_CardView);
            kcmc = (TextView) itemView.findViewById(R.id.card_kcmcTv);
            xf = (TextView) itemView.findViewById(R.id.card_xfTv);
            jd = (TextView) itemView.findViewById(R.id.card_jdTv);
            cj = (TextView) itemView.findViewById(R.id.card_cjTv);
            yc = (TextView) itemView.findViewById(R.id.yctv);
        }

    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.swipe_item2, parent, false);
        GradeViewHolder nvh = new GradeViewHolder(v);
        v.setOnClickListener(this);
        return nvh;
    }

    public Object getItem(int position) {
        return grades.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final GradeViewHolder personViewHolder, final int i) {
        //根据位置分配颜色和数据
        final Grade grade = (Grade) this.getItem(i);

        switch (i%2) {
            case 0:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color0));
                break;
            case 1:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color1));
                break;
        }
        personViewHolder.kcmc.setText(grade.getKcmc() + "  " + grade.getKcxz());
        personViewHolder.xf.setText("学分:" + grade.getXf());
        personViewHolder.jd.setText("绩点:" + grade.getJd());
        personViewHolder.cj.setText("成绩:" + grade.getCj());
        //设置点击事件
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GradeDetailActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", "" + grade.getKcmc() + "\n\n"
                        + "最后成绩：" + grade.getCj() + "\n"
                        + "期末成绩：" + returnCj(grade.getQmcj()) + "\n"
                        + "平时成绩：" + returnCj(grade.getPscj()) + "\n"
                        + "实验成绩：" + returnCj(grade.getSycj()));
                String xqxn = grade.getXn() + grade.getXq();
                intent.putExtra("xqxn", xqxn);
                mContext.startActivity(intent);
            }
        });

        //设置滑动点击事件
        personViewHolder.yc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DBGradeManager dbgradeManager = new DBGradeManager(view.getContext());
                OwnToast.Short("”"+grade.getKcmc()+"”"+"已隐藏");
                dbgradeManager.delete(grade);
                dbgradeManager.closeDB();
                int position=personViewHolder.getAdapterPosition();
                grades.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0,getItemCount()+1);
            }

        });

    }


    public String returnCj(String cj) {
        if (cj.equals("null")) {
            return "";
        } else {
            return cj;
        }
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }


}