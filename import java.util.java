import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Bed;
import model.Inpatient;
import model.Patient;
import model.PatientCategory;

public class HospitalSystem {

    // Hospital has exactly one ward with 20 beds
    private static final int TOTAL_BEDS = 20;
    private static final int ROWS = 4;
    private static final int COLUMNS = 5;
    private static final int WARD_NUMBER = 1;

    private ArrayList<Patient> patients;
    private Bed[][] beds;

    // Constructor
    public HospitalSystem() {
        patients = new ArrayList<>();
        beds = new Bed[ROWS][COLUMNS];

        initializeBeds();
    }

    // ---------------------------------------------------------
    // BED INITIALIZATION
    // ---------------------------------------------------------

    private void initializeBeds() {

        int bedNumber = 1;

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                beds[row][column] = new Bed(bedNumber);
                bedNumber++;
            }
        }
    }

    // ---------------------------------------------------------
    // PATIENT MANAGEMENT
    // ---------------------------------------------------------

    /**
     * Registers a new patient.
     * Returns false if the Patient ID already exists.
     */
    public boolean registerPatient(Patient patient) {

        if (patient == null) {
            return false;
        }

        if (searchPatient(patient.getPatientId()) != null) {
            return false;
        }

        patients.add(patient);
        return true;
    }

    /**
     * Searches for a patient using Patient ID.
     */
    public Patient searchPatient(String patientId) {

        for (Patient patient : patients) {

            if (patient.getPatientId().equalsIgnoreCase(patientId)) {
                return patient;
            }
        }

        return null;
    }

    /**
     * Updates patient details.
     */
    public boolean updatePatient(String patientId,
                                 String firstName,
                                 String surname,
                                 int age,
                                 String gender,
                                 String medicalCondition) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        patient.setFirstName(firstName);
        patient.setSurname(surname);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);

        return true;
    }

    /**
     * Deletes a patient.
     * If the patient is an inpatient, their bed is released first.
     */
    public boolean deletePatient(String patientId) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        // Release bed if patient is an inpatient
        if (patient instanceof Inpatient) {
            releaseBed(patientId);
        }

        return patients.remove(patient);
    }

    /**
     * Returns a copy of the patient list.
     */
    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    /**
     * Displays all registered patients.
     */
    public void displayAllPatients() {

        if (patients.isEmpty()) {
            System.out.println("\nNo patients are currently registered.");
            return;
        }

        System.out.println("\n========== ALL PATIENTS ==========");

        for (Patient patient : patients) {
            patient.displayDetails();
            System.out.println("----------------------------------");
        }
    }

    // ---------------------------------------------------------
    // BED MANAGEMENT
    // ---------------------------------------------------------

    /**
     * Allocates a bed to an inpatient.
     *
     * bedNumber must be between 1 and 20.
     */
    public boolean allocateBed(String patientId, int bedNumber) {

        Patient patient = searchPatient(patientId);

        // Patient must exist
        if (patient == null) {
            return false;
        }

        // Only inpatients can receive beds
        if (!(patient instanceof Inpatient)) {
            return false;
        }

        Inpatient inpatient = (Inpatient) patient;

        // Check whether this inpatient already has a bed
        if (hasBed(patientId)) {
            return false;
        }

        // Check valid bed number
        if (bedNumber < 1 || bedNumber > TOTAL_BEDS) {
            return false;
        }

        Bed bed = getBed(bedNumber);

        // Prevent allocating an occupied bed
        if (bed.isOccupied()) {
            return false;
        }

        // Assign the inpatient
        bed.assignInpatient(inpatient);

        // Update inpatient's bed and ward
        inpatient.setBedNumber(bedNumber);
        inpatient.setWardNumber(WARD_NUMBER);

        return true;
    }

    /**
     * Releases the bed occupied by a patient.
     */
    public boolean releaseBed(String patientId) {

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                Bed bed = beds[row][column];

                if (bed.isOccupied()
                        && bed.getInpatient().getPatientId()
                        .equalsIgnoreCase(patientId)) {

                    bed.releaseBed();

                    Patient patient = searchPatient(patientId);

                    if (patient instanceof Inpatient) {
                        Inpatient inpatient = (Inpatient) patient;

                        inpatient.setBedNumber(0);
                        inpatient.setWardNumber(0);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether a patient currently has a bed.
     */
    public boolean hasBed(String patientId) {

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                Bed bed = beds[row][column];

                if (bed.isOccupied()
                        && bed.getInpatient().getPatientId()
                        .equalsIgnoreCase(patientId)) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets a specific bed using its bed number.
     */
    public Bed getBed(int bedNumber) {

        if (bedNumber < 1 || bedNumber > TOTAL_BEDS) {
            return null;
        }

        int index = bedNumber - 1;

        int row = index / COLUMNS;
        int column = index % COLUMNS;

        return beds[row][column];
    }

    /**
     * Checks whether all 20 beds are occupied.
     */
    public boolean areAllBedsOccupied() {

        return getOccupiedBedCount() == TOTAL_BEDS;
    }

    /**
     * Returns the number of occupied beds.
     */
    public int getOccupiedBedCount() {

        int count = 0;

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                if (beds[row][column].isOccupied()) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Returns the number of available beds.
     */
    public int getAvailableBedCount() {

        return TOTAL_BEDS - getOccupiedBedCount();
    }

    // ---------------------------------------------------------
    // DISPLAY BED INFORMATION
    // ---------------------------------------------------------

    /**
     * Displays the complete 4 x 5 ward layout.
     */
    public void displayWardLayout() {

        System.out.println("\n========== WARD 1 ==========");
        System.out.println("         4 x 5 BED LAYOUT");
        System.out.println();

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                Bed bed = beds[row][column];

                if (bed.isOccupied()) {

                    System.out.print(
                            "[B" + String.format("%02d", bed.getBedNumber())
                            + " OCC] ");

                } else {

                    System.out.print(
                            "[B" + String.format("%02d", bed.getBedNumber())
                            + " AVA] ");
                }
            }

            System.out.println();
        }
    }

    /**
     * Displays all available beds.
     */
    public void displayAvailableBeds() {

        System.out.println("\n========== AVAILABLE BEDS ==========");

        boolean found = false;

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                Bed bed = beds[row][column];

                if (!bed.isOccupied()) {

                    System.out.println(
                            "B" + String.format("%02d", bed.getBedNumber())
                            + " - Available");

                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No beds are available.");
        }
    }

    /**
     * Displays all occupied beds.
     */
    public void displayOccupiedBeds() {

        System.out.println("\n========== OCCUPIED BEDS ==========");

        boolean found = false;

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                Bed bed = beds[row][column];

                if (bed.isOccupied()) {

                    Inpatient inpatient = bed.getInpatient();

                    System.out.println(
                            "B" + String.format("%02d", bed.getBedNumber())
                            + " - Patient: "
                            + inpatient.getPatientId()
                            + " - "
                            + inpatient.getFirstName()
                            + " "
                            + inpatient.getSurname());

                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No beds are currently occupied.");
        }
    }

    // ---------------------------------------------------------
    // REPORTS
    // ---------------------------------------------------------

    /**
     * Returns the total number of registered patients.
     */
    public int getTotalPatients() {
        return patients.size();
    }

    /**
     * Calculates the ward occupancy percentage.
     */
    public double calculateOccupancyPercentage() {

        return (getOccupiedBedCount() / (double) TOTAL_BEDS) * 100;
    }

    /**
     * Displays the hospital report.
     */
    public void displayReport() {

        System.out.println("\n========================================");
        System.out.println("          HOSPITAL WARD REPORT");
        System.out.println("========================================");

        System.out.println(
                "Total Registered Patients: "
                + getTotalPatients());

        System.out.println(
                "Total Occupied Beds: "
                + getOccupiedBedCount());

        System.out.println(
                "Total Available Beds: "
                + getAvailableBedCount());

        System.out.println(
                "Total Beds: "
                + TOTAL_BEDS);

        System.out.printf(
                "Ward Occupancy: %.2f%%%n",
                calculateOccupancyPercentage());

        System.out.println("========================================");
    }

    // ---------------------------------------------------------
    // SORTING
    // ---------------------------------------------------------

    /**
     * Sorts patients alphabetically by surname.
     */
    public void sortPatientsBySurname() {

        patients.sort(
                Comparator.comparing(
                        Patient::getSurname,
                        String.CASE_INSENSITIVE_ORDER)
        );
    }

    /**
     * Sorts patients by Patient ID.
     */
    public void sortPatientsByPatientId() {

        patients.sort(
                Comparator.comparing(
                        Patient::getPatientId,
                        String.CASE_INSENSITIVE_ORDER)
        );
    }

    /**
     * Displays patients after sorting.
     */
    public void displaySortedPatients() {

        System.out.println("\n========== SORTED PATIENTS ==========");

        if (patients.isEmpty()) {
            System.out.println("No patients are registered.");
            return;
        }

        for (Patient patient : patients) {

            System.out.println(
                    patient.getPatientId()
                    + " - "
                    + patient.getFirstName()
                    + " "
                    + patient.getSurname());
        }
    }
}