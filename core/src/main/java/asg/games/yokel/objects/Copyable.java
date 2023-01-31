package asg.games.yokel.objects;

public interface Copyable<T> {
 T copy();
 T deepCopy();
}