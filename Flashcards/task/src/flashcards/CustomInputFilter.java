package flashcards;

import java.io.*;

public class CustomInputFilter extends FilterInputStream {
    private InputStream in;
    private PrintStream out;

    CustomInputFilter(InputStream in, PrintStream out) {
        super(in);
        this.out = out;
    }
    @Override
    public int read() throws IOException {
        int chr = super.read();
        out.write(chr);
        System.out.println((char)chr);
        return chr;
    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        out.write(b, off, bytesRead);
        return bytesRead;
    }
}
