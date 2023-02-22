package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.service.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"카드 서비스"}, description = "카드 서비스를 담당합니다.")
public class CardController {

    private final CardService cardService;

    /**
     * 몽키차트 TOP 5 카드 (찜)
     */
    @GetMapping("/card/rank")
    @ApiOperation(value = "몽키차트 TOP 5 카드", notes = "찜 횟수가 많은 TOP 5 카드를 조회합니다.")
    public List<CardByFavorResDTO> selectTopThreeCard() {
        return cardService.selectFavorByRank();
    }

    /**
     * 관심혜택 맞춤 카드
     */
    @GetMapping("/card/recommend")
    @ApiOperation(value = "관심 혜택 맞춤 카드", notes = "회원가입 시 선택한 3가지의 혜택으로 카드를 추천합니다.")
    public List<CardByBenefitResDTO> CardByChooseBenefitList(Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.findCardByChooseBenefit(userId);
    }

    /**
     * 카드 검색 (카드명)
     */
    @GetMapping("/card/name")
    @ApiOperation(value = "카드명 검색", notes = "카드명으로 검색합니다.")
    public Page<CardResDTO> searchCardByName(@RequestParam(name = "search") String name,
                                             @PageableDefault(size = 100) Pageable pageable) {
        return cardService.searchCardByName(name, pageable);
    }

    /**
     * 카드 검색 (카드사)
     */
    @GetMapping("/card/company")
    @ApiOperation(value = "카드사 검색", notes = "카드사로 검색합니다.")
    public Page<CardResDTO> searchCardByCompany(@RequestParam(name = "search") String company,
                                                @PageableDefault(size = 100) Pageable pageable) {
        return cardService.searchCardByCompany(company, pageable);
    }

    /**
     * 카드 검색 (혜택)
     */
    @GetMapping("/card/benefit")
    @ApiOperation(value = "카드 혜택 검색", notes = "카드 혜택으로 검색합니다.")
    public CardCountResDTO searchCardByBenefit(@RequestParam(name = "search") String benefit,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "0") int offset,
                                               @RequestParam(defaultValue = "100") int limit) {
        return cardService.searchCardByBenefit(benefit, page, offset, limit);
    }

    /**
     * 전체 카드 조회
     */
    @GetMapping("/card")
    @ApiOperation(value = "전체 카드 조회", notes = "전체 카드를 조회합니다.")
    public Page<CardResDTO> selectAllCard(@PageableDefault(size = 100) Pageable pageable) {
        return cardService.selectAllCard(pageable);
    }

    /**
     * 카드 상세정보 조회
     */
    @GetMapping("/card/{cardId}")
    @ApiOperation(value = "카드 상세정보 조회", notes = "카드 상세정보를 조회합니다.")
    public CardDetailResDTO selectCardById(@PathVariable Long cardId) {
        return cardService.selectCardById(cardId);
    }

    /**
     * 카드 신청 내역
     */
    @GetMapping("/info/apply")
    @ApiOperation(value = "신청한 카드 내역", notes = "신청한 카드 내역을 조회합니다.")
    public List<CardResDTO> selectPaidCard(Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.paidList(userId);
    }

    /**
     * 나의 관심(찜) 카드 내역
     */
    @GetMapping("/info/favor")
    @ApiOperation(value = "찜한 카드 내역", notes = "내가 찜한 카드들의 내역을 조회합니다.")
    public List<CardResDTO> selectCardByFavor(Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.selectCardByFavor(userId);
    }

    /**
     * 카드 신청
     */
    @PostMapping("/card/apply/{cardId}")
    @ApiOperation(value = "카드 신청", notes = "카드를 신청합니다.")
    public String payCard(@PathVariable Long cardId, Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.savePaid(userId, cardId);
    }

    /**
     * 카드 신청 취소
     */
    @DeleteMapping("/card/apply/{cardId}")
    @ApiOperation(value = "신청한 카드 취소", notes = "신청한 카드를 취소합니다.")
    public String deletePaidCard(@PathVariable Long cardId, Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.cancelPaid(userId, cardId);
    }

    /**
     * 찜하기 or 찜하기 취소
     */
    @PostMapping("/card/favor/{cardId}")
    @ApiOperation(value = "찜 기능", notes = "유저가 카드를 찜 하거나 찜 취소를 할 수 있습니다.")
    public String favorCard(Authentication authentication, @PathVariable Long cardId) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return cardService.saveFavor(userId, cardId);
    }
}
