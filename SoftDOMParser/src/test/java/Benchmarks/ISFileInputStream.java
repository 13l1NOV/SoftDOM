package Benchmarks;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import SoftParser.RandomAccessInputStream;

public class ISFileInputStream extends RandomAccessInputStream {
	private FileInputStream fis;
	private ByteBuffer buffer;
	private FileChannel fileChannel;
	private int prefpos;
	private int index;

	public ISFileInputStream(String path) throws IOException {
		fis = new FileInputStream(path);
		buffer = ByteBuffer.allocate(1024*10);
		buffer.position(buffer.limit()); // cacheSize
		fileChannel = fis.getChannel();
	}

	@Override
	public int read() throws IOException {
		if( buffer.capacity() <= index++) {
			setCahce();
		}
		return buffer.get();
	} 

	private void setCahce() throws IOException {
		buffer.position(0);
		prefpos+=buffer.capacity();
		fileChannel.read(buffer, prefpos);
		buffer.position(0);
		index = 0;
	}
	
	public void seek(int pos) throws IOException {
		prefpos = pos;
		buffer.position(0);
		fileChannel.read(buffer, pos);
		buffer.position(0);
		index=0;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		fis.close();
	}
}