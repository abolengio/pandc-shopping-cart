package com.ab.cart.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileLineWriterTest {

    @Mock
    FileWriterFactory fileWriterFactory;

    @Test
    public void shouldWriteToWriterAndCloseItAtTheEnd() throws IOException {
        StringWriter out = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        when(fileWriterFactory.getFileWriter("filePath")).thenReturn(bufferedWriter);
        FileLineWriter writer = new FileLineWriter(fileWriterFactory, "filePath");
        writer.addLine("some line");
        assertThat(out.toString(), is("some line\n"));
        assertThat(writerIsClosed(bufferedWriter), is(true));

    }

    @Test
    public void shouldCloseWriterWhenExceptionOccurs() throws IOException {
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        doThrow(new IOException()).when(bufferedWriter).write(anyString());
        when(fileWriterFactory.getFileWriter("filePath")).thenReturn(bufferedWriter);
        FileLineWriter writer = new FileLineWriter(fileWriterFactory, "filePath");
        try {
            writer.addLine("does not really matter");
            fail("Exception expected");
        } catch (Exception exc) {
            verify(bufferedWriter).close();
        }

    }

    private boolean writerIsClosed(Writer writer) throws IOException {
        try {
            writer.write(" ");
        } catch (IOException exc) {
            return exc.getMessage().contains("Stream closed");
        }
        return false;
    }

}
