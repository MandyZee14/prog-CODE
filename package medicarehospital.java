package medicarehospital.model;

public enum PatientCategory {
    INPATIENT,
    OUTPATIENT,
    EMERGENCY
}
package medicarehospital.model;

public class Patient {

    private String patientId;
    private String firstName;
    private String surname;
    private int age;
    private String gender;
    private String medicalCondition;
    private PatientCategory category;

    public Patient(String patientId, String firstName, String surname,
                   int age, String gender, String medicalCondition,
                   PatientCategory category) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public PatientCategory getCategory() {
        return category;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public void displayDetails() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("First Name: " + firstName);
        System.out.println("Surname: " + surname);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Medical Condition: " + medicalCondition);
        System.out.println("Category: " + category);
    }
}
package medicarehospital.model;

public class Inpatient extends Patient {

    private int wardNumber;
    private int bedNumber;

    public Inpatient(String patientId, String firstName, String surname,
                     int age, String gender, String medicalCondition,
                     int wardNumber, int bedNumber) {

        super(patientId, firstName, surname, age, gender,
              medicalCondition, PatientCategory.INPATIENT);

        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Ward Number: " + wardNumber);
        System.out.println("Bed Number: B" + String.format("%02d", bedNumber));
    }
}
package medicarehospital.model;

public class Bed {

    private int bedNumber;
    private Inpatient inpatient;

    public Bed(int bedNumber) {
        this.bedNumber = bedNumber;
        this.inpatient = null;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public Inpatient getInpatient() {
        return inpatient;
    }

    public boolean isOccupied() {
        return inpatient != null;
    }

    public void assignInpatient(Inpatient inpatient) {
        this.inpatient = inpatient;
    }

    public void releaseBed() {
        this.inpatient = null;
    }

    @Override
    public String toString() {
        if (isOccupied()) {
            return "B" + String.format("%02d", bedNumber)
                    + " - Occupied by "
                    + inpatient.getPatientId();
        }

        return "B" + String.format("%02d", bedNumber)
                + " - Available";
    }
}