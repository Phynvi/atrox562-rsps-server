package com.rs.io;

import java.math.BigInteger;

import com.rs.utils.Utils;

public final class OutputStream extends Stream {

	private static final int[] BIT_MASK = new int[32];
	private int opcodeStart = 0;

	static {
		for (int i = 0; i < 32; i++)
			BIT_MASK[i] = (1 << i) - 1;
	}

	public OutputStream(int capacity) {
		setBuffer(new byte[capacity]);
	}

	public OutputStream() {
		setBuffer(new byte[16]);
	}

	public OutputStream(byte[] buffer) {
		this.setBuffer(buffer);
		this.offset = buffer.length;
		length = buffer.length;
	}

	public void checkCapacityPosition(int position) {
		if (position >= getBuffer().length) {
			byte[] newBuffer = new byte[position + 16];
			System.arraycopy(getBuffer(), 0, newBuffer, 0, getBuffer().length);
			setBuffer(newBuffer);
		}
	}

	public void skip(int length) {
		setOffset(getOffset() + length);
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void writeBytes(byte[] b, int offset, int length) {
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void writeBytes(byte[] b) {
		int offset = 0;
		int length = b.length;
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void writeByte(int i) {
		writeByte(i, offset++);
	}

	private void writeByte(int i, int position) {
		checkCapacityPosition(position);
		getBuffer()[position] = (byte) i;
	}

	public void writeByte128(int i) {
		writeByte(i + 128);
	}

	public void writeByteC(int i) {
		writeByte(-i);
	}

	public void write128Byte(int i) {
		writeByte(128 - i);
	}

	public void writeShortLE128(int i) {
		writeByte(i + 128);
		writeByte(i >> 8);
	}

	public void writeShort128(int i) {
		writeByte(i >> 8);
		writeByte(i + 128);
	}

	public void writeSmart(int i) {
		if (i >= 128) {
			writeShort(i + 32768);
		} else {
			writeByte(i);
		}
	}

	public void writeShort(int i) {
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeShortLE(int i) {
		writeByte(i);
		writeByte(i >> 8);
	}

	public void write3Bytes(int i) {
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeInt(int i) {
		writeByte(i >> 24);
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeIntV1(int i) {
		writeByte(i >> 8);
		writeByte(i);
		writeByte(i >> 24);
		writeByte(i >> 16);
	}

	public void writeIntV2(int i) {
		writeByte(i >> 16);
		writeByte(i >> 24);
		writeByte(i);
		writeByte(i >> 8);
	}

	public void writeIntLE(int i) {
		writeByte(i);
		writeByte(i >> 8);
		writeByte(i >> 16);
		writeByte(i >> 24);
	}

	public void writeLong(long l) {
		writeByte((int) (l >> 56));
		writeByte((int) (l >> 48));
		writeByte((int) (l >> 40));
		writeByte((int) (l >> 32));
		writeByte((int) (l >> 24));
		writeByte((int) (l >> 16));
		writeByte((int) (l >> 8));
		writeByte((int) l);
	}

	public void writePSmarts(int i) {
		if (i < 128) {
			writeByte(i);
			return;
		}
		if (i < 32768) {
			writeShort(32768 + i);
			return;
		} else {
			System.out.println("Error psmarts out of range:");
			return;
		}
	}

	public void writeString(String s) {
		checkCapacityPosition(getOffset() + s.length() + 1);
		System.arraycopy(s.getBytes(), 0, getBuffer(), getOffset(), s.length());
		setOffset(getOffset() + s.length());
		writeByte(0);
	}

	public void writePacket(int id) {
		writeByte(id);
	}

	public void writePacketVarByte(int id) {
		writePacket(id);
		writeByte(0);
		opcodeStart = getOffset() - 1;
	}

	public void writePacketVarShort(int id) {
		writePacket(id);
		writeShort(0);
		opcodeStart = getOffset() - 2;
	}

	public void endPacketVarByte() {
		writeByte(getOffset() - (opcodeStart + 2) + 1, opcodeStart);
	}

	public void endPacketVarShort() {
		int size = getOffset() - (opcodeStart + 2);
		writeByte(size >> 8, opcodeStart++);
		writeByte(size, opcodeStart);
	}

	public void initBitAccess() {
		bitPosition = getOffset() * 8;
	}

	public void finishBitAccess() {
		setOffset((bitPosition + 7) / 8);
	}

	public void writeBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			checkCapacityPosition(bytePos);
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos++] |= value >> numBits - bitOffset & BIT_MASK[bitOffset];
			numBits -= bitOffset;
		}
		checkCapacityPosition(bytePos);
		if (numBits == bitOffset) {
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos] |= value & BIT_MASK[bitOffset];
		} else {
			getBuffer()[bytePos] &= ~(BIT_MASK[numBits] << bitOffset - numBits);
			getBuffer()[bytePos] |= (value & BIT_MASK[numBits]) << bitOffset - numBits;
		}
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	@Override
	public short readShortl() {
		return (short) ((short) ((buffer[offset++] & 0xff) << 8) | (short) (buffer[offset++] & 0xff));
	}

}