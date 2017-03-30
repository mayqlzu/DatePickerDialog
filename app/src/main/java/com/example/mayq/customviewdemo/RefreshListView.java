package com.example.mayq.customviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by mayq on 2016/7/26.
 * <p/>
 * 一个自定义ListView，支持下拉刷新和自动加载下一页，
 * UI比较简陋，但是原理都涉及到了
 */
public class RefreshListView extends ListView {
    private boolean noMore = false;
    private boolean enableLoadMore = true;

    private View header;
    private ImageView ivHeader;
    private TextView tvHeader;

    private View footer;
    private TextView tvFooter;

    private float yDown;
    private boolean firstItemAtTop = true;//内容的第一行处于top位置
    private int MIN_PADDING_TOP;
    private static final int RATIO = 2; // pull hard

    private static final int HEADER_ORIGINAL = 0;
    private static final int HEADER_PULLING_SHORT = 1;
    private static final int HEADER_PULLING_LONG = 2;
    private static final int HEADER_REFRESHING = 3;
    private int headerState = HEADER_ORIGINAL;

    private static final int FOOTER_GONE = 0;
    private static final int FOOTER_MORE = 1;
    private static final int FOOTER_LOADING = 2;
    private static final int FOOTER_NO_MORE = 3;
    private int footerState = FOOTER_GONE;

    private OnRefreshListener listener;

    public RefreshListView(Context context, final AttributeSet attrs) {
        super(context, attrs);

        //public static View inflate(Context context, int resource, ViewGroup root) {
        header = inflate(context, R.layout.header, null);
        MIN_PADDING_TOP = -getHeaderHeight();
        //public void setPadding(int left, int top, int right, int bottom) {
        header.setPadding(0, MIN_PADDING_TOP, 0, 0);

        addHeaderView(header);

        ivHeader = (ImageView) header.findViewById(R.id.iv);
        tvHeader = (TextView) header.findViewById(R.id.tv);

        setOnScrollListener(new OnScrollListener() {
            private int preLast;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItemAtTop = (0 == firstVisibleItem);
                //Log.d("mayq", "firstItemAtTop=" + firstItemAtTop);

                if (!enableLoadMore || noMore || null == listener)
                    return;

                int topRowVerticalPosition = (getChildCount() == 0) ? 0 : getChildAt(0).getTop();
                int lastItem = firstVisibleItem + visibleItemCount;

                /*
                 * 滚动到最后一行的时候自动加载下一页
                 * topRowVerticalPosition < 0 是为了避免第一页不满一屏的情况下自动触发加载第二页，
                 * totalItemCount包含了footer，所以用 >1 判断有无item
				 */
                if (topRowVerticalPosition < 0 && totalItemCount > 1 && lastItem == totalItemCount) {
                    // to avoid multiple calls for last item
                    if (preLast != lastItem) {
                        if (FOOTER_LOADING != footerState) {
                            footerState = FOOTER_LOADING;
                            updateFooter();
                            listener.loadMore();
                        }
                    }
                }

                preLast = lastItem;
            }
        });

        //set footer
        footer = inflate(context, R.layout.footer, null);
        tvFooter = (TextView) footer.findViewById(R.id.tv);
        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FOOTER_LOADING != footerState) {
                    footerState = FOOTER_LOADING;
                    updateFooter();
                    listener.loadMore();
                }
            }
        });

        addFooterView(footer);
        footerState = enableLoadMore ? FOOTER_MORE : FOOTER_GONE;
        updateFooter();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (HEADER_REFRESHING == headerState)
                    break;

                float yNow = ev.getY();
                float yDiff = yNow - yDown;

                int paddingTopNow = MIN_PADDING_TOP + (int) yDiff / RATIO;
                if (firstItemAtTop) {
                    if (paddingTopNow > MIN_PADDING_TOP)
                        setSelection(0); //scroll to top, or scroll faster than finger

                    header.setPadding(0, paddingTopNow, 0, 0);
                    // update text
                    if (paddingTopNow < 0) {
                        headerState = HEADER_PULLING_SHORT;
                        tvHeader.setText("下拉刷新");
                    } else {
                        headerState = HEADER_PULLING_LONG;
                        tvHeader.setText("松开刷新");
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                // restore immediately, looks goods even without animation
                if (HEADER_PULLING_LONG == headerState) {
                    header.setPadding(0, 0, 0, 0);
                    headerState = HEADER_REFRESHING;
                    tvHeader.setText("正在刷新");
                    if (null != listener) {
                        listener.onRefresh();
                    }
                } else if (HEADER_PULLING_SHORT == headerState) {
                    headerState = HEADER_ORIGINAL;
                    header.setPadding(0, MIN_PADDING_TOP, 0, 0);
                    tvHeader.setText("下拉刷新");
                } else {
                    //
                }

                break;
            default:
                break;

        }

        boolean result = super.onTouchEvent(ev);
        return result;
    }

    private int getHeaderHeight() {
        int widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        //protected void measureChild(View child, int parentWidthMeasureSpec,
        //int parentHeightMeasureSpec) {
        header.measure(widthSpec, heightSpec);
        int height = header.getMeasuredHeight();
        return height;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void loadMore();
    }

    public void finishRefresh() {
        headerState = HEADER_ORIGINAL;
        header.setPadding(0, MIN_PADDING_TOP, 0, 0);
    }

    public void finishLoadMore(boolean hasMore) {
        if (hasMore) {
            footerState = FOOTER_MORE;
        } else {
            footerState = FOOTER_NO_MORE;
            noMore = true;
        }
        updateFooter();
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        listener = l;
    }

    public void setEnableLoadMore(boolean enable) {
        enableLoadMore = enable;
        footerState = FOOTER_GONE;
        updateFooter();
    }

    private void updateFooter() {
        switch (footerState) {
            case FOOTER_GONE:
                // TODO why still occupy space ?
                //footer.setVisibility(View.GONE);
                removeFooterView(footer);
                break;
            case FOOTER_MORE:
                footer.setVisibility(View.VISIBLE);
                tvFooter.setText("更多");
                break;
            case FOOTER_NO_MORE:
                footer.setVisibility(View.VISIBLE);
                tvFooter.setText("全部加载完毕");
                break;
            case FOOTER_LOADING:
                footer.setVisibility(View.VISIBLE);
                tvFooter.setText("正在加载下一页");
                break;
        }

    }


}
