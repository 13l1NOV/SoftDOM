package SoftParser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ISMappedByteBufferByRAF extends RandomAccessInputStream { // 15-17 seconds	
	private FileChannel fc;
	private MappedByteBuffer bb;

	@SuppressWarnings("resource")
	public ISMappedByteBufferByRAF(String path) throws IOException {
		fc = new RandomAccessFile(path, "r").getChannel();
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

