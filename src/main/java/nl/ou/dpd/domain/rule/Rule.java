package nl.ou.dpd.domain.rule;

/**
 * A {@link Rule} is part of a condition. TODO: explain this more extensively.
 *
 * @author Martin de Boer
 */
public interface Rule<SUBJECT_TYPE> {

    /**
     * Applies this rule to the specified subject.
     *
     * @param subject the subject to apply this {@link Rule} to.
     * @return {@code true} if this {@link Rule} applies to the specified {@code subject}, or {@code false} otherwise.
     */
    public boolean process(SUBJECT_TYPE subject);

}