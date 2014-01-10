package dk.dtu.sb.compiler;

/**
 * Compiler-exception to use when an error occurs in a compiler implementing
 * {@link AbstractCompiler}.
 */
public class CompilerException extends Exception {

    private static final long serialVersionUID = 1L;

    public CompilerException(String message) {
        super(message);
    }

    public CompilerException(String message, Throwable t) {
        super(message, t);
    }

    public CompilerException(Throwable t) {
        super(t);
    }
}
