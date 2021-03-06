package com.wq.androidtest.activity.system;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wq.androidlibrary.util.ToastUtil;
import com.wq.androidtest.R;
import com.wq.androidtest.activity.base.BaseActivity;
import com.wq.androidtest.base.TestApplication;
import com.wq.androidtest.gson.HotDownloadGson;
import com.wq.androidtest.gson.TopApplistItemGson;
import com.wq.androidtest.request.GsonRequest;
import com.wq.androidtest.temp.LruImageCache;

import java.util.List;

/**
 * Created by wangqi on 15/11/4.
 */
public class RecyleViewGridViewActivity extends BaseActivity {

    RecyclerView mGridView;
    MyAdapter mAdapter;
    View preFocusView;
    View curFocusView;

    View borderView;
    FrameLayout rootView;
    AnimatorSet set;
    ViewTreeObserver treeObserver;
    String url = "http://172.16.10.137/api/v1/hotdownload?platform=market&ver=2.5.0&cno=200&&days=30";
    List<TopApplistItemGson> list;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyleviewgridview);
        initViews();
        mAdapter = new MyAdapter();
        mGridView.setClickable(true);
        mGridView.setFocusableInTouchMode(true);
        mGridView.setFocusable(true);
        mGridView.addItemDecoration(new DividerGridItemDecoration2(this));
        mGridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        mGridView.setAdapter(mAdapter);
        treeObserver = rootView.getViewTreeObserver();
        treeObserver.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                preFocusView = oldFocus;
                curFocusView = newFocus;
                moveFocusView();
            }
        });

        GsonRequest<HotDownloadGson> hotDLRequest = new GsonRequest<HotDownloadGson>(url,
                HotDownloadGson.class, new Response.Listener<HotDownloadGson>() {

            @Override
            public void onResponse(HotDownloadGson response) {
                list = response.getTopapplist();
                mAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        TestApplication.getsInstance().requestQueue.add(hotDLRequest);

    }

    private void initViews() {
        rootView = (FrameLayout) findViewById(R.id.root_view);
        mGridView = (HorizontalGridView) findViewById(R.id.gridview);
        borderView = LayoutInflater.from(this).inflate(R.layout.item_border, rootView).findViewById(R.id.border);
    }


    private void moveFocusView() {
        if (preFocusView == null || curFocusView == null || !(preFocusView instanceof LinearLayout) || !(curFocusView instanceof LinearLayout)) {
            return;
        }

        float preX = preFocusView.getLeft();
        float preY = preFocusView.getTop();
        float curX = curFocusView.getLeft();
        float curY = curFocusView.getTop();

        set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(borderView, "translationX", preX, curX));
        set.playTogether(ObjectAnimator.ofFloat(borderView, "translationY", preY, curY));
        set.setDuration(500).start();
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            final View root = inflater.inflate(R.layout.item_recyleviewgridview_item, null);
            root.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                        v.requestFocus();
                    }
                    int offset = 20;
                    float eventX = v.getLeft() + event.getX();
                    int width = mGridView.getWidth();
                    if (lastX != 0) {
                        float delta = lastX - eventX;
                        if (delta > 5 || delta < -5) {
                            if (eventX + offset > width) {
                                mGridView.smoothScrollBy(100, 0);
                            }
                            if (eventX - offset < 0) {
                                mGridView.smoothScrollBy(-100, 0);
                            }
                        }
                    }

                    lastX = eventX;
                    return false;
                }
            });

            root.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    final Integer pos = (Integer) v.getTag();
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && pos != null && pos > 1) {
                        int left = v.getLeft();
                        if (left == 0) {
                            int idx = pos - 2;
                            mGridView.smoothScrollToPosition(idx);
                            int nextFocusLeftId = v.getNextFocusLeftId();
                            final View viewById = mGridView.findViewById(idx);
                            if (viewById != null) {
                                ToastUtil.showToast(mCtx, "left view not null");
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewById.requestFocus();
                                    }
                                }, 100);
                            }
                        }
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && pos != null && pos < mAdapter.getItemCount() - 2) {
                        int right = v.getRight();
                        int width = v.getWidth();
                        if (right > mGridView.getWidth() - width) {
                            int idx = pos + 2;
                            mGridView.smoothScrollToPosition(idx);
                            final View viewById = mGridView.findViewById(idx);
                            if (viewById != null) {
                                ToastUtil.showToast(mCtx, "right view not null");
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewById.requestFocus();
                                    }
                                }, 100);
                            }
                        }
                    }
                    return false;
                }
            });


            return new ViewHolder(root);
        }

        float lastX;

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TextView textView = (TextView) holder.itemView.findViewById(R.id.name);
            NetworkImageView icon = (NetworkImageView) holder.itemView.findViewById(R.id.icon);
            if (list == null) {
                textView.setText("position" + position);
            } else {
                textView.setText(list.get(position).getTitle());
                LruImageCache lruImageCache = LruImageCache.instance();
                com.android.volley.toolbox.ImageLoader imageLoader = new com.android.volley.toolbox.ImageLoader(TestApplication.getsInstance().requestQueue, lruImageCache);
                icon.setImageUrl(list.get(position).getLogo(), imageLoader);

                holder.itemView.setClickable(true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String idx = "-1";
                        for (int i = 0; i < mAdapter.getItemCount(); i++) {
                            if (v.equals(mGridView.getChildAt(i))) {
                                idx = i + "";
                            }
                        }
                        ToastUtil.showToast(RecyleViewGridViewActivity.this, "pos:" + position + ", child idx " + idx+",view id " + v.getId());
                    }
                });
            }
            holder.itemView.setTag(position);
            holder.itemView.setId(position);

        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 50;
            } else {
                return list.size();
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }

    public static class DividerGridItemDecoration2 extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
        private Drawable mDivider;

        public DividerGridItemDecoration2(Context context) {
            Resources resources = context.getResources();
            mDivider = resources.getDrawable(R.drawable.divider_bg02);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

//            drawHorizontal(c, parent);
//            drawVertical(c, parent);

        }

        private int getSpanCount(RecyclerView parent) {
            // 列数
            int spanCount = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {

                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) layoutManager)
                        .getSpanCount();
            }
            return spanCount;
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin
                        + mDivider.getIntrinsicWidth() + temp;
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight() + temp;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        int temp = 100;

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);

                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin + mDivider.getIntrinsicWidth() + temp;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicWidth() + temp;

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                    int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    {
                        return true;
                    }
                } else {
                    childCount = childCount - childCount % spanCount;
                    if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                        return true;
                }
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                  int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    if (pos >= childCount)
                        return true;
                } else
                // StaggeredGridLayoutManager 且横向滚动
                {
                    // 如果是最后一行，则不需要绘制底部
                    if ((pos + 1) % spanCount == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition,
                                   RecyclerView parent) {
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
            {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
            {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(),
                        mDivider.getIntrinsicHeight());
            }
            int space = 0;
            outRect.set(space, space, space, space);
        }
    }

}
