package SoftParser;

import java.io.IOException;
import java.io.InputStream;

public abstract class RandomAccessInputStream extends InputStream {
	public abstract void seek(int pos) throws IOException;
	
	@Override
	public void close() throws IOException {
		super.close();
	}
}