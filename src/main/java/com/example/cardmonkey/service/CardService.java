package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.entity.*;
import com.example.cardmonkey.repository.*;
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
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    /**
     * 몽키차트 TOP 5 카드 (찜)
     */
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
    static List<CardByFavorResDTO> getCardResponseDTOS(List<Card> cardList, List<CardByFavorResDTO> cardByFavorResDTOList, List<Object[]> objects, CardRepository cardRepository) {
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
    public List<CardByBenefitResDTO> findCardByChooseBenefit(String userId) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

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
    public Page<CardResDTO> searchCardByName(String name, Pageable pageable){
        return cardRepository.findAllByNameContains(name, pageable)
                .map(CardResDTO::new);
    }

    /**
     * 카드 검색 (카드사)
     */
    public Page<CardResDTO> searchCardByCompany(String company, Pageable pageable){
        return cardRepository.findAllByCompanyContains(company, pageable)
                .map(CardResDTO::new);
    }

    /**
     * 카드 검색 (혜택)
     */
    public CardCountResDTO searchCardByBenefit(String benefit,int page, int offset, int limit) {;
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
    public CardDetailResDTO selectCardById(Long cardId) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드 정보를 찾을 수 없습니다."));
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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));;

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
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));;
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드 정보를 찾을 수 없습니다."));

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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));;
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드 정보를 찾을 수 없습니다."));

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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));;

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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));;
        Card findCard = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드 정보를 찾을 수 없습니다."));

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
    public ReviewResDTO selectReviewByCard(Long cardId){
        // 리뷰대상 카드정보 생성
        Card findCard = cardRepository.findById(cardId).get();

        // 대상 카드의 리뷰정보 추출 & Count
        ReviewResDTO res = new ReviewResDTO();
        List<Comment> commentList = commentRepository.findAll();
        res.SetComment(commentList);

        // 대상 카드에 포함된 리뷰갯수 카운트
        for(Review review : reviewRepository.findAllByCard(findCard)){
            res.CountReview(
                    review.getReviewComments().stream()
                            .map(ReviewComment::getComment)
                            .map(Comment::getMessage)
                            .collect(Collectors.toList())
            );
        }
        return res;
    }

    public String saveReview(String memberId, Long cardId, List<Long> commentIds){
        // 리뷰 작성자 & 리뷰 카드정보 생성
        Member findMember = memberRepository.findByUserId(memberId).get();
        Card findCard = cardRepository.findById(cardId).get();

        // 선택 리뷰정보 생성
        List<Comment> comments = new ArrayList<>();
        for(Long commentId : commentIds){
            comments.add(
                    commentRepository.findById(commentId).orElseThrow()
            );
        }

        // 기존 리뷰정보 확인
        if(!checkExistsReview(findMember, findCard)){

            // 리뷰가 존재하지 않는경우, 새로운 리뷰작성
            ReviewReqDTO req = new ReviewReqDTO();
            reviewRepository.save(
                    req.toEntity(findMember, findCard, comments)
            );
            return "리뷰선택 완료!";
        }else{

            // 리뷰가 존재하는 경우, 다시 누른것으로 판단 => 리뷰삭제 진행
            reviewRepository.deleteByMemberAndCard(findMember, findCard);
            return "리뷰삭제 완료!";
        }
    }

    private boolean checkExistsPaid(Long memberId, Long cardId) {
        return paidRepository.existsByMemberIdAndCardId(memberId, cardId);
    }

    private boolean checkExistsFavor(Long memberId, Long cardId) {
        return favorRepository.existsByMemberIdAndCardId(memberId, cardId);
    }

    private boolean checkExistsReview(Member member, Card card){
        return reviewRepository.existsByMemberAndCard(member, card);
    }

    // public List<CardByFavorResDTO> selectFavorByRank() {
    //     List<CardQueryDTO> rankCards = favorRepository.selectFavorByRank();
    //     List<Long> cardIds = new ArrayList<>();
    //     List<Long> favor = new ArrayList<>();
    //     for (CardQueryDTO rankCard : rankCards) {
    //         cardIds.add(rankCard.getCardId());
    //         favor.add(rankCard.getCount());
    //         System.out.println(rankCard.getCardId());
    //         System.out.println(rankCard.getCount());
    //     }
    //
    //     List<CardByFavorResDTO> result = new ArrayList<>();
    //     List<Card> findCards = cardRepository.findCardByIds(cardIds);
    //
    //     int count = 0;
    //     for (Card findCard : findCards) {
    //         CardByFavorResDTO dto = CardByFavorResDTO.builder()
    //                 .id(findCard.getId())
    //                 .name(findCard.getName())
    //                 .company(findCard.getCompany())
    //                 .image(findCard.getImageURL())
    //                 .type(findCard.getCardType())
    //                 .favor(favor.get(count))
    //                 .build();
    //
    //         result.add(dto);
    //         count++;
    //     }
    //     Collections.sort(result, (o1,o2) -> Math.toIntExact(o1.getFavor() - o2.getFavor()));
    //
    //     return result;
    // }
}
