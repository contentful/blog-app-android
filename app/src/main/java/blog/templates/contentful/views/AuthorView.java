package blog.templates.contentful.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import blog.templates.contentful.R;
import blog.templates.contentful.dto.Author;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;

/** Author View. */
public class AuthorView extends LinearLayout {
  @InjectView(R.id.photo) ImageView photo;
  @InjectView(R.id.name) TextView name;
  @InjectView(R.id.bio) TextView bio;

  public AuthorView(Context context) {
    super(context);
    init();
  }

  private void init() {
    View.inflate(getContext(), R.layout.view_author, this);
    ButterKnife.inject(this);
  }

  public void populate(Author author) {
    Picasso.with(getContext()).load(author.profilePhoto()).fit().centerInside().into(photo);
    name.setText(author.name());
    bio.setText(author.bio());
  }
}
