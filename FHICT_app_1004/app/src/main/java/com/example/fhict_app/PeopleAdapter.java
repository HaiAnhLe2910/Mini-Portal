package com.example.fhict_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.myViewHolder> {
    private List<PersonItem> mDataset;
    private Context mContext;
    private ItemClickListener mClickListener;

    //Provide a suitable constructor
    public PeopleAdapter(List<PersonItem> mDataset, Context mContext){
        this.mDataset=mDataset;
        this.mContext=mContext;
    }


    //Create new views
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listpeople, parent, false);
        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    //Replace the contents of a view
    @Override
    public void onBindViewHolder(myViewHolder holder,int position){

       PersonItem currentPerson =mDataset.get(position);
        String person_name = currentPerson.getName();
        //String person_name = mDataset.get(position);
        holder.person_name.setText(person_name);
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
        public TextView person_name;
        public LinearLayout parentLayout;



        public myViewHolder(View personView){
            super(personView);
            person_name=personView.findViewById(R.id.person_name);
            parentLayout=personView.findViewById(R.id.parent_layout);
            personView.setOnClickListener(this);
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
    String getItem(int id) {
        return mDataset.get(id).getName();
    }
    String getPhoneNumber(int id) {return mDataset.get(id).getPhone();}
    String getOffice(int id) {return mDataset.get(id).getOffice();}
    String getEmail(int id) {return mDataset.get(id).getEmail();}


        // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void filterList(ArrayList<PersonItem> filteredList){
        mDataset = filteredList;
        notifyDataSetChanged();
    }

}
