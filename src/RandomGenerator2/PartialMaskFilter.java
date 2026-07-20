package RandomGenerator2;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static RandomGenerator2.RT2.initStr;

public class PartialMaskFilter extends DocumentFilter {
    //初始是“1”
    private StringBuilder realText = new StringBuilder(initStr);

    public String getRealText() {
        return realText.toString();
    }
    //从第三位开始换成“*”
    private String mask(String s) {
        if (s.length() <= 2) {
            return s;
        }
        //从第三位开始换成“*”
        return s.substring(0, 2) + "*".repeat(s.length() - 2);
    }

    private void updateDocument(FilterBypass fb) throws BadLocationException {
        String display = mask(realText.toString());
        //调用父类DocumentFilter的replace(重写的)
        super.replace(fb, 0, fb.getDocument().getLength(), display, null);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (length > 0) {
            realText.delete(offset, offset + length);
        }
        if (text != null) {
            realText.insert(offset, text);
        }
        updateDocument(fb);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (length > 0) {
            realText.delete(offset, offset + length);
        }
        updateDocument(fb);
    }
}
