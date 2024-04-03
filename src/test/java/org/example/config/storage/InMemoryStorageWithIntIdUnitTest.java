package org.example.config.storage;

import org.example.config.storage.inmemory.InMemoryStorageWithIntId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InMemoryStorageWithIntIdUnitTest {

    private static final String SOME_STR_OBJECT_1 = "str1";
    private static final String SOME_STR_OBJECT_2 = "str2";
    private static final String SOME_STR_OBJECT_3 = "str3";
    private static final String SOME_STR_OBJECT_4 = "str4";
    private static final int TEST_ID_1 = 1;
    private static final int TEST_ID_2 = 2;
    private static final int TEST_ID_3 = 3;

    private static final String EXPECTED_EXCEPTION_MESSAGE = "Datasource is overflow";
    private static final int EXPECTED_ID = 4;
    private static final int EXPECTED_ID_FOR_FIRST_ADDED_ITEM = 1;

    @Spy
    InMemoryStorageWithIntId<String> testInstance;

    @Test
    void shouldFindNextId() {
        testInstance.put(TEST_ID_1, SOME_STR_OBJECT_1);
        testInstance.put(TEST_ID_2, SOME_STR_OBJECT_2);
        testInstance.put(TEST_ID_3, SOME_STR_OBJECT_3);

        Integer actualResult = testInstance.findNextId();

        assertEquals(EXPECTED_ID, actualResult);
    }

    @Test
    void shouldFindNextId_whenSourceIsEmpty() {
        Integer actualResult = testInstance.findNextId();

        assertEquals(EXPECTED_ID_FOR_FIRST_ADDED_ITEM, actualResult);
    }

    @Test
    void shouldFindNextId_whenIdsNotInRow() {
        testInstance.put(TEST_ID_3, SOME_STR_OBJECT_3);
        testInstance.put(TEST_ID_1, SOME_STR_OBJECT_1);

        Integer actualResult = testInstance.findNextId();

        assertEquals(EXPECTED_ID, actualResult);
    }

    @Test
    void shouldFindNextId_whenMaxIdInSourceIsEqualToMaxIntValue() {
        testInstance.put(Integer.MAX_VALUE, SOME_STR_OBJECT_4);

        Exception exception = assertThrows(RuntimeException.class, () ->
            testInstance.findNextId()
        );

        assertEquals(EXPECTED_EXCEPTION_MESSAGE, exception.getMessage());
    }
}