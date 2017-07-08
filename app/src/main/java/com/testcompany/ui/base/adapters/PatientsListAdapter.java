package com.testcompany.ui.base.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.testcompany.R;
import com.testcompany.datalayer.models.PatientModel;
import com.testcompany.utils.view.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class PatientsListAdapter extends RecyclerView.Adapter<PatientsListAdapter.PatientsHolder> {


    private Activity context;
    private List<PatientModel> lstPatients;
    private int lastPosition = -1;

    public PatientsListAdapter(List<PatientModel> lstPatients, Activity context) {
        this.lstPatients = lstPatients;
        this.context = context;
    }

    @Override
    public PatientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatientsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false));

    }

    public void clearData() {
        int size = this.lstPatients.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.lstPatients.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void updateList(List<PatientModel> lstPatients) {
        this.lstPatients = lstPatients;
        notifyItemRangeInserted(0, lstPatients.size() - 1);
    }

    public List<PatientModel> getData() {
        return lstPatients;
    }

    @Override
    public void onBindViewHolder(PatientsHolder holder, final int position) {

        PatientModel data = lstPatients.get(position);
        holder.tvPatientName.setText(data.getPatient_name());
        holder.tvPatientAge.setVisibility(View.GONE);
        //holder.tvPatientAge.setText(context.getString(R.string.age).concat(String.valueOf(data.getPatient_age())));
        holder.tvPatientDisorder.setText(data.getDisorderString());
        if (data.getPercentage() <=15){
            holder.ivFlag.setImageResource(R.drawable.green);
        }else if (data.getPercentage() >=75){
            holder.ivFlag.setImageResource(R.drawable.img_red);
        }else{
            holder.ivFlag.setImageResource(R.drawable.img_yellow);
        }
        holder.itemView.setOnClickListener(v -> onItemClick(holder.getAdapterPosition(), holder));
        holder.cardView.setOnClickListener(v -> onItemClick(holder.getAdapterPosition(), holder));
        //setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return lstPatients.size();
    }

    public static class PatientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPatientName)
        CustomTextView tvPatientName;
        @BindView(R.id.tvPatientDisorder)
        CustomTextView tvPatientDisorder;
        @BindView(R.id.tvPatientAge)
        CustomTextView tvPatientAge;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.ivFlag)
        ImageView ivFlag;

        public PatientsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(PatientsHolder holder) {
        clearAnimation(holder);
    }

    public void clearAnimation(PatientsHolder holder) {
        holder.itemView.clearAnimation();
    }

    public abstract void onItemClick(int position, PatientsHolder holder);
}

