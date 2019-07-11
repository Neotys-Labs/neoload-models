package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.listener.TestEventListener;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventListenerUtilsTest {

    @Test
    public void testReadSupportedAction(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.readSupportedAction("");
        verify(spy, times(1)).readSupportedAction("");
    }

    @Test
    public void testReadUnsupportedAction(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.readUnsupportedAction("");
        verify(spy, times(1)).readUnsupportedAction("");
    }

    @Test
    public void testReadSupportedParameterWithWarn(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.readSupportedParameterWithWarn("","","","");
        verify(spy, times(1)).readSupportedParameterWithWarn("","","","");
    }

    @Test
    public void testReadUnsupportedParameter(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.readUnsupportedParameter("","","");
        verify(spy, times(1)).readUnsupportedParameter("","","");
    }

    @Test
    public void testReadSupportedFunctionWithWarn(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.readSupportedFunctionWithWarn("","",null,"");
        verify(spy, times(1)).readSupportedFunctionWithWarn("","",null,"");
    }

    @Test
    public void testStartReadingScripts(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.startReadingScripts(1);
        verify(spy, times(1)).startReadingScripts(1);
    }

    @Test
    public void testEndReadingScripts(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.endReadingScripts();
        verify(spy, times(1)).endReadingScripts();
    }

    @Test
    public void testStartScript(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.startScript("");
        verify(spy, times(1)).startScript("");
    }

    @Test
    public void testEndScript(){
        EventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        spy.endScript();
        verify(spy, times(1)).endScript();
    }
}