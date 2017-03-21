package nl.ou.dpd.data.parser;

/**
 * A {@link Parser} provides functionality for parsing any input file(s).
 *
 * @author Martin de Boer
 */
public interface Parser<T> {

    /**
     * Parses the specified file, and returns a representation of type {@link T} of the file content.
     *
     * @param filename the full name of the file to parse
     * @return the parsed content of the file.
     */
    T parse(String filename);

}
