package mcscheduler.testutil;

import mcscheduler.commons.core.index.Index;

/**
 * A utility class containing a list of {@code Index} objects to be used in tests.
 */
public class TypicalIndexes {
    public static final Index INDEX_FIRST_WORKER = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_WORKER = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_WORKER = Index.fromOneBased(3);
    public static final Index INDEX_FOURTH_WORKER = Index.fromOneBased(4);

    public static final Index INDEX_FIRST_SHIFT = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_SHIFT = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_SHIFT = Index.fromOneBased(3);
}
