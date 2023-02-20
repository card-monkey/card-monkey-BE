package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.repository.CardRepository;
import com.example.cardmonkey.repository.FavorRepository;
import com.example.cardmonkey.repository.MemberRepository;
import com.example.cardmonkey.repository.PaidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final FavorRepository favorRepository;
    private final PaidRepository paidRepository;

    /**
     * 몽키차트 TOP 5 카드 (찜)
     */
    // TODO: 리팩토링
    public List<CardByFavorResDTO> selectFavorByRank() {
        List<Card> cardList = new ArrayList<>();
        List<CardByFavorResDTO> cardByFavorResDTOList = new ArrayList<>();
        List<Object[]> objects = favorRepository.selectFavorByRank();
        if (objects.size() == 0 || objects == null) {
            return null;
        }
        return getCardResponseDTOS(cardList, cardByFavorResDTOList, objects, cardRepository);
    }

    /**
     * 인기 TOP 5 카드정보를 받아 옴
     */
    // TODO: 리팩토링
    static List<CardByFavorResDTO> getCardResponseDTOS(List<Card> cardList, List<CardByFavorResDTO> cardByFavorResDTOList, List<Object[]> objects, CardRepository cardRepository) {
        int cnt = 0;
        for (Object[] obj : objects) {
            cardList.add(cardRepository.findAllById(((BigInteger) obj[0]).longValue()));
            cardByFavorResDTOList.add(cnt, new CardByFavorResDTO(cardList.get(cnt), ((BigInteger) obj[1]).intValue()));
            cnt++;
        }
        return cardByFavorResDTOList;
    }

    /**
     * 관심혜택 맞춤 카드
     */
    public List<CardByBenefitResDTO> findCardByChooseBenefit(String userId) {
        Member findMember = memberRepository.findByUserId(userId).get();

        List<String> benefits = findMember.getBenefit().makeBenefitList();

        List<CardByBenefitResDTO> result = new ArrayList<>();
        for (String benefit : benefits) {
            long qty = cardRepository.countByBenefit(benefit);
            int index = (int) (Math.random() * qty);

            Card findCard = cardRepository.recommendRandomCardByBenefit(benefit, index);

            CardByBenefitResDTO dto = CardByBenefitResDTO.builder()
                    .id(findCard.getId())
                    .name(findCard.getName())
                    .company(findCard.getCompany())
                    .image(findCard.getImageURL())
                    .type(findCard.getCardType())
                    .benefit(benefit)
                    .build();

            result.add(dto);
        }
        return result;
    }

    /**
     * 카드 검색 (카드명)
     */
    public List<CardResDTO> searchCardByName(String name){
        return cardRepository.findAllByNameContains(name)
                .stream()
                .map(c -> new CardResDTO(c.getId(), c.getName(), c.getCompany(), c.getImageURL(), c.getCardType()))
                .collect(Collectors.toList());
    }

    /**
     * 카드 검색 (카드사)
     */
    public List<CardResDTO> searchCardByCompany(String company){
        return cardRepository.findAllByCompanyContains(company)
                .stream()
                .map(c -> new CardResDTO(c.getId(), c.getName(), c.getCompany(), c.getImageURL(), c.getCardType()))
                .collect(Collectors.toList());
    }

    /**
     * 카드 검색 (혜택)
     */
    public List<CardResDTO> searchCardByBenefit(String benefit) {
        return cardRepository.findAllByBenefit(benefit)
                .stream()
                .map(c -> new CardResDTO(c.getId(), c.getName(), c.getCompany(), c.getImageURL(), c.getCardType()))
                .collect(Collectors.toList());
    }

    /**
     * 전체 카드 조회
     */
    public List<CardResDTO> selectAllCard(){
        return cardRepository.findAll()
                .stream()
                .map(c -> new CardResDTO(c.getId(), c.getName(), c.getCompany(), c.getImageURL(), c.getCardType()))
                .collect(Collectors.toList());
    }

    /**
     * 카드 상세정보 조회
     */
    public CardDetailResDTO selectCardById(Long cardId) {
        Card findCard = cardRepository.findById(cardId).get();
        List<String> benefits = findCard.getBenefit().makeBenefitList();

        return CardDetailResDTO.builder()
                .id(findCard.getId())
                .name(findCard.getName())
                .company(findCard.getCompany())
                .image(findCard.getImageURL())
                .type(findCard.getCardType())
                .benefit(benefits)
                .build();
    }

    /**
     * 카드 신청 내역
     */
    // TODO: 리팩토링
    public List<PaidResDTO> paidList(String userId) {
        return paidRepository.findByMember(memberRepository.findByUserId(userId).get()).stream()
                .map(PaidResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 카드 신청
     */
    // TODO: 리팩토링
    @Transactional
    public String paidRequest(Long id, String memberId) {
        if (memberId != null){
            Member member = memberRepository.findByUserId(memberId).get();
            Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            try { // 기존 신청내역이 있는지 확인
                if(paidRepository.findAllByMemberAndCard(member, card) != null){
                    return "Apply record exist";
                }else{
                    paidRepository.save(new PaidReqDTO(member, card).toEntity());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Apply failed";
            }
            return "Apply success";
        }else{
            return "No member Info";
        }
    }

    /**
     * 카드 신청 취소
     */
    // TODO: 리팩토링
    @Transactional
    public String paidCancel(Long id, String memberId) {
        if (memberId != null) {
            Member member = memberRepository.findByUserId(memberId).get();
            Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            try {
                paidRepository.delete(
                        paidRepository.findAllByMemberAndCard(member, card)
                );
            } catch (Exception e) {
                e.printStackTrace();
                return "delete failed";
            }
            return "delete success";
        }else{
            return "No member Info";
        }
    }

    /**
     * 나의 관심(찜) 카드 내역
     */
    // TODO: 리팩토링
    public List<FavorResponseDTO> selectCardByFavor(String userId) {
        Member member = memberRepository.findByUserId(userId).get();
        List<Favor> favors = favorRepository.findAllByMember(member);
        return favors.stream().map(FavorResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    // TODO: 리팩토링
    @Transactional
    public String saveFavor(Long id, String memberId) {
        if (memberId != null) {
            Member member = memberRepository.findByUserId(memberId).get();
            Card card = cardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            if (favorRepository.findAllByCardAndMember(card, member) == null) {
                updateFavorStatus("new" , null, 1, card, member);
                return "찜하기 완료";
            } else {
                if (favorRepository.findAllByCardAndMember(card, member).getStatus() == 1) {
                    updateFavorStatus(null, favorRepository.findAllByCardAndMember(card, member), 0, card, null);
                    return "찜하기 취소 완료";
                } else {
                    updateFavorStatus(null, favorRepository.findAllByCardAndMember(card, member), 1, card, null);
                    return "찜하기 완료";
                }
            }
        }
        return "회원 정보가 없습니다.";
    }

    /**
     * 찜 상태 업데이트 (관심상품)
     */
    // TODO: 리팩토링
    @Transactional
    public void updateFavorStatus(String check, Favor res, int status, Card card, Member member) {
        Favor favor;
        if (check == null) {
            favor = new Favor(res.getId(), res.getMember(), card, status);
        } else {
            favor = new Favor(null, member, card, status);
        }
        favorRepository.save(favor);
    }

    /**
     * 리뷰 조회
     */

    /**
     * 리뷰 선택
     */

    // public List<CardResDTO> searchCardByBenefit(String benefit){
    //     List<CardResDTO> answer = new ArrayList<>();
    //     List<Card> temp = cardRepository.findAll();
    //     Benefit tempBenefit;
    //
    //     if(benefit.equals("coffee")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getCoffee() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("transportation")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getTransportation() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("movie")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getMovie() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("delivery")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getDelivery() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("phone")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getPhone() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             }catch (Exception e){
    //
    //             }
    //
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("gas")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getGas() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             }catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("simplepayment")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getSimplePayment() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("tax")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getTax() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //
    //     if(benefit.equals("shopping")){
    //         for(int i = 0; i < temp.size(); i++) {
    //             tempBenefit = temp.get(i).getBenefit();
    //             try {
    //                 if (tempBenefit.getShopping() != null) {
    //                     answer.add(new CardResDTO(temp.get(i).getId(), temp.get(i).getName(), temp.get(i).getCompany(), temp.get(i).getCardType(), temp.get(i).getImageURL()));
    //                 }
    //             } catch (Exception e){
    //
    //             }
    //         }
    //         return answer;
    //     }
    //     return answer;
    // }

    // public CardDetailResDTO selectCardById(Long id){
    //     Optional<Card> cardFoundById = cardRepository.findById(id);
    //     Benefit tempBenefit = cardFoundById.get().getBenefit();
    //     ArrayList<String> benefit = new ArrayList<>();
    //
    //     if(tempBenefit.getCoffee() != null) benefit.add("coffee");
    //     if(tempBenefit.getTransportation() != null) benefit.add("transportation");
    //     if(tempBenefit.getMovie() != null) benefit.add("movie");
    //     if(tempBenefit.getDelivery() != null) benefit.add("delivery");
    //     if(tempBenefit.getPhone() != null) benefit.add("phone");
    //     if(tempBenefit.getGas() != null) benefit.add("gas");
    //     if(tempBenefit.getSimplePayment() != null) benefit.add("simplepayment");
    //     if(tempBenefit.getTax() != null) benefit.add("tax");
    //     if(tempBenefit.getShopping() != null) benefit.add("shopping");
    //
    //     CardDetailResDTO cardDetail = new CardDetailResDTO(cardFoundById.get().getId(), cardFoundById.get().getName(), cardFoundById.get().getCompany(), cardFoundById.get().getCardType(), cardFoundById.get().getImageURL(), benefit);
    //
    //     return cardDetail;
    // }

}
