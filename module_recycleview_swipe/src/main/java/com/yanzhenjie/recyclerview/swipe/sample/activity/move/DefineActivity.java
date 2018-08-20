/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.recyclerview.swipe.sample.activity.move;

import java.util.Collections;
import java.util.List;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.sample.R;
import com.yanzhenjie.recyclerview.swipe.sample.adapter.MainAdapter;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMovementListener;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * <p>
 * 自定义拖拽规则的。
 * </p>
 * Created by Yan Zhenjie on 2016/8/3.
 */
public class DefineActivity extends BaseDragActivity {

    private MainAdapter mAdapter;
    private List<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = getAdapter();
        mDataList = getDataList();

        // 设置Item移动参数回调监听。
        getRecyclerView().setOnItemMovementListener(mItemMovementListener);
    }


    @Override
    protected void addHeaderFooter(final SwipeMenuRecyclerView recyclerView) {
        View header = getLayoutInflater().inflate(R.layout.layout_header_switch_recycleview_swipe, recyclerView, false);
        recyclerView.addHeaderView(header);

        SwitchCompat switchCompat = (SwitchCompat) header.findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 控制是否可以侧滑删除。
                recyclerView.setItemViewSwipeEnabled(isChecked);
            }
        });
    }


    @Override
    protected OnItemMoveListener getItemMoveListener() {
        // 监听拖拽和侧滑删除，更新UI和数据源。
        return onItemMoveListener;
    }

    /**
     * Item移动参数回调监听，这里自定义Item怎样移动。
     */
    public static OnItemMovementListener mItemMovementListener = new OnItemMovementListener() {
        @Override
        public int onDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
            // 假如让第一个不能拖拽。
            if (targetViewHolder.getAdapterPosition() == 0) {
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                // Grid可以上下左右拖拽。
                return OnItemMovementListener.LEFT |
                        OnItemMovementListener.UP |
                        OnItemMovementListener.RIGHT |
                        OnItemMovementListener.DOWN;
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

                // 横向List只能左右拖拽。
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return (OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT);
                }
                // 竖向List只能上下拖拽。
                else {
                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN;
                }
            }
            return OnItemMovementListener.INVALID;// 返回无效的方向。
        }

        @Override
        public int onSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
            // 假如让第一个不能滑动删除。
            if (targetViewHolder.getAdapterPosition() == 0) {
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                // 横向Grid上下侧滑。
                if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                // 竖向Grid左右侧滑。
                else {
                    return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                // 横向List上下侧滑。
                if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN;
                }
                // 竖向List左右侧滑。
                else {
                    return OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT;
                }
            }
            return OnItemMovementListener.INVALID;// 其它均返回无效的方向。
        }
    };

    /**
     * 监听拖拽和侧滑删除，更新UI和数据源。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            // TODO 如何让不同的ViewType之间可以拖拽，就是去掉这个判断。
            // if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) return false;

            int fromPosition = srcHolder.getAdapterPosition();
            int toPosition = targetHolder.getAdapterPosition();

            if (toPosition == 0) {// 保证第一个不被挤走。
                return false;
            }
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(mDataList, i, i + 1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(mDataList, i, i - 1);

            mAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
        }

        @Override
        public void onItemDismiss(int position) {
            mDataList.remove(position);
            mAdapter.notifyItemRemoved(position);
            Toast.makeText(DefineActivity.this, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
        }
    };
}