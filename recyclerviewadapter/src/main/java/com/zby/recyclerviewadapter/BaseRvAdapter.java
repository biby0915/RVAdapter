package com.zby.recyclerviewadapter;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zby.recyclerviewadapter.listener.OnItemChildClickListener;
import com.zby.recyclerviewadapter.listener.OnItemClickListener;
import com.zby.recyclerviewadapter.listener.OnItemLongClickListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuBingYang
 * @date 2019-07-24
 */
public abstract class BaseRvAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mDataList;
    protected int mLayoutResId;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Map<Integer, OnItemChildClickListener> mOnItemChildClickListeners;

    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;

    private static final int TYPE_HEADER = 0x1001;
    private static final int TYPE_FOOTER = 0x1002;
    private static final int TYPE_EMPTY = 0x1003;

    public BaseRvAdapter(int layoutId, List<T> dataList) {
        mLayoutResId = layoutId;
        mDataList = dataList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = null;
        switch (viewType) {
            case TYPE_HEADER:
                holder = createViewHolder(mHeaderView);
                break;
            case TYPE_FOOTER:
                holder = createViewHolder(mFooterView);
                break;
            case TYPE_EMPTY:
                holder = createViewHolder(mEmptyView);
                break;
            default:
                holder = createCustomHolder(parent, viewType);
                bindClick(holder);

        }
        return holder;
    }

    private void bindClick(final VH holder) {
        if (holder == null) {
            return;
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(BaseRvAdapter.this, v, holder.getAdapterPosition() - getHeaderLayoutCount());
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(BaseRvAdapter.this, v, holder.getAdapterPosition() - getHeaderLayoutCount());
                }
            });
        }

        if (mOnItemChildClickListeners != null) {
            for (final Map.Entry<Integer, OnItemChildClickListener> entry : mOnItemChildClickListeners.entrySet()) {
                holder.getView(entry.getKey()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        entry.getValue().onItemClick(BaseRvAdapter.this, v, holder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int offset = getHeaderLayoutCount();
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        } else if (mDataList.isEmpty() && offset == position) {
            return TYPE_EMPTY;
        } else if (position < mDataList.size() + offset) {
            return 1;
        } else {
            return TYPE_FOOTER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case TYPE_HEADER:
                break;
            case TYPE_FOOTER:
                break;
            case TYPE_EMPTY:
                break;
            default:
                convert(holder, getItem(position), position);
                break;
        }

    }

    public abstract void convert(VH holder, T data, int position);

    @Override
    public int getItemCount() {
        int count = 0;
        if (mHeaderView != null) {
            count++;
        }

        if (mFooterView != null) {
            count++;
        }
        if (mDataList.isEmpty()) {
            if (mEmptyView != null) {
                count++;
            }
        } else {
            count += mDataList.size();
        }
        return count;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public T getItem(int position) {
        if (position >= 0 && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    private VH createCustomHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;

        return createViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @SuppressWarnings("unchecked")
    protected VH createViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (VH) new ViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (VH) new ViewHolder(view);
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (ViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && ViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    final public int getHeaderLayoutCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public void setHeaderView(View view) {
        mHeaderView = view;
    }

    public void setFooterView(View view) {
        mFooterView = view;
    }

    public void setEmptyLayout(View view) {
        this.mEmptyView = view;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @SuppressLint("UseSparseArrays")
    public void addOnItemChildClickListener(@IdRes int viewId, OnItemChildClickListener listener) {
        if (mOnItemChildClickListeners == null) {
            mOnItemChildClickListeners = new HashMap<>();
        }

        mOnItemChildClickListeners.put(viewId, listener);
    }
}
