package org.example.coworking.loader;

import org.example.coworking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class UserLoader extends AbstractLoaderImpl<User> {

    private final String filePath;

    @Autowired
    public UserLoader(@Value("${user.path}") String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected String getFilepath() {
        return filePath;
    }
}
