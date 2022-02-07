package toolkit;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import java.io.*;
import java.util.*;

/**
 * A user-defined function for Neo4j that will emit an event into a redis backed message queue.
 */
public class Neo4jHelpers {

    @UserFunction
    @Description("helper to diff two maps")
    public Map<String, Serializable> diff(
            @Name(value = "m1") Map<String, java.io.Serializable> prev,
            @Name(value = "m2") Map<String, java.io.Serializable> next
    ) {
        return mapDifference(prev, next);
    }

    public static <K, V> Map<K, V> mapDifference(
            Map<? extends K, ? extends V> left,
            Map<? extends K, ? extends V> right
    ) {

        Map<K, V> diff = new HashMap<>();
        Set<K> keys = new HashSet<>();

        if (left.equals(right)) return diff;

        keys.addAll(left.keySet());
        keys.addAll(right.keySet());

        for (K key : keys) {
            V leftVal = left.get(key);
            V rightVal = right.get(key);

            /* key is now set, or was removed, or changed */
            if (leftVal == null || rightVal == null || !left.get(key).equals(rightVal))
                diff.put(key, rightVal);
        }

        return diff;
    }
}