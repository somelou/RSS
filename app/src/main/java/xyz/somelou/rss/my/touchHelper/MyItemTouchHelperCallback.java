package xyz.somelou.rss.my.touchHelper;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

import xyz.somelou.rss.my.myGroup.MyGroupRecyclerAdapter;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback  {

    private MyGroupRecyclerAdapter myAdapter;
    private List<String> datas;

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public void setMyAdapter(MyGroupRecyclerAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    public MyItemTouchHelperCallback(MyGroupRecyclerAdapter myAdapter){
        this.myAdapter = myAdapter;
    }

    //Callback回调监听时先调用的，用来判断当前是什么动作，比如判断方向（监听哪个方向的拖动）
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //要监听的拖拽方向，不监听为0

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //要监听的侧滑方向，不监听为0
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int flags = makeMovementFlags(dragFlags, 0);
        return flags;//即监听向上也监听向下

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();
        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();
        if(!myAdapter.isPinnedPosition(fromPosition)&&!myAdapter.isPinnedPosition(toPosition)){
            System.out.println("进入");
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(datas, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(datas, i, i - 1);
                }
            }
            myAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }else if(!myAdapter.isPinnedPosition(fromPosition)&&myAdapter.isPinnedPosition(toPosition)){
            System.out.println("进入clear");
            datas.remove(toPosition);
            myAdapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
    }


}
