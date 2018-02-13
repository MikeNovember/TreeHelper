public class StructureException extends Exception {

	public enum ExceptionCause {
		CHILD_ID_TOO_BIG,
		CHILD_ID_TOO_SMALL,
		NOT_ENOUGTH_CHILD_NODES,
		CHILD_NODE_NOT_SET;
	}
	
	private final ExceptionCause cause;
	
	public StructureException( ExceptionCause cause ) {
		this.cause = cause;
	}
	
	public String getExceptionCause() {
		return cause.name();
	}
	
	private static final long serialVersionUID = -8956155073563936385L;
}
