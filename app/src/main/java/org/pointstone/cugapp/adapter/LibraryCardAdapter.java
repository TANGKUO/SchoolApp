package org.pointstone.cugapp.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.pointstone.cugapp.CugApplication;
import org.pointstone.cugapp.R;
import org.pointstone.cugapp.bean.Book;
import org.pointstone.cugapp.utils.InformationShared;
import org.pointstone.cugapp.utils.OwnToast;
import org.pointstone.cugapp.view.RotateTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keyboy on 2017/2/25.
 */

public class LibraryCardAdapter extends RecyclerView.Adapter<LibraryCardAdapter.LibraryViewHolder> {


    private List<Book> books;
    private static Context context;
    private ProgressDialog pd;
    private int flag;

    public LibraryCardAdapter(List<Book> books, Context context,int flag) {
        this.books = books;
        this.context = context;
        this.flag=flag;

    }


    //自定义ViewHolder类
    static class LibraryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView bookName;
        TextView bookId;
        TextView lendTime;
        TextView returnTime;
        TextView again;

        RotateTextView textView;


        public LibraryViewHolder(final View itemView) {
            super(itemView);
            //itemView.setBackgroundColor(context.getResources().getColor(R.color.course_grid_color2));
            cardView = (CardView) itemView.findViewById(R.id.book_CardView);
            bookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            bookId = (TextView) itemView.findViewById(R.id.tv_book_id);
            lendTime = (TextView) itemView.findViewById(R.id.tv_lend_time);
            returnTime = (TextView) itemView.findViewById(R.id.tv_return_time);
            again = (TextView) itemView.findViewById(R.id.tv_again);

            textView = (RotateTextView) itemView.findViewById(R.id.tv_time_out);
        }

    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.library_swipe, parent, false);
        LibraryViewHolder nvh = new LibraryViewHolder(v);
        return nvh;
    }

    public Object getItem(int position) {
        return books.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final LibraryViewHolder personViewHolder, final int i) {
        //根据位置分配颜色和数据
        final Book book = (Book) this.getItem(i);

        switch (i ) {
            case 0:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color1));
                break;

            case 1:
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.course_grid_color5));
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
                personViewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grade_color0));
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
        personViewHolder.bookName.setText("书名: " +book.getName() );
        personViewHolder.bookId.setText("条形码: " + book.getId());
        personViewHolder.lendTime.setText("借阅日期: " + book.getLend_time());
        personViewHolder.returnTime.setText("归还日期: " + book.getReturn_time());
        if(flag==1) {
            try {
                if (DateCompare(book.getReturn_time()) == 0)
                    personViewHolder.textView.setText("图书超期");
                else if (DateCompare(book.getReturn_time()) == 1)
                    personViewHolder.textView.setText("即将到期");
                else
                    personViewHolder.textView.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        personViewHolder.textView.setDegrees(30);

        //设置点击事件
        final LibraryViewHolder gvholder = (LibraryViewHolder) personViewHolder;
        gvholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                renew(book.getId());

                if(flag==1)
                    renewDialog(book.getId());
                else
                    historyDetail(book.getName(),book.getId(),book.getLend_time(),book.getReturn_time());

            }
        });

        //设置滑动点击事件
        gvholder.again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renew(book.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    //续命单本图书函数
    public void renew(String barcode ) {
//        CugApplication.getInstance().getRequestQueue().cancelAll("My Tag_renew");
        pd= ProgressDialog.show(context,"续借中","请稍后......");
        String url = "https://api.cugapp.com/public_api/CugApp/renwal_single_book";
        HashMap<String, String> hashMap = new HashMap<String,String>();
        hashMap.put("unionid", InformationShared.getString("unionid"));
        hashMap.put("password",InformationShared.getString("library_password"));
        hashMap.put("barcode",barcode);
        JSONObject jsonObject = new JSONObject(hashMap);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean status = response.getBoolean("status");
                            System.out.println(status);
                            if(status) {
                                pd.cancel();
                                OwnToast.Short("续命成功");
                            }
                            else {
                                pd.cancel();
                                OwnToast.Short("已达续借最大次数，不能重复续命");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if( error instanceof NetworkError) {
                            //网络异常
                            OwnToast.Short("网络异常");
                        } else if( error instanceof ServerError) {
                            //服务器响应错误
                            OwnToast.Short("已达续借最大次数，不能重复续命");
                        } else if( error instanceof AuthFailureError) {
                            //请求时验证凭证失败
                            OwnToast.Short("请求时验证凭证失败");
                        } else if( error instanceof ParseError) {
                            //解析服务器返回数据错误
                            OwnToast.Short("解析服务器返回数据错误");
                        } else if( error instanceof NoConnectionError) {
                            //网络请求无任何连接
                            OwnToast.Short("网络请求无任何连接");
                        } else if( error instanceof TimeoutError) {
                            //超时
                            OwnToast.Short("请求超时");
                        }else{
                            //成功
                            OwnToast.Short("续命成功");
                        }
                    }
                }) {
            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-API-KEY","s0048ksww8ck8kc8kg4wc8ooskwsgc00k4kg0kkk");
                return headers;
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                pd.dismiss();
            }

        };
           /* 解决重复请求问题 */
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 0, 1f) {
            @Override
            public int getCurrentRetryCount() {
                //表示当前已经重复请求一次，就不会再第二次重复请求，从而屏蔽掉Volley的自动重复请求功能
                return 1;
            }
        });
        jsObjRequest.setTag("My Tag_renew");
        CugApplication.getInstance().addToRequestQueue(jsObjRequest);
    }

    //续借单本图书对话框
    private void renewDialog(final String barcode){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle("续借图书"); //设置标题
        builder.setMessage("是否确认续借该书?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                renew(barcode);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    //显示借阅历史详情函数
    public void historyDetail(String bookName,String code,String borrowTime,String returnTime) {

        new android.support.v7.app.AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("借阅详情")
                .setMessage(
                        "借阅书名:  " + bookName + "\n\n"
                                + "借阅时间:  " + borrowTime + "\n\n"
                                + "归还时间:  " + returnTime+ "\n\n"
                                + "条行码号:  " + code+ "\n\n")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setNegativeButton("取消", null).create().show();
    }



    //计算图书是否超期函数
    public static int DateCompare(String s) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        //得到指定模范的时间
        Date d2 = sdf.parse(str);
        Date d1 = sdf.parse(s);
        System.out.println(((d1.getTime() - d2.getTime())/(24*3600*1000)));
        //比较
        if(((d1.getTime() - d2.getTime())/(24*3600*1000)) <0) {
            System.out.println("超期");
            return 0;
        }
        else if(((d1.getTime() - d2.getTime())/(24*3600*1000)) >=0&&((d1.getTime() - d2.getTime())/(24*3600*1000)) <=7) {
            System.out.println("即将到期");
            return 1;
        }
        else{
            System.out.println("正常");
            return 2;
        }
    }


}