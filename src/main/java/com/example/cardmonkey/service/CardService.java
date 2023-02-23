package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.entity.Paid;
import com.example.cardmonkey.exception.NoSuchCardException;
import com.example.cardmonkey.exception.NoSuchMemberException;
import com.example.cardmonkey.repository.CardRepository;
import com.example.cardmonkey.repository.FavorRepository;
import com.example.cardmonkey.repository.MemberRepository;
import com.example.cardmonkey.repository.PaidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final FavorRepository favorRepository;
    private final PaidRepository paidRepository;

    /**
     * 몽키차트 TOP 5 카드 (찜)
     */
    public List<CardByFavorResDTO> selectCardByFavorRank() {
        List<Card> cardList = new ArrayList<>();
        List<CardByFavorResDTO> cardByFavorResDTOList = new ArrayList<>();
        List<Object[]> objects = favorRepository.findByTop5FavorRank();
        if (objects.size() == 0) return null;

        return getCardByFavorResDtos(cardList, cardByFavorResDTOList, objects, cardRepository);
    }

    /**
     * 인기 TOP 5 카드정보를 받아 옴
     */
    static List<CardByFavorResDTO> getCardByFavorResDtos(List<Card> cardList, List<CardByFavorResDTO> cardByFavorResDTOList, List<Object[]> objects, CardRepository cardRepository) {
        int cnt = 0;
        for (Object[] obj : objects) {
            cardList.add(cardRepository.findById(((BigInteger) obj[0]).longValue()).get());
            cardByFavorResDTOList.add(cnt, new CardByFavorResDTO(cardList.get(cnt), ((BigInteger) obj[1]).intValue()));
            cnt++;
        }
        return cardByFavorResDTOList;
    }

    /**
     * 관심혜택 맞춤 카드
     */
    public List<CardByBenefitResDTO> selectCardByChooseBenefit(String userId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);

        List<String> benefits = findMember.getBenefit().makeBenefitList();

        List<CardByBenefitResDTO> result = new ArrayList<>();
        for (String benefit : benefits) {
            long qty = cardRepository.countByBenefit(benefit);
            int index = (int) (Math.random() * qty);

            Card findCard = cardRepository.findRandomCardByBenefit(benefit, index);

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
    public Page<CardResDTO> selectCardByName(String name, Pageable pageable){
        return cardRepository.findAllByNameContains(name, pageable)
                .map(CardResDTO::new);
    }

    /**
     * 카드 검색 (카드사)
     */
    public Page<CardResDTO> selectCardByCompany(String company, Pageable pageable){
        return cardRepository.findAllByCompanyContains(company, pageable)
                .map(CardResDTO::new);
    }

    /**
     * 카드 검색 (혜택)
     */
    public CardCountResDTO selectCardByBenefit(String benefit, int page, int offset, int limit) {;
        offset = limit * page;
        long count = cardRepository.countByBenefit(benefit);

        List<CardResDTO> content = cardRepository.findAllByBenefit(benefit, offset, limit)
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());

        return CardCountResDTO.builder()
                .content(content)
                .totalElements(count)
                .build();
    }

    /**
     * 전체 카드 조회
     */
    public Page<CardResDTO> selectAllCard(Pageable pageable){
        return cardRepository.findAll(pageable)
                .map(CardResDTO::new);
    }

    /**
     * 카드 상세정보 조회
     */
    public CardDetailResDTO selectCardDetail(Long cardId) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                NoSuchCardException::new);
        List<String> benefits = findCard.getBenefit().makeBenefitList();

        return CardDetailResDTO.builder()
                .id(findCard.getId())
                .name(findCard.getName())
                .company(findCard.getCompany())
                .image(findCard.getImageURL())
                .type(findCard.getCardType())
                .annualFee(findCard.getAnnualFee())
                .lastMonthPaid(findCard.getLastMonthPaid())
                .apply(findCard.getApplyURL())
                .benefit(benefits)
                .build();
    }

    /**
     * 카드 신청 내역
     */
    public List<CardResDTO> selectPaidList(String userId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);

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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                NoSuchCardException::new);

        if (checkExistsPaid(findMember.getId(), findCard.getId())) {
            return "이미 신청하신 카드입니다.";
        }
        Paid paid = Paid.createPaid(findMember, findCard);
        paidRepository.save(paid);
        return "카드신청 완료";
    }

    /**
     * 카드 신청 취소
     */
    @Transactional
    public String cancelPaid(String userId, Long cardId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                NoSuchCardException::new);

        if (!checkExistsPaid(findMember.getId(), findCard.getId())) {
            return "신청내역이 존재하지 않습니다.";
        }
        paidRepository.deleteByMemberIdAndCardId(findMember.getId(), findCard.getId());
        return "카드신청 취소 완료";
    }

    /**
     * 나의 관심(찜) 카드 내역
     */
    public List<CardResDTO> selectFavorList(String userId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);

        return favorRepository.findAllByMemberId(findMember.getId())
                .stream()
                .map(CardResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    @Transactional
    public String saveAndCancelFavor(String userId, Long cardId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                NoSuchCardException::new);

        if (!checkExistsFavor(findMember.getId(), findCard.getId())) {
            Favor favor = Favor.createFavor(findMember, findCard);
            favorRepository.save(favor);
            return "찜하기 완료";
        }
        favorRepository.deleteByMemberIdAndCardId(findMember.getId(), findCard.getId());
        return "찜하기 취소 완료";
    }

    private boolean checkExistsPaid(Long memberId, Long cardId) {
        return paidRepository.existsByMemberIdAndCardId(memberId, cardId);
    }

    private boolean checkExistsFavor(Long memberId, Long cardId) {
        return favorRepository.existsByMemberIdAndCardId(memberId, cardId);
    }
}
