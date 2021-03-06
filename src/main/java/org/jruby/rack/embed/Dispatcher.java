package org.jruby.rack.embed;

import java.io.IOException;

import org.jruby.rack.AbstractRackDispatcher;
import org.jruby.rack.DefaultRackApplication;
import org.jruby.rack.RackApplication;
import org.jruby.rack.RackContext;
import org.jruby.rack.RackEnvironment;
import org.jruby.rack.RackInitializationException;
import org.jruby.rack.RackResponseEnvironment;
import org.jruby.runtime.builtin.IRubyObject;

public class Dispatcher extends AbstractRackDispatcher {

    private final IRubyObject application;
    private RackApplication rackApplication;

    public Dispatcher(RackContext rackContext, IRubyObject application) {
        super(rackContext);
        this.application = application;
    }

    @Override
    protected RackApplication getApplication() throws RackInitializationException {
        if (rackApplication == null) {
            rackApplication = new DefaultRackApplication(application);
            rackApplication.init();
        }
        return rackApplication;
    }
    
    @Override
    protected void afterException(
            RackEnvironment request, Exception re,
            RackResponseEnvironment response) throws IOException {
        // TODO a fast draft (probably should use rack.errors) :
        context.log("Error:", re);
        response.sendError(500);
    }

    @Override
    protected void afterProcess(RackApplication app) throws IOException {
        // NOOP
    }

    @Override
    public void destroy() {
        rackApplication.destroy();
    }
    
}
