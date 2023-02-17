package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.FavorResponseDTO;
import com.example.cardmonkey.dto.CardByBenefitResDTO;
import com.example.cardmonkey.dto.PaidReqDTO;
import com.example.cardmonkey.dto.PaidResDTO;
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

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final FavorRepository favorRepository;
    private final PaidRepository paidRepository;

    /*=============================================
     * 카드 신청내역 조회 : code by 주찬혁(crossbell8368)
     =============================================*/
    // 전달받은 userId를 조회조건으로 사용
    public List<PaidResDTO> paidList(String userId) {
        return paidRepository.findByMember(memberRepository.findByUserId(userId)).stream()
                .map(PaidResDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 인기 TOP 3 카드
     */

    /**
     * 관심혜택 맞춤 카드
     */


    /**
     * 나의 관심 카드 (찜 내역)
     */
    @Transactional
    public List<FavorResponseDTO> selectCardByFavor(String userId) {
        Member member = memberRepository.findByUserId(userId);
        List<Favor> favors = favorRepository.findAllByMember(member);
        return favors.stream().map(FavorResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * 카드사 검색
     */

    /**
     * 카드명 검색
     */

    /**
     * 카드 혜택 검색
     */
    public List<CardByBenefitResDTO> findCardByChooseBenefit(String userId) {
        Member findMember = memberRepository.findByUserId(userId);

        List<String> benefits = findMember.getBenefit().makeBenefitList();

        List<CardByBenefitResDTO> result = new ArrayList<>();
        for (String benefit : benefits) {
            long qty = cardRepository.countByBenefit(benefit);
            int index = (int) (Math.random() * qty);

            Card findCard = cardRepository.recommendRandomCardByBenefit(benefit, index);

            CardByBenefitResDTO dto = CardByBenefitResDTO.builder()
                    .id(findCard.getId())
                    .benefit(benefit)
                    .name(findCard.getName())
                    .company(findCard.getCompany())
                    .image(findCard.getImageURL())
                    .type(findCard.getCardType())
                    .build();

            result.add(dto);
        }
        return result;
    }

    /**
     * 전체 카드 조회
     */

    /**
     * 카드 상세정보 조회
     */

    /*=============================================
     * 카드신청 : code by 주찬혁(crossbell8368)
     =============================================*/
    public String paidRequest(Long id, String memberId) {
        if (memberId != null){
            Member member = memberRepository.findByUserId(memberId);
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
     * 찜하기 or 찜하기 취소 (관심상품)
     */
    public String saveFavor(Long id, String memberId) {
        if (memberId != null) {
            Member member = memberRepository.findByUserId(memberId);
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

    /**
     * 혜택 변경
     */

    /*=============================================
    * 카드신청 취소 : code by 주찬혁(crossbell8368)
    =============================================*/
    public String paidCancel(Long id, String memberId) {
        if (memberId != null) {
            Member member = memberRepository.findByUserId(memberId);
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

}
