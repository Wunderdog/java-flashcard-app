package flashcards;

import java.io.*;

public class TeePrintStream extends PrintStream {
    private final PrintStream second;
    private final InputStream input;
    private final InputStreamReader inReader;

    public TeePrintStream(OutputStream out, PrintStream second, InputStream input) {
        super(out);
        this.second = second;
        this.input = input;
        this.inReader = new InputStreamReader(input);
    }
    public InputStream getIn() {
        return input;
    }
    /*
    * Closes the main stream.
    * The second stream is just flushed but <b>not</b> closed.
    * @see java.io.PrintStream#close()
    * */
    @Override
    public void close() {
        super.close();
    }

    @Override
    public void flush() {
        super.flush();
        second.flush();
    }
    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        second.write(buf, off, len);
        while (true) {
            try {
                if (!inReader.ready()) break;
                System.out.println("READY 1");
                super.write(inReader.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void write(int b) {
        super.write(b);
        second.write(b);
        while (true) {
            try {
                if (!inReader.ready()) break;
                System.out.println("READY 2");

                super.write(inReader.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        second.write(b);
        while (true) {
            try {
                if (!inReader.ready()) break;
                System.out.println("READY 3");
                super.write(inReader.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
