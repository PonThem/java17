package com.valuelab.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SampleForm {

    // 文字列がnull、空文字、空白でないことをチェック
    @NotBlank
    private String userId;

    // 正規表現でチェック 0,1のみ可
    @Pattern(regexp = "[01]", message = "{form_validation_pattern_error_please_specify_a_specific_value}")
    private String userName;

}
