package ch.e7n.metronome.parser;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.e7n.metronome.CustomKeyboard;
import ch.e7n.metronome.Smiley;
import ch.e7n.metronome.SmileyLibrary;
import ch.e7n.metronome.SmileySpan;

/**
 * Created by robertre on 9/2/14.
 */
public class Parser {
    private String s;
    int pos;

    public Parser(String s) {
        this.s = s;
    }

    private char peek() throws ParserException {
        if(! finished()) {
            return s.charAt(pos);
        } else {
            throw new ParserException("Cannot peek(): no more characters. Was parsing: '" + s + "'.");
        }
    }

    private void consume() throws ParserException {
        if(finished()) {
            throw new ParserException("Cannot consume(): no more characters");
        }
        pos++;
        skipWhitespace();
    }

    private void skipWhitespace() {
        try {
            while(!finished() && peek() == ' ') {
                pos++;
            }
        } catch (ParserException e) {
            // Should not happen
            throw new RuntimeException(e);
        }
    }

    private boolean finished() {
        return pos >= s.length();
    }

    public MusicObject parse() throws ParserException {
        pos = 0;
        skipWhitespace();
        List<MusicObject> result = new ArrayList<MusicObject>();

        try {
            while (!finished()) {
                result.add(parseRhythm());
            }
        } catch (ParserException e) {
            // Log.e("parse()", e.getMessage(), e);
            throw e;
        }
        return new Sequence(result);
    }

    public SpannableString parseToString(Resources r) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        try {
            pos = 0;
            Smiley s;
            while (!finished()) {
                switch (peek()) {
                    case 't':
                    case 'n':
                    case 'r':
                        String str = ""+peek();
                        consume();
                        int numerator = readNumber();
                        expectSlash();
                        int denominator = readNumber();
                        str += numerator + "/" + denominator;
                        Smiley smiley = SmileyLibrary.smileys.getFromString(str);
                        if(smiley != null) {
                            appendSmiley(ssb, smiley, r);
                        } else {
                            ssb.append(str);
                        }
                        break;
                    case '~':
                        consume();
                        appendSmiley(ssb, SmileyLibrary.smileys.getFromString("~"), r);
                        break;
                    default:
                        ssb.append(peek());
                        consume();
                }
            }
            return new SpannableString(ssb);
        } catch (ParserException e) {
            return new SpannableString(s);
        }
    }

    private MusicObject parseRhythm() throws ParserException {
        switch(peek()) {
            case 't':
                consume();
                return parseTimeSignature();
            case 'n':
                consume();
                return parseNote();
            case 'r':
                consume();
                return parseRest();
            case '(':
                consume();
                return parseGroup();
            case '{':
                consume();
                return parseTwoHands();
            default:
                throw new ParserException("unexpected character '" + peek() + "' at position " + pos);
        }
    }

    private boolean isDigit() throws ParserException {
        return (peek() >= '0' && peek() <= '9');
    }

    private void expectSlash() throws ParserException {
        expect('/');
    }

    private void expect(char e) throws ParserException {
        char c = peek();
        if(c != e) {
            throw new ParserException("Expected '"+e+"' at position " + pos + " found '" + c + "'.");
        }
        consume();
    }

    private boolean isTimes() throws ParserException {
        return peek() == '*';
    }

    private boolean isTilde() throws ParserException {
        return peek() == '~';
    }

    private boolean isCloseGroup() throws ParserException {
        return peek() == ')';
    }

    private int readNumber() throws ParserException {
        StringBuilder sb = new StringBuilder();
        while (!finished() && isDigit()) {
            sb.append(peek());
            consume();
        }
        try {
            return Integer.parseInt(sb.toString());
        } catch(NumberFormatException e) {
            throw new ParserException("Could not parse integer at position " + pos + ": " + e.getMessage());
        }
    }

    private MusicObject parseRest() throws ParserException {
        int numerator = readNumber();
        expectSlash();
        int denominator = readNumber();
        Rest n = new Rest(numerator, denominator);
        while(!finished() && isTilde()) {
            consume();
            expect('r');
            numerator = readNumber();
            expectSlash();
            denominator = readNumber();
            n = n.extend(new Rest(numerator, denominator));
        }
        if(!finished() && isTimes()) {
            consume();
            int times = readNumber();
            return new Repeat(n, times);
        } else {
            return n;
        }
    }

    private MusicObject parseNote() throws ParserException {
        int numerator = readNumber();
        expectSlash();
        int denominator = readNumber();
        Note n = new Note(numerator, denominator);
        while(!finished() && isTilde()) {
            consume();
            expect('n');
            numerator = readNumber();
            expectSlash();
            denominator = readNumber();
            n = n.extend(new Note(numerator, denominator));
        }
        if(!finished() && isTimes()) {
            consume();
            int times = readNumber();
            return new Repeat(n, times);
        } else {
            return n;
        }
    }

    private MusicObject parseTimeSignature() throws ParserException {
        int numerator = readNumber();
        expectSlash();
        int denominator = readNumber();
        return new TimeSignature(numerator, denominator);
    }

    private MusicObject parseTwoHands() throws ParserException {
        throw new ParserException("Two hands not implemented yet");
    }


    private MusicObject parseGroup() throws ParserException {
        List<MusicObject> result = new ArrayList<MusicObject>();

        while (true) {
            skipWhitespace();
            if(isCloseGroup()) {
                consume();
                MusicObject seq = new Sequence(result);
                if(!finished() && isTimes()) {
                    consume();
                    int times = readNumber();
                    return new Repeat(seq, times);
                } else {
                    return seq;
                }
            } else {
                result.add(parseRhythm());
            }
        }
    }

    private void appendSmiley(SpannableStringBuilder ssb, Smiley smiley, Resources r) {
        String text = "X";
        // Create the ImageSpan
        Drawable drawable = r.getDrawable(smiley.getDrawableId());
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new SmileySpan(drawable, ImageSpan.ALIGN_BASELINE, smiley);

        ssb.append(text);
        ssb.setSpan(span, ssb.length()-1, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
