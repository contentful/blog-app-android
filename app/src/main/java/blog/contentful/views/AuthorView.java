package blog.contentful.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import blog.contentful.R;
import blog.contentful.vault.Author;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.contentful.vault.Asset;
import com.squareup.picasso.Picasso;

public class AuthorView extends LinearLayout {
  @BindView(R.id.photo) ImageView photo;

  @BindView(R.id.name) TextView name;

  @BindView(R.id.bio) TextView bio;

  public AuthorView(Context context) {
    super(context);
    init();
  }

  private void init() {
    View.inflate(getContext(), R.layout.view_author, this);
    ButterKnife.bind(this);
  }

  public void populate(Author author) {
    Asset asset = author.profilePhoto();
    if (asset != null) {
      Picasso.with(getContext()).load(asset.url()).fit().centerInside().into(photo);
    }
    name.setText(author.name());
    bio.setText(author.biography());
  }
}
