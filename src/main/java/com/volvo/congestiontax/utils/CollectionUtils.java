package com.volvo.congestiontax.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

  public static <T> Stream<T> safeStream(Collection<T> collection) {
    return collection == null ? Stream.empty() : collection.stream();
  }

  public static <T> boolean isEmpty(Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }

//  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//    List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
//    list.sort(Entry.comparingByValue());
//
//    Map<K, V> result = new LinkedHashMap<>();
//    for (Entry<K, V> entry : list) {
//      result.put(entry.getKey(), entry.getValue());
//    }
//
//    return result;
//  }

}
