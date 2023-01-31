package asg.games.yokel.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.OpenALSound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.UtilitiesClass;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import asg.games.yokel.objects.YokelRoom;

public class YokelUtilities extends UtilitiesClass {
    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    /**
     * Represents a failed index search.
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;

    private final static String LEFT_CURLY_BRACET_HTML = "&#123;";
    private final static String RIGHTT_CURLY_BRACET_HTML = "&#125;";
    private final static Json json = new Json();

    public static void setSizeFromDrawable(Actor actor, Drawable drawable){
        if(actor != null && drawable != null){
            actor.setWidth(drawable.getMinWidth());
            actor.setHeight(drawable.getMinHeight());
        }
    }

    public static int getNextTableName(YokelRoom yokelRoom) {
        int tableIndex = -1;
        if(yokelRoom != null) {
            Array<Integer> tables = yokelRoom.getAllTableIndexes();
            int size = sizeOf(tables);
            if(size > 0){
                int[] indices = new int[size];
                for(int i = 0; i < size; i++) {
                    indices[i] = tables.get(i);
                }
                Arrays.sort(indices);
                int max = indices[size - 1];
                int num = findMissingNumber(indices, max);
                if(num == -1) {
                    return max + 1;
                }
                return num;
            } else if(size == 0) {
                return 1;
            }
        }
        return tableIndex;
    }

    /*



    public static String printYokelObject(YokelObject yokelObject) {
        return json.prettyPrint(yokelObject);
    }

    @NotNull
    public static String printYokelObjects(Array<? extends YokelObject> yokelObjects){
        StringBuilder sb = new StringBuilder();
        for(YokelObject yObject : safeIterable(yokelObjects)){
            sb.append(printYokelObject(yObject)).append('\n');
        }
        return sb.toString();
    }

    @NotNull
    public static Array<String> toPlainTextArray(Array<? extends YokelObject> objects) {
        Array<String> plainTexts = GdxArrays.newArray();
        for(YokelObject yokelObject : safeIterable(objects)){
            plainTexts.add(jsonToString(yokelObject.toString()));
        }
        return plainTexts;
    }


    private static void resetGameBlock(Actor actor){
        if(actor instanceof GameBlock){
            ((GameBlock) actor).reset();
        }
    }

    public static void resetGameBlockActors(SnapshotArray<Actor> children) {
        if(children != null){
            Actor[] actors = children.begin();
            for (Actor actor : actors) {
                resetGameBlock(actor);
            }
            children.end();
        }
    }


     */

    /**
     * Function to find the missing number in a array
     * @param arr : given array
     * @param max : value of maximum number in the series
     */
    public static int findMissingNumber(int[] arr, int max){
        Arrays.sort(arr);

        int currentValue = 1;
        int missingValue = -1;
        boolean foundFirstMissing = false;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != currentValue) {
                for (int j = currentValue; j < arr[i]; j++) {
                    missingValue = j;
                    foundFirstMissing = true;
                    break;
                }
            }
            currentValue = arr[i] + 1;
            if(foundFirstMissing) {
                break;
            }
        }
        return missingValue;
    }




    public static Array<String> arrayToList(String[] o) {
        int size = o.length;
        Array<String> array = new Array<>(size);

        for (String s : o) {
            array.add(s);
        }
        return array;
    }

    public static <T> Iterable<T> safeIterable(Iterable<T> collection){
        return safeIterable(collection, false);
    }

    public static <T> Iterable<T> safeIterable(Iterable<T> collection, boolean newArray){
        if(collection != null){
            return newArray? GdxArrays.newArray(collection) : collection;
        } else {
            return GdxArrays.newArray();
        }
    }

    public static <T> Array<T> iterableToArray(Iterable<T> iterable) {
        Array<T> returnList = new Array<T>();
        if(iterable != null) {
            for(T o : iterable) {
                returnList.add(o);
            }
        }
        return returnList;
    }


    public static Label createLabel(Skin skin, String text, float size){
        Label label = new Label("", skin);
        if(!isEmpty(text)){
            label.setText(text);
        }
        label.setFontScale(size);
        return label;
    }

    /** @param actor might have an ID attached using name setter.
     * @return actor's ID or null. */
    public static String getActorId(Actor actor) {
        String id = "";
        if(actor != null){
            id = actor.getName();
        }
        return id;
    }

    public static <T extends Actor> T getActorFromCell(Class<T> klass, Cell<T> cell) {
        if(cell != null && klass != null && isInstance(klass, cell.getActor())){
            return cell.getActor();
        }
        return null;
    }

    private static <T extends Actor> boolean isInstance(Class<T> klass, T actor) {
        System.out.println("klass=" + klass.getSimpleName());
        System.out.println("actor class=" + actor.getClass().getSimpleName());
        return equalsIgnoreCase(klass.getSimpleName(), actor.getClass().getSimpleName());
    }

    public static float maxFloat(Float... floats) {
        float max = 0;
        if(floats != null){
            for(float f : floats){
                max = Math.max(max, f);
            }
        }
        return max;
    }


    public static void flushIterator(Iterator<?> iterator) {
        while(iterator != null && iterator.hasNext()){
            iterator.remove();
        }
    }


    public static <Type> ObjectMap.Values<Type> getMapValues(ObjectMap<?, Type> objectMap) {
        return new ObjectMap.Values<>(objectMap);
    }

    public static <Type> ObjectMap.Keys<Type> getMapKeys(ObjectMap<Type, ?> objectMap) {
        return new ObjectMap.Keys<>(objectMap);
    }

    public static <Type> Iterator<Type> getArrayIterator(Array<Type> array) {
        return new ArrayIterator<>(array);
    }

    @Deprecated
    public static float getSoundDuration(Sound soundFile) {
        /*FileHandle file=Gdx.files.internal(filepath);
        FileHandle external=Gdx.files.external("mygame/"+filepath);
        if(!external.exists())file.copyTo(external); //copy the file to the external storage
        Mpg123Decoder   decoder = new Mpg123Decoder(external);
        System.out.println("name:"+filepath+"  duration:"+decoder.getLength());
        System.out.println("Sound type: " + soundFile.getClass());
        soundFile.play()*/
        //if(soundFile instanceof OpenALSound){
        //    return ((OpenALSound) soundFile).duration();
        //} else {
        //    return -1;
       // }
        return 1;
    }

    public static int sizeOf(Iterable<Integer> collection) {
        return collection == null ? -1 : (int) StreamSupport.stream(collection.spliterator(), false).count();
    }

    public static class IDGenerator {
        private IDGenerator(){}

        public static String getID(){
            return replace(GwtUUIDUtil.get() + "", "-","") ;
            //return replace(UUID.randomUUID() + "", "-","") ;
        }

        public static Array<String> getGroupOfIDs(int num) throws IllegalArgumentException{
            if(num > 0) {
                Array<String> ids = new Array<>();
                for(int i = 0; i < num; i++){
                    ids.add(getID());
                }
                return ids;
            }
            throw new IllegalArgumentException("Cannot create Group of ID's for number less than 1.");
        }
    }

    public static boolean containsAny(Array<Object> c1, Array<Object> c2, boolean identity){
        boolean containsAny = false;
        if(null != c1 && null != c2){
            for(Object o : safeIterable(c2)){
                if (!c1.contains(o, identity)) {
                    continue;
                }
                containsAny = true;
                break;
            }
        }
        return containsAny;
    }

    /**
     * @param title
     * @return
     */
    public static String cleanTitle(String title) throws GdxRuntimeException{
        String ret = "";
        if (title != null) {
            ret = title.replace("#8211", "-")
                    .replace("#8217", "'")
                    .replace("#8220", "\"")
                    .replace("#8221", "\"")
                    .replace("#8230", "...")
                    .replace("#038", "&");
        }
        return ret;
    }
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isEmpty(Iterable<?> collection) {
        return collection == null || !collection.iterator().hasNext();
    }

    public static boolean isStaticArrayEmpty(Object[] array){
        return array == null || array.length < 1;
    }

    public static <T> String[] toStringArray(Array<T> collection) {
        if(collection != null){
            int size = collection.size;
            String[] c2 = new String[size];

            for(int c = 0; c < size; c++){
                Object o = collection.get(c);
                if(o != null){
                    c2[c] = o.toString();
                }
            }
            return c2;
        }
        return new String[1];
    }

    public static <T> String[] toStringArray(ObjectMap.Values<T> values) {
        Array<T> array = new Array<>();
        if(values != null){
            while(values.hasNext){
                array.add(values.next());
            }
        }
        return toStringArray(array);
    }

    /**
     * Creates a new Iterable Array object.
     * @param //array
     * @param //<T>
     * @return
     */
    //@NotNull
   /* public static <T> Array.ArrayIterable<T> toIterable(Array<T> array){
        if(isArrayEmpty(array)){
            return new Array.ArrayIterable<>(array);
        }
        return new Array.ArrayIterable<>(GdxArrays.newArray());
    }*/

    public static String getJsonString(Object o){
        return json.toJson(o);
    }

    public static <T> String getJsonString(Class<T> type, Object o){
        return json.toJson(o, type);
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr){
        return json.fromJson(type, jsonStr);
    }

    public static String jsonToString(String str){
        return replace(replace(str, "{",LEFT_CURLY_BRACET_HTML),"}", RIGHTT_CURLY_BRACET_HTML);
    }

    public static String stringToJson(String str){
        return replace(replace(str, LEFT_CURLY_BRACET_HTML,"{"), RIGHTT_CURLY_BRACET_HTML,"}");
    }

    public static String getStringValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return objects[index];
        }
        return null;
    }

    public static boolean getBooleanValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return otob(objects[index]);
        }
        return false;
    }


    public static boolean otob(Object o){
        if(o != null){
            return Boolean.parseBoolean(otos(o));
        }
        return false;
    }

    public static String otos(Object o){
        if(o != null){
            return o.toString();
        }
        return "";
    }

    public static int otoi(Object o){
        if(o != null){
            return Integer.parseInt(otos(o));
        }
        return -1;
    }

    public static long otol(Object o){
        if(o != null){
            return Long.parseLong(otos(o));
        }
        return -1;
    }

    public static float otof(Object o){
        if(o != null){
            return Float.parseFloat(otos(o));
        }
        return -1;
    }

    public static Array<String> getFileNames(File folder){
        Array<String> retFileNames = new Array<>();
        if(folder != null){
            File[] fileNames = folder.listFiles();

            if(fileNames != null){
                for(File file : fileNames){
                    if(file != null){
                        if(file.isDirectory()){
                            getFileNames(file);
                        } else {
                            retFileNames.add(file.getName());
                        }
                    }
                }
            }
        }
        return retFileNames;
    }

    public static Array<String> getFileNames(String path){
        Array<String> fileNames = new Array<>();
        if(path != null){
            fileNames.addAll(getFileNames(new File(path)));
        }
        return fileNames;
    }

    public static TextureRegion get2DAnimationFrame(Animation<Object> animation, int keyFrame) throws GdxRuntimeException{
        if(animation == null){
            throw new GdxRuntimeException("Animation cannot be null.");
        }
        if(keyFrame < 0){
            throw new GdxRuntimeException("keyFrame must be greater than 0.");
        }

        Object frame = animation.getKeyFrame(keyFrame);
        if(frame == null){
            return null;
        }

        if(frame instanceof TextureRegion){
            return (TextureRegion) frame;
        }
        throw new GdxRuntimeException("Frame is not an instance of " + TextureRegion.class + ". frame=" + frame.getClass());
    }

    public static void drawBackgroundRect(Batch batch, Rectangle rectangle, Color color) {
        if(batch != null && rectangle != null && color != null){
            batch.end();

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.end();
            shapeRenderer.dispose();

            batch.begin();
        }
    }


    public static boolean isValidPayload(String[] payload, int size){
        if(payload != null){
            return payload.length == size;
        }
        return false;
    }

    /*
    public static void invokeMethodOnMatrix(int maxWidth, int maxHeight, Object o,
                                    String methodName, Class<?>[] paramClasses, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for(int r = 0; r < maxWidth; r++){
            for(int c = 0; c < maxHeight; c++){
                Class<?> clazz = o.getClass();
                Method m = clazz.getDeclaredMethod(methodName, paramClasses);

                m.invoke(o, args);
            }
        }
    }*/

    public static String printBounds(Actor actor){
        if(actor != null){
            String name = actor.getClass().getSimpleName() + "@" + Integer.toHexString(actor.hashCode());
            String cname = actor.getName();
            if(cname != null){
                name = cname;
            }
            return name + "(" + actor.getX() + "," + actor.getY() + ")[w:" + actor.getWidth() + " h:" + actor.getHeight() + "]";
        } else {
            return "";
        }
    }

    public static boolean setDebug(boolean b, Actor... actors){
        if(actors != null){
            for(Actor actor : actors){
                if(actor != null){
                    actor.setDebug(b);
                }
            }
        }
        return b;
    }


    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. The comparison is <strong>case insensitive</strong>.</p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param cs1  the first CharSequence, may be {@code null}
     * @param cs2  the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-insensitive), or both {@code null}
     * @since 3.0 Changed signature from equalsIgnoreCase(String, String) to equalsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs the {@code CharSequence} to be processed
     * @param ignoreCase whether or not to be case insensitive
     * @param thisStart the index to start on the {@code cs} CharSequence
     * @param substring the {@code CharSequence} to be looked for
     * @param start the index to start on the {@code substring} CharSequence
     * @param length character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }


    /**
     * <p>Checks if CharSequence contains a search CharSequence irrespective of case,
     * handling {@code null}. Case-insensitivity is defined as by
     * {@link String#equalsIgnoreCase(String)}.
     *
     * <p>A {@code null} CharSequence will return {@code false}.</p>
     *
     * <pre>
     * StringUtils.containsIgnoreCase(null, *) = false
     * StringUtils.containsIgnoreCase(*, null) = false
     * StringUtils.containsIgnoreCase("", "") = true
     * StringUtils.containsIgnoreCase("abc", "") = true
     * StringUtils.containsIgnoreCase("abc", "a") = true
     * StringUtils.containsIgnoreCase("abc", "z") = false
     * StringUtils.containsIgnoreCase("abc", "A") = true
     * StringUtils.containsIgnoreCase("abc", "Z") = false
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param searchStr  the CharSequence to find, may be null
     * @return true if the CharSequence contains the search CharSequence irrespective of
     * case or false if not or {@code null} string input
     * @since 3.0 Changed signature from containsIgnoreCase(String, String) to containsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.
     * For more control over the split use the StrTokenizer class.</p>
     *
     * <p>A {@code null} input String returns {@code null}.
     * A {@code null} separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  {@code null} splits on whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str  the String to parse, may be {@code null}
     * @param separatorChars the separate character
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are
     * treated as empty token separators; if {@code false}, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final Array<String> list = new Array<>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        //return list.toArray(GdxArrays.newArray());
        return list.toArray();
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int max)
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(final String text, final String searchString, final String replacement, final int max) {
        return replace(text, searchString, replacement, max, false);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String,
     * case sensitively/insensitively based on {@code ignoreCase} value.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *, false)         = null
     * StringUtils.replace("", *, *, *, false)           = ""
     * StringUtils.replace("any", null, *, *, false)     = "any"
     * StringUtils.replace("any", *, null, *, false)     = "any"
     * StringUtils.replace("any", "", *, *, false)       = "any"
     * StringUtils.replace("any", *, *, 0, false)        = "any"
     * StringUtils.replace("abaa", "a", null, -1, false) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1, false)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0, false)   = "abaa"
     * StringUtils.replace("abaa", "A", "z", 1, false)   = "abaa"
     * StringUtils.replace("abaa", "A", "z", 1, true)   = "zbaa"
     * StringUtils.replace("abAa", "a", "z", 2, true)   = "zbza"
     * StringUtils.replace("abAa", "a", "z", -1, true)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for (case insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @param ignoreCase if true replace is case insensitive, otherwise case sensitive
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    private static String replace(final String text, String searchString, final String replacement, int max, final boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        if (ignoreCase) {
            searchString = searchString.toLowerCase();
        }
        int start = 0;
        int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = Math.max(replacement.length() - replLength, 0);
        increase *= max < 0 ? 16 : Math.min(max, 64);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
        }
        buf.append(text, start, text.length());
        return buf.toString();
    }

    /**
     * <p>Case in-sensitive find of the first index within a CharSequence
     * from the specified position.</p>
     *
     * <p>A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtils.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param searchStr  the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search CharSequence (always &ge; startPos),
     *  -1 if no match or {@code null} string input
     * @since 2.5
     * @since 3.0 Changed signature from indexOfIgnoreCase(String, String, int) to indexOfIgnoreCase(CharSequence, CharSequence, int)
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int endLimit = str.length() - searchStr.length() + 1;
        if (startPos > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>Finds the first index within a CharSequence, handling {@code null}.
     * This method uses {@link String#indexOf(String, int)} if possible.</p>
     *
     * <p>A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * StringUtils.indexOf(null, *, *)          = -1
     * StringUtils.indexOf(*, null, *)          = -1
     * StringUtils.indexOf("", "", 0)           = 0
     * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
     * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
     * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
     * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
     * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
     * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
     * StringUtils.indexOf("aabaabaa", "b", -1) = 2
     * StringUtils.indexOf("aabaabaa", "", 2)   = 2
     * StringUtils.indexOf("abc", "", 9)        = 3
     * </pre>
     *
     * @param seq  the CharSequence to check, may be null
     * @param searchSeq  the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search CharSequence (always &ge; startPos),
     *  -1 if no match or {@code null} string input
     * @since 2.0
     * @since 3.0 Changed signature from indexOf(String, String, int) to indexOf(CharSequence, CharSequence, int)
     */
    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }
        return charIndexOf(seq, searchSeq, startPos);
    }

    /**
     * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
     *
     * @param cs the {@code CharSequence} to be processed
     * @param searchChar the {@code CharSequence} to be searched for
     * @param start the start index
     * @return the index where the search sequence was found
     */
    static int charIndexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar.toString(), start);
        } else if (cs instanceof StringBuilder) {
            return ((StringBuilder) cs).indexOf(searchChar.toString(), start);
        } else if (cs instanceof StringBuffer) {
            return ((StringBuffer) cs).indexOf(searchChar.toString(), start);
        }
        return cs.toString().indexOf(searchChar.toString(), start);
    }
}
