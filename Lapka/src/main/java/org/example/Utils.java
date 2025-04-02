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
    // feels like a terrible code...
    public static Object [] appendEntity (Object [] entities, Object entity, Class entityClass) {
        Object[] newEntities = Arrays.copyOf(entities, entities.length + 1);
        for (int i = 0; i < entities.length; i++) {
            newEntities[i] = entityClass.cast(entities[i]);
        }
        newEntities[entities.length] = entity;
        return newEntities;
    }

}
