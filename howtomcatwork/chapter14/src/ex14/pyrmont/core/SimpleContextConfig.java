package ex14.pyrmont.core;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

public class SimpleContextConfig implements LifecycleListener {
    public void lifecycleEvent(LifecycleEvent lifecycleEvent) {
        if (Lifecycle.START_EVENT.equals(lifecycleEvent.getType())) {
            Context context = (Context) lifecycleEvent.getLifecycle();
            context.setConfigured(true);
        }
    }
}
