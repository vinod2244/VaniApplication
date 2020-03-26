package com.shukla.tech.hospitalapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.Serializable;

import static com.shukla.tech.hospitalapplication.VaniHearingDatabase.TABLE_NAME_ADD;

@Entity(tableName = TABLE_NAME_ADD)
public class AddData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "district")
    private String district;

    @ColumnInfo(name = "contact")
    private String contact;

    @ColumnInfo(name = "alternateContact")
    private String alternateContact;

    @ColumnInfo(name = "idCard")
    private String idCard;

    @ColumnInfo(name = "idCardValue")
    private String idCardValue;

    @ColumnInfo(name = "earProblem")
    private String earProblem;

    @ColumnInfo(name = "hearingLoss")
    private String hearingLoss;

    @ColumnInfo(name = "speechDelay")
    private String speechDelay;

    @ColumnInfo(name = "others")
    private String others;

    @ColumnInfo(name = "otoscopicImageLeft")
    private String otoscopicImageLeft;

    @ColumnInfo(name = "otoscopicImageRight")
    private String otoscopicImageRight;

    @ColumnInfo(name = "waxLeft")
    private String waxLeft;

    @ColumnInfo(name = "waxRight")
    private String waxRight;

    @ColumnInfo(name = "earDischargeLeft")
    private String earDischargeLeft;

    @ColumnInfo(name = "earDischargeRight")
    private String earDischargeRight;

    @ColumnInfo(name = "perforationLeft")
    private String perforationLeft;

    @ColumnInfo(name = "perforationRight")
    private String perforationRight;

    @ColumnInfo(name = "audiogramReport")
    private String audiogramReport;

    @ColumnInfo(name = "audiogramLeft")
    private String audiogramLeft;

    @ColumnInfo(name = "audiogramRight")
    private String audiogramRight;

    @ColumnInfo(name = "hearingAidTrialLeftEar")
    private String hearingAidTrialLeftEar;

    @ColumnInfo(name = "hearingAidTrialRightEar")
    private String hearingAidTrialRightEar;

    @ColumnInfo(name = "hearingImprovement")
    private String hearingImprovement;

    @ColumnInfo(name = "noResponseR1")
    private String noResponseR1;

    @ColumnInfo(name = "noResponseR2")
    private String noResponseR2;

    @ColumnInfo(name = "noResponseR3")
    private String noResponseR3;

    @ColumnInfo(name = "noResponseR4")
    private String noResponseR4;

    @ColumnInfo(name = "noResponseR5")
    private String noResponseR5;

    @ColumnInfo(name = "normalSpeech")
    private String normalSpeech;

    @ColumnInfo(name = "delayedSpeech")
    private String delayedSpeech;

    @ColumnInfo(name = "stammering")
    private String stammering;

    @ColumnInfo(name = "misArticulation")
    private String misArticulation;

    @ColumnInfo(name = "puberphonia")
    private String puberphonia;

    @ColumnInfo(name = "earDischargeProblem")
    private String earDischargeProblem;

    @ColumnInfo(name = "hearingProblem")
    private String hearingProblem;

    @ColumnInfo(name = "earPerforation")
    private String earPerforation;

    @ColumnInfo(name = "speechProblem")
    private String speechProblem;

    @ColumnInfo(name = "othersFinalScreeningFinding")
    private String othersFinalScreeningFinding;

    public AddData(String date, String name, String age, String gender, String address, String district, String contact, String alternateContact, String idCard, String idCardValue,
                   String earProblem, String hearingLoss, String speechDelay, String others, String otoscopicImageLeft, String otoscopicImageRight,
                   String waxLeft, String waxRight, String earDischargeLeft, String earDischargeRight, String perforationLeft, String perforationRight,
                   String audiogramReport, String audiogramLeft, String audiogramRight, String hearingAidTrialLeftEar, String hearingAidTrialRightEar,
                   String hearingImprovement, String noResponseR1, String noResponseR2, String noResponseR3, String noResponseR4, String noResponseR5,
                   String normalSpeech, String delayedSpeech, String stammering, String misArticulation, String puberphonia, String earDischargeProblem,
                   String hearingProblem, String earPerforation, String speechProblem, String othersFinalScreeningFinding) {
        this.date = date;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.district = district;
        this.contact = contact;
        this.alternateContact = alternateContact;
        this.idCard = idCard;
        this.idCardValue = idCardValue;
        this.earProblem = earProblem;
        this.hearingLoss = hearingLoss;
        this.speechDelay = speechDelay;
        this.others = others;
        this.otoscopicImageLeft = otoscopicImageLeft;
        this.otoscopicImageRight = otoscopicImageRight;
        this.waxLeft = waxLeft;
        this.waxRight = waxRight;
        this.earDischargeLeft = earDischargeLeft;
        this.earDischargeRight = earDischargeRight;
        this.perforationLeft = perforationLeft;
        this.perforationRight = perforationRight;
        this.audiogramReport = audiogramReport;
        this.audiogramLeft = audiogramLeft;
        this.audiogramRight = audiogramRight;
        this.hearingAidTrialLeftEar = hearingAidTrialLeftEar;
        this.hearingAidTrialRightEar = hearingAidTrialRightEar;
        this.hearingImprovement = hearingImprovement;
        this.noResponseR1 = noResponseR1;
        this.noResponseR2 = noResponseR2;
        this.noResponseR3 = noResponseR3;
        this.noResponseR4 = noResponseR4;
        this.noResponseR5 = noResponseR5;
        this.normalSpeech = normalSpeech;
        this.delayedSpeech = delayedSpeech;
        this.stammering = stammering;
        this.misArticulation = misArticulation;
        this.puberphonia = puberphonia;
        this.earDischargeProblem = earDischargeProblem;
        this.hearingProblem = hearingProblem;
        this.earPerforation = earPerforation;
        this.speechProblem = speechProblem;
        this.othersFinalScreeningFinding = othersFinalScreeningFinding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAlternateContact() {
        return alternateContact;
    }

    public void setAlternateContact(String alternateContact) {
        this.alternateContact = alternateContact;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardValue() {
        return idCardValue;
    }

    public void setIdCardValue(String idCardValue) {
        this.idCardValue = idCardValue;
    }

    public String getEarProblem() {
        return earProblem;
    }

    public void setEarProblem(String earProblem) {
        this.earProblem = earProblem;
    }

    public String getHearingLoss() {
        return hearingLoss;
    }

    public void setHearingLoss(String hearingLoss) {
        this.hearingLoss = hearingLoss;
    }

    public String getSpeechDelay() {
        return speechDelay;
    }

    public void setSpeechDelay(String speechDelay) {
        this.speechDelay = speechDelay;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOtoscopicImageLeft() {
        return otoscopicImageLeft;
    }

    public void setOtoscopicImageLeft(String otoscopicImageLeft) {
        this.otoscopicImageLeft = otoscopicImageLeft;
    }

    public String getOtoscopicImageRight() {
        return otoscopicImageRight;
    }

    public void setOtoscopicImageRight(String otoscopicImageRight) {
        this.otoscopicImageRight = otoscopicImageRight;
    }

    public String getWaxLeft() {
        return waxLeft;
    }

    public void setWaxLeft(String waxLeft) {
        this.waxLeft = waxLeft;
    }

    public String getWaxRight() {
        return waxRight;
    }

    public void setWaxRight(String waxRight) {
        this.waxRight = waxRight;
    }

    public String getEarDischargeLeft() {
        return earDischargeLeft;
    }

    public void setEarDischargeLeft(String earDischargeLeft) {
        this.earDischargeLeft = earDischargeLeft;
    }

    public String getEarDischargeRight() {
        return earDischargeRight;
    }

    public void setEarDischargeRight(String earDischargeRight) {
        this.earDischargeRight = earDischargeRight;
    }

    public String getPerforationLeft() {
        return perforationLeft;
    }

    public void setPerforationLeft(String perforationLeft) {
        this.perforationLeft = perforationLeft;
    }

    public String getPerforationRight() {
        return perforationRight;
    }

    public void setPerforationRight(String perforationRight) {
        this.perforationRight = perforationRight;
    }

    public String getAudiogramReport() {
        return audiogramReport;
    }

    public void setAudiogramReport(String audiogramReport) {
        this.audiogramReport = audiogramReport;
    }

    public String getAudiogramLeft() {
        return audiogramLeft;
    }

    public void setAudiogramLeft(String audiogramLeft) {
        this.audiogramLeft = audiogramLeft;
    }

    public String getAudiogramRight() {
        return audiogramRight;
    }

    public void setAudiogramRight(String audiogramRight) {
        this.audiogramRight = audiogramRight;
    }

    public String getHearingAidTrialLeftEar() {
        return hearingAidTrialLeftEar;
    }

    public void setHearingAidTrialLeftEar(String hearingAidTrialLeftEar) {
        this.hearingAidTrialLeftEar = hearingAidTrialLeftEar;
    }

    public String getHearingAidTrialRightEar() {
        return hearingAidTrialRightEar;
    }

    public void setHearingAidTrialRightEar(String hearingAidTrialRightEar) {
        this.hearingAidTrialRightEar = hearingAidTrialRightEar;
    }

    public String getHearingImprovement() {
        return hearingImprovement;
    }

    public void setHearingImprovement(String hearingImprovement) {
        this.hearingImprovement = hearingImprovement;
    }

    public String getNoResponseR1() {
        return noResponseR1;
    }

    public void setNoResponseR1(String noResponseR1) {
        this.noResponseR1 = noResponseR1;
    }

    public String getNoResponseR2() {
        return noResponseR2;
    }

    public void setNoResponseR2(String noResponseR2) {
        this.noResponseR2 = noResponseR2;
    }

    public String getNoResponseR3() {
        return noResponseR3;
    }

    public void setNoResponseR3(String noResponseR3) {
        this.noResponseR3 = noResponseR3;
    }

    public String getNoResponseR4() {
        return noResponseR4;
    }

    public void setNoResponseR4(String noResponseR4) {
        this.noResponseR4 = noResponseR4;
    }

    public String getNoResponseR5() {
        return noResponseR5;
    }

    public void setNoResponseR5(String noResponseR5) {
        this.noResponseR5 = noResponseR5;
    }

    public String getNormalSpeech() {
        return normalSpeech;
    }

    public void setNormalSpeech(String normalSpeech) {
        this.normalSpeech = normalSpeech;
    }

    public String getDelayedSpeech() {
        return delayedSpeech;
    }

    public void setDelayedSpeech(String delayedSpeech) {
        this.delayedSpeech = delayedSpeech;
    }

    public String getStammering() {
        return stammering;
    }

    public void setStammering(String stammering) {
        this.stammering = stammering;
    }

    public String getMisArticulation() {
        return misArticulation;
    }

    public void setMisArticulation(String misArticulation) {
        this.misArticulation = misArticulation;
    }

    public String getPuberphonia() {
        return puberphonia;
    }

    public void setPuberphonia(String puberphonia) {
        this.puberphonia = puberphonia;
    }

    public String getEarDischargeProblem() {
        return earDischargeProblem;
    }

    public void setEarDischargeProblem(String earDischargeProblem) {
        this.earDischargeProblem = earDischargeProblem;
    }

    public String getHearingProblem() {
        return hearingProblem;
    }

    public void setHearingProblem(String hearingProblem) {
        this.hearingProblem = hearingProblem;
    }

    public String getEarPerforation() {
        return earPerforation;
    }

    public void setEarPerforation(String earPerforation) {
        this.earPerforation = earPerforation;
    }

    public String getSpeechProblem() {
        return speechProblem;
    }

    public void setSpeechProblem(String speechProblem) {
        this.speechProblem = speechProblem;
    }

    public String getOthersFinalScreeningFinding() {
        return othersFinalScreeningFinding;
    }

    public void setOthersFinalScreeningFinding(String othersFinalScreeningFinding) {
        this.othersFinalScreeningFinding = othersFinalScreeningFinding;
    }
}
