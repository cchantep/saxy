package saxy;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import net.sf.saxon.trans.XPathException;

/**
 * Custom XHTML(5) emitter.
 *
 * @author Cedric Chantepie
 */
public class XHTMLEmitter extends net.sf.saxon.event.XHTMLEmitter {
    // --- Shared ---

    static final List<String> selfClosing = Arrays.
        asList(new String[] {
                "link", "meta", "hr", "br", "wbr", "img", "param", 
                "embed", "video", "audio", "source", "track", "area", 
                "rect", "input", "button"
            });

    // --- Properties ---

    /**
     * Character buffer
     */
    final StringBuffer buff = new StringBuffer();

    /**
     * {@inheritDoc}
     */
    protected void openDocument() throws XPathException {
        super.openDocument();

        if ("html".equals(outputProperties.get("doctype-public"))) {
            try {
                writer.write("<!DOCTYPE html>");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } // end of catch
        } // end of if
    } // end of openDocument

    /**
     * Emit tag close, either by self closing, or separate closing tag.
     */
    protected String emptyElementTagCloser(final String tag, final int code) {
        if (selfClosing.contains(tag)) {
            return "/>";
        } else {
            return "></" + tag + '>';
        } // end of else
    } // end of emptyElementTagCloser

    /**
     * Do not emit comment, except if started with '[' and ended with ']'
     * (browser conditionals).
     */
    public void comment(final CharSequence comment, 
                        final int locationId, 
                        final int properties) 
        throws XPathException {

        final int len = comment.length();

        if (len <= 2 ||
            comment.charAt(0) != '[' || comment.charAt(len-1) != ']') {

            return; // Skip plain comment
        } // end of if

        buff.setLength(0); // clear

        super.comment(process(this.buff, comment), locationId, properties);
    } // end of comment

    /**
     * {@inheritDoc}
     */
    public void characters(final CharSequence chars, 
                           final int locationId, 
                           final int properties) 
        throws XPathException {

        buff.setLength(0); // clear

        super.characters(process(this.buff, chars), locationId, properties);
    } // end of characters

    /**
     * Pre-process |chars| sequence.
     */
    protected String process(final StringBuffer b, final CharSequence chars) {
        final int len = chars.length();

        char last = (char) -1;
        for (int i = 0; i < len; i++) {
            if (Character.isWhitespace(chars.charAt(i))) {
                if (Character.isWhitespace(last)) {
                    continue; // Skip multiple spaces
                } else {
                    b.append(last = ' '); // normalize to plain space
                } // end of else
            } else {
                b.append(last = chars.charAt(i));
            } // end of else
        } // end of for

        return b.toString();
    } // end of process
} // end of class XHTMLEmitter
