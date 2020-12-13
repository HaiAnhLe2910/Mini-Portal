package com.example.fhict_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.myViewHolder> {
    private List<CourseItem> mDataset;
    private Context mContext;
    private ScheduleAdapter.ItemClickListener mClickListener;

    //Provide a suitable constructor
    public ScheduleAdapter(List<CourseItem> mDataset, Context mContext){
        this.mDataset=mDataset;
        this.mContext=mContext;
    }


    //Create new views
    @Override
    public ScheduleAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listcourse, parent, false);
        ScheduleAdapter.myViewHolder vh = new ScheduleAdapter.myViewHolder(v);
        return vh;
    }


    //Replace the contents of a view

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CourseItem currentCourse =mDataset.get(position);
        String course_name = currentCourse.getCourseName();
        String teacher_name = currentCourse.getTeacherAbbreviation();
        String room=currentCourse.getRoom();

        //String person_name = mDataset.get(position);
        holder.course_name.setText(course_name);
        holder.teacher_name.setText(teacher_name);
        holder.room_name.setText(room);
    }



    //Return the size of your dataset
    @Override
    public int getItemCount(){
        return mDataset.size();
    }

    //Provide a reference to the views for each data item
    //Complex data items may need more than one view per item, and
    //provide the access to all the views for a data item in a view holder
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //each data item is just a string in our case
        public TextView course_name;
        public TextView teacher_name;
        public LinearLayout parentLayout;
        public TextView room_name;


        public myViewHolder(View courseView){
            super(courseView);
            course_name=courseView.findViewById(R.id.course_name);
            teacher_name=courseView.findViewById(R.id.teacher_name);
            parentLayout=courseView.findViewById(R.id.parent_layout_course);
            room_name = courseView.findViewById(R.id.room);
            courseView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener!=null)
                mClickListener.onItemClick(v,getAdapterPosition());
        }

    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // convenience method for getting data at click position
    String getCourseName(int id) {
        return mDataset.get(id).getCourseName();
    }
    // allows clicks events to be caught
    void setClickListener(ScheduleAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
