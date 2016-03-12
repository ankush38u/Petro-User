package com.paprbit.module.retrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.RequestData;
import com.paprbit.module.retrofit.utility.Storage;

import java.util.List;

/**
 * Created by ankush38u on 1/14/2016.
 */
public class AllRequestsAdapter extends RecyclerView.Adapter<AllRequestsAdapter.RequestHolder> {
    private List<RequestData> requestDatas;
    private Context context;

    public AllRequestsAdapter() {
    }

    public AllRequestsAdapter(List<RequestData> schemeDatas) {
        this.requestDatas = schemeDatas;
    }

    public AllRequestsAdapter(List<RequestData> schemeDatas, Context context) {
        this.requestDatas = schemeDatas;
        this.context = context;
    }

    @Override
    public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_recycler_view, parent, false);

        return new RequestHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RequestHolder holder, final int position) {
        final RequestData rd = requestDatas.get(position);
        holder.pid.setText("Pump: " + Storage.pumpList.get(Integer.parseInt(rd.getPid())));
        //holder.carno.setText("Car No: " + rd.getCarno());
        holder.petrol.setText("Liter: " + rd.getQuantity());
        holder.carno.setVisibility(View.GONE);
        String x = (rd.getStatus() == 0) ? "Pending" : "Successful";
        holder.status.setText("Status: " + x);

        /*holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("");
                builder.setMessage("");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return requestDatas.size();
    }


    public static class RequestHolder extends RecyclerView.ViewHolder {
        protected TextView pid;
        protected TextView carno;
        protected TextView petrol;
        protected TextView status;

        public RequestHolder(View v) {
            super(v);
            pid = (TextView) v.findViewById(R.id.tv_pid);
            carno = (TextView) v.findViewById(R.id.tv_carno);
            petrol = (TextView) v.findViewById(R.id.tv_petrol);
            status = (TextView) v.findViewById(R.id.status_tv);
        }


    }
}
