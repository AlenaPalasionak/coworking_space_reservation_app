package org.example.coworking.loader;

import org.example.coworking.model.Menu;

public class MenuLoader extends AbstractLoaderImpl<Menu> {

    private final String filePath;

    public MenuLoader(String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected String getFilepath() {
        return filePath;
    }
}
