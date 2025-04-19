package com.markus.guide.springboot.elasticsearch.service;

import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.markus.guide.springboot.elasticsearch.domain.MovieDO;

import java.io.IOException;
import java.util.List;

/**
 * @author: markus
 * @date: 2025/4/19 20:36
 * @Description:
 * @Blog: https://markuszhang.com
 * It's my honor to share what I've learned with you!
 */
public interface MoviesService {

    public MovieDO getMovieById(String index, Long id) throws IOException;

    public void createMovie(MovieDO movieDO) throws IOException;

    public void indexMovie(MovieDO movieDO) throws IOException;

    public void updateMovie(MovieDO movieDO) throws IOException;

    public List<BulkResponseItem> bulkMovies(MovieDO movieDO) throws IOException;

    public List<MovieDO> mGetMovies(List<Long> ids) throws IOException;
}
