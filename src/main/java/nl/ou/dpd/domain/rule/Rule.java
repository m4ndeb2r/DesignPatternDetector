package nl.ou.dpd.domain.rule;

/**
 * A {@link Rule} is part of a {@link Condition}. A {@link Rule} evaluates subjects (object of type {@link SUBJECT_TYPE}
 * using a number of attributes. These attributes are the following:
 * <ul>
 * <li>a {@code mould} - a blueprint/template containing the properties an ideal subject should have</li>
 * <li>a {@code scope} - the {@link Scope} of the evaluation, i.e. object, relation, ...</li>
 * <li>a {@code topic} - the {@link Topic} to be evaluated by this {@link Rule}, i.e. type, visibility, ...</li>
 * <li>a {@code operation} - the evaluation {@link Operation}, i.e. equals, not_equals, exists, not_exists, ...</li>
 * </ul>
 *
 * @param <SUBJECT_TYPE> the mould/subject type. This is the type of object this {@link Rule} applies to. Note that the
 *                       {@code mould} (the blueprint, or template, for this {@link Rule} is of the same type.
 * @author Martin de Boer
 * @see Condition
 */
public abstract class Rule<SUBJECT_TYPE> {

    private final SUBJECT_TYPE mould;
    private final Scope scope;
    private final Topic topic;
    private final Operation operation;

    /**
     * Creates a rule based on the specified arguments.
     *
     * @param mould    an object of type {@link SUBJECT_TYPE}, containing all the features this {@link Rule} may use to
     *                 evaluate a subject will be applied to
     * @param scope    the scope of the evaluation (object, relation, ...)
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param operation the evaluation operation (equals, exists, ...)
     */
    public Rule(SUBJECT_TYPE mould, Scope scope, Topic topic, Operation operation) {
        if (mould == null || scope == null || topic == null || operation == null) {
            throw new IllegalArgumentException("All arguments are mandatory.");
        }
        this.mould = mould;
        this.scope = scope;
        this.topic = topic;
        this.operation = operation;
    }

    /**
     * Applies this rule to the specified subject.
     *
     * @param subject the subject to apply this {@link Rule} to.
     * @return {@code true} if this {@link Rule} applies to the specified {@code subject}, or {@code false} otherwise.
     */
    public abstract boolean process(SUBJECT_TYPE subject);

    /**
     * Returns the mould for the subject to evaluate.
     *
     * @return the mould (blueprint) of this {@link Rule}
     */
    public SUBJECT_TYPE getMould() {
        return mould;
    }

    /**
     * Returns the scope of the evaluation by this {@link Rule}.
     *
     * @return the {@link Scope} of this {@link Rule}
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns the topic that is evaluated by this {@link Rule}.
     *
     * @return the {@link Topic} of this {@link Rule}
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Returns the evaluation operation used by this {@link Rule}.
     *
     * @return the {@link Operation} this {@link Rule} is using when processing the evaluation.
     */
    public Operation getOperation() {
        return operation;
    }
}