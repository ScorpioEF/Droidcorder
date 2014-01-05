package bjtu.group6.droidcorder.model;

public class AudioFileInfo {

	private long audioId;
	private String fileName;
	private String createTime;
	private String fileSize;
	private String filePath;
	private String duration;

	public long getAudioId() {
		return audioId;
	}
	public void setAudioId(long audioId) {
		this.audioId = audioId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
}
