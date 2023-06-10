package Benchmarks;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import SoftParser.RandomAccessInputStream;

public class ISMappedByteBufferByFIS extends RandomAccessInputStream {
	private FileChannel fc;
	private ByteBuffer bb;
	private byte[] cache;
	private int index;

	@SuppressWarnings("resource")
	public ISMappedByteBufferByFIS(String path) throws IOException {
		fc = new FileInputStream(path).getChannel();
		bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		cache = new byte[1024];
	}

	@Override
	public int read() throws IOException {
		if(index >= cache.length) {
			if(bb.position() + index >= bb.limit())
			{
				return -1;
			}
			if(cache.length > bb.limit() - bb.position()) {
				cache = new byte[bb.limit() - bb.position()];
			}
			index = 0;
			bb.get(cache);
		}
		return cache[index++];
	}
	
	public void seek(int pos) throws IOException {
		bb.position(pos);
		index = cache.length;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		fc.close();
	}
}
 /*
public class ISMappedByteBufferByFIS extends RandomAccessInputStream {
	private FileChannel fc;
	private ByteBuffer bb;

	public ISMappedByteBufferByFIS(String path) throws IOException {
		fc = new FileInputStream(path).getChannel();
		bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	}

	@Override
	public int read() throws IOException {
		if(bb.position() == bb.limit()) {
			return -1;
		}
		return bb.get();
	}
	
	public void seek(int pos) throws IOException {
		bb.position(pos);
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		fc.close();
	}
}
*/