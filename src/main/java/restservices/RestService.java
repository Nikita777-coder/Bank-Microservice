package restservices;

public interface RestService {
    <T> T get(String uri, Class<T> typeKey);
}
