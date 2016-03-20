/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chienpao.notepad.notepad.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

// Your model just have to extend RealmObject.
// This will inherit an annotation which produces proxy getters and setters for all fields.
public class Patient extends RealmObject {

    // All fields are by default persisted.
    private String patientLastName;
    private String patientFirstName;
    private String patientDateOfBirth;
    private String firstClinicHospital;
    private String firstDoctorName;
    private String expectDate;
    private String secondClinicHospital;
    private String secondDoctorName;
    private String actualDate;

    // Constructor
    public Patient() {
    }

    public Patient(String actualDate, String expectDate, String firstClinicHospital, String firstDoctorName, String patientDateOfBirth, String patientFirstName, String patientLastName, String secondClinicHospital, String secondDoctorName) {
        this.actualDate = actualDate;
        this.expectDate = expectDate;
        this.firstClinicHospital = firstClinicHospital;
        this.firstDoctorName = firstDoctorName;
        this.patientDateOfBirth = patientDateOfBirth;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.secondClinicHospital = secondClinicHospital;
        this.secondDoctorName = secondDoctorName;
    }

    // Other objects in a one-to-one relation must also subclass RealmObject
    //private Dog dog;

    // One-to-many relations is simply a RealmList of the objects which also subclass RealmObject
    //private RealmList<Cat> cats;

    // You can instruct Realm to ignore a field and not persist it.
    @Ignore
    private int tempReference;

    private long id;

    // The standard getters and setters your IDE generates are fine.
    // Realm will overload them and code inside them is ignored.
    // So if you prefer you can also just have empty abstract methods.


    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(String expectDate) {
        this.expectDate = expectDate;
    }

    public String getFirstClinicHospital() {
        return firstClinicHospital;
    }

    public void setFirstClinicHospital(String firstClinicHospital) {
        this.firstClinicHospital = firstClinicHospital;
    }

    public String getFirstDoctorName() {
        return firstDoctorName;
    }

    public void setFirstDoctorName(String firstDoctorName) {
        this.firstDoctorName = firstDoctorName;
    }

    public String getPatientDateOfBirth() {
        return patientDateOfBirth;
    }

    public void setPatientDateOfBirth(String patientDateOfBirth) {
        this.patientDateOfBirth = patientDateOfBirth;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getSecondClinicHospital() {
        return secondClinicHospital;
    }

    public void setSecondClinicHospital(String secondClinicHospital) {
        this.secondClinicHospital = secondClinicHospital;
    }

    public String getSecondDoctorName() {
        return secondDoctorName;
    }

    public void setSecondDoctorName(String secondDoctorName) {
        this.secondDoctorName = secondDoctorName;
    }

    public int getTempReference() {
        return tempReference;
    }

    public void setTempReference(int tempReference) {
        this.tempReference = tempReference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
