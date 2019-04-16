package com.rs.cache;

import com.rs.io.InputStream;
import com.rs.utils.Utils;

public final class CacheFileManager {

	private CacheFile cacheFile;
	private ContainersInformation information;
	private boolean discardFilesData;

	private byte[][][] filesData;

	public CacheFileManager(CacheFile cacheFile, boolean discardFilesData) {
		this.cacheFile = cacheFile;
		this.discardFilesData = discardFilesData;
		byte[] informContainerPackedData = Cache.getConstainersInformCacheFile()
				.getContainerData(cacheFile.getIndexFileId());
		if (informContainerPackedData == null)
			return;
		information = new ContainersInformation(informContainerPackedData);
		resetFilesData();
	}

	public CacheFile getCacheFile() {
		return cacheFile;
	}

	public int getContainersSize() {
		return information.getContainers().length;
	}

	public int getFilesSize(int containerId) {
		if (!validContainer(containerId))
			return -1;
		return information.getContainers()[containerId].getFiles().length;
	}

	public void resetFilesData() {
		filesData = new byte[information.getContainers().length][][];
	}

	public boolean validFile(int containerId, int fileId) {
		if (!validContainer(containerId))
			return false;
		if (fileId < 0 || information.getContainers()[containerId].getFiles().length <= fileId)
			return false;
		return true;

	}

	public boolean validContainer(int containerId) {
		if (containerId < 0 || information.getContainers().length <= containerId)
			return false;
		return true;
	}

	public int[] getFileIds(int containerId) {
		if (!validContainer(containerId))
			return null;
		return information.getContainers()[containerId].getFilesIndexes();
	}

	public int getContainerId(String containerName) {
		if (containerName == null)
			return -1;
		int hash = Utils.getNameHash(containerName);
		for (int containerIndex = 0; containerIndex < information.getContainersIndexes().length; containerIndex++) {
			if (information.getContainers()[information.getContainersIndexes()[containerIndex]].getNameHash() == hash)
				return information.getContainersIndexes()[containerIndex];
		}
		return -1;
	}

	public byte[] getFileData(int containerId, int fileId) {
		return getFileData(containerId, fileId, null);
	}

	public boolean loadFilesData(int containerId, int[] container_keys) {
		byte[] data = cacheFile.getContainerUnpackedData(containerId, container_keys);
		if (data == null)
			return false;
		if (filesData[containerId] == null) {
			if (information.getContainers()[containerId] == null)
				return false; // container inform doesnt exist anymore
			filesData[containerId] = new byte[information.getContainers()[containerId].getFiles().length][];
		}
		if (information.getContainers()[containerId].getFilesIndexes().length == 1) {
			int fileId = information.getContainers()[containerId].getFilesIndexes()[0];
			filesData[containerId][fileId] = data;
		} else {
			int readPosition = data.length;
			int amtOfLoops = data[--readPosition] & 0xff;
			readPosition -= amtOfLoops * (information.getContainers()[containerId].getFilesIndexes().length * 4);
			InputStream stream = new InputStream(data);
			int filesSize[] = new int[information.getContainers()[containerId].getFilesIndexes().length];
			stream.setOffset(readPosition);
			for (int loop = 0; loop < amtOfLoops; loop++) {
				int offset = 0;
				for (int fileIndex = 0; fileIndex < information.getContainers()[containerId]
						.getFilesIndexes().length; fileIndex++)
					filesSize[fileIndex] += offset += stream.readInt();
			}
			byte[][] filesBufferData = new byte[information.getContainers()[containerId].getFilesIndexes().length][];
			for (int fileIndex = 0; fileIndex < information.getContainers()[containerId]
					.getFilesIndexes().length; fileIndex++) {
				filesBufferData[fileIndex] = new byte[filesSize[fileIndex]];
				filesSize[fileIndex] = 0;
			}
			stream.setOffset(readPosition);
			int sourceOffset = 0;
			for (int loop = 0; loop < amtOfLoops; loop++) {
				int dataRead = 0;
				for (int fileIndex = 0; fileIndex < information.getContainers()[containerId]
						.getFilesIndexes().length; fileIndex++) {
					dataRead += stream.readInt();
					System.arraycopy(data, sourceOffset, filesBufferData[fileIndex], filesSize[fileIndex], dataRead);
					sourceOffset += dataRead;
					filesSize[fileIndex] += dataRead;
				}
			}
			for (int fileIndex = 0; fileIndex < information.getContainers()[containerId]
					.getFilesIndexes().length; fileIndex++)
				filesData[containerId][information.getContainers()[containerId]
						.getFilesIndexes()[fileIndex]] = filesBufferData[fileIndex];
		}
		return true;

	}

	public byte[] getFileData(int containerId, int fileId, int[] container_keys) {
		if (!validFile(containerId, fileId))
			return null;
		if (filesData[containerId] == null || filesData[containerId][fileId] == null) {
			if (!loadFilesData(containerId, container_keys))
				return null;
		}
		byte[] data = filesData[containerId][fileId];
		if (discardFilesData) {
			if (filesData[containerId].length == 1)
				filesData[containerId] = null;
			else
				filesData[containerId][fileId] = null;

		}
		return data;

	}

	public ContainersInformation getInformation() {
		return information;
	}
}
