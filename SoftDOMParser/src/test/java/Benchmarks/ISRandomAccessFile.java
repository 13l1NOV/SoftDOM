package Benchmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import SoftParser.RandomAccessInputStream;

public class ISRandomAccessFile extends RandomAccessInputStream {
	private RandomAccessFile raf;
	private byte[] cache;
	private int index;
	private int cacheSize = 1024*10;

	public ISRandomAccessFile(String path) throws IOException {
		raf = new RandomAccessFile(path, "r");
		cache = new byte[cacheSize];
	}

	@Override
	public int read() throws IOException {
		if( cacheSize <= index) {
			setCahce();
		}
		return cache[index++];
	} 

	private void setCahce() throws IOException {
		raf.read(cache);
		prefpos+=cacheSize;
		index = 0;
	}
	private int prefpos;
	
	public void seek(int pos) throws IOException {
		if(prefpos - cacheSize <= pos && pos < prefpos) {
			index = pos - (prefpos - cacheSize);
			return;
		}
		raf.seek(pos);
		prefpos = pos;
		setCahce();
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		raf.close();
	}
}
