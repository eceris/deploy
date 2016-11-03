package com.daou.deploy.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author eceris
 * 
 *         패치파일 업로드 이후 결과를 내려주기 위한 모델
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchModel {
    //커스텀
    public String hostId; //호스트아이디
    public String path; //파일 경로
    public String fileName; //파일 이름
    //표준판
    public String status; //상태
    public String version; //버전
}
