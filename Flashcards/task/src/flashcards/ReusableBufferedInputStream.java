package flashcards;

import java.io.IOException;
import java.io.InputStream;

public class ReusableBufferedInputStream extends InputStream {
    private byte[]      buffer = null;
    private int         writeIndex = 0;
    private int         readIndex  = 0;
    private InputStream source = null;

    public ReusableBufferedInputStream(byte[] buffer) {
        this.buffer = buffer;
    }
    public ReusableBufferedInputStream setSource(InputStream source){
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
        this.source.close();
    }
}