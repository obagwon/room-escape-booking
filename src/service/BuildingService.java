package service;

import model.Building.Building;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Building Service Layer interacts after being called by the controller from  View.
 *
 * @author 220031985
 */
public class BuildingService implements Serializable {
    private static final String DATA_FILE = "buildingData.txt";

    private Map<String, Building> buildings = new HashMap<>();

    /**
     * Constructor BuildingService.
     */
    public BuildingService() {
    }

    public Map<String, Building> getBuildings() {
        return buildings;
    }

    /**
     * Adding the Buildings to the Model.
     *
     * @param buildingName Building Name.
     * @param address      Building Address.
     * @param email        Email of  the User (Log Purposes).
     */
    public void addBuilding(String buildingName, String address, String email) {

        if (buildings.containsKey(buildingName.toLowerCase())) {
            throw new IllegalArgumentException("이미 등록된 지점입니다.\n");
        } else if (buildingName.isEmpty()) {
            throw new IllegalArgumentException("지점명을 입력해주세요.\n");
        } else if (address.isEmpty()) {
            throw new IllegalArgumentException("지점 주소를 입력해주세요.\n");
        } else {

            buildings.put(buildingName.toLowerCase(), new Building(buildingName, address, email, null));
        }
    }

    /**
     * View Buildings present in the system.
     *
     * @return
     */
    public Map<String, Building> viewBuildings() {
        Map<String, Building> viewBuildings = new HashMap<>();
        if (buildings.isEmpty()) {
            throw new IllegalArgumentException("등록된 지점이 없습니다.\n");
        } else {
            viewBuildings.putAll(buildings);
            return viewBuildings;
        }
    }


    /**
     * Delete the Building
     *
     * @param buildingName Building Name.
     */
    // Additional Info: Ability to delete room if the building is deleted
    // (Addressed).
    public void delBuilding(String buildingName) {
        if (buildings.containsKey(buildingName.toLowerCase())) {
            buildings.remove(buildingName.toLowerCase());

        } else if (buildingName.isEmpty()) {
            throw new IllegalArgumentException("지점명을 입력해주세요.");
        } else {
            throw new IllegalArgumentException("등록되지 않은 지점입니다.");
        }
    }

    /**
     * Check if the Building exists.
     *
     * @param buildingName Building Name.
     * @return true if contains.
     */
    public boolean checkBuilding(String buildingName) {

        if (buildings.containsKey(buildingName.toLowerCase())) {
            return true;
        } else {
            throw new IllegalArgumentException("등록되지 않은 지점입니다.\n");
        }
    }

    /**
     * Save the Building Data.
     *
     * @throws IOException throws IOException.
     */
    public void buildSave() throws IOException {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            o.writeObject(buildings);
        }
    }

    /**
     * Load the Building Data.
     */
    public boolean buildLoad() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file))) {
            buildings = (Map<String, Building>) oi.readObject();
            return true;
        }
    }

    /**
     * For test purposes.
     *
     * @return size of the building map.
     */
    public int buildCount() {
        return buildings.size();
    }
}
