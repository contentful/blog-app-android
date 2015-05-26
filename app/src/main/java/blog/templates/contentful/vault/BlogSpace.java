package blog.templates.contentful.vault;

import blog.templates.contentful.lib.Const;
import com.contentful.vault.Space;

@Space(value = Const.SPACE_ID, models = { Author.class, Category.class, Post.class })
public class BlogSpace {
}
