package blog.contentful.adapters;

import android.view.View;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import blog.contentful.R;
import blog.contentful.vault.Author;
import blog.contentful.vault.Post;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostListAdapter extends AbsListAdapter<Post, PostListAdapter.ViewHolder> {
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd, MMM yyyy");

  @Override protected void bindView(ViewHolder holder, Post post, View rootView) {
    holder.title.setText(post.title());
    holder.date.setText(getBottomText(post));
  }

  @Override protected ViewHolder createViewHolder(View rootView) {
    return new ViewHolder(rootView);
  }

  @Override protected int getLayoutResId() {
    return R.layout.list_item_post;
  }

  private String getBottomText(Post item) {
    StringBuilder sb = new StringBuilder();
    if (item.date() != null) {
      String date = ISODateTimeFormat.dateOptionalTimeParser()
          .parseDateTime(item.date())
          .toString(DATE_FORMATTER);

      sb.append(date);
    }

    List<Author> authors = item.authors();
    if (authors != null) {
      if (sb.length() > 0) {
        sb.append(". ");
      }

      sb.append("by ").append(authors.get(0).name());
    }

    return sb.toString();
  }

  static class ViewHolder {
    @BindView(R.id.title) TextView title;

    @BindView(R.id.date) TextView date;

    ViewHolder(View rootView) {
      ButterKnife.bind(this, rootView);
    }
  }
}
