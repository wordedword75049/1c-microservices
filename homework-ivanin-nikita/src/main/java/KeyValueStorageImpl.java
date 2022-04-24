import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework.task1.MalformedDataException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

public class KeyValueStorageImpl<K, V> implements KeyValueStorage<K, V> {
    private final HashMap<K, V> storageData;
    private final Path pathToStorageFile;
    private boolean storageClosed = false;

    public KeyValueStorageImpl(String pathToStorage) throws IOException, ClassNotFoundException {
        if (!Files.exists(Paths.get(pathToStorage))) {
            throw new IllegalArgumentException(String.format("%s does not exist", pathToStorage));
        }
        this.pathToStorageFile = Paths.get(pathToStorage, "storage.bin");
        if (!Files.exists(this.pathToStorageFile)) {
            Files.createFile(this.pathToStorageFile);
            this.storageData = new HashMap<>();
        } else {
            InputStream fileInputStream = Files.newInputStream(Paths.get(String.valueOf(this.pathToStorageFile)));
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(fileInputStream);
                this.storageData = (HashMap<K, V>) objectInputStream.readObject();
            } catch (StreamCorruptedException e) {
                throw new MalformedDataException(e);
            }            
            objectInputStream.close();
            fileInputStream.close();
        }
    }

    private void checkClosed() {
        if (this.storageClosed) {
            throw new RuntimeException("storage is closed");
        }
    }

    @Override
    public V read(K key) {
        this.checkClosed();
        return this.storageData.get(key);
    }

    @Override
    public boolean exists(K key) {
        this.checkClosed();
        return this.storageData.containsKey(key);
    }

    @Override
    public void write(K key, V value) {
        this.checkClosed();
        this.storageData.put(key, value);
    }

    @Override
    public void delete(K key) {
        this.checkClosed();
        this.storageData.remove(key);
    }

    @Override
    public Iterator<K> readKeys() {
        this.checkClosed();
        return this.storageData.keySet().iterator();
    }

    @Override
    public int size() {
        this.checkClosed();
        return this.storageData.size();
    }

    @Override
    public void close() throws IOException {
        this.storageClosed = true;
        this.flush();
    }

    @Override
    public void flush() throws IOException {
        OutputStream fileOutputStream = Files.newOutputStream(Paths.get(String.valueOf(this.pathToStorageFile)));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.storageData);
        objectOutputStream.close();
        fileOutputStream.close();
    }
}