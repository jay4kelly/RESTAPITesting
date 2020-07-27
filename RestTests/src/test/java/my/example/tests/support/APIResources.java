package my.example.tests.support;

import java.util.HashMap;
import java.util.Map;

public enum APIResources {

    UsersResource("/public-api/users"),
    PostsResource("/public-api/posts"),
    CommentsResource("/public-api/comments"),
    AlbumsResource("/public-api/albums"),
    PhotosResource("/public-api/photos");

    private static final Map<String, APIResources> BY_RESOURCE = new HashMap<String, APIResources>();

    public final String resource;

    static {
        for (APIResources r: values()) {
            BY_RESOURCE.put(r.resource, r);
        }
    }

    APIResources(String resource){
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

}
