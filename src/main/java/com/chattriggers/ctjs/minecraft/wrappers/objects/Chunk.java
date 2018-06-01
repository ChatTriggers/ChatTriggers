package com.chattriggers.ctjs.minecraft.wrappers.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Chunk {
    @Getter
    private net.minecraft.world.chunk.Chunk chunk;

    public Chunk(net.minecraft.world.chunk.Chunk chunkIn) {
        this.chunk = chunkIn;
    }

    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    public List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>();

        Arrays.stream(this.chunk.getEntityLists()).forEach(list -> entities.addAll(
                list.stream()
                    .map(Entity::new)
                    .collect(Collectors.toList())
        ));

        return entities;
    }

    /**
     * Gets every entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use {@code Java.type().class} to get this)
     * @return the entity list
     */
    public List<Entity> getAllEntitiesOfType(Class clazz) {
        return getAllEntities()
                .stream()
                .filter(entity -> entity.getClass().equals(clazz))
                .collect(Collectors.toList());
    }
}
