package saxy;

import java.io.IOException;

/**
 * Similar to XHTMLEmitter, but strips extraneous whitespace.
 *
 * @author Cedric Chantepie
 */
public class StripXHTMLEmitter extends XHTMLEmitter {

    /**
     * {@inheritDoc}
     */
    public void writeCharSequence(final CharSequence s) throws IOException {
        if (s.length() == 1 && Character.isWhitespace(s.charAt(0))) {
            return; // Skip whitespace only sequence
        } // end of if

        super.writeCharSequence(s);
    } // end of writeCharSequence

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
} // end of class StripXHTMLEmitter
