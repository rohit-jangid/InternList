package com.rohfl.internslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.rohfl.internslist.R;
import com.rohfl.internslist.model.Internship;

import java.util.List;

public class InternshipsAdapter extends RecyclerView.Adapter<InternshipsAdapter.MainViewHolder>{

    Context mContext;
    List<Internship> mList;

    OnLastItemListener onLastItemListener;

    public InternshipsAdapter(Context mContext, List<Internship> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0) {
            v = LayoutInflater.from(mContext).inflate(R.layout.internship_card_adapter, parent, false);
            return new ViewHolder(v);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.loading_layout, parent, false);
            return new LoadingHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull InternshipsAdapter.MainViewHolder holder, int position) {
        if(position == mList.size() - 1) {
            onLastItemListener.onLastItem(position);
        } else {
            // if the holder is an object/instance of ViewHolder then do the following else do nothing
            if(holder instanceof ViewHolder) {
                Internship internship = mList.get(position);
                ((ViewHolder) holder).title_tv.setText(internship.getRoleName());
                ((ViewHolder) holder).company_name_tv.setText(internship.getCompanyName());
                ((ViewHolder) holder).stipend_tv.setText(internship.getStipendProvided());

                if (internship.isWorkFromHome()) {
                    ((ViewHolder) holder).is_work_from_home_tv.setText("Work from Home");
                } else ((ViewHolder) holder).is_work_from_home_tv.setText("Work from Office");

                if (internship.isPartTimeAllowed()) {
                    ((ViewHolder) holder).part_time_tv.setText("Part time allowed");
                    ((ViewHolder) holder).part_time_tv.setVisibility(View.VISIBLE);
                } else ((ViewHolder) holder).part_time_tv.setVisibility(View.GONE);

                ((ViewHolder) holder).duration_tv.setText(internship.getInternshipDuration());
                ((ViewHolder) holder).is_internship_tv.setText(internship.getTypeOfJob());

                ((ViewHolder) holder).apply_by_date_tv.setText(internship.getExpireDate());

                if (internship.getCompanyLogoUrl() != null) {
                    Glide.with(mContext).load(internship.getCompanyLogoUrl()).into(((ViewHolder) holder).compony_logo_iv);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) != null)
            return 0;
        else
            return 1;
    }

    public void updateList(List<Internship> mList) {
//        removeNull();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    /***
     * removes null
     */
    public void removeNull() {
        if (this.mList.size() > 0 && this.mList.get(this.mList.size() - 1) == null) {
            this.mList.remove(this.mList.size() - 1);
            notifyItemRemoved(this.mList.size()-1);
        }
    }

    /**
     * creating a callback kind of thing to detect the last object is reached or not
     * @param onLastItemListener
     */
    public void setOnLastItemListener(OnLastItemListener onLastItemListener) {
        this.onLastItemListener = onLastItemListener;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends MainViewHolder {
        TextView title_tv, company_name_tv, is_work_from_home_tv, stipend_tv, duration_tv, part_time_tv,
                is_internship_tv, apply_by_date_tv;
        ImageView compony_logo_iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            company_name_tv = itemView.findViewById(R.id.company_name_tv);
            is_work_from_home_tv = itemView.findViewById(R.id.is_work_from_home_tv);
            stipend_tv = itemView.findViewById(R.id.stipend_tv);
            duration_tv = itemView.findViewById(R.id.duration_tv);
            part_time_tv = itemView.findViewById(R.id.part_time_tv);
            is_internship_tv = itemView.findViewById(R.id.is_internship_tv);
            apply_by_date_tv = itemView.findViewById(R.id.apply_by_date_tv);
            compony_logo_iv = itemView.findViewById(R.id.company_logo_iv);
        }
    }

    public class LoadingHolder extends MainViewHolder {

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
