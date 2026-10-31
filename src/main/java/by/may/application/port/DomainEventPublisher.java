package by.may.application.port;

public interface DomainEventPublisher {
    void publish(Object event);
}
