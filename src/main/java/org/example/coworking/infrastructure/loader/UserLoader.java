package org.example.coworking.infrastructure.loader;

import org.example.coworking.model.User;

public class UserLoader extends AbstractLoaderImpl<User> {

        private final String filePath;

        public UserLoader(String filePath) {
            super();
            this.filePath = filePath;
        }

        @Override
        protected String getFilepath() {
            return filePath;
        }
    }
