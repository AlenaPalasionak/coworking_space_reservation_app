package org.example.coworking.loader;

import org.example.coworking.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MenuLoader extends AbstractLoaderImpl<Menu> {

    private final String filePath;

    @Autowired
    public MenuLoader(@Value("${menu.path}") String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected String getFilepath() {
        return filePath;
    }
}
