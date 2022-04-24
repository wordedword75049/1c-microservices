import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework.task1.MalformedDataException;
import ru.mipt1c.homework.tests.task1.AbstractSingleFileStorageTest;
import ru.mipt1c.homework.tests.task1.Student;
import ru.mipt1c.homework.tests.task1.StudentKey;

import java.io.IOException;

public class KeyValueStorageTest extends AbstractSingleFileStorageTest {

    private<T1, T2> KeyValueStorage<T1, T2> getTheStorage(String path) throws MalformedDataException {
        KeyValueStorage<T1, T2> storage = null;
        try {
            storage = new KeyValueStorageImpl<>(path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return storage;
    }


    @Override
    protected KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException {
        return getTheStorage(path);
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException {
        return getTheStorage(path);
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException {
        return getTheStorage(path);
    }
}