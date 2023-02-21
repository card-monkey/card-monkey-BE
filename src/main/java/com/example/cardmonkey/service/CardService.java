package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.CardByBenefitResDTO;
import com.example.cardmonkey.dto.CardByFavorResDTO;
import com.example.cardmonkey.dto.CardDetailResDTO;
import com.example.cardmonkey.dto.CardResDTO;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.entity.Paid;
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
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 카드 검색 (카드사)
     */
    public List<CardResDTO> searchCardByCompany(String company){
        return cardRepository.findAllByCompanyContains(company)
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 카드 검색 (혜택)
     */
    public List<CardResDTO> searchCardByBenefit(String benefit) {
        return cardRepository.findAllByBenefit(benefit)
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 전체 카드 조회
     */
    public List<CardResDTO> selectAllCard(){
        return cardRepository.findAll()
                .stream()
                .map(CardResDTO::new)
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
    public List<CardResDTO> paidList(String userId) {
        Member findMember = memberRepository.findByUserId(userId).get();

        return paidRepository.findAllByMemberId(findMember.getId())
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 카드 신청
     */
    @Transactional
    public String savePaid(String userId, Long cardId) {
        Member findMember = memberRepository.findByUserId(userId).get();
        Card findCard = cardRepository.findById(cardId).get();

        if (!checkExistsPaid(findMember.getId(), findCard.getId())) {
            Paid paid = Paid.createPaid(findMember, findCard);
            paidRepository.save(paid);
            return "카드신청 완료";
        } else {
            return "이미 신청하신 카드입니다.";
        }
    }

    /**
     * 카드 신청 취소
     */
    @Transactional
    public String cancelPaid(String userId, Long cardId) {
        Member findMember = memberRepository.findByUserId(userId).get();
        Card findCard = cardRepository.findById(cardId).get();

        if (checkExistsPaid(findMember.getId(), findCard.getId())) {
            paidRepository.deleteByMemberIdAndCardId(findMember.getId(), findCard.getId());
            return "카드신청 취소 완료";
        } else {
            return "신청내역이 존재하지 않습니다.";
        }
    }

    /**
     * 나의 관심(찜) 카드 내역
     */
    public List<CardResDTO> selectCardByFavor(String userId) {
        Member findMember = memberRepository.findByUserId(userId).get();

        return favorRepository.findAllByMemberId(findMember.getId())
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    @Transactional
    public String saveFavor(String userId, Long cardId) {
        Member findMember = memberRepository.findByUserId(userId).get();
        Card findCard = cardRepository.findById(cardId).get();

        if (!checkExistsFavor(findMember.getId(), findCard.getId())) {
            Favor favor = Favor.createFavor(findMember, findCard);
            favorRepository.save(favor);
            return "찜하기 완료";
        } else {
            favorRepository.deleteByMemberIdAndCardId(findMember.getId(), findCard.getId());
            return "찜하기 취소 완료";
        }
    }

    /**
     * 리뷰 조회
     */

    /**
     * 리뷰 선택
     */

    private boolean checkExistsPaid(Long memberId, Long cardId) {
        return paidRepository.existsByMemberIdAndCardId(memberId, cardId);
    }

    private boolean checkExistsFavor(Long memberId, Long cardId) {
        return favorRepository.existsByMemberIdAndCardId(memberId, cardId);
    }
}
