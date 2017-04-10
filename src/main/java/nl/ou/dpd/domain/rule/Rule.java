package nl.ou.dpd.domain.rule;

/**
 * A {@link Rule} is part of a {@link Condition}. A {@link Rule} evaluates subjects (object of type {@link SUBJECT_TYPE}
 * using a number of attributes. These attributes are the following:
 * <ul>
 * <li>a {@code mould} - a blueprint/template containing the properties an ideal subject should have</li>
 * <li>a {@code topic} - the {@link Topic} to be evaluated by this {@link Rule}, i.e. type, visibility, ...</li>
 * <li>a {@code scope} - the {@link Scope} of the evaluation, i.e. object, relation, ...</li>
 * <li>a {@code operator} - the evaluation {@link Operator}, i.e. equals, not_equals, exists, not_exists, ...</li>
 * </ul>
 *
 * @param <SUBJECT_TYPE> the mould/subject type. This is the type of object this {@link Rule} applies to. Note that the
 *                       {@code mould} (the blueprint, or template, for this {@link Rule} is of the same type.
 * @author Martin de Boer
 * @see Condition
 */
public abstract class Rule<SUBJECT_TYPE> {

    private final SUBJECT_TYPE mould;
    private final Topic topic;
    private final Scope scope;
    private final Operator operator;

    /**
     * Creates a rule based on the specified arguments.
     *
     * @param mould    an object of type {@link SUBJECT_TYPE}, containing all the features this {@link Rule} may use to
     *                 evaluate a subject will be applied to
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param scope    the scope of the evaluation (object, relation, ...)
     * @param operator the evaluation operator (equals, exists, ...)
     */
    public Rule(SUBJECT_TYPE mould, Topic topic, Scope scope, Operator operator) {
        if (mould == null || topic == null || scope == null || operator == null) {
            throw new IllegalArgumentException("All arguments are mandatory.");
        }
        this.mould = mould;
        this.topic = topic;
        this.scope = scope;
        this.operator = operator;
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
     * Returns the topic that is evaluated by this {@link Rule}.
     *
     * @return the {@link Topic} of this {@link Rule}
     */
    protected Topic getTopic() {
        return topic;
    }

    /**
     * Returns the scope of the evaluation by this {@link Rule}.
     *
     * @return the {@link Scope} of this {@link Rule}
     */
    protected Scope getScope() {
        return scope;
    }

    /**
     * Returns the evaluation operator used by this {@link Rule}.
     *
     * @return the {@link Operator} this {@link Rule} is using when processing the evaluation.
     */
    protected Operator getOperator() {
        return operator;
    }
}