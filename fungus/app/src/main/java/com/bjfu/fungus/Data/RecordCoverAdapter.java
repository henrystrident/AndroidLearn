package com.bjfu.fungus.Data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bjfu.fungus.Collect.CollectInformationTube;
import com.bjfu.fungus.R;
import com.bjfu.fungus.Record.UpdateInformationBasic;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordCoverAdapter extends RecyclerView.Adapter<RecordCoverAdapter.ViewHolder> {

    private List<RecordCover> recordCoverList;
    private static final int DELETE_SUCCEED=0;
    private static final int NETWORK_ERROR=1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DELETE_SUCCEED:

                    recordCoverList.remove(Integer.parseInt((String)msg.obj));
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }
    };

    public RecordCoverAdapter(List<RecordCover> recordCoverList)
    {
        this.recordCoverList = recordCoverList;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        View recordCoverView;
        ImageView avatarView;
        TextView collectNumberView;
        TextView dateView;
        ImageView imageView;
        TextView chineseNameView;
        TextView locationView;
        ImageView deleteView;

        public ViewHolder(View view)
        {
            super(view);
            recordCoverView = view;
            avatarView = view.findViewById(R.id.recordCoverAvatarView);
            collectNumberView = view.findViewById(R.id.recordCoverCollectNumberView);
            dateView = view.findViewById(R.id.recordCoverDateView);
            imageView = view.findViewById(R.id.recordCoverImageView);
            chineseNameView = view.findViewById(R.id.recordCoverChineseNameView);
            locationView = view.findViewById(R.id.recordCoverLoationView);
            deleteView = view.findViewById(R.id.deleteView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.recordCoverView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateInformationBasic.class);
                intent.putExtra("collectNumber", viewHolder.collectNumberView.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 点击弹出是否确定删除
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("是否删除");
                alertDialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String collectNumber = viewHolder.collectNumberView.getText().toString();
                        String username = v.getContext().getSharedPreferences("CURRENT_USER_INFO", Context.MODE_PRIVATE)
                                .getString("username", "");
                        String ip = v.getContext().getSharedPreferences("networkSetting", Context.MODE_PRIVATE)
                                .getString("ip", "");
                        String port = v.getContext().getSharedPreferences("networkSetting", Context.MODE_PRIVATE)
                                .getString("port", "");
                        deleteRecord(username, collectNumber, ip, port, v.getContext(), viewHolder.getAdapterPosition());
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }

        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RecordCover recordCover = recordCoverList.get(position);
        if (recordCover.getAvatar() != null)
        {
            holder.avatarView.setImageBitmap(recordCover.getAvatar());
        }
        holder.collectNumberView.setText(recordCover.getCollectNumber());
        holder.dateView.setText(recordCover.getDate());
        holder.imageView.setImageBitmap(recordCover.getImage());
        holder.chineseNameView.setText(recordCover.getChineseName());
        holder.locationView.setText(recordCover.getLocation());
    }

    @Override
    public int getItemCount() {
        return recordCoverList.size();
    }

    public void update(RecordCover data)
    {
        if (data != null)
        {
            recordCoverList.add(data);
            notifyDataSetChanged();
        }
    }

    public void clearData()
    {
        recordCoverList.clear();
        notifyDataSetChanged();
    }

    /**
     * 向服务器端发送请求，删除记录
     */
    private void deleteRecord(String username, String collectNumber, String ip, String port, final Context context, final int position)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("collectNumber", collectNumber)
                .build();
        Request request = new Request.Builder()
                .url(ip+port+"delete")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(context, "网络连接失败，无法删除该记录", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = DELETE_SUCCEED;
                message.obj = position+"";
                handler.sendMessage(message);
            }
        });
    }



}
