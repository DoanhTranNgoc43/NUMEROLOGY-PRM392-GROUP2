package com.example.numerology_prm392_group2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LotteryResult implements Serializable {
    private String resultId;
    private Date drawDate;
    private String specialPrize;
    private String firstPrize;
    private List<String> secondPrizes;
    private List<String> thirdPrizes;
    private List<String> fourthPrizes;
    private List<String> fifthPrizes;
    private List<String> sixthPrizes;
    private List<String> seventhPrizes;
    private List<String> eighthPrizes;

    public LotteryResult() {}

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }

    public Date getDrawDate() { return drawDate; }
    public void setDrawDate(Date drawDate) { this.drawDate = drawDate; }

    public String getSpecialPrize() { return specialPrize; }
    public void setSpecialPrize(String specialPrize) { this.specialPrize = specialPrize; }

    public String getFirstPrize() { return firstPrize; }
    public void setFirstPrize(String firstPrize) { this.firstPrize = firstPrize; }

    public List<String> getSecondPrizes() { return secondPrizes; }
    public void setSecondPrizes(List<String> secondPrizes) { this.secondPrizes = secondPrizes; }

    public List<String> getThirdPrizes() { return thirdPrizes; }
    public void setThirdPrizes(List<String> thirdPrizes) { this.thirdPrizes = thirdPrizes; }

    public List<String> getFourthPrizes() { return fourthPrizes; }
    public void setFourthPrizes(List<String> fourthPrizes) { this.fourthPrizes = fourthPrizes; }

    public List<String> getFifthPrizes() { return fifthPrizes; }
    public void setFifthPrizes(List<String> fifthPrizes) { this.fifthPrizes = fifthPrizes; }

    public List<String> getSixthPrizes() { return sixthPrizes; }
    public void setSixthPrizes(List<String> sixthPrizes) { this.sixthPrizes = sixthPrizes; }

    public List<String> getSeventhPrizes() { return seventhPrizes; }
    public void setSeventhPrizes(List<String> seventhPrizes) { this.seventhPrizes = seventhPrizes; }

    public List<String> getEighthPrizes() { return eighthPrizes; }
    public void setEighthPrizes(List<String> eighthPrizes) { this.eighthPrizes = eighthPrizes; }

    public List<String> getAllTwoDigitWinningNumbers() {
        List<String> twoDigitNumbers = new ArrayList<>();

        if (specialPrize != null && specialPrize.length() >= 2) {
            twoDigitNumbers.add(specialPrize.substring(specialPrize.length() - 2));
        }

        if (firstPrize != null && firstPrize.length() >= 2) {
            twoDigitNumbers.add(firstPrize.substring(firstPrize.length() - 2));
        }

        addTwoDigitsFromList(twoDigitNumbers, secondPrizes);
        addTwoDigitsFromList(twoDigitNumbers, thirdPrizes);
        addTwoDigitsFromList(twoDigitNumbers, fourthPrizes);
        addTwoDigitsFromList(twoDigitNumbers, fifthPrizes);
        addTwoDigitsFromList(twoDigitNumbers, sixthPrizes);
        addTwoDigitsFromList(twoDigitNumbers, seventhPrizes);
        addTwoDigitsFromList(twoDigitNumbers, eighthPrizes);

        return twoDigitNumbers;
    }

    private void addTwoDigitsFromList(List<String> twoDigitNumbers, List<String> prizes) {
        if (prizes != null) {
            for (String prize : prizes) {
                if (prize != null && prize.length() >= 2) {
                    twoDigitNumbers.add(prize.substring(prize.length() - 2));
                }
            }
        }
    }
}