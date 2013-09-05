package tuxkids.tuxlib.persist;

import java.util.HashMap;
import java.util.LinkedList;

import playn.core.PlayN;
import playn.core.Storage;
import tuxkids.tuxlib.persist.Persistable.Constructor;
import tuxkids.tuxlib.persist.Persistable.Data;
import tuxkids.tuxlib.persist.Persistable.ParseDataException;

/**
 * Allows for the persistence and reconstruction of {@link Persistable} 
 * objects. 
 */
public class PersistUtils {

	private static HashMap<Class<?>, Constructor> constructorMap = 
			new HashMap<Class<?>, Persistable.Constructor>();
	private static HashMap<String, Class<?>> nameMap = 
			new HashMap<String, Class<?>>();
	static {
		// You MUST register your Persistable class here for it to be reconstructable
		
		for (Class<?> key : constructorMap.keySet()) {
			nameMap.put(key.getName(), key);
		}
	}
	
	// signifies null values as a string
	private final static String NULL = "<NULL>";
	
	// a buffer for our persisting data
	private static LinkedList<String> store = new LinkedList<String>();
	
	// saves the collected data to storage
	private static void saveStore(String tag) {
		StringBuilder sb = new StringBuilder();
		for (String line : store) {
			if (sb.length() > 0) sb.append("\n");
			sb.append(line == null ? NULL : line);
		}
		String value = sb.toString();
		PlayN.storage().setItem(tag, value);
		store.clear();
	}
	
	// loads the collected data from storage
	private static void loadStore(String tag) {
		store.clear();
		String data = PlayN.storage().getItem(tag);
		if (data == null) return;
		String[] lines = data.split("\n");
		for (String line : lines) {
			store.add(line.equals(NULL) ? null : line);
		}
	}
	
	/** 
	 * Persists the given objects as a String in {@link Storage}, under
	 * the given identifier tag.
	 */
	public static void persist(Persistable persistable, String tag) {
		Data data = new Data(true);
		store.clear();
		try {
			persistable.persist(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveStore(tag);
	}

	/** 
	 * Fetches the given object from {@link Storage}, using
	 * the given identifier tag. Returns null if fetching fails.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fetch(Class<T> clazz, String tag) {
		Data data = new Data(false);
		try {
			loadStore(tag);
			if (store == null) return null;
			
			Persistable obj = construct(clazz.getName());
			obj.persist(data);
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			store.clear();
		}
		return null;
	}
	
	/** Returns true if the given tag is currently being used for {@link Storage} */
	public static boolean stored(String tag) {
		return PlayN.storage().getItem(tag) != null;
	}

	/** Removes the given tag and its associated data from {@link Storage} */
	public static void clear(String tag) {
		PlayN.storage().removeItem(tag);
	}
	
	/** 
	 * Returns a new instance of the class with given unqualified name, which
	 * can be gotten with the {@link Class#getName()} method. If the class is not
	 * registered, throws a {@link ParseDataException}. It is not likely this method
	 * would need to be called outside of the {@link Data} class.
	 */
	protected static Persistable construct(String type) throws ParseDataException {
		Class<?> c = nameMap.get(type);
		if (c == null) throw new ParseDataException("No constructor for type: " + type);
		Constructor constructor = constructorMap.get(c);
		return constructor.construct();
	}
	
	/** 
	 * Reads a line during a {@link PersistUtils#persist(Persistable, String)}
	 * operation. This should only be called from {@link Data}.
	 */
	protected static String read() throws ParseDataException {
		if (store.isEmpty()) throw new ParseDataException("No data");
		return store.removeFirst();
	}

	/** 
	 * Writes a line during a {@link PersistUtils#persist(Persistable, String)}
	 * operation. This should only be called from {@link Data}. 
	 */
	protected static void write(String data) {
		store.add(data);
	}

}
