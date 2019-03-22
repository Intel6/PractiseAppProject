package com.example.android.taskbosstwo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

abstract public class SwipeToDeleteCallBack extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint; //DON~T KNOW WHAT IT DOES
    private ColorDrawable mBackground; //Color of backgroud after swipe started
    private int backgroundColour; //int value of colour?
    private Drawable deleteDrawable; // ????
    private int intrinsicWidth;
    private int intrinsicHeight;

    //Constructor 1

    SwipeToDeleteCallBack(Context context){
        mContext = context; //Activity context
        mBackground = new ColorDrawable(); //Set to colorDrawble object
        backgroundColour = Color.parseColor("#b80f0a");
        mClearPaint = new Paint(); //Paint object ??
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext,R.drawable.ic_delete);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth(); //Get height and width of delete icon
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

    }



    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0 , ItemTouchHelper.LEFT);
    }

    //No movement needed
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


        //Get 1 card view object and the card height
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean ifCancelled = dX == 0 && !isCurrentlyActive;

        if (ifCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        //Set childDraw color
        mBackground.setColor(backgroundColour);
        //Set bounds of where the delete colour will appear
        //Get views each side. DON't know what +dX added to right does
        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                itemView.getRight(), itemView.getBottom());
        mBackground.draw(c); //Creates the element?

        //Icon Position for setting bounds
        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
       //Method
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    //Return swipe to 70% check
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    // Will implement in main
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
