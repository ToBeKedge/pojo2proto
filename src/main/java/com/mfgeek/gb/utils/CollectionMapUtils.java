package com.mfgeek.gb.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <h3>DESCRIPTION</h3>
 * Utility class on collection or map attribut.
 *
 * <h3>IMPLEMENTATION</h3>
 *
 * <h3>USAGE</h3>
 *
 * @author cyrilm
 *
 */
public final class CollectionMapUtils {

	/** Constructor. */
	private CollectionMapUtils() {
		super();
	}

	/**
	 * @param <T> the type of elements of the array
	 * @param pArray an array of elements
	 * @return a list from the array
	 */
	@SafeVarargs
	public static <T> List<T> asList(T... pArray) {
		List<T> list = null;

		if (pArray != null) {
			list = new ArrayList<T>();
			for (T t : pArray) {
				list.add(t);
			}
		}

		return list;
	}

	/**
	 * @param <T> the type of elements of the array
	 * @param pArray an array of elements
	 * @return <code>true</code> if only one row in the array, <code>false</code> otherwise
	 */
	@SafeVarargs
	public static <T> boolean onlyOneRow(final T... pArray) {
		return isNotEmptyOrNull(pArray) && pArray.length == 1;
	}

	/**
	 * @param <T> the type of elements of the collection
	 * @param pCollection a collection
	 * @return <code>true</code> if only one row in the collection, <code>false</code> otherwise
	 */
	public static <T> boolean onlyOneRow(final Collection<T> pCollection) {
		return isNotEmptyOrNull(pCollection) && pCollection.size() == 1;
	}

	/**
	 * @param <K> the type of keys of the map
	 * @param <V> the type of values of the map
	 * @param pMap a map
	 * @return <code>true</code> if only one row in the map, <code>false</code> otherwise
	 */
	public static <K, V> boolean onlyOneRow(final Map<K, V> pMap) {
		return isNotEmptyOrNull(pMap) && pMap.size() == 1;
	}

	/**
	 * @param <T> the type of elements of the array
	 * @param pArray an array of elements
	 * @return <code>true</code> if only one row in the array, <code>false</code> otherwise
	 */
	@SafeVarargs
	public static <T> boolean moreThanOneRow(final T... pArray) {
		return isNotEmptyOrNull(pArray) && pArray.length > 1;
	}

	/**
	 * @param <T> the type of elements of the collection
	 * @param pCollection a collection
	 * @return <code>true</code> if only one row in the collection, <code>false</code> otherwise
	 */
	public static <T> boolean moreThanOneRow(final Collection<T> pCollection) {
		return isNotEmptyOrNull(pCollection) && pCollection.size() > 1;
	}

	/**
	 * @param <K> the type of keys of the map
	 * @param <V> the type of values of the map
	 * @param pMap a map
	 * @return <code>true</code> if only one row in the map, <code>false</code> otherwise
	 */
	public static <K, V> boolean moreThanOneRow(final Map<K, V> pMap) {
		return isNotEmptyOrNull(pMap) && pMap.size() > 1;
	}

	/**
	 * @param <T> the type of elements of the array
	 * @param pArray an array of elements
	 * @return <code>true</code> if the array is <code>null</code> or empty
	 */
	@SafeVarargs
	public static <T> boolean isEmptyOrNull(T... pArray) {
		return pArray == null || pArray.length == 0;
	}

	/**
	 * @param pCollection a collection
	 * @return <code>true</code> if the collection is <code>null</code> or empty
	 */
	public static boolean isEmptyOrNull(final Collection<?> pCollection) {
		return pCollection == null || pCollection.isEmpty();
	}

	/**
	 * @param pMap a map
	 * @return <code>true</code> if the map is <code>null</code> or empty
	 */
	public static boolean isEmptyOrNull(final Map<?, ?> pMap) {
		return pMap == null || pMap.isEmpty();
	}

	/**
	 * @param <T> the type of elements of the array
	 * @param pArray an array of elements
	 * @return <code>true</code> if the array is NOT <code>null</code> or empty
	 */
	@SafeVarargs
	public static <T> boolean isNotEmptyOrNull(T... pArray) {
		return !isEmptyOrNull(pArray);
	}

	/**
	 * @param pCollection a collection
	 * @return <code>true</code> if the collection is NOT <code>null</code> or empty
	 */
	public static boolean isNotEmptyOrNull(final Collection<?> pCollection) {
		return !isEmptyOrNull(pCollection);
	}

	/**
	 * @param pMap a map
	 * @return <code>true</code> if the map is NOT <code>null</code> or empty
	 */
	public static boolean isNotEmptyOrNull(final Map<?, ?> pMap) {
		return !isEmptyOrNull(pMap);
	}

	/**
	 * @param <K> the type of keys of the map
	 * @param <V> the type of values of the map
	 * @param pMap a {@link Map} of values
	 * @return a {@link List} from keys of the {@link Map}.
	 */
	public static <K, V> List<K> mapToListOfKeys(final Map<K, V> pMap) {
		if (pMap != null) {
			return new ArrayList<K>(pMap.keySet());
		}
		return null;
	}

	/**
	 * @param <K> the type of keys of the map
	 * @param <V> the type of values of the map
	 * @param pMap a {@link Map} of values
	 * @return a {@link List} from values of the {@link Map}.
	 */
	public static <K, V> List<V> mapToListOfValues(final Map<K, V> pMap) {
		if (pMap != null) {
			return new ArrayList<V>(pMap.values());
		}
		return null;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pList a {@link List} of values
	 * @return a {@link Map} from the {@link List} (key == value).
	 */
	public static <T> Map<T, T> listToMap(final List<T> pList) {
		return collectionToMap(pList, false);
	}

	/**
	 * @param <T> the type of list contents
	 * @param pList a {@link List} of values
	 * @param pKeyOnly list as key and <code>null</code> for value
	 * @return a {@link Map} from the {@link List} (key == value).
	 */
	public static <T> Map<T, T> listToMap(final List<T> pList, boolean pKeyOnly) {
		return collectionToMap(pList, pKeyOnly);
	}

	/**
	 * @param <T> the type of list contents
	 * @param pCollection a {@link Collection} of values
	 * @return a {@link Map} from the {@link List} (key == value).
	 */
	public static <T> Map<T, T> collectionToMap(final Collection<T> pCollection) {
		return collectionToMap(pCollection, false);
	}

	/**
	 * @param <T> the type of list contents
	 * @param pCollection a {@link Collection} of values
	 * @param pKeyOnly list as key and <code>null</code> for value
	 * @return a {@link Map} from the {@link List} (key == value).
	 */
	public static <T> Map<T, T> collectionToMap(final Collection<T> pCollection, boolean pKeyOnly) {
		if (pCollection != null) {
			Map<T, T> map = new LinkedHashMap<T, T>();
			for (T value : pCollection) {
				map.put(value, pKeyOnly ? null : value);
			}
			return map;
		}
		return null;
	}

	/**
	 * @param <K> a key type
	 * @param <V> a value type
	 * @param pMap a map
	 * @param pKey a key
	 * @param pValue a value
	 */
	public static <K, V> void addToMap(Map<K, List<V>> pMap, K pKey, V pValue) {
		if (pMap != null && pValue != null) {
			List<V> value = new ArrayList<V>();
			value.add(pValue);
			addToMap(pMap, pKey, value);
		}
	}

	/**
	 * @param <K> a key type
	 * @param <V> a value type
	 * @param pMap a map
	 * @param pKey a key
	 * @param pValue a value
	 */
	public static <K, V> void addToMap(Map<K, List<V>> pMap, K pKey, List<V> pValue) {
		if (pMap != null && pValue != null) {
			List<V> list = pMap.get(pKey);
			if (list == null) {
				list = new ArrayList<V>();
				pMap.put(pKey, list);
			}
			union(list, pValue);
			deduplicate(list, true);
		}
	}

	/**
	 * @param <K> a key type
	 * @param <V> a value type
	 * @param pMap a map
	 * @return a list with all values
	 */
	public static <K, V> List<V> getAllValues(Map<K, List<V>> pMap) {
		List<V> list = null;
		if (pMap != null) {
			list = new ArrayList<>();

			for (List<V> values : pMap.values()) {
				list.addAll(values);
			}
		}
		return list;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pColl1 a collection
	 * @param pColl2 a collection
	 * @return <code>true</code> if union with sucess, <code>false</code> otherwise
	 */
	public static <T> boolean union(final Collection<T> pColl1, final Collection<T> pColl2) {
		boolean result = false;

		if (!Collections.disjoint(pColl1, pColl2)) {
			Iterator<T> iterator = pColl2.iterator();

			T t = null;
			while (iterator.hasNext()) {
				t = iterator.next();

				if (pColl1.contains(t)) {
					iterator.remove();
					result |= true;
				}
			}
		}

		result |= addAll(pColl1, pColl2);

		return result;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pColl a collection
	 * @return <code>true</code> if deduplicate with sucess, <code>false</code> if not change
	 */
	public static <T> boolean deduplicate(Collection<T> pColl) {
		return deduplicate(pColl, false);
	}

	/**
	 * @param <T> the type of list contents
	 * @param pColl a collection
	 * @param pRemoreNullValue <code>true</code> if <code>null</code> value must be remove from the
	 *          list, <code>false</code> otherwise
	 * @return <code>true</code> if deduplicate with sucess, <code>false</code> if not change
	 */
	public static <T> boolean deduplicate(Collection<T> pColl, boolean pRemoreNullValue) {
		boolean result = false;

		if (!isEmptyOrNull(pColl)) {
			Iterator<T> iterator = pColl.iterator();

			T t = null;
			while (iterator.hasNext()) {
				t = iterator.next();
				if (t == null && pRemoreNullValue || Collections.frequency(pColl, t) > 1) {
					iterator.remove();
					result |= true;
				}
			}
		}

		return result;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pCollection a list contents
	 * @return a copy of the list contents
	 */
	public static <T> Collection<T> copy(Collection<T> pCollection) {
		Collection<T> collection = null;

		if (pCollection != null) {
			collection = new ArrayList<T>();

			boolean result = addAll(collection, pCollection);

			if (!result) {
				collection = new ArrayList<T>();
			}
		}

		return collection;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pColl1 a collection
	 * @param pColl2 a collection
	 * @return <code>true</code> if add all with sucess, <code>false</code> otherwise
	 */
	public static <T> boolean addAll(Collection<T> pColl1, Collection<T> pColl2) {
		boolean result = false;
		for (T e : pColl2) {
			result |= pColl1.add(e);
		}
		return result;
	}

	/**
	 * @param <T> the type of list contents
	 * @param pColl a collection
	 * @param pValue a value to add
	 * @return <code>true</code> if value not contained and added, <code>false</code> otherwise
	 */
	public static <T> boolean addIfNotContains(Collection<T> pColl, T pValue) {
		if (pColl != null && !pColl.contains(pValue)) {
			return pColl.add(pValue);
		}
		return false;
	}

	/**
	 * @param <K> the type of map keys
	 * @param <V> the type of map values
	 * @param pMap a map
	 * @param pKey a key to add
	 * @param pValue a value to add
	 * @return <code>true</code> if key not contained and added, <code>false</code> otherwise
	 */
	public static <K, V> boolean addIfNotContains(Map<K, V> pMap, K pKey, V pValue) {
		if (pMap != null && !pMap.containsKey(pKey)) {
			pMap.put(pKey, pValue);
			return true;
		}
		return false;
	}

	/**
	 * @param <T> type of collection elements
	 * @param pExpected expected list
	 * @param pActual actual list
	 * @return <code>true</code> if equals, <code>false</code> otherwise
	 */
	public static <T extends Comparable<? super T>> boolean equals(Collection<T> pExpected, Collection<T> pActual) {
		if (pExpected == null && pActual == null) {
			return true;
		} else if (pExpected != null && pActual == null) {
			return false;
		} else if (pExpected == null && pActual != null) {
			return false;
		} else {
			List<T> expected = new ArrayList<T>(pExpected);
			List<T> actual = new ArrayList<T>(pActual);

			Collections.sort(expected);
			Collections.sort(actual);

			return expected.equals(actual);
		}
	}

	/**
	 * Sort a map by value according to given value comparator.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param pUnsortMap the map to sort
	 * @param pComparator the comparator
	 * @return the sorted map
	 */
	public static <K, V> Map<K, V> sortByValue(Map<K, V> pUnsortMap, Comparator<Map.Entry<K, V>> pComparator) {

		// 1. Convert Map to List of Map
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(pUnsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		Collections.sort(list, pComparator);

		// 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

}
