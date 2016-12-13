import controller.CommentsResource;
import controller.MediaResource;
import controller.SessionResource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class ApplicationConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(SessionResource.class,MediaResource.class, CommentsResource.class));
    }
}