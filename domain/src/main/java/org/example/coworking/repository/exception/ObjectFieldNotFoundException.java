package org.example.coworking.repository.exception;

/**
 * Exception thrown when a required field of an Object is not found.
 */
public class ObjectFieldNotFoundException extends RuntimeException {

    /**
     * Constructs a new ObjectFieldNotFoundException with the specified message.
     *
     * @param message The detail message explaining the missing object field.
     */
    public ObjectFieldNotFoundException(String message) {
        super(message);
    }
}
