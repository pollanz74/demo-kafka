package com.example.demo.kafka.shutdown;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ContainerStoppedEvent;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
public class ContainerListener {

	private final ApplicationShutdown shutdown;

	@EventListener
	public void onContainerStopped(final ContainerStoppedEvent event) {
		if (log.isDebugEnabled()) log.debug("onContainerStopped({})", event);
		shutdown.shutdownApplication();
	}

}
