package linoleum.html;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.net.MalformedURLException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

public class Document extends HTMLDocument {

	public HTMLEditorKit.ParserCallback getReader(final int pos) {
		final Object desc = getProperty(StreamDescriptionProperty);
		if (desc instanceof URL) { 
			setBase((URL)desc);
		}
		final Reader reader = new Reader(pos);
		return reader;
	}

	public HTMLEditorKit.ParserCallback getReader(final int pos, final int popDepth, final int pushDepth, final HTML.Tag insertTag) {
		final Object desc = getProperty(StreamDescriptionProperty);
		if (desc instanceof URL) { 
			setBase((URL)desc);
		}
		final Reader reader = new Reader(pos, popDepth, pushDepth, insertTag);
		return reader;
	}

	public Map<String, URL> getFrames() {
		final Map<String, URL> map = new HashMap<>();
		final ElementIterator it = new ElementIterator(this);
		Element next = null;

		while ((next = it.next()) != null) {
			final AttributeSet attr = next.getAttributes();
			if (matchNameAttribute(attr, HTML.Tag.FRAME)) {
				final String target = (String)attr.getAttribute(HTML.Attribute.NAME);
				try {
					final URL url = new URL(getBase(), (String)attr.getAttribute(HTML.Attribute.SRC));
					map.put(target, url);
				} catch (final MalformedURLException e) {}
			}
		}
		return map;
	}

	private static boolean matchNameAttribute(final AttributeSet attr, final HTML.Tag tag) {
		final Object o = attr.getAttribute(StyleConstants.NameAttribute);
		if (o instanceof HTML.Tag) {
			final HTML.Tag name = (HTML.Tag) o;
			if (name == tag) {
				return true;
			}
		}
		return false;
	}

	public class Reader extends HTMLReader {

		public Reader(final int offset) {
			this(offset, 0, 0, null);
		}

		public Reader(final int offset, final int popDepth, final int pushDepth, final HTML.Tag insertTag) {
			super(offset, popDepth, pushDepth, insertTag);
			registerTag(HTML.Tag.HEAD, new EmptyAction());
		}

		class EmptyAction extends TagAction {

			public void start(final HTML.Tag t, final MutableAttributeSet attr) {}

			public void end(final HTML.Tag t) {}
		}
	}
}
