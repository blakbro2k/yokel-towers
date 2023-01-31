package asg.games.yokel.objects;

import asg.games.yokel.persistence.YokelStorageAdapter;

/**
 * Visitor Class to save child YokelObjects
 */
public interface YokelObjectJPAVisitor {
    void visitSave(YokelStorageAdapter adapter);
}
