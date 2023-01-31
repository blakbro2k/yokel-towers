package asg.games.yokel.persistence;

public interface Saveable<T> {
    /** Add Object to transaction list */
    void saveObject(T object);

    /** Commit transactions */
    void commitTransactions();

    /** Delete transactions */
    void rollBackTransactions();
}
