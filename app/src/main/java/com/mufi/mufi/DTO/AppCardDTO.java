package com.mufi.mufi.DTO;

import java.io.Serializable;

public class AppCardDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String card1, card2, card3, card4;
    private String cardId;

    // 카드 번호가 16자리 한 번에 온 경우
    public AppCardDTO(String cardNumber, String cardId) {
        card1 = cardNumber.substring(0, 4);
        card2 = cardNumber.substring(4, 8);
        card3 = cardNumber.substring(8, 12);
        card4 = cardNumber.substring(12, 16);
        this.cardId = cardId;
    }

    // 카드 번호가 4자리씩 온 경우
    public AppCardDTO(String card1, String card2, String card3, String card4) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
    }

    public String getCard1() {
        return card1;
    }
    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public String getCard2() {
        return card2;
    }
    public void setCard2(String card2) {
        this.card2 = card2;
    }

    public String getCard3() {
        return card3;
    }
    public void setCard3(String card3) {
        this.card3 = card3;
    }

    public String getCard4() {
        return card4;
    }
    public void setCard4(String card4) {
        this.card4 = card4;
    }

    public String getCardNumber() {
        return card1 + card2 + card3 + card4;
    }

    public String getCardId() {
        return cardId;
    }
}
