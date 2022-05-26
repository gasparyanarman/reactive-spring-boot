package com.reactive.reactor.controller;

import com.reactive.reactor.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReactiveController {
    @GetMapping("/")
    public String ok() {
        List<Integer> numberList = new ArrayList<>();
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5, 6);

        numbers.log().subscribe(new Subscriber<>() {
            private Subscription s;
            private int chunk;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                numberList.add(integer);
                chunk++;

                if (chunk % 2 == 0) {
                    s.request(2);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        System.out.println(numberList);

        return "OK";
    }

    @GetMapping("/movie")
    public Mono<Movie> getMovie() {
        return Mono.just(new Movie("Movie 1", "desc", 1));
    }

    @GetMapping("/movies")
    public Flux<Movie> getMovies() {
        Movie movie1 = new Movie("Movie 1", "desc", 1);
        Movie movie2 = new Movie("Movie 2", "desc", 2);
        Movie movie3 = new Movie("Movie 3", "desc", 3);
        Movie movie4 = new Movie("Movie 4", "desc", 4);

        Flux<Movie> movies = Flux.just(movie1, movie2, movie3, movie4);

        movies.subscribe(new Subscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("Subscribed to publisher");
                this.subscription = s;
                subscription.request(1);
            }

            @Override
            public void onNext(Movie movie) {
                System.out.println("Chunk with id " + movie.getNumber() + " received");
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Something went wrong");
            }

            @Override
            public void onComplete() {
                System.out.println("Everything completed");
            }
        });

        return movies;
    }
}
