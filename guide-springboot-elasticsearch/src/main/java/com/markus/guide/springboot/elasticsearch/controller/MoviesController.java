package com.markus.guide.springboot.elasticsearch.controller;

import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.markus.guide.springboot.elasticsearch.domain.MovieDO;
import com.markus.guide.springboot.elasticsearch.service.MoviesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author: markus
 * @date: 2025/4/19 21:33
 * @Description:
 * @Blog: https://markuszhang.com
 * It's my honor to share what I've learned with you!
 */
@RestController
@RequestMapping("/movies")
public class MoviesController {
    @Resource
    private MoviesService moviesService;

    @GetMapping("/get")
    public MovieDO getMoviesById(@RequestParam("id") Long id) throws IOException {
        return moviesService.getMovieById("movies", id);
    }

    @PostMapping("/create")
    public void createMovies(@RequestBody MovieDO movieDO) throws IOException {
        moviesService.createMovie(movieDO);
    }


    @PostMapping("/index")
    public void indexMovies(@RequestBody MovieDO movieDO) throws IOException {
        moviesService.indexMovie(movieDO);
    }


    @PostMapping("/update")
    public void updateMovie(@RequestBody MovieDO movieDO) throws IOException {
        moviesService.updateMovie(movieDO);
    }

    @PostMapping("/bulk")
    public List<BulkResponseItem> bulkMovies(@RequestBody MovieDO movieDO) throws IOException {
        return moviesService.bulkMovies(movieDO);
    }
    @PostMapping("/mget")
    public List<MovieDO> mGetMovies(@RequestBody List<Long> ids) throws IOException {
        return moviesService.mGetMovies(ids);
    }

    // todo 批量查询
}
