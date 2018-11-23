package com.vehicles.monitor.utils;

import com.vehicles.monitor.config.YAMLConfig;
import com.vehicles.monitor.model.Company;
import com.vehicles.monitor.model.VehicleInfoDomain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StoreUtils {

    public static String statusId(String companyName, String vehicleId) {
        return companyName + "-" + vehicleId;
    }

    public static void initializeVehiclesByCompany(Company company,
                                                   Map<String, LocalDateTime> vidToTime,
                                                   Map<String, List<VehicleInfoDomain>> companyToVehicles,
                                                   Map<String, List<VehicleInfoDomain>> vidToVehicles) {
        Map<String, String> vehicles = company.getVehicles();

        for (String vid : vehicles.keySet()) {
            String regNr = vehicles.get(vid);
            VehicleInfoDomain vehicleInfoDomain = new VehicleInfoDomain(vid, regNr, company.getName(), company.getAddress());
            vidToTime.putIfAbsent(vid, LocalDateTime.MIN);
            initCompanyToVehicles(companyToVehicles, vehicleInfoDomain);
            initVidToVehicles(vidToVehicles, vehicleInfoDomain);
        }
    }

    private static void initCompanyToVehicles(Map<String, List<VehicleInfoDomain>> companyToVehicles, VehicleInfoDomain vehicleInfoDomain) {
        String companyName = vehicleInfoDomain.getCompanyName();
        if (!companyToVehicles.containsKey(companyName)) {
            companyToVehicles.putIfAbsent(companyName, new ArrayList<>());
        }
        List<VehicleInfoDomain> vehicles = companyToVehicles.get(companyName);
        vehicles.add(vehicleInfoDomain);
//        companyToVehicles.put(companyName, vehicles);
    }

    private static void initVidToVehicles(Map<String, List<VehicleInfoDomain>> vidToVehicles, VehicleInfoDomain vehicleInfoDomain) {
        String vehicleId = vehicleInfoDomain.getVehicleId();
        if (!vidToVehicles.containsKey(vehicleId)) {
            vidToVehicles.putIfAbsent(vehicleId, new ArrayList<>());
        }
        List<VehicleInfoDomain> vehicleInfos = vidToVehicles.get(vehicleId);
        vehicleInfos.add(vehicleInfoDomain);
//        vidToVehicles.put(vehicleId, vehicleInfos);
    }

    public static void initializeVehicles(YAMLConfig ymlConfig,
                                          Map<String, LocalDateTime> vidToTime,
                                          Map<String, List<VehicleInfoDomain>> companyToVehicles,
                                          Map<String, List<VehicleInfoDomain>> vidToVehicles) {
        if(vidToTime.size() > 0 || companyToVehicles.size() > 0 || vidToVehicles.size() > 0 ) {
            return;
        }

        for (Company company : ymlConfig.getCompanies()) {
            initializeVehiclesByCompany(company, vidToTime, companyToVehicles, vidToVehicles);
        }
    }
}
