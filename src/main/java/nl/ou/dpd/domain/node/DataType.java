package nl.ou.dpd.domain.node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Represents a data type node in a system design. A data type can be a String, Integer, UnlimitedInteger or Boolean.
 * All other types are represented by {@link Clazz}es or {@link Interface}s.
 * <p>
 * For the allowed id's and their meaning, see: <a href="http://argouml.tigris.org//profiles/uml14/default-uml14.xmi" />
 *
 * @author Martin de Boer
 * @see Clazz
 * @see Interface
 */
public class DataType extends Node {

    private static final Logger LOGGER = LogManager.getLogger(DataType.class);

    /**
     * Contains the allowed data types (id's and names).
     */
    public enum Allowed {
        DATATYPE_INTEGER("-84-17--56-5-43645a83:11466542d86:-8000:000000000000087C", "Integer"),
        DATATYPE_UNLIMITED_INTEGER("-84-17--56-5-43645a83:11466542d86:-8000:000000000000087D", "UnlimitedInteger"),
        DATATYPE_STRING("-84-17--56-5-43645a83:11466542d86:-8000:000000000000087E", "String"),
        DATATYPE_DOUBLE("-84-26-0-54--1e9ba376:15aad4320f4:-8000:000000000000086E", "Double"),
        DATATYPE_BOOLEAN("-84-17--56-5-43645a83:11466542d86:-8000:0000000000000880", "Boolean");

        final private String id;
        final private String name;

        private Allowed(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static boolean containsId(String id) {
            return Arrays.stream(Allowed.values())
                    .filter(allowed -> allowed.getId().equals(id))
                    .findFirst()
                    .orElse(null) != null;
        }
    }

    /**
     * Constructs a {@link DataType} instance, based on the specified {@code id}.
     *
     * @param id a unique id for this {@link DataType}
     */
    public DataType(String id) {
        this(id, null, null);
    }

    /**
     * Constructs a {@link DataType} instance with the specified parameters. The input parameters are validated using
     * the {@link Allowed} enum.
     *
     * @param id         a unique id for this {@link DataType}
     * @param visibility the {@link Visibility} of this {@link DataType}
     * @param isAbstract {@code true} is this {@link DataType} is abstract, {@code false} if not, or {@code null} if
     *                   undefined
     */
    public DataType(String id, Visibility visibility, Boolean isAbstract) {
        super(getValidId(id), getNameForId(id), NodeType.DATA_TYPE, visibility, null, isAbstract);
    }

    private static String getValidId(String id) {
        if (Allowed.containsId(id)) {
            return id;
        }
        return handleUnexpectedIdError(id);
    }

    private static String handleUnexpectedIdError(String id) {
        final StringBuilder builder = new StringBuilder(String.format("Unexpected DataType id: '%s'.\nAllowed ids are:", id));
        appendAllowedIds(builder);
        final String msg = builder.toString();

        LOGGER.error(msg);
        throw new IllegalArgumentException(msg);
    }

    private static void appendAllowedIds(StringBuilder builder) {
        for (Allowed allowed : Allowed.values()) {
            builder.append(String.format("\n\t'%s'", allowed.getId()));
        }
    }

    private static String getNameForId(String id) {
        return Arrays.stream(Allowed.values())
                .filter(allowed -> allowed.getId().equals(getValidId(id)))
                .findFirst()
                .orElse(null)
                .getName();
    }

}
