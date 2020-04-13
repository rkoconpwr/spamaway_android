package com.koconr.smspam.database;

import com.koconr.smspam.database.dao.MessageDao;
import com.koconr.smspam.model.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DataBaseCacheTest {

    @Mock
    private MessageDao messageDao;

    private List<Message> messageList = new LinkedList<>();

    private DataBaseCache dataBaseCache;

    @Before
    public void setUp() throws Exception {
        messageList.clear();
        dataBaseCache = new DataBaseCache(null, null, messageDao, messageList);
    }

    @Test
    public void shouldFindAndDeleteMessage() {
        messageList.add(new Message(123, "", ""));
        dataBaseCache.deleteMessage(123);

    }

    @Test
    public void shouldNotDeleteWhenWrongElement() {
        dataBaseCache.deleteMessage(123);
    }

    @Test
    public void shouldAdd() {
        dataBaseCache.addMessage("", "");
    }

    @Test
    public void shouldNotAddWhenElementExists() {
        Message message = new Message(123, "", "");
        messageList.add(message);

        dataBaseCache.addMessage(message);
    }
}