package blog.templates.contentful.adapters;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class AbsListAdapter<T, VH> extends BaseAdapter {
  protected List<T> data;

  @SuppressWarnings("unchecked")
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    VH holder;
    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(getLayoutResId(), parent, false);

      holder = createViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (VH) convertView.getTag();
    }

    bindView(holder, getItem(position), convertView);

    return convertView;
  }

  /**
   * Called once view was inflated/recycled and needs to be set for display.
   *
   * @param holder view holder
   * @param t adapter item
   * @param rootView root view
   */
  protected abstract void bindView(VH holder, T t, View rootView);

  /**
   * Called after a view was inflated, create and return new view holder for the
   * given {@code rootView}.
   *
   * @param rootView root view
   * @return view holder
   */
  protected abstract VH createViewHolder(View rootView);

  @LayoutRes
  protected abstract int getLayoutResId();

  /**
   * Set the data for this adapter.
   *
   * @param data data
   */
  public void setData(List<T> data) {
    this.data = data;
  }

  @Override public int getCount() {
    if (data == null) {
      return 0;
    }

    return data.size();
  }

  @Override public T getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

}
