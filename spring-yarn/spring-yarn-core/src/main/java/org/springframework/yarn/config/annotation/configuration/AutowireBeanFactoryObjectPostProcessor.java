package org.springframework.yarn.config.annotation.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.Assert;
import org.springframework.yarn.config.annotation.ObjectPostProcessor;


final class AutowireBeanFactoryObjectPostProcessor implements ObjectPostProcessor<Object>, DisposableBean, SmartLifecycle {

	private final static Log log = LogFactory.getLog(AutowireBeanFactoryObjectPostProcessor.class);

    private final AutowireCapableBeanFactory autowireBeanFactory;
    private final List<DisposableBean> disposableBeans = new ArrayList<DisposableBean>();
    private final List<Lifecycle> lifecycleBeans = new ArrayList<Lifecycle>();

    private boolean running;

    public AutowireBeanFactoryObjectPostProcessor(AutowireCapableBeanFactory autowireBeanFactory) {
        Assert.notNull(autowireBeanFactory, "autowireBeanFactory cannot be null");
        this.autowireBeanFactory = autowireBeanFactory;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.Initializer#initialize(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T postProcess(T object) {
        T result = (T) autowireBeanFactory.initializeBean(object, null);
        if(result instanceof DisposableBean) {
            disposableBeans.add((DisposableBean) result);
        }
        if(result instanceof Lifecycle) {
        	lifecycleBeans.add((Lifecycle) result);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        for(DisposableBean disposable : disposableBeans) {
            try {
                disposable.destroy();
            } catch(Exception error) {
            	log.error(error);
            }
        }
    }

	@Override
	public void start() {
		running = true;
		for (Lifecycle bean : lifecycleBeans) {
			bean.start();
		}
	}

	@Override
	public void stop() {
		for (Lifecycle bean : lifecycleBeans) {
			bean.stop();
		}
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

}
