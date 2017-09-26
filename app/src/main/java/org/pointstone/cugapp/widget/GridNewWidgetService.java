package org.pointstone.cugapp.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.GradeYear;
import org.pointstone.cugapp.utils.InformationShared;

/**
 * Created by Administrator on 2017/3/18.
 */

public class GridNewWidgetService extends RemoteViewsService {

    private static final String TAG = GridNewWidgetService.class.getName();
    private String[][] contents;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e(TAG, "11onGetViewFactory");
        return new GridNewRemoteViewFactory(this, intent);
    }


    private class GridNewRemoteViewFactory implements RemoteViewsFactory {
        private Context mContext;

        public GridNewRemoteViewFactory(Context context, Intent intent) {
            Log.e(TAG, "GridViewRemoteViewFactory");
            mContext = context;

            contents=CourseNewWidget.con.clone();
        }

        @Override
        public void onCreate() {
            Log.e(TAG, "GridOnCreate");
            contents=CourseNewWidget.con.clone();
        }

        @Override
        public void onDataSetChanged() {
            contents=CourseNewWidget.con.clone();
        }

        @Override
        public void onDestroy() {
            Log.e(TAG, "RemoteViewsFactoryOnDestroy");
        }

        @Override
        public int getCount() {
            return 48;
        }

        public Object getItem(int position) {
            if (position % 8 == 0) {
                return null;
            }
            int row=position/8;
            int column=position%8-1;

            return contents[row][column];
        }



        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_item_new);
            String course=(String)getItem(position);
            rv.setTextViewText(R.id.grid_text, "");
            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_transparent));
            if(course!=null)
            {
                //如果有课,那么添加数
                if( course.charAt(5)!='\n'){

                    if (course.charAt(1)==':')
                    {
                        rv.setTextViewText(R.id.grid_text, course.substring(2,course.length()-1));
                    }else{
                        rv.setTextViewText(R.id.grid_text, course.substring(3,course.length()-1));
                    }

                    rv.setTextColor(R.id.grid_text, Color.WHITE);


                    String spl[]=course.split(":");
                    if(spl.length==2){
                        rv.setTextViewText(R.id.grid_text, spl[1]);
                    }

                    int color=Integer.parseInt(spl[0]);
                    //  if(spl.length==2){
                    switch( color%10 ) {
                        case 0:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color0));

                            break;
                        case 1:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color1));
                            break;
                        case 2:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color2));
                            break;
                        case 3:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color3));
                            break;
                        case 4:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color4));
                            break;
                        case 5:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color5));
                            break;
                        case 6:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color6));
                            break;
                        case 7:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color7));
                            break;
                        case 8:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color8));
                            break;
                        case 9:
                            rv.setInt(R.id.grid_text,"setBackgroundColor",mContext.getResources().getColor(R.color.course_grid_color9));
                            break;
                    }
                    //  }else
          /*  {
                switch( color%10 ) {
                    case 0:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_0));

                        break;
                    case 1:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_1));

                        break;
                    case 2:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_2));
                        break;
                    case 3:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_3));
                        break;
                    case 4:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_4));
                        break;
                    case 5:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_5));
                        break;
                    case 6:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_6));
                        break;
                    case 7:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_7));
                        break;
                    case 8:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_8));
                        break;
                    case 9:
                        textView.setBackground(mContext.getResources().getDrawable(R.drawable.course_background_9));
                        break;
                }*/

                }
            }else{
                rv.setTextViewText(R.id.grid_text, getSignFromPosition(position));
                rv.setTextColor(R.id.grid_text,mContext.getResources().getColor(R.color.black));
                int currentclass = GradeYear.getCurrentClass();
                if (currentclass != 0) {
                     if(currentclass+1==(position/8+1)*2){
                         rv.setTextColor(R.id.grid_text,mContext.getResources().getColor(R.color.currentdate));
                     }

                }
            }


            // 设置 第position位的“视图”对应的响应事件
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(CourseNewWidget.COLLECTION_VIEW_NEW_EXTRA, position);
            rv.setOnClickFillInIntent(R.id.widgetCourseNewLayout, fillInIntent);

            return rv;
        }

        private String getSignFromPosition(int position) {
            if (InformationShared.getInt("SummerWinter") == 0) {
                switch (position / 8 + 1) {
                    case 1:
                        return "08:00\n1\n\n2\n09:35";
                    case 2:
                        return "10:05\n3\n\n4\n11:40";
                    case 3:
                        return "14:00\n5\n\n6\n15:35";
                    case 4:
                        return "16:00\n7\n\n8\n17:35";
                    case 5:
                        return "19:00\n9\n\n10\n20:35";
                    case 6:
                        return "21:05\n11\n\n12\n22:40";
                }
            }else{
                switch (position / 8 + 1) {
                    case 1:
                        return "08:00\n1\n\n2\n09:35";
                    case 2:
                        return "10:05\n3\n\n4\n11:40";
                    case 3:
                        return "14:30\n5\n\n6\n16:05";
                    case 4:
                        return "16:30\n7\n\n8\n18:05";
                    case 5:
                        return "19:30\n9\n\n10\n21:05";
                    case 6:
                        return "21:35\n11\n\n12\n23:10";
                }
            }

            return null;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
