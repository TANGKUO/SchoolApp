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
import org.pointstone.cugapp.activities.ExamDetailActivity;
import org.pointstone.cugapp.utils.Exam;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ExamCardViewAdapter extends RecyclerView.Adapter<ExamCardViewAdapter.GradeViewHolder> {


    private List<Exam> exams;
    private static Context context;
    private Context mContext;
    public ExamCardViewAdapter(List<Exam> exams, Context context) {
        this.exams = exams;
        this.context = context;
        mContext=context;
    }


    //自定义ViewHolder类
    static class GradeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView kcmc;
        TextView kssj;
        TextView js;
        TextView zh;


        public GradeViewHolder(final View itemView) {
            super(itemView);
            //itemView.setBackgroundColor(context.getResources().getColor(R.color.course_grid_color2));
            cardView = (CardView) itemView.findViewById(R.id.exam_CardView);
            kcmc = (TextView) itemView.findViewById(R.id.card_kcmcTv);
            kssj = (TextView) itemView.findViewById(R.id.card_kssjTv);
            js = (TextView) itemView.findViewById(R.id.card_jsTv);
            zh = (TextView) itemView.findViewById(R.id.card_zhTv);
        }

    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.exam_card_item, parent, false);
        GradeViewHolder nvh = new GradeViewHolder(v);
        return nvh;
    }

    public Object getItem(int position) {
        return exams.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final GradeViewHolder personViewHolder, final int i) {
        //根据位置分配颜色和数据
        final Exam exam = (Exam) this.getItem(i);
        switch (i % 2) {
            case 0:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color0));
                break;
            case 1:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color1));
                break;
            case 2:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color2));
                break;
            case 3:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color3));
                break;
            case 4:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color4));
                break;
            case 5:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color5));
                break;
            case 6:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color6));
                break;
            case 7:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color7));
                break;
            case 8:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color8));
                break;
            case 9:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color9));
                break;
        }
        personViewHolder.kcmc.setText(exam.getKczwmc() );
        personViewHolder.kssj.setText("时间\n" + exam.getKssj());
        personViewHolder.js.setText("考场\n" + exam.getJsmc());
        personViewHolder.zh.setText("座号\n" + exam.getZwh());
        //设置点击事件
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ExamDetailActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", ""+exam.getXn()+" "+returnTerm(exam.getXq())+"\n"+exam.getKczwmc()+"\n\n"
                        +exam.getKssj()+"\n"
                        +"考试地点："+exam.getJsmc()+"\n"
                        +"座位号："+exam.getZwh());
                mContext.startActivity(intent);
            }
        });

    }
    public String returnTerm(String xq){
        if(xq.equals("1")){
            return "上学期";
        }else{
            return "下学期";
        }
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }


}