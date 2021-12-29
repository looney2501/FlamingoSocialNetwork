package com.map_toysocialnetwork_gui.Repository.RepositoryExceptions;

/**
 * Runtime exception thrown by main.com.map_toysocialnetwork_gui.com.map_toysocialnetwork_gui.Domain.com.map_toysocialnetwork_gui.Repository objects.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException() {
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
