package org.pointstone.cugapp.utils;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/6.
 * <p>
 * 学年和年级等的互相转换
 * 0代表大一上，1代表大一下，2代表大二上，以此类推
 */

public class GradeYear {

    //根据学号返回当前选择框的序号
    public static int getCurrentGrade() {
        int index = 0;
        String str_year = InformationShared.getString("id").substring(0, 4);
        int year = Integer.parseInt(str_year);
        Calendar c = Calendar.getInstance();
        int current_year = c.get(Calendar.YEAR);

        int grade = current_year - year;
        if(c.get(Calendar.MONTH)+1 <=7 )
        {
            grade-=1;
        }
        if (c.get(Calendar.MONTH)+1 >= 8 || c.get(Calendar.MONTH)+1 <= 1) {
            switch (grade) {
                case 0:
                    index = 0;
                    break;
                case 1:
                    index = 2;
                    break;
                case 2:
                    index = 4;
                    break;
                case 3:
                    index = 6;
                    break;
            }
        } else {
            switch (grade) {
                case 0:
                    index = 1;
                    break;
                case 1:
                    index = 3;
                    break;
                case 2:
                    index = 5;
                    break;
                case 3:
                    index = 7;
                    break;
            }
        }
        return index;
    }

    //根据当前选择框的序号返回当前学年
    public static String getCurrentXN(int index) {
        String str_year = InformationShared.getString("id").substring(0, 4);
        int year = Integer.parseInt(str_year);
        String str = "";
        switch (index) {
            case 0:
            case 1:
                str = year + "-" + (year + 1);
                return str;
            case 2:
            case 3:
                str = (year + 1) + "-" + (year + 2);
                return str;
            case 4:
            case 5:
                str = (year + 2) + "-" + (year + 3);
                return str;
            case 6:
            case 7:
                str = (year + 3) + "-" + (year + 4);
                return str;
        }
        return str;
    }

    //根据当前选择框的序号返回当前学期
    public static String getCurrentXQ(int index) {
        String str = "";
        switch (index) {
            case 0:
            case 2:
            case 4:
            case 6:
                str = "1";
                break;

            case 1:
            case 3:
            case 5:
            case 7:
                str = "2";
                break;

        }
        return str;
    }

    public static void setCurrentWeek(int w) {
        if (InformationShared.getInt("currentweek" + w) != 0) {
            int days0[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int days1[] = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int start_month = InformationShared.getInt("start_month" + w);
            int start_day = InformationShared.getInt("start_day" + w);
            int start_week = InformationShared.getInt("start_week" + w);
            Calendar c = Calendar.getInstance();
            int current_year = c.get(Calendar.YEAR);
            int current_month = c.get(Calendar.MONTH) + 1;
            int current_day = c.get(Calendar.DAY_OF_MONTH);
            int subday = 0;
            if (!(current_year % 4 == 0 && current_year % 100 != 0 || current_year % 400 == 0)) {
                if (start_month < current_month) {
                    subday = subday + days0[start_month] - start_day;
                    for (int i = start_month + 1; i < current_month; i++) {
                        subday = subday + days0[i];
                    }
                    subday = subday + current_day;
                } else if (start_month == current_month) {
                    subday = subday + current_day - start_day;
                } else {
                    subday = subday + days0[start_month] - start_day;
                    for (int i = start_month + 1; i < 13; i++) {
                        subday = subday + days0[i];
                    }
                    for (int i = 1; i < current_month; i++) {
                        subday = subday + days0[i];
                    }
                    subday = subday + current_day;
                }


            } else {
                if (start_month < current_month) {
                    subday = subday + days1[start_month] - start_day;
                    for (int i = start_month + 1; i < current_month; i++) {
                        subday = subday + days1[i];
                    }
                    subday = subday + current_day;
                } else if (start_month == current_month) {
                    subday = subday + current_day - start_day;
                } else {
                    subday = subday + days1[start_month] - start_day;
                    for (int i = start_month + 1; i < 13; i++) {
                        subday = subday + days1[i];
                    }
                    for (int i = 1; i < current_month; i++) {
                        subday = subday + days1[i];
                    }
                    subday = subday + current_day;
                }
            }

            if(subday>=0){
                int current_week = subday / 7 + start_week;
                if (current_week > 20) {
                    current_week = 20;
                }
                InformationShared.setInt("currentweek" + w, current_week);
            }

        }
    }

    //返回某一学期某一周周一到周日月份,日期
    public static int[] getMonMonthDay(int term,int week)
    {
        int XN=Integer.parseInt(getCurrentXN(term).substring(0,4))+1;

        int days[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (XN% 4 == 0 && XN % 100 != 0 || XN% 400 == 0)
        {
            days[2]=29;
        }
        int start_week=InformationShared.getInt("start_week"+term);
        int start_month=InformationShared.getInt("start_month"+term);
        int start_day=InformationShared.getInt("start_day"+term);
        int sub=Math.abs(start_week-week)*7;
        int day=start_day;
        int i=0;
        if(start_week<week)
        {

            for(i=0;i<sub;i++)
            {
                day++;
                if(day==days[start_month]+1)
                {
                    start_month++;
                    day=1;
                    if(start_month==13)
                    {
                        start_month=1;
                    }
                }
            }
        }else
        {
            for(i=0;i<sub;i++)
            {
                day--;
                if(day==0)
                {
                    start_month--;
                    if(start_month==0)
                    {
                        start_month=12;
                    }
                    day=days[start_month];

                }
            }
        }
        int date[]=new int[8];
        int day2;
        date[0]=start_month;
        date[1]=day;
        int index=1;
        for(i=0;i<7;i++)
        {
            if(date[1]+i>days[date[0]])
            {
                day=date[1]+i-days[date[0]];
            }else {
                day=date[1]+i;
            }
            date[index]=day;
            index++;

        }
        return date;
    }
    public static void setSummerWinter(){
        Calendar c = Calendar.getInstance();
        int current_month = c.get(Calendar.MONTH)+1;
        if(current_month>=5&&current_month<=9)
        {
            InformationShared.setInt("SummerWinter",1);
        }else
        {
            InformationShared.setInt("SummerWinter",0);
        }
    }

    //得到当前时间要上的课
    public static int  getCurrentClass()
    {
        Calendar c = Calendar.getInstance();

        int current_minutes=getMinutes(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));

        int current_class=0;
        int SummerWinter=InformationShared.getInt("SummerWinter");
        if(SummerWinter==0)
        {
            if (current_minutes>=0&&current_minutes<getMinutes(9,35))
            {
                current_class=1;
            }else if(current_minutes<getMinutes(11,40))
            {
                current_class=3;
            }else if(current_minutes<getMinutes(15,35))
            {
                current_class=5;
            }else if(current_minutes<getMinutes(17,35))
            {
                current_class=7;
            }
            else if(current_minutes<getMinutes(20,35))
            {
                current_class=9;
            }else if(current_minutes<getMinutes(22,40))
            {
                current_class=11;
            }else{
                current_class=0;
            }
        }else{
            if (current_minutes>=0&&current_minutes<getMinutes(9,35))
            {
                current_class=1;
            }else if(current_minutes<getMinutes(11,40))
            {
                current_class=3;
            }else if(current_minutes<getMinutes(16,05))
            {
                current_class=5;
            }else if(current_minutes<getMinutes(18,05))
            {
                current_class=7;
            }
            else if(current_minutes<getMinutes(21,05))
            {
                current_class=9;
            }else if(current_minutes<getMinutes(23,10))
            {
                current_class=11;
            }else{
                current_class=0;
            }
        }

        return current_class;
    }

    public  static  int getMinutes(int hour,int minute)
    {
        return hour*60+minute;
    }


    //用于单天视图中前一天后一天返回日期星期
    public static String getDate(int change,int changeWeek)
    {
        Calendar cl = Calendar.getInstance();
        int current_month = cl.get(Calendar.MONTH) + 1;
        int current_day = cl.get(Calendar.DAY_OF_MONTH);
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        int xing=cl.get(Calendar.DAY_OF_WEEK);
        int month=0;
        int day=0;
        int week=0;
        if(isFirstSunday)
        {
            xing-=1;
            if(xing==0)
            {
                xing=7;
            }
        }

        week=xing+change;
        month=current_month;
        day=current_day+change+changeWeek*7;

        int XN=cl.get(Calendar.YEAR);
        int days[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (XN% 4 == 0 && XN % 100 != 0 || XN% 400 == 0)
        {
            days[2]=29;
        }

        if(day<1)
        {
            month-=1;
            if(month<1)
               month=12;
            day=days[month]+day;
        }else if(day>28){
            if(day>=days[month]+1)
            {
                month+=1;
                if(month==13)
                {
                    month=1;
                    day=day-days[12];
                }
                day=day-days[month-1];

            }
        }
        switch (week)
        {
            case 1:
               return  month+"月"+day+"日"+"星期一";
            case 2:
                return  month+"月"+day+"日"+"星期二";
            case 3:
                return  month+"月"+day+"日"+"星期三";
            case 4:
                return  month+"月"+day+"日"+"星期四";
            case 5:
                return  month+"月"+day+"日"+"星期五";
            case 6:
                return  month+"月"+day+"日"+"星期六";
            case 7:
                return  month+"月"+day+"日"+"星期日";
        }
        return "";
    }

    public static int getWeekDay(int change,int changeWeek)
    {
        Calendar cl = Calendar.getInstance();
        int current_month = cl.get(Calendar.MONTH) + 1;
        int current_day = cl.get(Calendar.DAY_OF_MONTH);
        boolean isFirstSunday = (cl.getFirstDayOfWeek() == Calendar.SUNDAY);
        int xing=cl.get(Calendar.DAY_OF_WEEK);
        int month=0;
        int day=0;
        int week=0;
        if(isFirstSunday)
        {
            xing-=1;
            if(xing==0)
            {
                xing=7;
            }
        }

        week=xing+change;
        month=current_month;
        day=current_day+change+changeWeek*7;

        int XN=cl.get(Calendar.YEAR);
        int days[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (XN% 4 == 0 && XN % 100 != 0 || XN% 400 == 0)
        {
            days[2]=29;
        }

        if(day<1)
        {
            month-=1;
            if(month<1)
                month=12;
            day=days[month]+day;
        }else if(day>28){
            if(day>=days[month]+1)
            {
                month+=1;
                if(month==13)
                {
                    month=1;
                    day=day-days[12];
                }
                day=day-days[month-1];

            }
        }
        return  week;
    }
}
