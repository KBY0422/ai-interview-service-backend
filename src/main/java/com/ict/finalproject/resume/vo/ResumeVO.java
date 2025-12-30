package com.ict.finalproject.resume.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeVO {
    Integer rIdx,mIdx;
    LocalDateTime r_regdate;

}