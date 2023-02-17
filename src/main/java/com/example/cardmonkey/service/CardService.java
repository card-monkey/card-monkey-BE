package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.CardDTO;
import com.example.cardmonkey.dto.CardDetailDTO;
import com.example.cardmonkey.entity.Benefit;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.CardType;
import com.example.cardmonkey.repository.CardRepository;
import com.example.cardmonkey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    //private final FavorRepository favorRepository;

    /**
     * 신청한 카드 내역
     */

    /**
     * 인기 TOP 3 카드
     */

    /**
     * 관심혜택 맞춤 카드
     */

    /**
     * 나의 관심 카드 (찜 내역)
     */

    /**
     * 카드사 검색(우석)
     */
    public List<CardDTO> searchCardByCompany(String cardCompany){

        List<CardDTO> cards = cardRepository.findAllByCompanyContains(cardCompany)
                .stream()
                .map(card -> new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()))
                .collect(Collectors.toList());

        return cards;
    }


    /**
     * 카드명 검색(우석)
     */
    public List<CardDTO> searchCardByName(String cardName){

        List<CardDTO> cards = cardRepository.findAllByNameContains(cardName)
                                            .stream()
                                            .map(card -> new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()))
                                            .collect(Collectors.toList());

        return cards;
    }

    /**
     * 카드 혜택 검색
     */
    public List<CardDTO> searchCardByBenefit(String cardBenefit){
        List<CardDTO> temp = null;
        System.out.println("여기?");
        switch (cardBenefit){
            case "coffee" :
                List<CardDTO> cards = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getCoffee()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards;
                break;
            case "transportation" :
                List<CardDTO> cards2 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getTransportation()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards2;
                break;
            case "movie" :
                List<CardDTO> cards3 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getMovie()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards3;
                break;
            case "delivery" :
                List<CardDTO> cards4 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getDelivery()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards4;
                break;
            case "phone" :
                List<Card> temp1 = cardRepository.findAll();
                List<CardDTO> cards5 = null;
                //
                break;
            case "gas" :
                List<CardDTO> cards6 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getGas()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards6;
                break;
            case "simplepayment" :
                List<CardDTO> cards7 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getSimplePayment()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards7;
                break;
            case "tax" :
                List<CardDTO> cards8 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getTax()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards8;
                break;
            case "shopping" :
                List<CardDTO> cards9 = cardRepository.findAll()
                        .stream()
                        .map(card -> ("yes").equals(card.getBenefit().getShopping()) ? new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()) : null)
                        .collect(Collectors.toList());
                temp = cards9;
                break;
        }
       return temp;
    }

    /**
     * 전체 카드 조회(우석)
     */
    public List<CardDTO> selectAllCard(){

        List<CardDTO> cards = cardRepository.findAll()
                                            .stream()
                                            .map(card -> new CardDTO(card.getId(), card.getName(), card.getCompany(), card.getCardType(), card.getImageURL()))
                                            .collect(Collectors.toList());

        return cards;
    }


    /**
     * 카드 상세정보 조회(우석)
     */
    public CardDetailDTO selectCardById(Long id){
        Optional<Card> cardFoundById = cardRepository.findById(id);
        Benefit tempBenefit = cardFoundById.get().getBenefit();
        ArrayList<String> benefit = new ArrayList<>();

        if(tempBenefit.getCoffee() != null) benefit.add("coffee");
        if(tempBenefit.getTransportation() != null) benefit.add("transportation");
        if(tempBenefit.getMovie() != null) benefit.add("movie");
        if(tempBenefit.getDelivery() != null) benefit.add("delivery");
        if(tempBenefit.getPhone() != null) benefit.add("phone");
        if(tempBenefit.getGas() != null) benefit.add("gas");
        if(tempBenefit.getSimplePayment() != null) benefit.add("simplepayment");
        if(tempBenefit.getTax() != null) benefit.add("tax");
        if(tempBenefit.getShopping() != null) benefit.add("shopping");

        CardDetailDTO cardDetail = new CardDetailDTO(cardFoundById.get().getId(), cardFoundById.get().getName(), cardFoundById.get().getCompany(), cardFoundById.get().getCardType(), cardFoundById.get().getImageURL(), benefit);

        return cardDetail;
    }

    /**
     * 카드 신청
     */

    /**
     * 찜하기 or 찜하기 취소 (관심상품)
     */
//    public String saveFavor(Long id, String memberId) {
//        if (memberId != null) {
//            Member member = memberRepository.findByUserId(memberId);
//            Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
//            if (favorRepository.findAllByCardAndMember(card, member) == null) {
//                updateFavorStatus("new" , null, 1, card, member);
//                return "찜하기 완료";
//            } else {
//                if (favorRepository.findAllByCardAndMember(card, member).getStatus() == 1) {
//                    updateFavorStatus(null, favorRepository.findAllByCardAndMember(card, member), 0, card, null);
//                    return "찜하기 취소 완료";
//                } else {
//                    updateFavorStatus(null, favorRepository.findAllByCardAndMember(card, member), 1, card, null);
//                    return "찜하기 완료";
//                }
//            }
//        }
//        return "회원 정보가 없습니다.";
//    }

    /**
     * 찜 상태 업데이트 (관심상품)
     */
//    public void updateFavorStatus(String check, Favor res, int status, Card card, Member member) {
//        Favor favor;
//        if (check == null) {
//            favor = new Favor(res.getId(), res.getMember(), card, status);
//        } else {
//            favor = new Favor(null, member, card, status);
//        }
//        favorRepository.save(favor);
//    }

    /**
     * 리뷰 조회
     */

    /**
     * 리뷰 선택
     */

    /**
     * 혜택 변경
     */

    /**
     * 신청한 카드 취소
     */

}
