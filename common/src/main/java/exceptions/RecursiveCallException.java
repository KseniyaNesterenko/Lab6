package exceptions;

/**
 * Uсключение, выбрасываемое когда script совершает рекурсивный вызов или вызов по циклу.
 */
public class RecursiveCallException extends RuntimeException {

    public RecursiveCallException() {

    }
}
