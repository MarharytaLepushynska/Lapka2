package org.example;

import java.util.Arrays;

class Utils {

    /**
     *
     * @param objects
     * @param entity
     * @return -1 if entity isn't in the array, else returns index of entity
     */
    public static int getIndexOfEntity (Object [] objects, Object entity) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].equals(entity)) {
                return i;
            }
        }
        return -1;
    }

}
