package ua.com.lhjlbjyjd.sibur;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lhjlbjyjd on 25.11.2017.
 */

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.ViewHolder> {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Goal[] mDataset;
    private Context context;
    private ViewHolder firstGoalView;
    private int taskId;
    private Task parentTask;

    private boolean currentGoalFound = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        RelativeLayout layout;
        ViewGroup group;
        public ViewHolder(RelativeLayout v, ViewGroup vg) {
            super(v);
            layout = v;
            group = vg;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GoalListAdapter(Goal[] myDataset, int taskId, Task task, Context context) {
        mDataset = myDataset;
        this.context = context;
        this.taskId = taskId;
        parentTask = task;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GoalListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        RelativeLayout v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item, parent, false).findViewById(R.id.goal_layout);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, parent);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoalDetailsActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("goalId", position);
                context.startActivity(intent);
            }
        });

        if(position == 0)
            firstGoalView = holder;

        ((CheckBox)holder.layout.findViewById(R.id.checkBox)).setEnabled(true);
        if(!currentGoalFound && !mDataset[position].getState() && ((MyApp)context.getApplicationContext()).getCurrentTask() != null){
            currentGoalFound = true;
        }else{
            ((CheckBox)holder.layout.findViewById(R.id.checkBox)).setChecked(mDataset[position].getState());
            holder.layout.findViewById(R.id.checkBox).setEnabled(false);
        }
        ((TextView)holder.layout.findViewById(R.id.goal_description_text)).setText(mDataset[position].getDescription());
        if(!mDataset[position].getState()) {
            holder.layout.findViewById(R.id.checkBox).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                    builder.setTitle("Подтверждение");
                    builder.setMessage("Вы подтверждаете выполнение задачи?");
                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            view.setEnabled(false);
                            mDataset[position].setState(true);
                            mDataset[position].setEndDate(System.currentTimeMillis());
                            if(position + 1 < mDataset.length) {
                                mDataset[position + 1].setBeginDate(System.currentTimeMillis());
                                holder.group.getChildAt(position + 1).findViewById(R.id.checkBox).setEnabled(true);
                                if(mDataset[position+1].getPhotoRequired())
                                    holder.group.getChildAt(position + 1).findViewById(R.id.additional_task).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ((GoalsListActivity)context).takePhoto(position + 1);
                                        }
                                    });
                            }else{
                                parentTask.setFulfilled();
                                ((MyApp)context.getApplicationContext()).addTask(parentTask);
                                ((MyApp)context.getApplicationContext()).currentTask = null;
                                ((GoalsListActivity)context).finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((CheckBox) view).setChecked(false);
                        }
                    });
                    builder.show();
                }
            });
        }else{
            CheckBox checkBox = holder.layout.findViewById(R.id.checkBox);
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        }

        ImageView additionalTaskButton = holder.layout.findViewById(R.id.additional_task);
        additionalTaskButton.setVisibility(View.GONE);

        if(mDataset[position].getEmailRequired()){
            additionalTaskButton.setVisibility(View.VISIBLE);
            additionalTaskButton.setBackgroundColor(Color.parseColor("#CB213D"));
        }
        if(mDataset[position].getPhotoRequired()){
            additionalTaskButton.setVisibility(View.VISIBLE);
            additionalTaskButton.setBackgroundColor(Color.parseColor("#43ff98"));
        }
        if(position == mDataset.length - 1)
            currentGoalFound = false;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void startFulfillingTask(){
        firstGoalView.layout.findViewById(R.id.checkBox).setEnabled(true);
        firstGoalView.layout.findViewById(R.id.checkBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                builder.setTitle("Подтверждение");
                builder.setMessage("Вы подтверждаете выполнение задачи?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        view.setEnabled(false);
                        mDataset[0].setState(true);
                        mDataset[1].setBeginDate(System.currentTimeMillis());
                        if(1 < mDataset.length)
                            firstGoalView.group.getChildAt(1).findViewById(R.id.checkBox).setEnabled(true);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((CheckBox) view).setChecked(false);
                    }
                });
                builder.show();
            }
        });
    }

}
