package com.tecmarket.backend.business.dto;

public record BusinessUserCandidateResponse(
        Integer userId,
        String fullName,
        String email,
        Boolean assigned) {
}

