package flashcards;

import java.io.IOException;
import java.io.InputStream;

public class DoubleBufferInputStream extends InputStream {
    private byte[]      buffer = null;
    private byte[]      buffer2 = null;
    private int         writeIndex = 0;
    private int         readIndex  = 0;
    private InputStream source = null;

    public DoubleBufferInputStream(byte[] buffer) {
        System.out.println("NEW DOUBLE BUFFER INPUT STREAM");
        this.buffer = buffer;
        this.buffer2 = buffer.clone();
    }
    public DoubleBufferInputStream setSource(InputStream source){
        this.source = source;
        this.writeIndex = 0;
        this.readIndex  = 0;
        return this;
    }
    @Override
    public int read() throws IOException {
        if(readIndex == writeIndex) {
            if(writeIndex == buffer.length) {
                writeIndex = 0;
                readIndex  = 0;
            }
            //data should be read into buffer.
            int bytesRead = readBytesIntoBuffer();
            while(bytesRead == 0) {
                //continue until you actually get some bytes !
                bytesRead = readBytesIntoBuffer();
            }
            System.out.println("Bytes Read: " + bytesRead);
            //if no more data could be read in, return -1;
            if(bytesRead == -1) {
                return -1;
            }
        }
        return 255 & this.buffer[readIndex++];
    }
    private int readBytesIntoBuffer() throws IOException {
        int bytesRead = this.source.read(this.buffer, this.writeIndex, this.buffer.length - this.writeIndex);
        writeIndex += bytesRead;
        return bytesRead;
    }
    @Override
    public void close() throws IOException {
        System.out.println("CLOSE");
        this.source.close();
    }
}
