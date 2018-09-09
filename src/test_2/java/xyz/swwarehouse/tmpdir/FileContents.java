package xyz.swwarehouse.tmpdir;

public enum FileContents {

	NOMAL_CONTENT("content");
	
	private String str;
	
	FileContents(final String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}
}
