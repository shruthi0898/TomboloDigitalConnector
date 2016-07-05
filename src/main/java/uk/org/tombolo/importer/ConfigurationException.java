package uk.org.tombolo.importer;

/**
 * Thrown if the importer requires configuration, but is not properly configured
 *
 */
public class ConfigurationException extends Exception {
    public ConfigurationException() { }

    public ConfigurationException(String message) {
        super (message);
    }

    public ConfigurationException(Throwable cause) {
        super (cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super (message, cause);
    }
}
