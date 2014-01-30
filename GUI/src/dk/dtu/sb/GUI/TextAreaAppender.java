package dk.dtu.sb.GUI;

import java.io.IOException;
import java.io.OutputStream;

import ch.qos.logback.core.OutputStreamAppender;

public class TextAreaAppender<ILoggingEvent> extends OutputStreamAppender<ILoggingEvent> {

    @Override
    public void start() {
        OutputStream stream = new OutputStream() {
            
            private StringBuilder buffer = new StringBuilder();
            
            @Override
            public void write(int b) throws IOException {
                char c = (char) b;
                String value = Character.toString(c);
                buffer.append(value);
                if (value.equals("\n")) {
                    Model.log(buffer.toString());
                    buffer.delete(0, buffer.length());
                }
                
            }
        };
        setOutputStream(stream);
        super.start();
    }


}
