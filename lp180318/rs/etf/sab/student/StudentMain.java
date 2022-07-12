package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

public class StudentMain {

    public static void main(String[] args) {
        AddressOperations addressOperations = new lp180318_AddressOperation(); // Change this to your implementation.
        CityOperations cityOperations = new lp180318_CityOperation(); // Do it for all classes.
        CourierOperations courierOperations = new lp180318_CourierOperation(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new lp180318_CourierRequestOperation();
        DriveOperation driveOperation = new lp180318_DriveOperation();
        GeneralOperations generalOperations = new lp180318_GeneralOperation();
        PackageOperations packageOperations = new lp180318_PackageOperation();
        StockroomOperations stockroomOperations = new lp180318_StockroomOperation();
        UserOperations userOperations = new lp180318_UserOperation();
        VehicleOperations vehicleOperations = new lp180318_VehicleOperation();

        TestHandler.createInstance(
                addressOperations,
                cityOperations,
                courierOperations,
                courierRequestOperation,
                driveOperation,
                generalOperations,
                packageOperations,
                stockroomOperations,
                userOperations,
                vehicleOperations);

        TestRunner.runTests();   

    }
}
