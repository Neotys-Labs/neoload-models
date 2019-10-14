package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventListenerUtilsTest {

    private TestEventListener spy;

    @Before
    public void before()   {
         spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testReadSupportedAction(){

        spy.readSupportedAction("");
        verify(spy, times(1)).readSupportedAction("");
    }

    @Test
    public void testReadUnsupportedAction(){

        spy.readUnsupportedAction("");
        verify(spy, times(1)).readUnsupportedAction("");
    }

    @Test
    public void testReadSupportedParameterWithWarn(){

        spy.readSupportedParameterWithWarn("","","","");
        verify(spy, times(1)).readSupportedParameterWithWarn("","","","");
    }

    @Test
    public void testReadUnsupportedParameter(){

        spy.readUnsupportedParameter("","","");
        verify(spy, times(1)).readUnsupportedParameter("","","");
    }

    @Test
    public void testReadSupportedFunctionWithWarn(){

        spy.readSupportedFunctionWithWarn("","",null,"");
        verify(spy, times(1)).readSupportedFunctionWithWarn("","",null,"");
    }

    @Test
    public void testStartReadingScripts(){

        spy.startReadingScripts(1);
        verify(spy, times(1)).startReadingScripts(1);
    }

    @Test
    public void testEndReadingScripts(){

        spy.endReadingScripts();
        verify(spy, times(1)).endReadingScripts();
    }

    @Test
    public void testStartScript(){

        spy.startScript("");
        verify(spy, times(1)).startScript("");
    }

    @Test
    public void testEndScript(){

        spy.endScript();
        verify(spy, times(1)).endScript();
    }
}