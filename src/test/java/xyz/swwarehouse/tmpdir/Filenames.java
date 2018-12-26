package xyz.swwarehouse.tmpdir;

public enum Filenames {

	EXTENSION(".txt"), 
	ENGLISH("filename"), 
	KOREAN("한글"), 
	EMPTY(""), 
	SPACE(" "), 
	ONE_DOT("."), 
	TWO_DOT(".."), 
	SLASH("/"), 
	BACK_SLASH("\\"), 
	QUESTION_MARK("?"), 
	PERCENT_SIGN("%"), 
	ASTERISK("*"), 
	COLON(":"), 
	VERTICAL_BAR("|"), 
	DOUBLE_QUOTATION("\""), 
	LEFT_ANGLE_BRACKET("<"), 
	RIGHT_ANGLE_BRACKET(">"), 
	TOO_LONG(
			"filenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilenamefilename");

	private String str;

	Filenames(final String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}
	
	public String incluedExtension() {
		return str + EXTENSION;
	}
}
