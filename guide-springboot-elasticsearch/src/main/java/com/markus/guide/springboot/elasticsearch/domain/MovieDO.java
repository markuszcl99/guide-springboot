package com.markus.guide.springboot.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author: markus
 * @date: 2025/4/19 20:36
 * @Description:
 * @Blog: https://markuszhang.com
 * It's my honor to share what I've learned with you!
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDO {
    private Long id;
    private List<String> genre;
    private String title;
}
