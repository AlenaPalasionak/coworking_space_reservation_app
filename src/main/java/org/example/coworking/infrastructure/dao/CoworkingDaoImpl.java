package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.json_loader.JsonLoader;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;

public class CoworkingDaoImpl implements CoworkingDao {
    private static List<CoworkingSpace> coworkingSpacesCache;
    private final JsonLoader coworkingSpaceJsonLoader;

    public CoworkingDaoImpl(JsonLoader coworkingSpaceJsonLoader) {
        this.coworkingSpaceJsonLoader = coworkingSpaceJsonLoader;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        boolean isUniqueIdGenerated;
        int generatedId;
        do {
            generatedId = IdGenerator.generateCoworkingId();
            isUniqueIdGenerated = true;

            for (CoworkingSpace coworkingSpaceFromCache : coworkingSpacesCache) {
                if (coworkingSpaceFromCache.getId() == generatedId) {
                    isUniqueIdGenerated = false;
                    break;
                }
            }
        } while (!isUniqueIdGenerated);
            coworkingSpace.setId(generatedId);
            coworkingSpacesCache.add(coworkingSpace);
        }


        @Override public List<CoworkingSpace> getAllSpaces () {
            return coworkingSpacesCache;
        }

        @Override public void deleteById ( int coworkingId){
            coworkingSpacesCache.removeIf(coworking -> coworking.getId() == coworkingId);
        }

        @Override public void updateSpace (CoworkingSpace newCoworkingSpace){
            for (int i = 0; i < coworkingSpacesCache.size(); i++) {
                CoworkingSpace oldCoworking = coworkingSpacesCache.get(i);
                if (oldCoworking.getId() == newCoworkingSpace.getId()) {
                    coworkingSpacesCache.set(i, newCoworkingSpace);
                    break;
                }
            }
        }

        @Override public CoworkingSpace getById ( int id){
            return coworkingSpacesCache.get(id);
        }

        @Override public void getCoworkingPlacesFromJson () {
            if (coworkingSpacesCache == null) {
                coworkingSpacesCache = getFromJson();
            }
        }

        @Override public void saveToJSON () {
            coworkingSpaceJsonLoader.convertToJson(coworkingSpacesCache);
        }

        private List<CoworkingSpace> getFromJson () {
            coworkingSpacesCache = coworkingSpaceJsonLoader.loadFromJson(CoworkingSpace.class);
            return coworkingSpacesCache;
        }
    }
