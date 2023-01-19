package com.nps.answer.json;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerForm {

    private String response;

    @NotNull
    private Integer score;

    private Long questionId;
}
