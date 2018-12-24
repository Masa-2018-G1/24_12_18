package com.sheygam.masa_2018_g1_24_12_18_cw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.my_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, manager.getOrientation());
        adapter = new MyAdapter();
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        rv.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new MyTouchCallBack());
        helper.attachToRecyclerView(rv);


    }

    class MyTouchCallBack extends ItemTouchHelper.Callback {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1 && snackbar != null) {
                    snackbar.dismiss();
                    count = 0;
                    snackbar = null;
                }
                return true;
            }
        });

        int count = 0;
        Snackbar snackbar;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.END | ItemTouchHelper.START);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Log.d("MY_TAG", "onMove: ");
            adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.remove(viewHolder.getAdapterPosition());
            if (snackbar == null) {
                snackbar = Snackbar.make(viewHolder.itemView, "Removed " + (++count), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();
            } else if (snackbar.isShown()) {
                snackbar.setText("Removed " + (++count));
            }

            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, 3000);
        }

        public static final float ALPHA_FULL = 1.0f;

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                Paint p = new Paint();
                Bitmap icon;

                if (dX > 0) {
            /* Note, ApplicationManager is a helper class I created
               myself to get a context outside an Activity class -
               feel free to use your own method */

                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delay);

                    /* Set your color for positive displacement */
                    p.setARGB(255, 255, 0, 0);

                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);

                    // Set the image icon for Right swipe
                    c.drawBitmap(icon,
                            (float) itemView.getLeft() + convertDpToPx(16),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                            p);
                } else {
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_done);

                    /* Set your color for negative displacement */
                    p.setARGB(255, 0, 255, 0);

                    // Draw Rect with varying left side, equal to the item's right side
                    // plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    //Set the image icon for Left swipe
                    c.drawBitmap(icon,
                            (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                            p);
                }

                // Fade out the view as it is swiped out of the parent's bounds
                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);

            } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                if(dY>0) {
                    View itemView = viewHolder.itemView;
                    Paint p = new Paint();
                    p.setARGB(255, 0, 0, 0);
                    c.drawRect((float) itemView.getLeft(),
                            (float)itemView.getTop()-dY,
                            (float)itemView.getRight(),
                            (float)itemView.getBottom(),p);
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }else{
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }

            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        }

        private int convertDpToPx(int dp) {
            return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }

    }
}
