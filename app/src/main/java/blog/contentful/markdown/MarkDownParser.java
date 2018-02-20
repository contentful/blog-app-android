package blog.contentful.markdown;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Block;
import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkDownParser {
  private final Parser parser = Parser.builder().build();

  private final AbstractVisitor simplifyInlineMarkdown = new AbstractVisitor() {
    @Override public void visit(Paragraph paragraph) {
      super.visit(paragraph);

      final Block parent = paragraph.getParent();
      if (parent instanceof Document) {
        if (parent.getFirstChild() == paragraph && parent.getLastChild() == paragraph) {
          while (paragraph.getFirstChild() != null) {
            paragraph.insertBefore(paragraph.getFirstChild());
          }
          paragraph.unlink();
        }
      }
    }
  };

  private final HtmlRenderer renderer = HtmlRenderer.builder().build();

  /**
   * Parse a given simple text and return html notation of it.
   *
   * @param toParse the string containing Markdown markup.
   * @return a String containing html representation of input, or null on error.
   */
  public String parse(String toParse) {
    if (toParse == null) {
      return null;
    }

    final Node node = parser.parse(toParse);
    node.accept(simplifyInlineMarkdown);

    return renderer.render(node);
  }
}
