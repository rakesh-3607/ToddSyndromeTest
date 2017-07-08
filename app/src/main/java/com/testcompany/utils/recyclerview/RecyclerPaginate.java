package com.testcompany.utils.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testcompany.R;

public final class RecyclerPaginate extends Paginate {

    private final RecyclerView recyclerView;
    private final Paginate.Callbacks callbacks;
    private final int loadingTriggerThreshold;
    private WrapperAdapter wrapperAdapter;
    private WrapperSpanSizeLookup wrapperSpanSizeLookup;

    RecyclerPaginate(RecyclerView recyclerView,
                     Paginate.Callbacks callbacks,
                     int loadingTriggerThreshold,
                     boolean addLoadingListItem,
                     LoadingListItemCreator loadingListItemCreator,
                     LoadingListItemSpanLookup loadingListItemSpanLookup) {
        this.recyclerView = recyclerView;
        this.callbacks = callbacks;
        this.loadingTriggerThreshold = loadingTriggerThreshold;

        // Attach scrolling listener in order to perform end offset check on each scroll event
        recyclerView.addOnScrollListener(mOnScrollListener);

        if (addLoadingListItem) {
            // Wrap existing adapter with new adapter that will add loading row
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            wrapperAdapter = new WrapperAdapter(adapter, loadingListItemCreator);
            adapter.registerAdapterDataObserver(mDataObserver);
            recyclerView.setAdapter(wrapperAdapter);

            // For GridLayoutManager use separate/customisable span lookup for loading row
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                wrapperSpanSizeLookup = new WrapperSpanSizeLookup(
                        ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanSizeLookup(),
                        loadingListItemSpanLookup,
                        wrapperAdapter);
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(wrapperSpanSizeLookup);
            }
        }

        // Trigger initial check since adapter might not have any items initially so no scrolling events upon
        // RecyclerView (that triggers check) will occur
        checkEndOffset();
    }

    @Override
    public void setHasMoreDataToLoad(boolean hasMoreDataToLoad) {
        if (wrapperAdapter != null) {
            wrapperAdapter.displayLoadingRow(hasMoreDataToLoad);
        }
    }

    @Override
    public void unbind() {
        recyclerView.removeOnScrollListener(mOnScrollListener);   // Remove scroll listener
        if (recyclerView.getAdapter() instanceof WrapperAdapter) {
            WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
            RecyclerView.Adapter adapter = wrapperAdapter.getWrappedAdapter();
            adapter.unregisterAdapterDataObserver(mDataObserver); // Remove data observer
            recyclerView.setAdapter(adapter);                     // Swap back original adapter
        }
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager && wrapperSpanSizeLookup != null) {
            // Swap back original SpanSizeLookup
            GridLayoutManager.SpanSizeLookup spanSizeLookup = wrapperSpanSizeLookup.getWrappedSpanSizeLookup();
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);
        }
    }

    void checkEndOffset() {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();

        int firstVisibleItemPosition;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            // https://code.google.com/p/android/issues/detail?id=181461
            if (recyclerView.getLayoutManager().getChildCount() > 0) {
                firstVisibleItemPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
            } else {
                firstVisibleItemPosition = 0;
            }
        } else {
            throw new IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager");
        }

        // Check if end of the list is reached (counting threshold) or if there is no items at all
        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + loadingTriggerThreshold)
                || totalItemCount == 0) {
            // Call load more only if loading is not currently in progress and if there is more items to load
            if (!callbacks.isLoading() && !callbacks.hasLoadedAllItems()) {
                callbacks.onLoadMore();
            }else{
                if (callbacks.stopLoading())
                stopLoading();
            }
        }
    }

    private void onAdapterDataChanged() {
        wrapperAdapter.displayLoadingRow(!callbacks.hasLoadedAllItems());
        checkEndOffset();
    }

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            checkEndOffset(); // Each time when list is scrolled check if end of the list is reached
        }
    };

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            wrapperAdapter.notifyDataSetChanged();
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            onAdapterDataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
            onAdapterDataChanged();
        }
    };

    public static class Builder {

        private final RecyclerView recyclerView;
        private final Paginate.Callbacks callbacks;

        private int loadingTriggerThreshold = 5;
        private boolean addLoadingListItem = true;
        private LoadingListItemCreator loadingListItemCreator;
        private LoadingListItemSpanLookup loadingListItemSpanLookup;

        public Builder(RecyclerView recyclerView, Paginate.Callbacks callbacks) {
            this.recyclerView = recyclerView;
            this.callbacks = callbacks;
        }

        /**
         * Set the offset from the end of the list at which the load more event needs to be triggered. Default offset
         * if 5.
         *
         * @param threshold number of items from the end of the list.
         */
        public Builder setLoadingTriggerThreshold(int threshold) {
            this.loadingTriggerThreshold = threshold;
            return this;
        }

        /**
         * Setup loading row. If loading row is used original adapter set on RecyclerView will be wrapped with
         * internal adapter that will add loading row as the last item in the list. Paginate will observer the
         * changes upon original adapter and remove loading row if there is no more data to load. By default loading
         * row will be added.
         *
         * @param addLoadingListItem true if loading row needs to be added, false otherwise.
         * @see {@link Paginate.Callbacks#hasLoadedAllItems()}
         */
        public Builder addLoadingListItem(boolean addLoadingListItem) {
            this.addLoadingListItem = addLoadingListItem;
            return this;
        }

        /**
         * Set custom loading list item creator. If no creator is set default one will be used.
         *
         * @param creator Creator that will ne called for inflating and binding loading list item.
         */
        public Builder setLoadingListItemCreator(LoadingListItemCreator creator) {
            this.loadingListItemCreator = creator;
            return this;
        }

        /**
         * Set custom SpanSizeLookup for loading list item. Use this when {@link GridLayoutManager} is used and
         * loading list item needs to have custom span. Full span of {@link GridLayoutManager} is used by default.
         *
         * @param loadingListItemSpanLookup LoadingListItemSpanLookup that will be called for loading list item span.
         */
        public Builder setLoadingListItemSpanSizeLookup(LoadingListItemSpanLookup loadingListItemSpanLookup) {
            this.loadingListItemSpanLookup = loadingListItemSpanLookup;
            return this;
        }

        /**
         * Create pagination functionality upon RecyclerView.
         *
         * @return {@link Paginate} instance.
         */
        public Paginate build() {
            if (recyclerView.getAdapter() == null) {
                throw new IllegalStateException("Adapter needs to be set!");
            }
            if (recyclerView.getLayoutManager() == null) {
                throw new IllegalStateException("LayoutManager needs to be set on the RecyclerView");
            }

            if (loadingListItemCreator == null) {
                loadingListItemCreator = LoadingListItemCreator.DEFAULT;
            }

            if (loadingListItemSpanLookup == null) {
                loadingListItemSpanLookup = new DefaultLoadingListItemSpanLookup(recyclerView.getLayoutManager());
            }

            return new RecyclerPaginate(recyclerView, callbacks, loadingTriggerThreshold, addLoadingListItem,
                    loadingListItemCreator, loadingListItemSpanLookup);
        }
    }


    /**
     * Loading view span will be calculated by according to layout manager loading list item
     */
    private static final class DefaultLoadingListItemSpanLookup implements LoadingListItemSpanLookup {

        private final int loadingListItemSpan;

        public DefaultLoadingListItemSpanLookup(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager instanceof GridLayoutManager) {
                // By default full span will be used for loading list item
                loadingListItemSpan = ((GridLayoutManager) layoutManager).getSpanCount();
            } else {
                loadingListItemSpan = 1;
            }
        }

        @Override
        public int getSpanSize() {
            return loadingListItemSpan;
        }
    }

    /**
     * RecyclerView creator that will be called to create and bind loading list item
     */
    public interface LoadingListItemCreator {

        /**
         * Create new loading list item {@link RecyclerView.ViewHolder}.
         *
         * @param parent   parent ViewGroup.
         * @param viewType type of the loading list item.
         * @return ViewHolder that will be used as loading list item.
         */
        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        /**
         * Bind the loading list item.
         *
         * @param holder   loading list item ViewHolder.
         * @param position loading list item position.
         */
        void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

        LoadingListItemCreator DEFAULT = new LoadingListItemCreator() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, parent, false);
                //ProgressWheel progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);
                //progressBar.setBarColor(0x50d2c2);
                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                // No binding for default loading row
            }
        };
    }

    /**
     * SpanSizeLookup that will be used to determine the span of loading list item.
     */
    public interface LoadingListItemSpanLookup {

        /**
         * @return the span of loading list item.
         */
        int getSpanSize();
    }

    private class WrapperSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private final GridLayoutManager.SpanSizeLookup wrappedSpanSizeLookup;
        private final LoadingListItemSpanLookup loadingListItemSpanLookup;
        private final WrapperAdapter wrapperAdapter;

        public WrapperSpanSizeLookup(GridLayoutManager.SpanSizeLookup gridSpanSizeLookup,
                                     LoadingListItemSpanLookup loadingListItemSpanLookup,
                                     WrapperAdapter wrapperAdapter) {
            this.wrappedSpanSizeLookup = gridSpanSizeLookup;
            this.loadingListItemSpanLookup = loadingListItemSpanLookup;
            this.wrapperAdapter = wrapperAdapter;
        }

        @Override
        public int getSpanSize(int position) {
            if (wrapperAdapter.isLoadingRow(position)) {
                return loadingListItemSpanLookup.getSpanSize();
            } else {
                return wrappedSpanSizeLookup.getSpanSize(position);
            }
        }

        public GridLayoutManager.SpanSizeLookup getWrappedSpanSizeLookup() {
            return wrappedSpanSizeLookup;
        }
    }

    public void stopLoading(){
        if (callbacks != null){
            wrapperAdapter.displayLoadingRow(false);
        }
    }
}