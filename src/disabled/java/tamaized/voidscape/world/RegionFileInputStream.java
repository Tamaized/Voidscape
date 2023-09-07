package tamaized.voidscape.world;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionBitmap;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class RegionFileInputStream implements AutoCloseable {
	private static final Logger LOGGER = LogManager.getLogger();
	private final ExtendedByteArrayInputStream file;
	private final IntBuffer offsets;

	RegionFileInputStream(InputStream path_) throws IOException {
		ByteBuffer header = ByteBuffer.allocateDirect(8192);
		this.offsets = header.asIntBuffer();
		this.offsets.limit(1024);
		header.position(4096);
		this.file = ExtendedByteArrayInputStream.fromStream(path_);
		RegionBitmap usedSectors = new RegionBitmap();
		usedSectors.force(0, 2);
		header.position(0);
		int i = this.file.read(header, 0L);
		if (i != -1) {
			if (i != 8192) {
				LOGGER.warn("Region file {} has truncated header: {}", path_, i);
			}

			long j = file.size();

			for (int k = 0; k < 1024; ++k) {
				int l = this.offsets.get(k);
				if (l != 0) {
					int i1 = getSectorNumber(l);
					int j1 = getNumSectors(l);
					if (i1 < 2) {
						LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", path_, k, i1);
						this.offsets.put(k, 0);
					} else if (j1 == 0) {
						LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", path_, k);
						this.offsets.put(k, 0);
					} else if ((long) i1 * 4096L > j) {
						LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", path_, k, i1);
						this.offsets.put(k, 0);
					} else {
						usedSectors.force(i1, j1);
					}
				}
			}
		}


	}

	private static ByteArrayInputStream createStream(ByteBuffer byteBuffer_, int int_) {
		return new ByteArrayInputStream(byteBuffer_.array(), byteBuffer_.position(), int_);
	}

	private static int getNumSectors(int int_) {
		return int_ & 255;
	}

	private static int getSectorNumber(int int_) {
		return int_ >> 8 & 16777215;
	}

	private static int getOffsetIndex(ChunkPos chunkPos_) {
		return chunkPos_.getRegionLocalX() + chunkPos_.getRegionLocalZ() * 32;
	}

	@Nullable
	synchronized DataInputStream getChunkDataInputStream(ChunkPos pos) throws IOException {
		int i = this.getOffset(pos);
		if (i == 0) {
			return null;
		} else {
			int j = getSectorNumber(i);
			int k = getNumSectors(i);
			int l = k * 4096;
			ByteBuffer bytebuffer = ByteBuffer.allocate(l);
			this.file.read(bytebuffer, j * 4096);
			bytebuffer.flip();
			if (bytebuffer.remaining() < 5) {
				LOGGER.error("Chunk {} header is truncated: expected {} but read {}", pos, l, bytebuffer.remaining());
				return null;
			} else {
				int i1 = bytebuffer.getInt();
				byte b0 = bytebuffer.get();
				if (i1 == 0) {
					LOGGER.warn("Chunk {} is allocated, but stream is missing", pos);
					return null;
				} else {
					int j1 = i1 - 1;
					if (j1 > bytebuffer.remaining()) {
						LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", pos, j1, bytebuffer.remaining());
						return null;
					} else if (j1 < 0) {
						LOGGER.error("Declared size {} of chunk {} is negative", i1, pos);
						return null;
					} else {
						return this.createChunkInputStream(pos, b0, createStream(bytebuffer, j1));
					}
				}
			}
		}
	}

	@Nullable
	private DataInputStream createChunkInputStream(ChunkPos chunkPos_, byte byte_, InputStream inputStream_) throws IOException {
		RegionFileVersion regionfileversion = RegionFileVersion.fromId(byte_);
		if (regionfileversion == null) {
			LOGGER.error("Chunk {} has invalid chunk stream version {}", chunkPos_, byte_);
			return null;
		} else {
			return new DataInputStream(new BufferedInputStream(regionfileversion.wrap(inputStream_)));
		}
	}

	private int getOffset(ChunkPos chunkPos_) {
		return this.offsets.get(getOffsetIndex(chunkPos_));
	}

	@Override
	public void close() throws IOException {
		this.file.close();
	}

	static class ExtendedByteArrayInputStream extends ByteArrayInputStream {

		private ExtendedByteArrayInputStream(byte[] b) {
			super(b);
		}

		static ExtendedByteArrayInputStream fromStream(InputStream is) throws IOException {
			byte[] buff = new byte[8192];
			int bytesRead;
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			while ((bytesRead = is.read(buff)) != -1)
				bao.write(buff, 0, bytesRead);
			return new ExtendedByteArrayInputStream(bao.toByteArray());
		}

		int read(ByteBuffer buffer, long offset) {
			int r = -1;
			long cap = buffer.capacity();
			long len = buf.length - offset;
			long lim = Math.min(cap, len);
			buffer.clear();
			for (int i = 0; i < lim; i++) {
				buffer.put(buf[Math.toIntExact(offset + i)]);
				if (r == -1)
					r = 0;
				r++;
			}
			return r;
		}

		public long size() {
			return buf.length;
		}
	}
}
