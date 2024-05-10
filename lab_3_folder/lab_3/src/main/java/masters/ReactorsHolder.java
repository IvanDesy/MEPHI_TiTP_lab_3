package masters;

import java.util.HashMap;
import java.util.Map;

import Objects.Reactor;

public class ReactorsHolder {
    private Map<String, Reactor> reactorMap;

    public ReactorsHolder() {
        this.reactorMap = new HashMap<>();
    }
    public void addReactor(String key, Reactor reactor) {
        reactorMap.put(key, reactor);
    }
    public Map<String, Reactor> getReactorMap() {
        return reactorMap;
    }
}
