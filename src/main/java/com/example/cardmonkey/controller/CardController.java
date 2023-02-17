package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.FavorResponseDTO;
import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.PaidResDTO;
import com.example.cardmonkey.service.CardService;
import com.example.cardmonkey.dto.CardByBenefitResDTO;
import com.example.cardmonkey.dto.CardDTO;
import com.example.cardmonkey.dto.CardDetailDTO;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.service.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"카드 서비스"}, description = "카드 서비스를 담당합니다.")
public class CardController {

    private final CardService cardService;

    /*=============================================
     * 카드 신청내역 조회 : code by 주찬혁(crossbell8368)
     =============================================*/
    @GetMapping("/paid/{id}")
    @ApiOperation(value = "신청한 카드 내역", notes = "신청한 카드 내역을 조회합니다.")
    public List<PaidResDTO> selectPaidCard(@PathVariable("id") String memberId) {
        return cardService.paidList(memberId);
    }

    /**
     * 인기 TOP 3 카드
     */
    @GetMapping("/card/rank")
    @ApiOperation(value = "인기 TOP 3 카드", notes = "구입된 횟수가 많은 인기 TOP 3 카드를 조회합니다.")
    public String selectTopThreeCard() {
        return null;
    }

    /**
     * 관심혜택 맞춤 카드
     */
    @GetMapping("/card/benefit/{userId}")
    @ApiOperation(value = "관심 혜택 맞춤 카드", notes = "회원가입 시 선택한 3가지의 혜택으로 카드를 추천합니다.")
    public List<CardByBenefitResDTO> CardByChooseBenefitList(@PathVariable String userId) {
        return cardService.findCardByChooseBenefit(userId);
    }

    /**
     * 나의 관심 카드 (찜 내역)
     */
    @GetMapping("/card/favor/{id}")
    @ApiOperation(value = "찜한 카드 내역", notes = "내가 찜한 카드들의 내역을 조회합니다.")
    public List<FavorResponseDTO> selectCardByFavor(@PathVariable String id) {
        return cardService.selectCardByFavor(id);
    }

    /**
     * 카드사 검색(우석)
     */
    @GetMapping("/card/company")
    @ApiOperation(value = "카드사 검색", notes = "카드사로 검색합니다.")
    public List<CardDTO> searchCardByCompany(@RequestParam(name = "company") String cardCompany) {
        return cardService.searchCardByCompany(cardCompany);
    }

    /**
     * 카드명 검색(우석)
     */
    @GetMapping("/card/name")
    @ApiOperation(value = "카드명 검색", notes = "카드명으로 검색합니다.")
    public List<CardDTO> searchCardByName(@RequestParam(name = "name") String cardName) {
        return cardService.searchCardByName(cardName);
    }

    /**
     * 카드 혜택 검색(우석)
     */
    @GetMapping("/card/benefit")
    @ApiOperation(value = "카드 혜택 검색", notes = "카드 혜택으로 검색합니다.")
    public List<CardDTO> searchCardByBenefit(@RequestParam(name = "benefit") String cardBenefit) {
        System.out.println(cardBenefit);
        return cardService.searchCardByBenefit(cardBenefit);
    }

    /**
     * 전체 카드 조회(우석)
     */
    @GetMapping("/card")
    @ApiOperation(value = "전체 카드 조회", notes = "전체 카드를 조회합니다.")
    public List<CardDTO> selectAllCard() {
        return cardService.selectAllCard();
    }

    /**
     * 카드 상세정보 조회(우석)
     */
    @GetMapping("/card/{id}")
    @ApiOperation(value = "카드 상세정보 조회", notes = "카드 상세정보를 조회합니다.")
    public String selectCardById(@PathVariable String id) {
        return null;
    }

    /*=============================================
     * 카드신청 : code by 주찬혁(crossbell8368)
     =============================================*/
    @PostMapping("/card/{id}")
    @ApiOperation(value = "카드 신청", notes = "카드를 신청합니다.")
    public String payCard(@PathVariable("id") Long id, Authentication authentication) {
        LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();
        String memberId = loginRequest.getUserId();
        return cardService.paidRequest(id, memberId);
    }

    /**
     * 찜하기 or 찜하기 취소 (관심상품)
     */
    @PostMapping("/card/{id}/favor")
    @ApiOperation(value = "찜 기능", notes = "유저가 관광지를 찜 하거나 찜 취소를 할 수 있습니다.")
    public String favorCard(@PathVariable Long id, Authentication authentication) {
        LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();
        String memberId = loginRequest.getUserId();
        System.out.println(memberId);
        return cardService.saveFavor(id, memberId);
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/card/{id}/review")
    @ApiOperation(value = "리뷰 조회", notes = "각 카드의 리뷰를 조회합니다.")
    public String selectReviewByCard(@PathVariable String id) {
        return null;
    }

    /**
     * 리뷰 선택
     */
    @PostMapping("/card/{id}/review")
    @ApiOperation(value = "리뷰 선택", notes = "각 카드에 원하는 리뷰를 선택합니다.")
    public String reviewCard(@PathVariable String id) {
        return null;
    }

    /**
     * 혜택 변경
     */
    @PostMapping("/changeBenefit/{id}")
    @ApiOperation(value = "혜택 변경", notes = "회원가입시 선택했던 3가지의 혜택을 수정합니다.")
    public String changeBenefit(@PathVariable String id) {
        return null;
    }

    /*=============================================
    * 카드신청 취소 : code by 주찬혁(crossbell8368)
    =============================================*/
    @DeleteMapping("/paid/{id}")
    @ApiOperation(value = "신청한 카드 취소", notes = "신청한 카드를 취소합니다.")
    public String deletePaidCard(@PathVariable("id") Long id, Authentication authentication) {
        LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();
        String memberId = loginRequest.getUserId();
        return cardService.paidCancel(id, memberId);
    }
}
